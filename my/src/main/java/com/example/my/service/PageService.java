package com.example.my.service;

import com.example.my.domain.dto.BoardDTO;
import com.example.my.domain.entity.Board;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Log4j2
public class PageService {

    private final ModelMapper modelMapper;

//    Board dtoToEntity(BoardDTO boardDTO) {
//        Board board = modelMapper.map(boardDTO, Board.class);
//        return board;
//    }

    BoardDTO entityToDto(Board board) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd").withZone(ZoneId.systemDefault());
        String regdate = formatter.format(board.getCreateDate());

        BoardDTO boardDTO = BoardDTO.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getAccount().getUserid())
                .regdate(regdate)
                .build();

        return boardDTO;
    }
}
