//package com.example.microstone.repository;
//
//import com.example.microstone.domain.ChatRoom;
//import com.example.microstone.domain.Enum.Role;
//import com.example.microstone.domain.Message;
//import com.example.microstone.domain.StudyGroup;
//import com.example.microstone.domain.User;
//import lombok.extern.log4j.Log4j2;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Commit;
//import org.springframework.transaction.annotation.Transactional;
//
//@SpringBootTest
//@Log4j2
//public class MessageRepositoryTests {
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    MessageRepository messageRepository;
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
//                .group_name("test group")
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
//                .chat_room_name("test chat room")
//                .build();
//
//        chatRoomRepository.save(chatRoom);
//
//        // 테스트용 유저 생성
//        User sender = User.builder()
//                .user_id("sender123")
//                .password("password")
//                .name("Sender User")
//                .nickname("sender")
//                .phone_num("010-1234-567899")
//                .email("sender@example.com")
//                .role(Role.USER)
//                .build();
//        userRepository.save(sender);
//
//        User replyedUser = User.builder()
//                .user_id("replyed123")
//                .password("password")
//                .name("Replyed User")
//                .nickname("replyed")
//                .phone_num("010-8765-432199")
//                .email("replyed@example.com")
//                .role(Role.USER)
//                .build();
//        userRepository.save(replyedUser);
//
//        // 메시지 생성 및 저장
//        Message message = Message.builder()
//                .sender_uid(sender)
//                .replyed_uid(replyedUser)
//                .chat_room_id(chatRoom)
//                .content("This is a test message.")
//                .read_person_num(0)
//                .build();
//
//        messageRepository.save(message);
//    }
//}