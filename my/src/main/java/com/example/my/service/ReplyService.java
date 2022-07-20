package com.example.my.service;

import com.example.my.domain.ProtocolDTO.Reply.ReplyDeleteDTO;
import com.example.my.domain.ProtocolDTO.Reply.ReplyListDTO;
import com.example.my.domain.ProtocolDTO.Reply.ReplyRegisterDTO;
import com.example.my.domain.dto.ReplyDTO;
import com.example.my.domain.entity.Account;
import com.example.my.domain.entity.Board;
import com.example.my.domain.entity.Reply;
import com.example.my.repository.AccountRepo;
import com.example.my.repository.BoardRepo;
import com.example.my.repository.ReplyRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ReplyService {

    private final ReplyRepo replyRepo;
    private final AccountRepo accountRepo;
    private final BoardRepo boardRepo;


    public ReplyRegisterDTO replyRegister(ReplyDTO replyDTO) {
        Optional<Account> optionalAccount = accountRepo.findByUserid(replyDTO.getReplyWriter());
        if(optionalAccount.isEmpty()) {
            log.error("작성자의 정보가 정확치 않습니다");
            throw new RuntimeException("작성자의 정보가 정확치 않습니다");
        }

        Optional<Board> optionalBoard = boardRepo.findById(replyDTO.getBoardId());
        if(optionalBoard.isEmpty()) {
            log.error("게시글 정보가 정확치 않습니다.");
            throw new RuntimeException("게시글 정보가 정확치 않습니다.");
        }

        Account account = optionalAccount.get();
        Board board = optionalBoard.get();

        Reply reply = Reply.builder()
                .board(board)
                .replyContent(replyDTO.getReplyContent())
                .account(account)
                .build();

        replyRepo.save(reply);

        ReplyRegisterDTO replyRegisterDTO = ReplyRegisterDTO.builder()
                .success(true)
                .code(200)
                .message(reply.getId() + "번 댓글이 등록되었습니다.")
                .build();

        return replyRegisterDTO;
    }

    public ReplyListDTO getList(Long bno) {
        Optional<Board> optionalBoard = boardRepo.findById(bno);
        if(optionalBoard.isEmpty()) {
            log.error("게시글 정보가 정확치 않습니다.");
            throw new RuntimeException("게시글 정보가 정확치 않습니다.");
        }
        
        Board board = optionalBoard.get();

        List<Reply> result = replyRepo.findAllByBoard(board);
        List<ReplyDTO> replyDTOList = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").withZone(ZoneId.systemDefault());
        result.forEach(reply -> {
            ReplyDTO replyDTO = ReplyDTO.builder()
                    .id(reply.getId())
                    .boardId(board.getId())
                    .replyWriter(reply.getAccount().getNickname())
                    .regdate(formatter.format(reply.getCreateDate()))
                    .replyContent(reply.getReplyContent())
                    .build();
            replyDTOList.add(replyDTO);
        });

        ReplyListDTO replyListDTO = ReplyListDTO.builder()
                .success(true)
                .replyDTOList(replyDTOList)
                .message(bno + "번의 댓글리스트")
                .build();

        return replyListDTO;
    }

    public ReplyDeleteDTO deleteReply(Long rno) {
        Optional<Reply> optionalReply = replyRepo.findById(rno);
        if(optionalReply.isEmpty()) {
            log.error("댓글 정보가 정확치 않습니다.");
            throw new RuntimeException("댓글 정보가 정확치 않습니다.");
        }

        Reply reply = optionalReply.get();

        replyRepo.delete(reply);

        ReplyDeleteDTO replyDeleteDTO = ReplyDeleteDTO.builder()
                .code(200)
                .message(rno + "번 댓글이 삭제되었습니다.")
                .success(true)
                .build();

        return replyDeleteDTO;
    }

}
