package com.example.my.controller;

import com.example.my.domain.ProtocolDTO.Board.page.PageRequestDTO;
import com.example.my.domain.dto.BoardDTO;
import com.example.my.service.BoardService;
import com.nimbusds.jose.util.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/list")
    public ResponseEntity boardList(PageRequestDTO pageRequestDTO){
        return ResponseEntity.ok().body(boardService.getList(pageRequestDTO));
    }

    @PostMapping("/register")
    public ResponseEntity boardRegister(@ModelAttribute BoardDTO boardDTO) throws IOException {
        return ResponseEntity.ok().body(boardService.boardRegister(boardDTO));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/detail")
    public ResponseEntity getBoard(@RequestParam Long bno) {
        return ResponseEntity.ok().body(boardService.boardDetail(bno));
    }
    
    @DeleteMapping("/delete")
    public ResponseEntity deleteBoard(@RequestParam Long bno){
        return ResponseEntity.ok().body(boardService.deleteBoard(bno));
    }

    @PutMapping("/modify")
    public ResponseEntity modifyBoard(@ModelAttribute BoardDTO boardDTO) throws IOException {
        return ResponseEntity.ok().body(boardService.modifyBoard(boardDTO));
    }

}
