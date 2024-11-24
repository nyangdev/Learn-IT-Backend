//package com.example.microstone.repository;
//
//import com.example.microstone.domain.Board;
//import lombok.extern.log4j.Log4j2;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Optional;
//
//@SpringBootTest
//@Log4j2
//public class BoardRepositoryTests {
//
//    @Autowired
//    private BoardRepository boardRepository;
//
//    // board Insert test
//    @Test
//    @Transactional
//    public void testInsert() {
//
//        for (int i = 0; i < 10; i++) {
//            Board board = Board.builder()
//                    .board_name("name..." + i)
//                    .build();
//
//            boardRepository.save(board);
//        }
//    }
//
//    // select
//    @Test
//    @Transactional
//    public void testRead() {
//
//        Long board_id = 1L;
//
//        java.util.Optional<Board> result = boardRepository.findById(board_id);
//
//        Board board = result.orElseThrow();
//
//        log.info("-----------board 데이터 조회-----------");
//        log.info(board);
//        log.info(board.getBoard_id());
//        log.info(board.getBoard_name());
//    }
//
//    // update
//    @Test
//    @Transactional
//    public void testModify() {
//        Long board_id = 5L;
//
//        Optional<Board> result = boardRepository.findById(board_id);
//
//        Board board = result.orElseThrow();
//        board.changeBoardName("Modified 5....");
//
//        boardRepository.save(board);
//    }
//
//    // delete
//    @Test
//    @Transactional
//    public void testDelete() {
//        Long board_id = 3L;
//
//        boardRepository.deleteById(board_id);
//
//        log.info("삭제 완료" + board_id);
//    }
//}
