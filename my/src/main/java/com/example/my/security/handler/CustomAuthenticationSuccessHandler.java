package com.example.my.security.handler;

import com.example.my.domain.ProtocolDTO.LoginResponseDTO;
import com.example.my.domain.dto.AccountDTO;
import com.example.my.domain.entity.Account;
import com.example.my.repository.AccountRepo;
import com.example.my.security.jwt.TokenProvider;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Log4j2
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {



    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private TokenProvider tokenProvider;

//    @Autowired
//    private CookieProvider cookieProvider;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("인증성공 핸들러");
        log.info(authentication);
        User user = (User)authentication.getPrincipal();


        Optional<Account> optionalAccount = accountRepo.findByUserid(user.getUsername());
        Account account = optionalAccount.get();

        String refreshToken = tokenProvider.getRefreshKeyToken(user);

        account.setJwtToken(refreshToken);
        accountRepo.save(account);
        AccountDTO accountDTO = AccountDTO.builder()
                .userid(account.getUserid())
                .build();


        LoginResponseDTO loginResponseDTO = LoginResponseDTO.builder()
                .code(200)
                .message(accountDTO.getUserid() + " 님 로그인 되었습니다.")
                .userid(accountDTO.getUserid())
                .accessToken(tokenProvider.getAccessToken(user))
                .refreshToken(refreshToken)
                .build();

//        Cookie accessCookie = cookieProvider.accessCookie(loginResponseDTO.getAccessToken());
//        Cookie refreshCookie = cookieProvider.refreshCookie(loginResponseDTO.getRefreshToken());
//        Cookie loggedCookie = cookieProvider.logged(loginResponseDTO.getUserid());

        String json = new Gson().toJson(loginResponseDTO);

//        response.addCookie(accessCookie);
//        response.addCookie(refreshCookie);
//        response.addCookie(loggedCookie);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);

    }
}
