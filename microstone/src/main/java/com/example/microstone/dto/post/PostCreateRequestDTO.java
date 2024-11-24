package com.example.microstone.dto.post;

import lombok.*;


// 게시글 생성 시 필요한 정보를 담고있는 DTO
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PostCreateRequestDTO {

    private Long board_id; // 게시판 고유 번호

    private String title; // 게시글 제목

    private String content; // 게시글 내용

    private String category;

//    private String content_image; // 게시글 이미지

    // 스터디그룹_이슈_전체수정
    private Long group_id; // 스터디그룹 ID

    private String group_type; // enum
}