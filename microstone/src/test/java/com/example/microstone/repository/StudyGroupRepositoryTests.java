//package com.example.microstone.repository;
//
//import com.example.microstone.domain.StudyGroup;
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
//public class StudyGroupRepositoryTests {
//
//    @Autowired
//    private StudyGroupRepository studyGroupRepository;
//
//    // insert
//    @Test
//    @Transactional
//    public void testInsert() {
//        for (int i = 0; i < 10; i++) {
//
//            StudyGroup studyGroup = StudyGroup.builder()
//                    .group_name("GroupName.." + i)
//                    .present_member_num(10 + i)
//                    .max_member_num(50+i)
//                    .build();
//
//            studyGroupRepository.save(studyGroup);
//        }
//    }
//
//    // max test
//    @Test
//    @Transactional
//    public void testMax() {
//        StudyGroup studyGroup = StudyGroup.builder()
//                .group_name("GroupName..max")
//                .present_member_num(30)
//                .max_member_num(101)
//                .build();
//
//        studyGroupRepository.save(studyGroup);
//    }
//
//    // select
//    @Test
//    @Transactional
//    public void testRead() {
//        Long group_id = 5L;
//
//        java.util.Optional<StudyGroup> result = studyGroupRepository.findById(group_id);
//
//        StudyGroup studyGroup = result.orElseThrow();
//
//        log.info("-----------StudyGroup 데이터 조회-----------");
//        log.info(studyGroup);
//        log.info(studyGroup.getGroup_id());
//        log.info(studyGroup.getGroup_name());
//        log.info(studyGroup.getPresent_member_num());
//        log.info(studyGroup.getMax_member_num());
//    }
//
//    // update
//    @Test
//    @Transactional
//    public void testUpdate() {
//        Long group_id = 6L;
//
//        Optional<StudyGroup> result = studyGroupRepository.findById(group_id);
//
//        StudyGroup studyGroup = result.orElseThrow();
//        studyGroup.changeGroupName("Update...6");
//
//        studyGroupRepository.save(studyGroup);
//    }
//
//    // delete
//    @Test
//    @Transactional
//    public void testDelete() {
//        Long group_id = 3L;
//
//        studyGroupRepository.deleteById(group_id);
//    }
//}