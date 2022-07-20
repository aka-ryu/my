package com.example.my.domain.ProtocolDTO.Reply;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDeleteDTO {

    private boolean success;
    private int code;
    private String message;
}
