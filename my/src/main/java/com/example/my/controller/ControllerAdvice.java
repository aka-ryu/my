package com.example.my.controller;

import com.example.my.domain.ProtocolDTO.RegisterResponseDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RestController
@Log4j2
public class ControllerAdvice {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> exceptionHandle(Exception e) {
        log.info("에러발동");
        log.info(e.getMessage());
        RegisterResponseDTO registerResponseDTO = RegisterResponseDTO.builder()
                .code(400)
                .message(e.getMessage())
                .build();
        //dsds



        return ResponseEntity.ok().body(registerResponseDTO);
    }
}
