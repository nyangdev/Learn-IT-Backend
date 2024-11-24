//package com.example.microstone.repository;
//
//import com.example.microstone.domain.Enum.GroupRole;
//import com.example.microstone.domain.Enum.JoinStatus;
//import com.example.microstone.domain.Enum.Role;
//import com.example.microstone.domain.Id.MemberListId;
//import com.example.microstone.domain.MemberList;
//import com.example.microstone.domain.StudyGroup;
//import com.example.microstone.domain.User;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.transaction.Transactional;
//import lombok.extern.log4j.Log4j2;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Commit;
//
//
//@SpringBootTest
//@Log4j2
//public class MemberListRepositoryTest {
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    StudyGroupRepository studyGroupRepository;
//
//    @Autowired
//    MemberListRepository memberListRepository;
//
//    @PersistenceContext
//    EntityManager em;
//
//    @Test
//    @Transactional
//    public void testInsert() {
//        User user = User.builder()
//                .uid(4L)
//                .user_id("js_clone123")
//                .password("jsjsjs123")
//                .name("김준성")
//                .nickname("나준성인데오늘부터팀장한다진짜다")
//                .phone_num("010-5678-1234")
//                .email("jsispm123@naver.com")
//                .role(Role.USER)
//                .build();
//
//        userRepository.save(user);
//
//        StudyGroup studyGroup = StudyGroup.builder()
//                .group_name("GroupName.. 1")
//                .present_member_num(10)
//                .max_member_num(50)
//                .build();
//
//        studyGroupRepository.save(studyGroup);
//
//
//        MemberListId MLid = new MemberListId();
//        MLid.setUid(user.getUid());
//        MLid.setGroup_id(studyGroup.getGroup_id());
//
//        MemberList ML = MemberList.builder()
//                .id(MLid)
//                .role(GroupRole.MEMBER)
//                .join_status(JoinStatus.PENDING)
//                .message_for_join("Make Minji Great Again!")
//                .build();
//
//        em.persist(ML);
//        em.flush();
//        em.clear();
//
//        //memberListRepository.save(ML);    em.flush()로 해결
//    }
//
//
//}
