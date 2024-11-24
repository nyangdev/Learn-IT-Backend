package com.example.microstone.repository;

import com.example.microstone.domain.PostRecommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRecommendRepository extends JpaRepository<PostRecommend, Long> {

    @Query("SELECT COUNT(p) > 0 FROM PostRecommend p WHERE p.id.postId = :post AND p.id.userId = :user")
    boolean existsPostRecommendBy(@Param("post") Long post, @Param("user") Long user);

    @Query("SELECT p FROM PostRecommend p WHERE p.id.postId = :post AND p.id.userId = :user")
    public PostRecommend findPostRecommend(@Param("post") Long post, @Param("user") Long user);

}
