//package com.example.my.security.dto;
//
//import com.example.my.domain.entity.Account;
//import lombok.Data;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//
//import java.util.Collection;
//
//@Data
//public class AccountContext extends User {
//
//    private User user;
//
//    public AccountContext(User user, Collection<? extends GrantedAuthority> authorities) {
//        super(user.getUsername(), user.getPassword(), authorities);
//        this.user = user;
//    }
//}
