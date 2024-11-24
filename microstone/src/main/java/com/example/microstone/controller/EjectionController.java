package com.example.microstone.controller;

import com.example.microstone.domain.Ejection;
import com.example.microstone.dto.EjectionRequestDTO;
import com.example.microstone.service.EjectionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/ejection")
public class EjectionController {

    @Autowired
    private EjectionService ejectionService;

    // 유저 추방 API
    @PostMapping("/eject")
    public Ejection ejectUser(@RequestBody EjectionRequestDTO ejectionRequestDTO,
                              Authentication authentication) {

        Long adminUid = (Long)authentication.getPrincipal();

        return ejectionService.ejectUser(
                ejectionRequestDTO.getEjectionUid(),
                adminUid,
                ejectionRequestDTO.getReason()
        );
    }
}
