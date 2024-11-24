package com.example.microstone.testInit;

import com.example.microstone.domain.Board;
import com.example.microstone.domain.Enum.*;
import com.example.microstone.domain.Post;
import com.example.microstone.domain.Reply;
import com.example.microstone.domain.User;
import com.example.microstone.repository.BoardRepository;
import com.example.microstone.repository.PostRepository;
import com.example.microstone.repository.ReplyRepository;
import com.example.microstone.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;

@SpringBootTest
@Log4j2
public class TestInit {
    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    BoardRepository boardRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @Transactional
    @Commit
    public void testInsert() {

        for(int i=0; i<30;i++) {
            // 테스트용 유저 생성
            User user = User.builder()
                    .user_id("js_clone123" +i)
                    .password(passwordEncoder.encode("jsjsjs123" + i))
                    .name("김준성"+i)
                    .nickname("나준성이다"+i)
                    .phone_num("010-5678-1234"+i)
                    .email("jsispm123@naver.com"+i)
                    .role(Role.USER)
                    .department(Department.ART)
                    .occupation(Occupation.STUDENT)
                    .build();

            userRepository.save(user);

            // 테스트용 board 생성
            Board board = Board.builder()
                    .board_name("준성이 대학원가라빔 게시판"+i)
                    .category(Category.FREE_BOARD)
                    .build();

            boardRepository.save(board);

            // 테스트용 post
            Post post = Post.builder()
                    .board_id(board)
                    .uid(user)
                    .category(Category.FREE_BOARD)
                    .group_type(GroupType.STUDY_GROUP)
                    .title("조하늘학생은 스터디그룹에 갑니다.")
                    .content("준성이가 이렇게 써달래요")
                    .recommend_num(7)
                    .not_recommend_num(1)
                    .views_num(100)
                    .reply_num(0)
                    .build();

            postRepository.save(post);

            Post post2 = Post.builder()
                    .board_id(board)
                    .uid(user)
                    .category(Category.FREE_BOARD)
                    .group_type(GroupType.PUBLIC)
                    .title("조하늘학생은 대학원에 갑니다.")
                    .content("준성이가 이렇게 써달래요")
                    .recommend_num(7)
                    .not_recommend_num(1)
                    .views_num(100)
                    .reply_num(0)
                    .build();

            postRepository.save(post2);

            // 테스트용 댓글
            for (int j = 1; j <= 3; j++) {
                Reply reply = Reply.builder()
                        .uid(user)
                        .content("제발요..."+ (i * 1 + j))
                        .recommand_num(100)
                        .group_type(GroupType.PUBLIC)
                        .post_id(post)
                        .build();
                replyRepository.save(reply);
            }

            for (int j = 1; j <= 3; j++) {
                Reply reply = Reply.builder()
                        .uid(user)
                        .content("제발요..."+ (i * 1 + j))
                        .recommand_num(100)
                        .group_type(GroupType.PUBLIC)
                        .post_id(post2)
                        .build();
                replyRepository.save(reply);
            }
        }
    }
}
