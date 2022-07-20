package com.example.my.domain.dto;

import com.example.my.domain.entity.Role;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AccountDTO {

    private String userid;

    private String password;

    private String email;

    private String nickname;

    private boolean fromSocial;

    private int money;

    private Collection<Role> roles = new ArrayList<>();
}
