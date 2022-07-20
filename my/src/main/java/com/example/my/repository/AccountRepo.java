package com.example.my.repository;

import com.example.my.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {

    Optional<Account> findByUserid(String Userid);
    Optional<Account> findByNickname(String nickname);

    Optional<Account> findByEmail(String email);

    Optional<Account> findByJwtToken(String JwtToken);
}
