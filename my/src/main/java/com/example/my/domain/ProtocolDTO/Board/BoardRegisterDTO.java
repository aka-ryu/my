package com.example.my.domain.ProtocolDTO.Board;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardRegisterDTO {

    private boolean success;
    private int code;
    private String message;
}
