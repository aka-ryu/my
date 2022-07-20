package com.example.my.user;

import antlr.Token;
import com.example.my.domain.dto.AccountDTO;
import com.example.my.domain.entity.Account;
import com.example.my.repository.AccountRepo;
import com.example.my.security.jwt.TokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.regex.Pattern;

@SpringBootTest
public class AccountSeviceTests {

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    // 회원가입 테스트
    @Test
    public void registerUser() {
        for (int i = 0; i < 1; i++) {
            AccountDTO accountDTO = AccountDTO.builder()
                    .userid("하이" + i)
                    .password(passwordEncoder.encode("test" + i))
                    .email("하이@하이" + i)
                    .nickname("하이" + i)
                    .build();

            Account account = modelMapper.map(accountDTO, Account.class);
            accountRepo.save(account);
            System.out.println(account.getId() + " 의 회원가입이 완료되었습니다.");
        }
    }

    // 로그인 테스트
    @Test
    public void loginUser() {
        AccountDTO accountDTO = AccountDTO.builder()
                .userid("uhas2002")
                .password("!910912a")
                .build();

        Optional<Account> optionalUser = accountRepo.findByUserid(accountDTO.getUserid());

        if (optionalUser.isEmpty()) {
            System.out.println("유저없음");
            throw new RuntimeException("유저없음");
        }

        Account account = optionalUser.get();

        if (!passwordEncoder.matches(accountDTO.getPassword(), account.getPassword())) {
            System.out.println("비번틀림 회원정보 다시 확인");
            throw new RuntimeException("비번틀림 회원정보 체크하셈");
        }

        System.out.println(accountDTO.getUserid() + " 님의 로그인 환영합니다.");

    }

    @Test
    public void registerValidation() {

    //  Optional<User> optionalUser = userRepository.findByUserid("test$0");
        Optional<Account> optionalUser = accountRepo.findByUserid("하이0");
        Account account = optionalUser.get();

        AccountDTO accountDTO = modelMapper.map(account, AccountDTO.class);


        String[] userv = new String[]{accountDTO.getUserid(), accountDTO.getNickname()};

        if (Pattern.matches("^[a-zA-Z0-9]+@[a-zA-Z0-9]+$", accountDTO.getEmail())) {
            System.out.println("이메일 맞음");
        }

        for(int i=0; i<userv.length; i++){
            if(Pattern.matches("^[ㄱ-ㅎ가-힣a-zA-Z0-9]*$", userv[i])) {
                System.out.println("영어숫자한글만 있음");
            } else {
                System.out.println("특문있음");
            }
        }
    }

    @Test
    public void tokenTest() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJxd2VyIiwiaXNzIjoiTVkiLCJpYXQiOjE2NTI1MjExNDQsImV4cCI6MTY1Mjc4MDM0NH0.IajwgF_UwC7726ERybx1-HRxoCcmA6Lj0dc1x-F-6BE";
        System.out.println(tokenProvider.tokenGetRoles(token));
    }

    @Test
    public void tokenTest2() {
        String token =
                "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJxd2VyIiwiaXNzIjoiTVkiLCJpYXQiOjE2NTI1MjExNDQsImV4cCI6MTY1Mjc4MDM0NH0.IajwgF_UwC7726ERybx1-HRxoCcmA6Lj0dc1x-F-6BE";
        Optional<Account> optionalAccount = accountRepo.findByJwtToken(token);

        if(optionalAccount.isEmpty()) {
            System.out.println("유저없음");
        } else  {
            Account account = optionalAccount.get();
            System.out.println(account);
        }
    }

    @Test
    public void tt() {

        String token = "eyJhbGciOiJIUzI1NiJ9.eyJST0xFIjpbIlVTRVIiXSwic3ViIjoicXdlciIsImlzcyI6Ik1ZIiwiaWF0IjoxNjUyNjY0ODYzLCJleHAiOjE2NTI5MjQwNjN9.fysKNvuxcVZH8KkYsTZJFoSpWY_Mg5Ld_y4IafRNqBM";

        String hd = "eyJhbGciOiJIUzI1NiJ9.eyJST0xFIjpbIlVTRVIiXSwic3ViIjoicXdlciIsImlzcyI6Ik1ZIiwiaWF0IjoxNjUyNjY0ODYzLCJleHAiOjE2NTI5MjQwNjN9.fysKNvuxcVZH8KkYsTZJFoSpWY_Mg5Ld_y4IafRNqBM";
        
        if(token.equals(hd)) {
            System.out.println("같음");
        } else  {
            System.out.println("다름");
        }
    }



}