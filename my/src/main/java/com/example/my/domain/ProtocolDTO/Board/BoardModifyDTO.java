package com.example.my.domain.ProtocolDTO.Board;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardModifyDTO {

    private boolean success;
    private int code;
    private String message;
}
