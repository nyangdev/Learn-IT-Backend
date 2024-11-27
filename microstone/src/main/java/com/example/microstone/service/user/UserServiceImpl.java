package com.example.microstone.service.user;

import com.example.microstone.domain.DeactivatedUser;
import com.example.microstone.domain.Enum.Department;
import com.example.microstone.domain.Enum.Occupation;
import com.example.microstone.domain.Enum.Role;
import com.example.microstone.domain.User;
import com.example.microstone.dto.user.*;
import com.example.microstone.exception.user.UserException;
import com.example.microstone.repository.DeactivatedUserRepository;
import com.example.microstone.repository.EjectionRepository;
import com.example.microstone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EjectionRepository ejectionRepository;

    // 유저탈퇴_민지
    private final DeactivatedUserRepository deactivatedUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean checkUserId(String user_id) {
        // User와 DeactivatedUser 테이블 둘다에서 중복 검사함
        boolean existsInUserTable = userRepository.existsByUser_id(user_id);
        boolean existsInDeactivatedUserTable = deactivatedUserRepository.existsByUserId(user_id);

        return existsInUserTable || existsInDeactivatedUserTable;
    }

    // 닉네임 중복확인
    @Override
    public boolean checkUserNickname(String nickname) {
        boolean existsInUserTable = userRepository.existsByNickname(nickname);
        boolean existsInDeactivatedUserTable = deactivatedUserRepository.existsByNickname(nickname);

        return existsInUserTable || existsInDeactivatedUserTable;
    }

    @Override
    public boolean checkUserEmail(String email) {
        boolean existsInUserTable = userRepository.existsByEmail(email);
        boolean existsInDeactivatedUserTable = deactivatedUserRepository.existsByEmail(email);

        return existsInUserTable || existsInDeactivatedUserTable;
    }


    // 비활성화
    @Override
    public void deactivateUser(String user_id) {
        User user = userRepository.findByUserId(user_id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id: " + user_id));

        user.setDeactivation_requested_at(LocalDateTime.now()); // 비활성화 요청 시간
        user.set_deactivated(true); // 계정 비활성화

        userRepository.save(user);
    }

    // 비활성화 취소
    @Override
    public void cancelDeactivate(String user_id) {
        User user = userRepository.findByUserId(user_id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id: " + user_id));

        user.setDeactivation_requested_at(null);
        user.set_deactivated(false);

        userRepository.save(user);
    }

    // 비활성화 후 30일 지난 계정 자동 삭제
    @Override
    @Scheduled(cron = "0 0 2 * * ?") // 매일 새벽 2시에 실행
    public void finalizeDeactivation() {
        LocalDateTime thresholdDate = LocalDateTime.now().minusDays(30);

        // 계정이 비활성화된 사용자들을 대상으로
        // 지금으로부터 30일 이상 된 사용자를 찾아냄
        List<User> userToDelete = userRepository.findAllByIsDeactivatedAndDeactivationRequestedAtBefore(thresholdDate);

        // 해당하는 user 아이디를 DeactivatedUser 테이블에 저장함
        for(User user : userToDelete) {
            DeactivatedUser deactivatedUser = DeactivatedUser.builder()
                    .user_id(user.getUser_id())
                    .deactivateAt(LocalDateTime.now())
                    .build();

            deactivatedUserRepository.save(deactivatedUser);

            // DeactivatedUser 테이블에 저장 후
            // User 테이블에서 해당하는 유저 정보 삭제
            userRepository.delete(user);
        }
    }

    @Override
    public Long registerUser(UserFormDTO userFormDTO) {
        // 이메일 추방 여부 확인
//        if (ejectionRepository.existsByUserEmail(userFormDTO.getEmail())) {
//            throw new IllegalArgumentException("This email has been banned from registering again.");
//        }

        // 아이디 중복확인
        if(checkUserId(userFormDTO.getUser_id())) {
            throw new IllegalArgumentException("User id already exists");
        }

        Role role;

        // 강제 관리자 역할 부여_민지
        if ("admin".equals(userFormDTO.getUser_id())) { // 또는 특정 조건
            role = Role.ADMIN;
        } else {
            role = Role.USER;
        }

        User user = User.builder()
                .user_id(userFormDTO.getUser_id())
                .password(passwordEncoder.encode(userFormDTO.getPassword()))
//                .password(passwordEncoder.encode(userFormDTO.getPassword() + "microstone!#")) //salt - 기존 비밀번호에 문자열 일부를 추가한 뒤 해싱한 값을 저장
                .name(userFormDTO.getName())
                .nickname(userFormDTO.getNickname())
                .phone_num(userFormDTO.getPhone_num())
                .email(userFormDTO.getEmail())
                // 0913
                .role(role)
                .occupation(userFormDTO.getOccupationAsEnum())
                .department(userFormDTO.getDepartmentAsEnum())
                // 유저탈퇴
                .is_deactivated(false) // 계정 생성시에 비활성화되지 않는다
                .build();

        userRepository.save(user);

        return user.getUid();
    }

    @Override
    public User findByUserId(String user_id) {
        return userRepository.findByUserId(user_id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public UserDTO getKakaoMember(String accessToken) {

        String email = getEmailFromKakaoAccessToken(accessToken);

        log.info("email: " + email);

        Optional<User> result = userRepository.findByUserId("kakao_" + email);

        // 0802issue
        // 기존의 회원
        if(result.isPresent()) {
            UserDTO userDTO = entityToDTO(result.get());

            return userDTO;
        }

        // 0802issue
        // 회원이 아니었다면
        User socialUser = makeSocialUser(email);
        userRepository.save(socialUser);

        UserDTO userDTO = entityToDTO(socialUser);

        return userDTO;
    }

    // 0803_google
    @Override
    public UserDTO getGoogleMember(String accessToken) {

        String email = getEmailFromGoogleAccessToken(accessToken);

        log.info("email: " + email);

        Optional<User> result = userRepository.findByUserId("google_" + email);

        if(result.isPresent()) {
            UserDTO userDTO = entityToDTO(result.get());

            return userDTO;
        }

        User googleUser = makeGoogleUser(email);
        userRepository.save(googleUser);

        UserDTO userDTO = entityToDTO(googleUser);

        return userDTO;
    }

    private String getEmailFromKakaoAccessToken(String accessToken) {

        String kakaoGetUserURL = "https://kapi.kakao.com/v2/user/me";

        if(accessToken == null) {
            throw new RuntimeException("Access token is null");
        }

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(kakaoGetUserURL).build();

        ResponseEntity<LinkedHashMap> response =
                restTemplate.exchange(
                        uriBuilder.toString(),
                        HttpMethod.GET,
                        entity,
                        LinkedHashMap.class);

        log.info(response);

        LinkedHashMap<String, LinkedHashMap> bodyMap = response.getBody();

        log.info("----------------------------------");
        log.info(bodyMap);

        LinkedHashMap<String, String> kakaoAccount = bodyMap.get("kakao_account");

        log.info("kakaoAccount: " + kakaoAccount);

        return kakaoAccount.get("email");
    }

    // 0803_google
    private String getEmailFromGoogleAccessToken(String accessToken) {

        String GoogleGetUserURL = "https://www.googleapis.com/oauth2/v3/userinfo";

        if(accessToken == null) {
            throw new RuntimeException("Access token is null");
        }

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(GoogleGetUserURL).build();

        ResponseEntity<LinkedHashMap> response =
                restTemplate.exchange(
                        uriBuilder.toString(),
                        HttpMethod.GET,
                        entity,
                        LinkedHashMap.class);

        log.info(response);


        // 0803_google
        LinkedHashMap<String, Object> bodyMap = response.getBody();

        log.info("----------------------------------");
        log.info(bodyMap);

        return (String) bodyMap.get("email");
    }

    // 해당 이메일을 가진 회원이 없다면
    // 새로운 회원을 추가할때 패스워드를 임의로 생성함
    private String makeTempPassword() {
        StringBuffer buffer = new StringBuffer();

        for(int i = 0; i < 10; i++) {
            buffer.append((char)((int)(Math.random()*55)+65));
        }

        return buffer.toString();
    }

    private User makeSocialUser(String email) {
        String tempPassword = makeTempPassword();

        log.info("tempPassword: " + tempPassword);

        String nickname = "kakao_" + email;

        User user = User.builder()
                .user_id("kakao_"+email)
                .name("kakao_name")
                .email(email)
                .password(passwordEncoder.encode(tempPassword))
                .nickname(nickname)
                .phone_num("000-0000-0000")
//                .social(true)
                .role(Role.USER)
                .department(Department.SOCIAL)
                .occupation(Occupation.SOCIAL)
                .build();

        return user;
    }

    // 0803_google
    private User makeGoogleUser(String email) {
        String tempPassword = makeTempPassword();

        log.info("tempPassword: " + tempPassword);

        String nickname = "google_" + email;

        User user = User.builder()
                .user_id("google_"+email)
                .name("google_name")
                .email(email)
                .password(passwordEncoder.encode(tempPassword))
                .nickname(nickname)
                .phone_num("000-0000-0000")
//                .social(true)
                .role(Role.USER)
                .department(Department.SOCIAL)
                .occupation(Occupation.SOCIAL)
                .build();

        return user;
    }

    // 소셜 로그인 리디렉션
    @Override
    public void saveAdditionalInfo(String user_id, UserAdditionalInfoDTO additionalInfo) {
        User user = userRepository.findByUserId(user_id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // String을 Enum으로 변환
        Department department = Department.valueOf(additionalInfo.getDepartment().toUpperCase());
        Occupation occupation = Occupation.valueOf(additionalInfo.getOccupation().toUpperCase());

        // 업데이트할 값들 설정
        user.setName(additionalInfo.getName());
        user.setNickname(additionalInfo.getNickname());
        user.setPhone_num(additionalInfo.getPhone_num());
        user.setDepartment(department);
        user.setOccupation(occupation);

        userRepository.save(user);
    }

    @Override
    public UserResponse getUserWithCustom(String user_id) {
        User user = userRepository.findByUser_id(user_id);
//        UserDTO userDTO = new UserDTO(user.getUser_id(), user.getPassword(), user.getNickname(), user.isSocial(), user.getEmail(), user.getRole());
        UserDTO userDTO = new UserDTO(user.getUser_id(), user.getPassword(), user.getNickname(), user.getEmail(), user.getRole());

        return new UserResponse(userDTO, user.getOccupation(), user.getDepartment(), user.getPhone_num(), user.getCreatedAt());
    }

    @Override
    public void updateUser(UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findByUser_id(userUpdateDTO.getUser_id());
        log.info("--------------------------------------------절취선");
        log.info("user: " + user);

        // 사용자 정보 업데이트
        user.setNickname(userUpdateDTO.getNickname());
        user.setPhone_num(userUpdateDTO.getPhone_num());
        user.setOccupation(userUpdateDTO.getOccupation());
        user.setDepartment(userUpdateDTO.getDepartment());

        userRepository.save(user);
    }

    @Override
    public UserDTO read(String user_id, String password) {
        Optional<User> result = userRepository.findByUserId(user_id);

        User user = result.orElseThrow(UserException.NOT_FOUND::get);

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw UserException.BAD_CREDENTIALS.get();
        }

        return new UserDTO(user);
    }
}