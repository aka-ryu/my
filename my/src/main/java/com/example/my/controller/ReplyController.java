package com.example.my.controller;

import com.example.my.domain.dto.ReplyDTO;
import com.example.my.domain.entity.Reply;
import com.example.my.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/reply")
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("/register")
    public ResponseEntity replyRegister(@RequestBody ReplyDTO replyDTO) {
        return ResponseEntity.ok().body(replyService.replyRegister(replyDTO));
    }

    @GetMapping("/list")
    public ResponseEntity replyList(@RequestParam Long bno) {
        return ResponseEntity.ok().body(replyService.getList(bno));
    }

    @DeleteMapping("/delete")
    public ResponseEntity replyDelete(@RequestParam Long rno) {
        return ResponseEntity.ok().body(replyService.deleteReply(rno));
    }
}
