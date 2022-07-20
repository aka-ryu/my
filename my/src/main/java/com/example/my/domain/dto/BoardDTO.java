package com.example.my.domain.dto;

import com.example.my.domain.entity.Account;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BoardDTO {

    private Long id;

    private String title;

    private String content;

    private String writer;

    private String regdate;

    private MultipartFile img;

    private String imgURL;

    private boolean imgDelete;
}
