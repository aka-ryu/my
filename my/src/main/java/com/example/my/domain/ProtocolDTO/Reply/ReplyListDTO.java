package com.example.my.domain.ProtocolDTO.Reply;

import com.example.my.domain.dto.ReplyDTO;
import lombok.*;

import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyListDTO {

    private boolean success;
    private List<ReplyDTO> replyDTOList;
    private String message;
}
