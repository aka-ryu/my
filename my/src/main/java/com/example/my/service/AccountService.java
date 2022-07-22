package com.example.my.service;

import com.example.my.domain.ProtocolDTO.LoggedDTO;
import com.example.my.domain.ProtocolDTO.Jwt.RefreshTokenDTO;
import com.example.my.domain.ProtocolDTO.RegisterResponseDTO;
import com.example.my.domain.dto.AccountDTO;
import com.example.my.domain.entity.Account;
import com.example.my.domain.entity.EmailCheck;
import com.example.my.domain.entity.Role;
import com.example.my.repository.AccountRepo;
import com.example.my.repository.EmailCheckRepo;
import com.example.my.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class AccountService {

    private final AccountRepo accountRepo;

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;

    private final EmailCheckRepo emailCheckRepo;


    // 회원가입전 유효성
    public void registerValidation(AccountDTO accountDTO) {

        //아이디
        if (!Pattern.matches("^[a-zA-Z0-9]*$", accountDTO.getUserid())) {
            log.warn("아이디는 영어,숫자만 가능");
            throw new RuntimeException("아이디는 영어,숫자만 가능");
        }

        //닉네임
        if (!Pattern.matches("^[ㄱ-ㅎ가-힣a-zA-Z0-9]*$", accountDTO.getNickname())) {
            log.warn("닉네임은 한글,영어,숫자만 가능");
            throw new RuntimeException("닉네임은 한글,영어,숫자만 가능");
        }

        //이메일
        if (!Pattern.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+"[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", accountDTO.getEmail())) {
            log.warn("올바른 이메일 형식을 입력하세요");
            throw new RuntimeException("올바른 이메일 형식을 입력하세요");
        }

        // 비밀번호
        if (!Pattern.matches("^(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=])(?=.\\S+$).*$", accountDTO.getPassword())) {
            log.warn("비밀번호는 숫자+문자+특수문자 가 1개이상 포함되어야합니다.(공백불가)");
            throw new RuntimeException("비밀번호는 숫자+문자+특수문자 가 1개이상 포함되어야합니다. (공백불가)");
        }

        // 아이디 중복체크
        Optional<Account> optionalUser = accountRepo.findByUserid(accountDTO.getUserid());
        if (!optionalUser.isEmpty()) {
            log.warn("이미 존재하는 아이디 입니다.");
            throw new RuntimeException("이미 존재하는 아이디 입니다.");
        }

        // 닉네임 중복체크
        Optional<Account> optionalUser1 = accountRepo.findByNickname(accountDTO.getNickname());
        if (!optionalUser1.isEmpty()) {
            log.warn("이미 존재하는 닉네임 입니다.");
            throw new RuntimeException("이미 존재하는 닉네임 입니다.");
        }

        // 이메일 중복체크
        Optional<Account> optionalUser2 = accountRepo.findByEmail(accountDTO.getEmail());
        if (!optionalUser2.isEmpty()) {
            log.warn("이미 존재하는 이메일 입니다.");
            throw new RuntimeException("이미 존재하는 이메일 입니다.");
        }


    }


    // 유저 회원가입
    public RegisterResponseDTO registerUser(AccountDTO accountDTO) {

        Optional<EmailCheck> optionalEmailCheck = emailCheckRepo.findByEmailAndIsChecked(accountDTO.getEmail(), true);
        if(optionalEmailCheck.isEmpty()) {
            log.error("이메일 인증에 문제가 있습니다.");
            throw new RuntimeException("이메일 인증에 문제가 있습니다.");
        }

        registerValidation(accountDTO);
        accountDTO.setPassword(passwordEncoder.encode(accountDTO.getPassword()));

        // 중복 체크 후 회원가입
        Account account = modelMapper.map(accountDTO, Account.class);
        account.addUserRole(Role.ROLE_USER);
        accountRepo.save(account);
        RegisterResponseDTO registerResponseDTO = RegisterResponseDTO.builder()
                .code(200)
                .message(accountDTO.getUserid() + " 님의 회원가입이 완료되었습니다.")
                .build();

        return registerResponseDTO;
    }

    //토큰 갱신
    public RefreshTokenDTO tokenRefresh(String refreshToken) {
        log.info("리프레시 토큰 진입.");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("겟컨텍" + SecurityContextHolder.getContext());
        System.out.println("principal " + authentication);

        String username = (String) authentication.getPrincipal();

        System.out.println("유저네임" + username);

        ArrayList<String> authList = new ArrayList<>();

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            authList.add(authority.getAuthority());
        }
        System.out.println("유저 권한 " + authList);
        System.out.println("기존토큰 " + refreshToken);

        String token = refreshToken.substring("Bearer ".length());
        System.out.println("추출 후 토큰" + token);

        Optional<Account> optionalAccount = accountRepo.findByUserid(username);

        // 해당 토큰이 저장된 유저가 없는경우
        if (optionalAccount.isEmpty()) {
            log.error("USER 가 없음");

            RefreshTokenDTO refreshTokenDTO = RefreshTokenDTO.builder()
                    .success(false)
                    .message("USER 가 없음")
                    .build();

            return refreshTokenDTO;

        } else {
            log.info("리프레쉬 토큰 유효,  토큰 재발급 시작");

            Account account = optionalAccount.get();


            // account(DB) 의 아이디와 인증객체의 이름이 다른경우 에러
            if (!username.equals(account.getUserid())) {
                log.error("jwt 인증정보가 정확하지 않습니다.");

                RefreshTokenDTO refreshTokenDTO = RefreshTokenDTO.builder()
                        .success(false)
                        .message("jwt 인증정보가 정확하지 않습니다.")
                        .build();

                return refreshTokenDTO;

            } else {
                String newRefreshToken = tokenProvider.getRefreshKeyToken(username, authList);

                // 디비에 새로운 토큰으로 갱신
                account.setJwtToken(newRefreshToken);
                accountRepo.save(account);

                RefreshTokenDTO refreshTokenDTO = RefreshTokenDTO.builder()
                        .success(true)
                        .message("리프레쉬 토큰 갱신 성공")
                        .accessToken(tokenProvider.getAccessToken(username, authList))
                        .refreshToken(newRefreshToken)
                        .build();

                return refreshTokenDTO;
            }
        }
    }


    // 유저 로그인
//    public LoginResponseDTO loginUser(AccountDTO accountDTO) {
//        Optional<Account> optionalUser = accountRepo.findByUserid(accountDTO.getUserid());
//
//        if (optionalUser.isEmpty()) {
//            System.out.println("회원정보 다시 확인");
//            throw new RuntimeException("회원정보 체크하셈");
//        }
//
//        Account account = optionalUser.get();
//
//        if (!passwordEncoder.matches(accountDTO.getPassword(), account.getPassword())) {
//            System.out.println("회원정보 다시 확인");
//            throw new RuntimeException("회원정보 체크하셈");
//        }
//
//        LoginResponseDTO loginResponseDTO = LoginResponseDTO.builder()
//                .code(200)
//                .message(accountDTO.getUserid() + " 님 로그인 되었습니다.")
//                .userid(accountDTO.getUserid())
//                .accessToken(tokenProvider.getAccessToken(accountDTO.getUserid()))
//                .refreshToken(tokenProvider.getRefreshKeyToken(accountDTO.getUserid()))
//                .build();
//
//        return loginResponseDTO;
//    }


    public LoggedDTO loggedCheck(String userid) {
        Optional<Account> optionalAccount = accountRepo.findByUserid(userid);

        if(optionalAccount.isEmpty()) {
            log.error("유저없음");
            throw new RuntimeException("유저없음");
        }

        Account account = optionalAccount.get();

        LoggedDTO loggedDTO = LoggedDTO.builder()
                .code(200)
                .success(true)
                .message("로그인 체크 성공, 로그인 상태 유지")
                .userid(account.getUserid())
                .build();

        return loggedDTO;
    }

}
