package com.example.my.security.filter;

import com.example.my.domain.dto.AccountDTO;
import com.example.my.security.jwt.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {


    private ObjectMapper objectMapper = new ObjectMapper();

    private TokenProvider tokenProvider;

    public CustomAuthenticationFilter(String defaultFilterProcessesUrl, TokenProvider tokenProvider) {
        super(defaultFilterProcessesUrl);
        this.tokenProvider = tokenProvider;
    }

    //로그인 시도
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        AccountDTO accountDTO = objectMapper.readValue(request.getReader(), AccountDTO.class);
        log.info("Attempt Authentication Login Filter");


        String username = accountDTO.getUserid();
        String password = accountDTO.getPassword();

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        return getAuthenticationManager().authenticate(authenticationToken);
    }



}
