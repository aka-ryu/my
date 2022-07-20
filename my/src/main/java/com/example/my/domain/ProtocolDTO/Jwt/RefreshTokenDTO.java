package com.example.my.domain.ProtocolDTO.Jwt;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenDTO {

    private boolean success;
    private String message;
    private String accessToken;
    private String refreshToken;
    private boolean logout;
}
