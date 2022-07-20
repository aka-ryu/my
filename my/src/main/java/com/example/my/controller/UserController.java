package com.example.my.controller;

import com.example.my.domain.ProtocolDTO.LoggedDTO;
import com.example.my.domain.dto.AccountDTO;
import com.example.my.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Log4j2
public class UserController {

    private final AccountService accountService;



    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody AccountDTO accountDTO){

        log.info("register controller " + accountDTO.getUserid());
        return ResponseEntity.ok().body(accountService.registerUser(accountDTO));
    }



    @GetMapping("/token/refresh")
    public ResponseEntity tokenRefresh(@RequestHeader(value = "Authorization") String refreshToken) {

//        log.info(jwtToken);
//        String token = jwtToken.substring("Bearer ".length());
//
//        log.info("토큰 리프레쉬 컨트롤러");
        return ResponseEntity.ok().body(accountService.tokenRefresh(refreshToken));
    }


//    @GetMapping("/ck")
//    public ResponseEntity ck() {
//        log.info("ck접속");
//        return ResponseEntity.ok().body("cd");
//    }

//    @PostMapping("/logged")
//    public ResponseEntity loggedCheck(@RequestBody LoggedDTO loggedDTO) {
//        log.info("로그인 체크 접속");
//        return ResponseEntity.ok().body(accountService.loggedCheck(loggedDTO.getUserid()));
//    }



}
