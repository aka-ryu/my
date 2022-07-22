package com.example.my.repository;

import com.example.my.domain.entity.EmailCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailCheckRepo extends JpaRepository<EmailCheck, Long> {

    List<EmailCheck> findAllByEmail(String email);

    Optional<EmailCheck> findByEmailAndIsChecked(String email, boolean isChecked);

    Optional<EmailCheck> findByEmailAndCode(String email, int code);
}
