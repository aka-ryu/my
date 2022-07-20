package com.example.my.domain.ProtocolDTO.Jwt;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoJwtDTO {

    private boolean hasToken;
    private int code;
    private String message;
    private String exceptionName;
}
