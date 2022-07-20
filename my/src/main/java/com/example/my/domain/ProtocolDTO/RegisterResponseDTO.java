package com.example.my.domain.ProtocolDTO;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponseDTO {

    private Object data;
    private String message;
    private int code;
}
