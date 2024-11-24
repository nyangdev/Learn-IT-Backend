package com.example.microstone.repository;

import com.example.microstone.domain.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {
    @Query("SELECT sg FROM StudyGroup sg WHERE sg.deleted_at IS NULL")
    List<StudyGroup> findAllActiveGroups();

    @Query("SELECT sg FROM StudyGroup sg WHERE sg.admin = :admin AND sg.deleted_at IS NULL")
    List<StudyGroup> findActiveGroupsByAdmin(@Param("admin") String admin);
}
