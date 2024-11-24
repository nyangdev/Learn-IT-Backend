package com.example.microstone.repository;

import com.example.microstone.domain.Enum.Category;
import com.example.microstone.domain.Post;
import com.example.microstone.domain.StudyGroup;
import com.example.microstone.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE p.deleted_at IS NULL AND p.group_type = com.example.microstone.domain.Enum.GroupType.PUBLIC AND p.category = :category ORDER BY p.post_id DESC")
    Page<Post> findCategoryAllPublicPosts(Pageable pageable, @Param("category") Category category);

    @Query("SELECT p FROM Post p WHERE p.deleted_at IS NULL AND p.group_type = com.example.microstone.domain.Enum.GroupType.STUDY_GROUP AND p.category = :category AND p.group_id = :group_id ORDER BY p.post_id DESC")
    Page<Post> findCategoryAllStudyGroupPosts(Pageable pageable, @Param("category") Category category, @Param("group_id")StudyGroup group_id);

    @Query("SELECT p FROM Post p WHERE p.deleted_at IS NULL AND p.group_type = com.example.microstone.domain.Enum.GroupType.PUBLIC ORDER BY p.post_id DESC")
    Page<Post> findAllPublicPosts(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.deleted_at IS NULL AND p.group_type = com.example.microstone.domain.Enum.GroupType.STUDY_GROUP AND p.group_id = :group_id ORDER BY p.post_id DESC")
    Page<Post> findAllStudyGroupPosts(Pageable pageable, @Param("group_id")StudyGroup group_id);


    @Query("SELECT LAST_INSERT_ID() FROM Post")
    Long findCurrentId();

    @Query("SELECT p FROM Post p WHERE p.deleted_at IS NOT NULL")
    List<Post> findAllDeletedPosts();

    // 탈퇴_민지
    @Query("SELECT p FROM Post p WHERE p.uid = :user AND p.group_id = :studyGroup")
    List<Post> findByUserAndGroupId(@Param("user") User user, @Param("studyGroup") StudyGroup studyGroup);

    // 스터디 그룹 내 게시판에서 키워드로 검색 (카테고리 선택)
    @Query("SELECT p FROM Post p WHERE p.group_id.group_id = :group_id AND p.title LIKE %:keyword% AND p.category = :category")
    List<Post> findByStudyGroupAndKeywordAndCategory(@Param("group_id") Long group_id,
                                                     @Param("keyword") String keyword,
                                                     @Param("category") Category category);

    // 스터디 그룹 내 전체 게시판에서 키워드로 검색
    @Query("SELECT p FROM Post p WHERE p.group_id.group_id = :group_id AND p.title LIKE %:keyword%")
    List<Post> findByStudyGroupAndKeyword(@Param("group_id") Long group_id, @Param("keyword") String keyword);

    // 스터디그룹 제외하고 공개 게시판에서 검색시
    // 전체 게시판에서 검색
    @Query("SELECT p FROM Post p WHERE p.group_id IS NULL AND (p.title LIKE %:keyword% OR p.content LIKE %:keyword%)")
    List<Post> findByKeywordInGeneral(@Param("keyword") String keyword);

    // 특정 게시판에서 검색
    @Query("SELECT p FROM Post p WHERE p.group_id IS NULL AND (p.title LIKE %:keyword% OR p.content LIKE %:keyword%) AND p.category = :category")
    List<Post> findByKeywordInGeneralAndCategory(@Param("keyword") String keyword, @Param("category") Category category);

    @Query("SELECT p.recommend_num FROM Post p WHERE p.deleted_at IS NULL AND p.post_id = :post_id")
    int getRecommendNum(@Param("post_id") Long post_id);

    @Query("SELECT p.not_recommend_num FROM Post p WHERE p.deleted_at IS NULL AND p.post_id = :post_id")
    int getNotRecommendNum(@Param("post_id") Long post_id);
}