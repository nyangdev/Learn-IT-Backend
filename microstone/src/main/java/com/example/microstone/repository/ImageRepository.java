package com.example.microstone.repository;

import com.example.microstone.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ImageRepository extends JpaRepository<Image, Long> {
    @Query("SELECT LAST_INSERT_ID() FROM Image")
    Long findCurrentId();
}
