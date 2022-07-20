package com.example.my.domain.dto;

import com.example.my.domain.entity.Account;
import com.example.my.domain.entity.Board;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReplyDTO {

    private Long id;

    private Long boardId;

    private String replyContent;

    private String replyWriter;

    private String regdate;
}
