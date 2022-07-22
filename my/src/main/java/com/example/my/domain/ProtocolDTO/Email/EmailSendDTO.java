package com.example.my.domain.ProtocolDTO.Email;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailSendDTO {
    private boolean success;
    private int code;
    private String message;
}
