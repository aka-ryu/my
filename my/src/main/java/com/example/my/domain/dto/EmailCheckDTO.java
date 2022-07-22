package com.example.my.domain.dto;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmailCheckDTO {

    private String email;

    private int code;
}
