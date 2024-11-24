//package com.example.microstone.repository;
//
//import com.example.microstone.domain.*;
//import com.example.microstone.domain.Enum.ReportStatus;
//import com.example.microstone.domain.Enum.ReportType;
//import com.example.microstone.domain.Enum.Role;
//import lombok.extern.log4j.Log4j2;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//@SpringBootTest
//@Log4j2
//public class ReportRepositoryTests {
//
//    @Autowired
//    ReportRepository reportRepository;
//
//    @Autowired
//    BoardRepository boardRepository;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    PostRepository postRepository;
//
//    @Autowired
//    ReplyRepository replyRepository;
//
//    @Test
//    @Transactional
//    public void testPost() {
//
//        Board board = Board.builder()
//                .board_name("post 신고 테스트용 게시판.")
//                .build();
//
//        boardRepository.save(board);
//
//        User user = User.builder()
//                .user_id("mj_clone1.")
//                .password("123.")
//                .name("김민지.")
//                .nickname("나민지인데오늘부터팀장한다진짜다.")
//                .phone_num("010-12333-1234.")
//                .email("mjclone@naver.com.")
//                .role(Role.USER)
//                .build();
//
//        userRepository.save(user);
//
//        // post 데이터
//        Post post = Post.builder()
//                .board_id(board)
//                .uid(user)
//                .title("example post")
//                .content("example content")
////                .post_upload_date(LocalDateTime.now())
//                .recommend_num(100)
//                .not_recommend_num(10)
//                .views_num(100)
//                .reply_num(0)
//                .build();
//
//        postRepository.save(post);
//
//        // 신고 테스트
//        Report report = Report.builder()
//                .report_type(ReportType.ABUSE_SLURS)
//                .comment("심한욕설")
//                .status(ReportStatus.RECEIVED)
//                .post_id(post)
//                .build();
//
//        reportRepository.save(report);
//    }
//
//    @Test
//    @Transactional
//    public void testReply() {
//
//        Board board = Board.builder()
//                .board_name("reply 신고 테스트용 게시판")
//                .build();
//
//        boardRepository.save(board);
//
//        User user = User.builder()
//                .user_id("mj_clone2")
//                .password("1234")
//                .name("이태웅")
//                .nickname("나태웅인데오늘부터팀장한다진짜다")
//                .phone_num("010-123-1111")
//                .email("taewoong@naver.com")
//                .role(Role.USER)
//                .build();
//
//        userRepository.save(user);
//
//        // post 데이터
//        Post post = Post.builder()
//                .board_id(board)
//                .uid(user)
//                .title("example post")
//                .content("example content")
////                .post_upload_date(LocalDateTime.now())
//                .recommend_num(100)
//                .not_recommend_num(10)
//                .views_num(100)
//                .reply_num(0)
//                .build();
//
//        postRepository.save(post);
//
//        // reply 데이터
//        Reply reply = Reply.builder()
//                .uid(user)
//                .content("제발요...")
//                .recommand_num(100)
//                .post_id(post)
//                .build();
//        replyRepository.save(reply);
//
//        // 신고 테스트
//        Report report = Report.builder()
//                .report_type(ReportType.LEAK_IMPERSONATION_FRAUD)
//                .comment("유출")
//                .status(ReportStatus.RECEIVED)
//                .reply_id(reply)
//                .build();
//
//        reportRepository.save(report);
//    }
//
//    @Autowired
//    StudyGroupRepository studyGroupRepository;
//
//    @Autowired
//    ChatRoomRepository chatRoomRepository;
//
//    @Autowired
//    MessageRepository messageRepository;
//
//    // 메세지 신고 테스트
//    @Test
//    @Transactional
//    public void testMessage() {
//
//        // 테스트를 위한 메세지 생성
//
//        // 테스트용 스터디 그룹
//        StudyGroup studyGroup = StudyGroup.builder()
//                .group_name("test group.....123")
//                .present_member_num(10)
//                .max_member_num(50)
//                .build();
//
//        studyGroupRepository.save(studyGroup);
//
//        // 테스트용 채팅방 생성
//        ChatRoom chatRoom = ChatRoom.builder()
//                .studyGroup(studyGroup)
//                .Announcement_message("공지사항..")
//                .chat_room_name("test chat room......")
//                .build();
//
//        chatRoomRepository.save(chatRoom);
//
//        // 테스트용 유저 생성
//        User sender = User.builder()
//                .user_id("sender123..")
//                .password("password..")
//                .name("Sender User..")
//                .nickname("sender..")
//                .phone_num("010-1234-567899..")
//                .email("sender@example.com..")
//                .role(Role.USER)
//                .build();
//        userRepository.save(sender);
//
//        // 메시지 생성 및 저장
//        Message message = Message.builder()
//                .sender_uid(sender)
//                .chat_room_id(chatRoom)
//                .content("This is a test message...")
//                .read_person_num(0)
//                .build();
//
//        messageRepository.save(message);
//
//        // 메세지 신고 기능 테스트
//        Report report = Report.builder()
//                .report_type(ReportType.CLICKBAIT_SPAM)
//                .comment("스팸입니다")
//                .status(ReportStatus.RECEIVED)
//                .message_id(message)
//                .build();
//
//        reportRepository.save(report);
//    }
//
//    @Test
//    @Transactional
//    public void testDelete() {
//        Long report_id = 2L;
//
//        reportRepository.deleteById(report_id);
//
//        log.info("삭제 완료" + report_id);
//    }
//}