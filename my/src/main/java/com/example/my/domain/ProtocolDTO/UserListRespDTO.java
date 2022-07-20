package com.example.my.domain.ProtocolDTO;

import com.example.my.domain.dto.AccountDTO;
import lombok.*;

import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserListRespDTO {

    private int code;
    private boolean success;
    private List<AccountDTO> data;
}
