package com.example.my.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.example.my.domain.dto.EmailCheckDTO;
import com.example.my.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/code")
    public ResponseEntity emailCodeSend(@RequestBody EmailCheckDTO emailCheckDTO) {
        return ResponseEntity.ok().body(emailService.sendEmail(emailCheckDTO));
    }

    @PostMapping("/certification")
    public ResponseEntity emailCertification(@RequestBody EmailCheckDTO emailCheckDTO) {
        return ResponseEntity.ok().body(emailService.certificationEmail(emailCheckDTO));
    }

}
