package com.example.microstone.service;

import com.example.microstone.domain.Board;
import com.example.microstone.domain.Enum.Category;
import com.example.microstone.domain.StudyGroup;
import com.example.microstone.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    // Board 생성 관련 로직
    // 게시판 종류는 정해져있음 - 자유, 문제공유, 질문
    // 전체 공개 게시판에 세 종류가 대표적으로 있고,
    // 스터디그룹이 생성될때마다 스터디그룹 내에 할당되는 세가지 게시판이 생긴다.

    // 시스템 시작 시 전체 공개 게시판이 존재하는지 확인하고 없으면 3개의 게시판을 생성한다
    // 데이터베이스에서 직접적으로 접근해서 추가하는 방식을 사용한다

    // 스터디그룹 내부에서만 접근 가능한 board
    // 스터디그룹 내 게시판 생성
    public void createStudyGroupBoard(StudyGroup studyGroup) {
        createBoardForGroup("질문 게시판", Category.QUESTION_BOARD, studyGroup);
        createBoardForGroup("문제공유 게시판", Category.PROBLEM_SHARE_BOARD, studyGroup);
        createBoardForGroup("자유 게시판", Category.FREE_BOARD, studyGroup);
    }

    private void createBoardForGroup(String board_name, Category category, StudyGroup studyGroup) {
        Board board = Board.builder()
                .board_name(board_name)
                .category(category)
                .group_id(studyGroup)
                .isPublic(false)
                .build();

        boardRepository.save(board);
    }
}
