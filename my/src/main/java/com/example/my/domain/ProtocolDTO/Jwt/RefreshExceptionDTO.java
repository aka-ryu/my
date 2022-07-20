package com.example.my.domain.ProtocolDTO.Jwt;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshExceptionDTO {

    private int code;
    private String message;
    private String exceptionName;
}
