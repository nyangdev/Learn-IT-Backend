package com.example.microstone.repository;

import com.example.microstone.domain.Board;
import com.example.microstone.domain.Enum.Role;
import com.example.microstone.domain.Post;
import com.example.microstone.domain.User;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Log4j2
public class PostRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    PostRepository postRepository;

    @Test
    @Transactional
    public void testInsert(){

        User user = User.builder()
                .user_id("js_clone123")
                .password("jsjsjs123")
                .name("김준성")
                .nickname("나준성인데오늘부터팀장한다진짜다")
                .phone_num("010-5678-1234")
                .email("jsispm123@naver.com")
                .role(Role.USER)
                .build();

            userRepository.save(user);

            // 테스트용 board 생성
            Board board = Board.builder()
                    .board_name("준성이 대학원가라빔 게시판")
                    .build();

            boardRepository.save(board);

            // 테스트용 post
            Post post = Post.builder()
                    .board_id(board)
                    .uid(user)
                    .title("응 아니야")
                    .content("조하늘 왈")
//                    .post_upload_date(LocalDateTime.now())
                    .recommend_num(8)
                    .not_recommend_num(10)
                    .views_num(1000)
                    .reply_num(2)
                    .build();

            postRepository.save(post);
        }

    // 재테스트
    @Test
    @Transactional
    @Rollback(false)
    public void testInsert2(){

        // 테스트용 board 생성
        Board board = Board.builder()
                .board_name("테스트 보드")
                .build();

        boardRepository.save(board);

        for(int i=0; i<10; i++){
            User user = User.builder()
                    .user_id("id" + i)
                    .password("1234")
                    .name("김민지")
                    .nickname("민지" + i)
                    .phone_num("010" + i)
                    .email("mingdeee" + i)
                    .role(Role.USER)
                    .build();
            userRepository.save(user);

            // 테스트용 post
            Post post = Post.builder()
                    .board_id(board)
                    .uid(user)
                    .title("응 아니야")
                    .content("조하늘 왈")
//                    .post_upload_date(LocalDateTime.now())
                    .recommend_num(8)
                    .not_recommend_num(10)
                    .views_num(1000)
                    .reply_num(2)
                    .build();

            postRepository.save(post);
        }
    }

}

