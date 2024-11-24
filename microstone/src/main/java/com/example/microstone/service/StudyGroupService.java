package com.example.microstone.service;

import com.example.microstone.domain.*;
import com.example.microstone.domain.Enum.GroupRole;
import com.example.microstone.domain.Enum.JoinStatus;
import com.example.microstone.domain.Enum.ManagerTransferRequestStatus;
import com.example.microstone.domain.Enum.RequestStatus;
import com.example.microstone.domain.Id.MemberListId;
import com.example.microstone.dto.JoinRequestDTO;
import com.example.microstone.dto.MemberResponseDTO;
import com.example.microstone.dto.studygroup.StudyGroupDTO;
import com.example.microstone.dto.studygroup.StudyGroupResponseDTO;
import com.example.microstone.repository.*;
import com.example.microstone.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyGroupService {

    private final StudyGroupRepository studyGroupRepository;

    // 가입
    private final JoinRequestRepository joinRequestRepository;
    private final UserRepository userRepository;
    private final MemberListRepository memberListRepository;
    private final PostRepository postRepository;
    private final PostService postService;
    private final ReplyRepository replyRepository;
    private final ReplyService replyService;

    // board
    private final BoardRepository boardRepository;
    private final BoardService boardService;

    // 유저를 그룹에서 퇴출하는 메소드
    @Transactional
    public void removeUserFromGroup(Long groupId, String userId, String currentUserId) {

        // 그룹을 찾습니다.
        StudyGroup group = studyGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Study group not found"));

        // 요청한 사용자가 그룹 관리자(admin)인지 확인합니다.
        if (!group.getAdmin().equals(currentUserId)) {
            throw new IllegalArgumentException("You are not authorized to remove this user.");
        }

        // 유저가 그룹에 속해 있는지 확인합니다.
        boolean isUserInGroup = memberListRepository.existsByGroupIdAndUserId(groupId, userId);
        if (!isUserInGroup) {
            throw new IllegalArgumentException("User is not a member of this group.");
        }

        // 그룹에서 해당 유저를 삭제합니다.
        memberListRepository.deleteByGroupIdAndUserId2(groupId, userId);
    }

    // StudyGroup을 StudyGroupResponseDTO로 변환하는 메소드
    private StudyGroupResponseDTO convertToResponseDTO(StudyGroup studyGroup) {
        String adminNickname = userRepository.findByUserId(studyGroup.getAdmin())
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"))
                .getNickname();
        return new StudyGroupResponseDTO(studyGroup, adminNickname);
    }

    // 모든 활성 그룹 가져오기
    public List<StudyGroupResponseDTO> getAllActiveGroups() {
        List<StudyGroup> activeGroups = studyGroupRepository.findAllActiveGroups();
        return activeGroups.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // 특정 사용자의 활성 그룹 가져오기
    public List<StudyGroupResponseDTO> getUserActiveGroups(String userId) {
        List<StudyGroup> userGroups = studyGroupRepository.findActiveGroupsByAdmin(userId);
        return userGroups.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // 관리자이양_민지
    private ManagerTransferRequestRepository managerTransferRequestRepository;


    public List<StudyGroupResponseDTO> getUserGroups(String userId) {
        List<MemberList> memberLists = memberListRepository.findByUserId(userId);
        return memberLists.stream()
                .map(memberList -> {
                    StudyGroup studyGroup = memberList.getGroup_id();
                    String adminNickname = userRepository.findByUserId(studyGroup.getAdmin()) // 관리자 ID로 닉네임 조회
                            .map(User::getNickname)
                            .orElse("알 수 없음");
                    return new StudyGroupResponseDTO(
                            studyGroup.getGroup_id(),
                            studyGroup.getGroup_name(),
                            adminNickname,  // 닉네임을 DTO로 반환
                            studyGroup.getPresent_member_num(),
                            studyGroup.getMax_member_num()
                    );
                })
                .collect(Collectors.toList());
    }

    // 스터디그룹 생성
    public StudyGroup createStudyGroup(StudyGroupDTO studyGroupDTO) {
        StudyGroup studyGroup = StudyGroup.builder()
                .group_name(studyGroupDTO.getGroup_name())
                .max_member_num(studyGroupDTO.getMax_member_num())
                .present_member_num(1) // 관리자만
                .admin(studyGroupDTO.getAdmin()) // 관리자 지정
                .build();

        StudyGroup savedStudyGroup = studyGroupRepository.save(studyGroup);

        // 3개의 게시판 생성
        boardService.createStudyGroupBoard(savedStudyGroup);

        // 그룹의 관리자 유저 조회
        User adminUser = userRepository.findByUserId(studyGroupDTO.getAdmin())
                .orElseThrow(() -> new IllegalArgumentException("Admin user not found"));

        // 관리자 유저를 MemberList에 추가
        MemberList adminMember = MemberList.builder()
                .id(new MemberListId(savedStudyGroup.getGroup_id(), adminUser.getUid()))
                .group_id(savedStudyGroup)
                .uid(adminUser)
                .role(GroupRole.LEADER) // 관리자 역할로 설정
                .join_status(JoinStatus.COMPLETED)
                .build();

        memberListRepository.save(adminMember);

        return savedStudyGroup;
    }

    @Transactional
    public void deleteStudyGroup(Long id) {
        Optional<StudyGroup> studyGroupOpt = studyGroupRepository.findById(id);

        if(studyGroupOpt.isPresent()) {
            StudyGroup studyGroup = studyGroupOpt.get();

            // 그룹 내 멤버들도 소프트 삭제 처리
            List<MemberList> members = memberListRepository.findByGroup_id_Group_id(studyGroup.getGroup_id());
            for (MemberList member : members) {
                member.setDeleted_at(LocalDateTime.now()); // 멤버 소프트 삭제
                memberListRepository.save(member);
            }

            studyGroup.setDeleted_at(LocalDateTime.now()); // 그룹 소프트 삭제
            studyGroupRepository.save(studyGroup);
        } else {
            throw new IllegalArgumentException("Study group not found");
        }
    }

    public JoinRequest createJoinRequest(JoinRequestDTO joinRequestDTO) {

        // 기존 가입 요청 상태 확인
        Optional<JoinRequest> existingRequest = joinRequestRepository.findPendingOrApprovedRequest(joinRequestDTO.getGroup_id(), joinRequestDTO.getUser_id());

        // 존재한다면
        if(existingRequest.isPresent()) {
            // 요청 상태를 가져온다
            RequestStatus status = existingRequest.get().getStatus();

            // 상태가 PENDING 이면
            if(status == RequestStatus.PENDING) {
                throw new IllegalArgumentException("이미 가입 요청을 보냈습니다.");
            } else if (status == RequestStatus.APPROVED) {
                throw new IllegalArgumentException("이미 이 그룹의 일원입니다.");
            }
        }

        // REJECTED 상태인 요청의 user 이거나
        // 요청을 보낸적 없는 user이면
        JoinRequest joinRequest = JoinRequest.builder()
                .group_id(joinRequestDTO.getGroup_id())
                .user_id(joinRequestDTO.getUser_id())
                .status(RequestStatus.PENDING)
                .message_for_join(joinRequestDTO.getMessage_for_join())
                .build();

        return joinRequestRepository.save(joinRequest);
    }

    // 현재 로그인한 사용자가 스터디그룹의 관리자 권한이 있는 사용자인지 확인하는 로직
    public boolean isUserAdminOfGroup(Long id, String user_id) {

        // 실존하는 스터디그룹인지 확인
        // 하 근데 존재하겠지 없겠냐 그치만 이런 로직도 필요하겠지.. 보안때문인걸까 왜지 하늘아 나 오늘따라 너무 작업하기싫다 어쩜 좋니 그치만 일할게
        StudyGroup studyGroup = studyGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Study group not found"));

        // 일치하면 true 불일치하면 false
        return studyGroup.getAdmin().equals(user_id);
    }

    // JoinRequest 조회하는 메서드
    public JoinRequestDTO getJoinRequestById(Long request_id) {
        JoinRequest joinRequest = joinRequestRepository.findById(request_id)
                .orElseThrow(() -> new IllegalArgumentException("Join request not found"));

        return convertToDTO(joinRequest);
    }

    private JoinRequestDTO convertToDTO(JoinRequest joinRequest) {
        return JoinRequestDTO.builder()
                .id(joinRequest.getId())
                .group_id(joinRequest.getGroup_id())
                .user_id(joinRequest.getUser_id())
                .status(joinRequest.getStatus())
                .message_for_join(joinRequest.getMessage_for_join())
                .build();
    }

    // 1. 전체 요청 로드
    public List<JoinRequestDTO> getAllJoinRequestsGroup(Long group_id) {
        return joinRequestRepository.findAllByGroupId(group_id)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 2. 가입 완료된 리스트만 선택해서 로드
    public List<JoinRequestDTO> getApprovedJoinRequestsForGroup(Long group_id) {
        return joinRequestRepository.findApprovedByGroupId(group_id)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 3. 가입 미완료된 리스트만 선택해서 로드
    public List<JoinRequestDTO> getNotApprovedJoinRequestsForGroup(Long group_id) {
        return joinRequestRepository.findNotApprovedByGroupId(group_id)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 0827 구현중
    // 요청 상태 업데이트
    // 스터디그룹 관리자만 가능
    @Transactional
    public void updateJoinRequestStatus(JoinRequestDTO joinRequestDTO) {

        // 가입요청의 아이디를 추출
        Optional<JoinRequest> joinRequestOpt = joinRequestRepository.findById(joinRequestDTO.getId());

        // 가입요청이 실제로 존재하는지 확인
        if(joinRequestOpt.isPresent()) {
            // Optional 내부의 실제 객체 가져옴
            JoinRequest joinRequest = joinRequestOpt.get();
            joinRequest.setStatus(joinRequestDTO.getStatus()); // enum 값 업데이트

            // 가입요청 승인시에
            if(joinRequestDTO.getStatus() == RequestStatus.APPROVED) {

                // 스터디 그룹과 사용자 정보 조회
                StudyGroup studyGroup = studyGroupRepository.findById(joinRequestDTO.getGroup_id())
                        .orElseThrow(() -> new IllegalArgumentException("Study group not found"));
                User user = userRepository.findByUserId(joinRequestDTO.getUser_id())
                        .orElseThrow(() -> new IllegalArgumentException("User not found"));

                // 이미 받은 요청인지 확인한다
                if(memberListRepository.existsByGroupIdAndUserId(studyGroup.getGroup_id(), user.getUser_id())) {
                    throw new IllegalArgumentException("이미 승인한 요청입니다.");
                }

                // 복합키 설정
                MemberListId memberListId = new MemberListId(studyGroup.getGroup_id(), user.getUid());

                MemberList memberList = MemberList.builder()
                        .id(memberListId)
                        .group_id(studyGroup)
                        .uid(user)
                        .role(GroupRole.MEMBER)
                        .join_status(JoinStatus.COMPLETED)
//                        .message_for_join(joinRequestDTO.getMessage_for_join())
                        .build();

                memberListRepository.save(memberList);

                // 스터디그룹 멤버 증원
                studyGroup.incrementMemberCount();
                studyGroupRepository.save(studyGroup);
            }

            joinRequestRepository.save(joinRequest);
        } else {
            throw new IllegalArgumentException("Join request not found");
        }
    }

    // 탈퇴_민지
    // 관리자는 탈퇴 불가, 사용자가 탈퇴하면 잔여 게시물 내용 삭제되는 문제 해결해야한다
    // 댓글은 삭제된 댓글입니다. 띄워주는걸로
    public void leaveStudyGroup(Long group_id, String user_id) {
        // 스터디그룹이 존재하는지 확인
        StudyGroup studyGroup = studyGroupRepository.findById(group_id)
                .orElseThrow(() -> new IllegalArgumentException("Study group not found"));

        // 유저가 존재하는지 확인
        User user = userRepository.findByUserId(user_id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 해당 유저가 스터디그룹에 존재하는 유저인지 확인
        boolean isUserInGroup = memberListRepository.existsByGroupIdAndUserId(group_id, user_id);
        if(!isUserInGroup) {
            throw new IllegalArgumentException("User is not in group");
        }

        MemberList member = memberListRepository.findByUserAndGroupId(user, studyGroup)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        // 사용자가 관리자라면 탈퇴 불가
        if(member.getRole() == GroupRole.LEADER) {
            throw new IllegalArgumentException("LEADER cannot leave the study group.");
        }

        // 잔여 게시글 처리
        List<Post> userPosts = postRepository.findByUserAndGroupId(user, studyGroup);
        for(Post post : userPosts) {
            // soft delete
            postService.deletePost(post.getPost_id());
            postRepository.save(post);
        }

        // 잔여 댓글 처리
        List<Reply> userReplies = replyRepository.findByUserAndGroupId(user, studyGroup);
        for(Reply reply : userReplies) {
            replyService.softDeleteReply(reply.getReply_id(), user_id);
            replyRepository.save(reply);
        }

        joinRequestRepository.deleteByUserIdAndGroupId(user.getUser_id(), group_id);

        if (studyGroup.getPresent_member_num() > 0) {
            studyGroup.setPresent_member_num(studyGroup.getPresent_member_num() - 1);
            studyGroupRepository.save(studyGroup);
        }

        memberListRepository.deleteByGroupIdAndUserId(group_id, user.getUid());
    }

    public StudyGroup getStudyGroupById(Long group_id) {
        return studyGroupRepository.findById(group_id)
                .orElseThrow(() -> new IllegalArgumentException("Study group not found"));
    }

    // 그룹 ID로 그룹 정보 조회하는 로직 수정
    public StudyGroupResponseDTO getStudyGroupId(Long groupId) {
        StudyGroup studyGroup = studyGroupRepository.findById(groupId)
                .orElseThrow(() -> new NoSuchElementException("해당 그룹이 존재하지 않습니다."));

        // 관리자 닉네임 조회
        String adminNickname = userRepository.findByUserId(studyGroup.getAdmin())
                .map(User::getNickname)
                .orElse("알 수 없음");

        // DTO로 변환하여 반환
        return new StudyGroupResponseDTO(studyGroup, adminNickname);
    }

    // 관리자 이양 요청 생성
    // 관리자 권한을 가진 유저만 접근 가능하다
    public void createManagerTransferRequest(Long group_id, String currentManagerId, String targetUserId) {

        // 유효성 검사
        // 스터디그룹이 존재하는지
        StudyGroup studyGroup = studyGroupRepository.findById(group_id)
                .orElseThrow(() -> new IllegalArgumentException("Study group not found"));

        // 관리자 유저 존재여부
        User currentManager = userRepository.findByUserId(currentManagerId)
                .orElseThrow(() -> new IllegalArgumentException("Current manager not found"));

        // 타겟 유저 존재여부
        User targetUser = userRepository.findByUserId(targetUserId)
                .orElseThrow(() -> new IllegalArgumentException("Target user not found"));

        // 관리자 권한 확인
        if(!studyGroup.getAdmin().equals(currentManagerId)) {
            // 권한이 없는 유저일때
            throw new IllegalArgumentException("You are not admin user");
        }

        // 타겟 유저가 스터디그룹 회원인지 확인
        boolean isUserInGroup = memberListRepository.existsByGroupIdAndUserId(group_id, targetUserId);
        if(!isUserInGroup) {
            throw new IllegalArgumentException("User is not in group");
        }

        // 여러명에게 한번에 관리자 이양 요청을 보낼 수 없도록 해야함
        // 이미 진행중인 관리자 이양 요청이 있는지 확인한다
        Optional<ManagerTransferRequest> existingRequest = managerTransferRequestRepository.findByGroupAndStatus(group_id, ManagerTransferRequestStatus.PENDING);
        if (existingRequest.isPresent()) {
            throw new IllegalArgumentException("There is already a pending manager transfer request for this group.");
        }

        // 관리자 이양 요청 생성
        ManagerTransferRequest request = ManagerTransferRequest.builder()
                .group(studyGroup)
                .targetUser(targetUser)
                .status(ManagerTransferRequestStatus.PENDING)
                .build();

        managerTransferRequestRepository.save(request);
    }

    // 관리자 유저가 요청을 취소하면 PENDING에서 CANCELLED로 기록되게 한다
    // 관리자 이양 요청 취소
    public void cancelManagerTransferRequest(Long group_id, String adminUserId) {

        // 스터디그룹 존재 확인
        StudyGroup group = studyGroupRepository.findById(group_id)
                .orElseThrow(() -> new IllegalArgumentException("Study group not found"));

        // 관리자 인지 확인
        if(!group.getAdmin().equals(adminUserId)) {
            throw new IllegalArgumentException("You are not admin user");
        }

        // 현재 진행중인 관리자 이양 요청이 있는지 확인
        ManagerTransferRequest request = managerTransferRequestRepository.findByGroupAndStatus(group_id, ManagerTransferRequestStatus.PENDING)
                .orElseThrow(() -> new IllegalArgumentException("No pending manager transfer request found for this group"));

        // 요청 상태를 CANCELLED로 업데이트
        request.setStatus(ManagerTransferRequestStatus.CANCELLED);
        managerTransferRequestRepository.save(request);
    }

    // 관리자 이양 요청 승인, 거절 기능
    // 타겟 유저만 수정 가능하다
    public void handleManagerTransgerRequest(Long request_id, String targetUserId, ManagerTransferRequestStatus response) {

        // 요청 존재 확인
        ManagerTransferRequest request = managerTransferRequestRepository.findById(request_id)
                .orElseThrow(() -> new IllegalArgumentException("Transfer request not found"));

        // 타겟 유저와 일치하는지 확인
        if(!request.getTargetUser().getUser_id().equals(targetUserId)) {
            throw new IllegalArgumentException("You are not authorized to respond to this request");
        }

        // response
        if(response == ManagerTransferRequestStatus.APPROVED) {
            transferManagerRole(request);
        }

        request.setStatus(response);
        managerTransferRequestRepository.save(request);
    }

    // 유저 역할 지정
    // role 변경
    private void transferManagerRole(ManagerTransferRequest request) {
        StudyGroup studyGroup = request.getGroup();

        // 기존 관리자를 일반 유저로 변경
        // 관리자 유저가 멤버리스트에 존재하는지 검사
        MemberList currentManagerMemberList = memberListRepository.findByUserAndGroupId(request.getCurrentManager(), studyGroup)
                .orElseThrow(() -> new IllegalArgumentException("Current manager not found"));

        // 권한 변경
        currentManagerMemberList.setRole(GroupRole.MEMBER);
        memberListRepository.save(currentManagerMemberList);

        // 새로운 관리자 설정
        MemberList newManagerMemberList = memberListRepository.findByUserAndGroupId(request.getTargetUser(), studyGroup)
                .orElseThrow(() -> new IllegalArgumentException("ew manager not found in member list"));

        newManagerMemberList.setRole(GroupRole.LEADER);
        memberListRepository.save(newManagerMemberList);

        // 스터디그룹 관리자 정보 업데이트
        studyGroup.setAdmin(request.getTargetUser().getUser_id());
        studyGroupRepository.save(studyGroup);
    }

    // 모든 스터디 그룹 로드
    public List<StudyGroupResponseDTO> getAllStudyGroups() {
        List<StudyGroup> studyGroups = studyGroupRepository.findAll();

        return studyGroups.stream()
                .map(studyGroup -> {
                    // 관리자 ID로 닉네임을 조회 (UserRepository로 처리)
                    String adminNickname = userRepository.findByUserId(studyGroup.getAdmin())
                            .map(User::getNickname)
                            .orElse("알 수 없음");

                    // 필요한 데이터를 DTO로 매핑
                    return new StudyGroupResponseDTO(
                            studyGroup.getGroup_id(),
                            studyGroup.getGroup_name(),
                            adminNickname,  // 관리자 닉네임 전달
                            studyGroup.getPresent_member_num(),
                            studyGroup.getMax_member_num()
                    );
                })
                .collect(Collectors.toList());
    }

    public List<MemberResponseDTO> getMembersByGroupId(Long groupId) {
        List<MemberList> members = memberListRepository.findByGroup_IdAndJoinStatus(groupId, JoinStatus.COMPLETED);
        return members.stream().map(member -> new MemberResponseDTO(
                member.getUid().getUser_id(),
                member.getUid().getNickname(),
                member.getRole().toString(),
                member.getGroup_id().getGroup_id(   )
        )).collect(Collectors.toList());
    }

}