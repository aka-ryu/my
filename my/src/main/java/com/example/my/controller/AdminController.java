package com.example.my.controller;

import com.example.my.domain.ProtocolDTO.UserListRespDTO;
import com.example.my.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Log4j2
public class AdminController {

    private AdminService adminService;

    @GetMapping("/userlist")
    public ResponseEntity userList(){
        log.info("유저리스트 진입");

        UserListRespDTO userListRespDTO = adminService.userList();


        return ResponseEntity.ok().body(userListRespDTO);
    }
}
