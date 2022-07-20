package com.example.my.security.service;

import com.example.my.domain.entity.Account;
import com.example.my.repository.AccountRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final AccountRepo accountRepo;

    @Override
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
        log.info("loadUserByUsername " + userid);
        Optional<Account> optionalUser = accountRepo.findByUserid(userid);

        if (optionalUser.isEmpty()) {
            log.error("User not found " + userid);
            throw new UsernameNotFoundException("User not found " + userid);
        }

        Account account = optionalUser.get();
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        account.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.name()));
        });


        return new User(account.getUserid(), account.getPassword(), authorities);
    }
}
