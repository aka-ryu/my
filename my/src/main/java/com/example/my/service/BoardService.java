package com.example.my.service;

import com.example.my.domain.ProtocolDTO.Board.BoardDeleteDTO;
import com.example.my.domain.ProtocolDTO.Board.BoardModifyDTO;
import com.example.my.domain.ProtocolDTO.Board.BoardRegisterDTO;
import com.example.my.domain.dto.BoardDTO;
import com.example.my.domain.ProtocolDTO.Board.page.PageRequestDTO;
import com.example.my.domain.ProtocolDTO.Board.page.PageResultDTO;
import com.example.my.domain.entity.Account;
import com.example.my.domain.entity.Board;
import com.example.my.domain.entity.Reply;
import com.example.my.repository.AccountRepo;
import com.example.my.repository.BoardRepo;
import com.example.my.repository.ReplyRepo;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class BoardService {


    private final BoardRepo boardRepo;
    private final ReplyRepo replyRepo;
    private final PageService pageService;
    private final AccountRepo accountRepo;
    private final Storage storage;


    public PageResultDTO<BoardDTO, Board> getList(PageRequestDTO pageRequestDTO) {

        Pageable pageable = pageRequestDTO.getPageable(Sort.by("id").descending());
        Page<Board> result = boardRepo.findAll(pageable);
        Function<Board, BoardDTO> fn = (entity -> pageService.entityToDto(entity));

        return new PageResultDTO<>(result, fn);
    }

    public BoardRegisterDTO boardRegister(BoardDTO boardDTO) throws IOException {

        Optional<Account> optionalAccount = accountRepo.findByUserid(boardDTO.getWriter());
        if (optionalAccount.isEmpty()) {
            log.error("???????????? ????????? ????????? ????????????");
            throw new RuntimeException("???????????? ????????? ????????? ????????????");
        }

        System.out.println("??????????");
        Account account = optionalAccount.get();

        Board board = Board.builder()
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .account(account)
                .isImg(0)
                .build();

        boardRepo.save(board);

        System.out.println("????????? ???????");
        if (boardDTO.getImg() != null) {
            BlobInfo blobInfo = BlobInfo.newBuilder(
                    "uhas2002",
                    board.getId().toString() + "/" + boardDTO.getImg().getOriginalFilename()
            ).build();
            storage.create(blobInfo, boardDTO.getImg().getBytes());

            Board imgBoard = Board.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .account(account)
                    .isImg(1)
                    .blobName(blobInfo.getName())
                    .bucket("uhas2002")
                    .build();

            boardRepo.save(imgBoard);
        }

        BoardRegisterDTO boardRegisterDTO = BoardRegisterDTO.builder()
                .success(true)
                .code(200)
                .message(board.getId() + " ??? ?????? ?????????????????????.")
                .build();

        return boardRegisterDTO;
    }

    public BoardDTO boardDetail(Long bno) {
        log.info("????????? ????????? ??????");
        Optional<Board> optionalBoard = boardRepo.findById(bno);
        if (optionalBoard.isEmpty()) {
            log.error("?????? ???????????? ????????????.");
            throw new RuntimeException("?????? ???????????? ????????????.");
        }

        Board board = optionalBoard.get();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd").withZone(ZoneId.systemDefault());

        BoardDTO boardDTO = BoardDTO.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getAccount().getUserid())
                .regdate(formatter.format(board.getCreateDate()))
                .build();

        if (board.getIsImg() == 1) {
            boardDTO.setImgURL("https://storage.googleapis.com/" + board.getBucket() + "/" + board.getBlobName());
        }

        return boardDTO;
    }


    public BoardDeleteDTO deleteBoard(Long bno) {
        Optional<Board> optionalBoard = boardRepo.findById(bno);
        if (optionalBoard.isEmpty()) {
            log.error("?????? ???????????? ?????? ????????? ?????????.");
            throw new RuntimeException("?????? ???????????? ?????? ????????? ?????????.");
        }

        Board board = optionalBoard.get();

        if (board.getIsImg() == 1) {
            BlobId blobId = BlobId.of(board.getBucket(), board.getBlobName());
            storage.delete(blobId);
        }

        List<Reply> result = replyRepo.findAllByBoard(board);
        result.forEach(reply -> {
            replyRepo.delete(reply);
        });
        boardRepo.delete(board);

        BoardDeleteDTO boardDeleteDTO = BoardDeleteDTO.builder()
                .code(200)
                .success(true)
                .message(bno + " ??? ?????? ??????????????? ?????????????????????.")
                .build();

        return boardDeleteDTO;
    }

    public BoardModifyDTO modifyBoard(BoardDTO boardDTO) throws IOException {

        Optional<Account> optionalAccount = accountRepo.findByUserid(boardDTO.getWriter());
        if (optionalAccount.isEmpty()) {
            log.error("???????????? ????????? ????????? ????????????");
            throw new RuntimeException("???????????? ????????? ????????? ????????????");
        }

        Account account = optionalAccount.get();

        Optional<Board> optionalBoard = boardRepo.findById(boardDTO.getId());
        if (optionalBoard.isEmpty()) {
            log.error("????????? ????????? ????????? ????????????.");
            throw new RuntimeException("????????? ????????? ????????? ????????????.");
        }

        Board originBoard = optionalBoard.get();

        if (boardDTO.getImg() != null && originBoard.getIsImg() == 0) {
            BlobInfo blobInfo = BlobInfo.newBuilder(
                    "uhas2002",
                    originBoard.getId().toString() + "/" + boardDTO.getImg().getOriginalFilename()
            ).build();
            storage.create(blobInfo, boardDTO.getImg().getBytes());

            Board board = Board.builder()
                    .id(originBoard.getId())
                    .title(boardDTO.getTitle())
                    .content(boardDTO.getContent())
                    .account(account)
                    .isImg(1)
                    .blobName(blobInfo.getName())
                    .bucket("uhas2002")
                    .build();

            boardRepo.save(board);

        } else if (boardDTO.getImg() != null && originBoard.getIsImg() == 1) {
            BlobId oroginBlobId = BlobId.of(originBoard.getBucket(), originBoard.getBlobName());
            storage.delete(oroginBlobId);

            BlobInfo blobInfo = BlobInfo.newBuilder(
                    "uhas2002",
                    originBoard.getId().toString() + "/" + boardDTO.getImg().getOriginalFilename()
            ).build();
            storage.create(blobInfo, boardDTO.getImg().getBytes());

            Board board = Board.builder()
                    .id(originBoard.getId())
                    .title(boardDTO.getTitle())
                    .content(boardDTO.getContent())
                    .account(account)
                    .isImg(1)
                    .blobName(blobInfo.getName())
                    .bucket("uhas2002")
                    .build();

            boardRepo.save(board);
        } else if (boardDTO.getImg() == null && originBoard.getIsImg() == 1 && boardDTO.isImgDelete())  {
            BlobId oroginBlobId = BlobId.of(originBoard.getBucket(), originBoard.getBlobName());
            storage.delete(oroginBlobId);

            Board board = Board.builder()
                    .id(originBoard.getId())
                    .title(boardDTO.getTitle())
                    .content(boardDTO.getContent())
                    .account(account)
                    .isImg(0)
                    .blobName(null)
                    .bucket(null)
                    .build();
            boardRepo.save(board);

        } else if (boardDTO.getImg() == null && originBoard.getIsImg() == 0) {
            Board board = Board.builder()
                    .id(originBoard.getId())
                    .title(boardDTO.getTitle())
                    .content(boardDTO.getContent())
                    .account(account)
                    .isImg(0)
                    .blobName(null)
                    .bucket(null)
                    .build();
            boardRepo.save(board);
        }


        BoardModifyDTO boardModifyDTO = BoardModifyDTO.builder()
                .success(true)
                .code(200)
                .message(boardDTO.getId() + " ??? ?????? ?????????????????????.")
                .build();

        return boardModifyDTO;
    }
}
