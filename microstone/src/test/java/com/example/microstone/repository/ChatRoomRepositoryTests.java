//package com.example.microstone.repository;
//
//import com.example.microstone.domain.ChatRoom;
//import com.example.microstone.domain.StudyGroup;
//import lombok.extern.log4j.Log4j2;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Commit;
//import org.springframework.transaction.annotation.Transactional;
//
//@SpringBootTest
//@Log4j2
//public class ChatRoomRepositoryTests {
//
//    @Autowired
//    ChatRoomRepository chatRoomRepository;
//
//    @Autowired
//    StudyGroupRepository studyGroupRepository;
//
//    @Test
//    @Transactional
//    public void testInsert() {
//
//        // 테스트용 스터디 그룹
//        StudyGroup studyGroup = StudyGroup.builder()
//                .group_name("테스트용")
//                .present_member_num(10)
//                .max_member_num(50)
//                .build();
//
//        studyGroupRepository.save(studyGroup);
//
//        // 테스트용 채팅방 생성
//        ChatRoom chatRoom = ChatRoom.builder()
//                .studyGroup(studyGroup)
//                .Announcement_message("공지사항")
//                .chat_room_name("테스트 채팅방 이름")
//                .build();
//
//        chatRoomRepository.save(chatRoom);
//    }
//}