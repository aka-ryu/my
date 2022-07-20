package com.example.my.domain.ProtocolDTO;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoggedDTO {

    private String userid;

    private String message;
    private int code;
    private boolean success;

}
