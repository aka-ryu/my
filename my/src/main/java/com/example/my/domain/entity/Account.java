package com.example.my.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Account extends TimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String userid;

    @Column(nullable = true)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column
    private boolean fromSocial;

    @Column
    private int money;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private Set<Role> roles = new HashSet<>();


    private String jwtToken;


    @Override
    public LocalDateTime getLastModifiedDate() {
        return super.getLastModifiedDate();
    }

    @Override
    public LocalDateTime getCreateDate() {
        return super.getCreateDate();
    }

    public void addUserRole(Role role) {
        roles.add(role);
    }

    public void setRefreshToken(String token) {
        setJwtToken(token);
    }

}
