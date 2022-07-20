package com.example.my.domain.ProtocolDTO.Jwt;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtDeniedDTO {

    private int code;
    private String message;
    // 만료여부 true=만료 , false=유효
    private boolean expired;
    // 유효여부 (기간만료 등의 발생할수 있는 거부가 아닌 경우 해킹위험이 있음으로 로그아웃시킬예정)
    private int valid;
}
