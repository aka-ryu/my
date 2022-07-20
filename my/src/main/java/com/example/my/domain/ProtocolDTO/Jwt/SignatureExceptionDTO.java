package com.example.my.domain.ProtocolDTO.Jwt;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignatureExceptionDTO {

    private int code;
    private String message;
    private String exceptionName;
}
