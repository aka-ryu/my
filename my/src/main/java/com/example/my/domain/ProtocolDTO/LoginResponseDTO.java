package com.example.my.domain.ProtocolDTO;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {

    private String userid;
    private String accessToken;
    private String refreshToken;
    private String message;
    private int code;

}
