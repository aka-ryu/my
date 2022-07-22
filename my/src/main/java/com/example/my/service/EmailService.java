package com.example.my.service;

import com.example.my.domain.ProtocolDTO.Email.EmailSendDTO;
import com.example.my.domain.dto.EmailCheckDTO;
import com.example.my.domain.entity.Account;
import com.example.my.domain.entity.EmailCheck;
import com.example.my.repository.AccountRepo;
import com.example.my.repository.EmailCheckRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailCheckRepo emailCheckRepo;
    private final AccountRepo accountRepo;

    @Value("${spring.mail.username}")
    private String sender;

    public EmailSendDTO sendEmail(EmailCheckDTO emailCheckDTO) {
        Optional<Account> optionalAccount = accountRepo.findByEmail(emailCheckDTO.getEmail());

        if(optionalAccount.isPresent()) {
            log.error("이미 가입된 이메일 입니다.");
            throw new RuntimeException("이미 가입된 이메일 입니다.");
        }

        List<EmailCheck> result = emailCheckRepo.findAllByEmail(emailCheckDTO.getEmail());
        result.forEach(emailCheck -> {
            emailCheckRepo.delete(emailCheck);
        });


        Random random = new Random();
        int bound = 999999;
        int code = random.nextInt(999999);
        EmailCheck emailCheck = EmailCheck.builder()
                .email(emailCheckDTO.getEmail())
                .code((int)(Math.random() * (999999 - 100000 + 1)) + 100000)
                .isChecked(false)
                .build();

        emailCheckRepo.save(emailCheck);



        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailCheckDTO.getEmail());
        message.setFrom(sender);
        message.setSubject("Sign up Code");
        message.setText(Integer.toString(emailCheck.getCode()));
        mailSender.send(message);

        EmailSendDTO emailSendDTO = EmailSendDTO.builder()
                .success(true)
                .code(200)
                .message(emailCheck.getEmail() + "로 인증코드가 발송되었습니다.")
                .build();

        return emailSendDTO;
    }


    public EmailSendDTO certificationEmail (EmailCheckDTO emailCheckDTO) {

        Optional<EmailCheck> optionalEmailCheck = emailCheckRepo.findByEmailAndCode(emailCheckDTO.getEmail(), emailCheckDTO.getCode());

        if(optionalEmailCheck.isEmpty()) {
            log.error("인증번호가 올바르지 않습니다.");
            throw new RuntimeException("인증번호가 올바르지 않습니다.");
        }

        EmailCheck emailCheck = optionalEmailCheck.get();
        emailCheck.setIsChecked(true);

        emailCheckRepo.save(emailCheck);

        EmailSendDTO emailSendDTO = EmailSendDTO.builder()
                .code(200)
                .success(true)
                .message("이메일 인증이 완료되었습니다.")
                .build();

        return emailSendDTO;
    }

}
