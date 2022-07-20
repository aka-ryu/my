package com.example.my.reply;

import com.example.my.domain.entity.Board;
import com.example.my.domain.entity.Reply;
import com.example.my.repository.BoardRepo;
import com.example.my.repository.ReplyRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest

public class replyTests {

    @Autowired
    private BoardRepo boardRepo;

    @Autowired
    private ReplyRepo replyRepo;

    @Test
    public void getList() {

        Long bno = 136L;

        Optional<Board> optionalBoard = boardRepo.findById(bno);
        if(optionalBoard.isEmpty()) {
            System.out.println("게시글 정보가 정확치 않습니다.");
            throw new RuntimeException("게시글 정보가 정확치 않습니다.");
        }
        System.out.println("여긴 하니?");
        Board board = optionalBoard.get();

        System.out.println(replyRepo.findAllByBoard(board));

    }

}
