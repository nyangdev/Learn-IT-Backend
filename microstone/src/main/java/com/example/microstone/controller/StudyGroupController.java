package com.example.microstone.controller;

import com.example.microstone.domain.Enum.ManagerTransferRequestStatus;
import com.example.microstone.domain.JoinRequest;
import com.example.microstone.domain.StudyGroup;
import com.example.microstone.dto.*;
import com.example.microstone.dto.studygroup.StudyGroupDTO;
import com.example.microstone.dto.studygroup.StudyGroupLeaveRequestDTO;
import com.example.microstone.dto.studygroup.StudyGroupResponseDTO;
import com.example.microstone.service.StudyGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/studygroup")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")
public class StudyGroupController {

    private final StudyGroupService studyGroupService;

    // 스터디 그룹 생성
    @PostMapping
    public ResponseEntity<Map<String, Object>> createStudyGroup(@RequestBody StudyGroupDTO studyGroupDTO,
                                                                Principal principal) {

        Map<String, Object> response = new HashMap<>();

        try {
            // 현재 로그인된 사용자의 이름을 관리자로 설정
            studyGroupDTO.setAdmin(principal.getName());

            StudyGroup createdStudyGroup = studyGroupService.createStudyGroup(studyGroupDTO);

            response.put("result", "success");
            response.put("studyGroup", createdStudyGroup);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("result", "fail");
            response.put("message", e.getMessage());

            return ResponseEntity.status(500).body(response);
        }
    }

    // 스터디그룹 삭제
    @PutMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteStudyGroup(@PathVariable("id") Long id) {

        Map<String, Object> response = new HashMap<>();

        try {
            studyGroupService.deleteStudyGroup(id);
            response.put("result", "success");
            response.put("message", "Study group deleted");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("result", "fail");
            response.put("message", e.getMessage());

            return ResponseEntity.status(500).body(response);
        }
    }

    // 스터디그룹 가입 요청
    @PostMapping("/join-request/{id}")
    public ResponseEntity<Map<String, Object>> joinStudyGroup(@PathVariable("id") Long id,
                                                              Principal principal, @RequestBody JoinRequestDTO joinRequestDTO) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 현재 로그인된 사용자를 가입하는 사용자로
            joinRequestDTO.setUser_id(principal.getName());

            // 요청을 보내는 그룹의 아이디 지정
            joinRequestDTO.setGroup_id(id);

            // 요청을 보낸 사용자가 그룹의 관리자인지 확인한다
            StudyGroup studyGroup = studyGroupService.getStudyGroupById(id);
            if(studyGroup.getAdmin().equals(joinRequestDTO.getUser_id())) {
                response.put("result", "fail");
                response.put("message", "관리자는 본인이 관리하는 스터디 그룹에 가입 요청을 보낼 수 없습니다");
                return ResponseEntity.status(400).body(response);
            }

            JoinRequest createdJoinRequest = studyGroupService.createJoinRequest(joinRequestDTO);

            response.put("result", "success");
            response.put("joinRequest", createdJoinRequest);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("result", "fail");
            response.put("message", e.getMessage());

            return ResponseEntity.status(500).body(response);
        }
    }

    // 스터디그룹 리스트 로드

    // 1. 전체 요청 로드
    @GetMapping("/request-list/{id}")
    public ResponseEntity<Map<String, Object>> getAllJoinRequests(@PathVariable("id") Long group_id,
                                                                  Principal principal) {

        Map<String, Object> response = new HashMap<>();

        try {
            // 스터디그룹의 관리자 권한 확인
            boolean isAdmin = studyGroupService.isUserAdminOfGroup(group_id, principal.getName());

            // 반환값이 false
            if(!isAdmin) {
                response.put("result", "fail");
                response.put("message", "You are the admin of this study group");
                return ResponseEntity.status(403).body(response); // Forbidden
            }

            List<JoinRequestDTO> joinRequests = studyGroupService.getAllJoinRequestsGroup(group_id);

            response.put("result", "success");
            response.put("joinRequests", joinRequests);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("result", "fail");
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // 2. 가입 완료된 리스트만 선택해서 로드
    @GetMapping("/request-list/{id}/approved")
    public ResponseEntity<Map<String, Object>> getApprovedJoinRequests(@PathVariable("id") Long group_id,
                                                                       Principal principal) {

        Map<String, Object> response = new HashMap<>();

        try {
            // 관리자 권한 확인
            boolean isAdmin = studyGroupService.isUserAdminOfGroup(group_id, principal.getName());

            // 반환값이 false
            if(!isAdmin) {
                response.put("result", "fail");
                response.put("message", "You are the admin of this study group");
                return ResponseEntity.status(403).body(response);
            }

            // 가입 완료된 리스트만 로드
            List<JoinRequestDTO> joinRequests = studyGroupService.getApprovedJoinRequestsForGroup(group_id);

            response.put("result", "success");
            response.put("joinRequests", joinRequests);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("result", "fail");
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // 3. 가입 미완료된 리스트만 선택해서 로드
    @GetMapping("/request-list/{id}/not-approved")
    public ResponseEntity<Map<String, Object>> getNotApprovedJoinRequests(@PathVariable("id") Long group_id,
                                                                          Principal principal) {

        Map<String, Object> response = new HashMap<>();

        // 관리자 권한 확인
        try {
            boolean isAdmin = studyGroupService.isUserAdminOfGroup(group_id, principal.getName());

            if(!isAdmin) {
                response.put("result", "fail");
                response.put("message", "You are the admin of this study group");
                return ResponseEntity.status(403).body(response);
            }

            // 가입 미완료된 리스트만 로드
            List<JoinRequestDTO> joinRequests = studyGroupService.getNotApprovedJoinRequestsForGroup(group_id);

            response.put("result", "success");
            response.put("joinRequests", joinRequests);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("result", "fail");
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // 0827 구현중
    // 스터디그룹 요청 처리
    // 관리자만 enum 값 변경 가능함
    @PutMapping("/join-request")
    public ResponseEntity<Map<String, Object>> updateJoinRequestStatus(@RequestBody JoinRequestDTO joinRequestDTO,
                                                                       Principal principal) {

        Map<String, Object> response = new HashMap<>();

        try {
            // 관리자 권한 확인
            boolean isAdmin = studyGroupService.isUserAdminOfGroup(joinRequestDTO.getGroup_id(), principal.getName());

            // 리턴값이 false
            // 관리자가 아님
            if(!isAdmin) {
                response.put("result", "fail");
                response.put("message", "You are not the admin of this study group");
                return ResponseEntity.status(403).body(response); // Forbidden
            }

            // 요청상태 업데이트
            studyGroupService.updateJoinRequestStatus(joinRequestDTO);

            response.put("result", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("result", "fail");
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // 탈퇴_민지
    // 스터디그룹 탈퇴
    @PutMapping("/leave")
    public ResponseEntity<Map<String, Object>> leaveStudyGroup(@RequestBody StudyGroupLeaveRequestDTO studyGroupLeaveRequestDTO) {

        Map<String, Object> response = new HashMap<>();

        try {
            studyGroupService.leaveStudyGroup(studyGroupLeaveRequestDTO.getGroup_id(), studyGroupLeaveRequestDTO.getUser_id());
            response.put("result", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("result", "fail");
            response.put("message", e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }

//    @DeleteMapping("/leave")
//    public ResponseEntity<Map<String, Object>> leaveStudyGroup(
//            @RequestParam("group_id") Long group_id, // 쿼리 파라미터로 전달
//            @RequestParam("user_id") String user_id) { // 쿼리 파라미터로 전달
//
//        Map<String, Object> response = new HashMap<>();
//
//        // 요청 데이터 로그 확인
//        System.out.println("Received group_id: " + group_id + ", user_id: " + user_id);
//
//        try {
//            // 스터디 그룹 탈퇴 로직 수행
//            studyGroupService.leaveStudyGroup(group_id, user_id);
//            response.put("result", "success");
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            response.put("result", "fail");
//            response.put("message", e.getMessage());
//            return ResponseEntity.status(400).body(response);
//        }
//    }

    @DeleteMapping("/{group_id}/remove-member/{user_id}")
    public ResponseEntity<Map<String, String>> removeUserFromGroup(
            @PathVariable("group_id") Long group_id,
            @PathVariable("user_id") String user_id,
            Principal principal) {
        try {
            // 퇴출 로직 구현
            studyGroupService.removeUserFromGroup(group_id, user_id, principal.getName());

            Map<String, String> response = new HashMap<>();
            response.put("result", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("result", "fail");
            response.put("message", e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }

    // 관리자 이양 요청 생성하기
    // 스터디그룹내에 관리자만 생성할 수 있다
    @PostMapping("/manager-replace/{group_id}")
    public ResponseEntity<Map<String, String>> createManagerTransferRequest(@PathVariable("group_id") Long group_id,
                                                                            @RequestParam("targetUserId") String targetUserId,
                                                                            Principal principal) {

        try {
            // public void createManagerTransferRequest(Long group_id, String currentManagerId, String targetUserId)
            studyGroupService.createManagerTransferRequest(group_id, principal.getName(), targetUserId);

            Map<String, String> response = new HashMap<>();
            response.put("result", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("result", "fail");
            response.put("message", e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }

    // 관리자 이양 요청 수락, 거절 처리
    @PutMapping("/manager-transfer-response/{request_id}")
    public ResponseEntity<Map<String, String>> handleManagerTransferRequest(@PathVariable("request_id") Long request_id,
                                                                            @RequestParam("status") ManagerTransferRequestStatus status,
                                                                            Principal principal) {

        try {
            // public void handleManagerTransgerRequest(Long request_id, String targetUserId, ManagerTransferRequestStatus response)
            // 타겟 유저 대상 메서드
            studyGroupService.handleManagerTransgerRequest(request_id, principal.getName(), status);
            Map<String, String> response = new HashMap<>();
            response.put("result", "success");
            response.put("message", status == ManagerTransferRequestStatus.APPROVED ? "Transfer approved" : "Transfer rejected");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("result", "fail");
            response.put("message", e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }

    // 관리자 이양 요청 취소
    // 스터디그룹 관리자만 접근 가능
    @PutMapping("/manager-transfer/cancel/{group_id}")
    public ResponseEntity<Map<String, String>> cancelManagerTransferRequest(@PathVariable Long group_id,
                                                                            @RequestParam("adminUserId") String adminUserId) {
        Map<String, String> response = new HashMap<>();

        try {
            studyGroupService.cancelManagerTransferRequest(group_id, adminUserId);

            response.put("result", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("result", "fail");
            response.put("message", e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }
//
//    // 유저가 속한 그룹 로드하기
//    @GetMapping("/user")
//    public ResponseEntity<List<StudyGroup>> getUserGroups(@RequestParam("user_id") String user_id) {
////        List<StudyGroupResponseDTO> userGroups = studyGroupService.getUserGroups(user_id);
////        return ResponseEntity.ok(userGroups);
//        List<StudyGroup> groups = studyGroupService.getUserActiveGroups(user_id);
//        return ResponseEntity.ok(groups);
//    }
//
//    // 모든 스터디 그룹 조회 api
//    @GetMapping("/all")
//    public ResponseEntity<List<StudyGroup>> getAllStudyGroups() {
////        List<StudyGroupResponseDTO> studyGroups = studyGroupService.getAllStudyGroups();
////
////        return ResponseEntity.ok(studyGroups);
//        List<StudyGroup> groups = studyGroupService.getAllActiveGroups();
//        return ResponseEntity.ok(groups);
//    }

    // 모든 활성 그룹 가져오기
    @GetMapping("/all")
    public ResponseEntity<List<StudyGroupResponseDTO>> getAllStudyGroups() {
        List<StudyGroupResponseDTO> studyGroups = studyGroupService.getAllActiveGroups();
        return ResponseEntity.ok(studyGroups);
    }

    // 특정 사용자의 그룹 가져오기
    @GetMapping("/user")
    public ResponseEntity<List<StudyGroupResponseDTO>> getUserGroups(@RequestParam("user_id") String userId) {
        List<StudyGroupResponseDTO> userGroups = studyGroupService.getUserActiveGroups(userId);
        return ResponseEntity.ok(userGroups);
    }

    @GetMapping("/{group_id}")
    public ResponseEntity<StudyGroupResponseDTO> getStudyGroupById(@PathVariable("group_id") Long group_id) {
        try {
            StudyGroupResponseDTO studyGroup = studyGroupService.getStudyGroupId(group_id);
            return ResponseEntity.ok(studyGroup);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    // 그룹 멤버 목록 조회 api
    @GetMapping("/{group_id}/members")
    public ResponseEntity<List<MemberResponseDTO>> getGroupMembers(@PathVariable("group_id") Long groupId) {
        List<MemberResponseDTO> members = studyGroupService.getMembersByGroupId(groupId);
        return ResponseEntity.ok(members);
    }
}