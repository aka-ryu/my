package com.example.my.security.handler;

import com.example.my.domain.ProtocolDTO.LoginResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private ObjectMapper mapper = new ObjectMapper();





    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        log.info("invaild userid or password");


        LoginResponseDTO loginResponseDTO = LoginResponseDTO.builder()
                .code(400)
                .message(exception.getMessage())
                .build();

        String json = new Gson().toJson(loginResponseDTO);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
}
