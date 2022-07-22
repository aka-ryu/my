package com.example.my.security.filter;

import com.example.my.domain.ProtocolDTO.Jwt.*;
import com.example.my.security.jwt.TokenProvider;
import com.example.my.security.service.CustomUserDetailsService;
import com.google.gson.Gson;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@Log4j2
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    // 클라이언트의 요청이 스프링컨트롤러에 닿기전에 필터로 먼저 검증함
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 아래의 경로에서는 token을 체크하지않음 (permitAll)
        if (
                request.getServletPath().equals("/api/login") ||
                        request.getServletPath().equals("/api/register") ||
                        request.getServletPath().equals("/") ||
                        request.getServletPath().startsWith("/board/list") ||
                        request.getServletPath().startsWith("/email/code") ||
                        request.getServletPath().startsWith("/email/certification")
        ) {

            log.info("인가가 필요없는 요청");
            filterChain.doFilter(request, response);

        } else if (request.getHeader("Authorization") == null
                    || request.getHeader("Authorization").equals("Bearer null") ) {

            log.error("토큰이 없음");

            NoJwtDTO noJwtDTO = NoJwtDTO.builder()
                    .message("jwt 토큰이 없음")
                    .hasToken(false)
                    .code(403)
                    .exceptionName("JwtNull")
                    .build();

            String json = new Gson().toJson(noJwtDTO);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);

        } else {

            try {

                String authorizationHeader = request.getHeader("Authorization");
                String token = authorizationHeader.substring("Bearer ".length());
                ArrayList<String> roles = new ArrayList<>();
                String userid = tokenProvider.validationToken(token);
                roles = tokenProvider.tokenGetRoles(token);


                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                for (String role : roles) {
                    authorities.add(new SimpleGrantedAuthority(role));
                }

                // 추출한내용으로 새로운 인증객체 생성
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userid, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                filterChain.doFilter(request, response);

                // 토큰 기간 만료시
            } catch (ExpiredJwtException e) {

                // 기간만료된 토큰이 리프레쉬 토큰일 경우
                if (request.getServletPath().equals("/api/token/refresh")) {
                    log.info("리프레시 토큰도 유효하지 않음, 로그아웃");

                    RefreshExceptionDTO refreshTokenDTO = RefreshExceptionDTO.builder()
                            .code(403)
                            .message("리프레쉬 토큰도 만료됨")
                            .exceptionName("RefreshExpired")
                            .build();

                    String json = new Gson().toJson(refreshTokenDTO);

                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(json);
                    // 액세스 토큰일 경우
                } else {
                    log.info("액세스 토큰 만료임, 리프레쉬 토큰 요청 보냄");

                    ExpiredDTO expiredDTO = ExpiredDTO.builder()
                            .code(403)
                            .message("토큰의 기간이 만료됨")
                            .exceptionName("Expired")
                            .build();

                    String json = new Gson().toJson(expiredDTO);

                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(json);
                }
                // 토큰의 시그니처가 불일치 할경우
            } catch (SignatureException e) {
                log.error("토큰의 시그니처가 불일치");

                SignatureExceptionDTO signatureExceptionDTO = SignatureExceptionDTO.builder()
                        .code(403)
                        .message("토큰의 시그니처가 불일치")
                        .exceptionName("Sigature")
                        .build();

                String json = new Gson().toJson(signatureExceptionDTO);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(json);

                // 기타 모든상황 (로그아웃시킬예정)
            } catch (Exception e) {

                JwtExceptionDTO jwtExceptionDTO = JwtExceptionDTO.builder()
                        .code(403)
                        .exceptionName("JwtException")
                        .message("정확한 이유를 알수없는 토큰 에러")
                        .build();

                String json = new Gson().toJson(jwtExceptionDTO);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(json);

            }
        }
    }
}


