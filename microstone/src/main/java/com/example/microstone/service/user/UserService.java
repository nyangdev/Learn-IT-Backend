package com.example.microstone.service.user;

import com.example.microstone.domain.User;
import com.example.microstone.dto.user.*;

public interface UserService {
    Long registerUser(UserFormDTO userFormDTO);

    User findByUserId(String user_id);

    UserDTO getKakaoMember(String accessToken);

    // 0803_google
    UserDTO getGoogleMember(String accessToken);

    default UserDTO entityToDTO(User user) {
        UserDTO dto = new UserDTO(
                user.getUser_id(),
                user.getPassword(),
                user.getNickname(),
//                user.isSocial(),
                user.getEmail(),
                user.getRole()
        );
        return dto;
    }

    // 0803_수정
    boolean checkUserId(String user_id);

    // 유저탈퇴_민지
    // 비활성화
    public void deactivateUser(String user_id);

    // 비활성화 취소
    public void cancelDeactivate(String user_id);

    // 30일 후 비활성화 계정 삭제
    public void finalizeDeactivation();

    // 닉네임 중복확인
    boolean checkUserNickname(String nickname);

    // 이메일 중복확인
    boolean checkUserEmail(String email);

    // 소셜 로그인 리디렉션
    public void saveAdditionalInfo(String user_id, UserAdditionalInfoDTO additionalInfo);

    public UserResponse getUserWithCustom(String user_id);

    public void updateUser(UserUpdateDTO userUpdateDTO);

    // 사용자 정보 가져오기
    public UserDTO read(String user_id, String password);

    // user_id에 해당하는 사용자 가져오기
    public UserDTO getByUserId(String user_id);

}