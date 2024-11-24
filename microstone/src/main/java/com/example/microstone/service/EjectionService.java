package com.example.microstone.service;

import com.example.microstone.domain.Ejection;
import com.example.microstone.domain.User;
import com.example.microstone.repository.EjectionRepository;
import com.example.microstone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EjectionService {

    // 유저 추방
    @Autowired
    private EjectionRepository ejectionRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Ejection ejectUser(Long ejectionUid, Long adminUid, String reason) {

        User ejectionUser = userRepository.findById(ejectionUid)
                .orElseThrow(() -> new RuntimeException("User not found " + ejectionUid));

        User adminUser = userRepository.findById(adminUid)
                .orElseThrow(() -> new RuntimeException("User not found " + adminUid));

        Ejection ejection = Ejection.builder()
                .ejection_uid(ejectionUser)
                .admin_uid(adminUser)
                .ejected_reason(reason)
                .user_email(ejectionUser.getEmail())
                .build();

        // 추방 정보 저장
        ejectionRepository.save(ejection);

        // 유저 삭제 처리
        userRepository.delete(ejectionUser);

        return ejection;
    }
}
