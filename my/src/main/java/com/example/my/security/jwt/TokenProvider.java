package com.example.my.security.jwt;

import com.example.my.repository.AccountRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;

@Log4j2
@Service
@RequiredArgsConstructor
public class TokenProvider {


    @Value("${Jwt.secret_key}")
    String SECRET_KEY;
    private final AccountRepo accountRepo;



    public String getAccessToken(User user){


        ArrayList<String> authList = new ArrayList<>(user.getAuthorities().size());

        for (GrantedAuthority authority : user.getAuthorities()) {
            authList.add(authority.getAuthority());
        }

        return Jwts.builder()
                .claim("ROLE", authList)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .setSubject(user.getUsername())
                .setIssuer("MY")
                .setIssuedAt(new Date())
                .setExpiration(new Date().from(Instant.now().plus(10, ChronoUnit.MINUTES)))
                .compact();
    }

    public String getRefreshKeyToken(User user){

        ArrayList<String> authList = new ArrayList<>(user.getAuthorities().size());

        for (GrantedAuthority authority : user.getAuthorities()) {
            authList.add(authority.getAuthority());
        }

        return Jwts.builder()
                .claim("ROLE", authList)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .setSubject(user.getUsername())
                .setIssuer("MY")
                .setIssuedAt(new Date())
                .setNotBefore(new Date().from(Instant.now().plus(10, ChronoUnit.MINUTES)))
                .setExpiration(new Date().from(Instant.now().plus(10, ChronoUnit.DAYS)))
                .compact();
    }

    public String getAccessToken(String username, ArrayList<String> authList){


        return Jwts.builder()
                .claim("ROLE", authList)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .setSubject(username)
                .setIssuer("MY")
                .setIssuedAt(new Date())
                .setExpiration(new Date().from(Instant.now().plus(10, ChronoUnit.MINUTES)))
                .compact();
    }

    public String getRefreshKeyToken(String username, ArrayList<String> authList){


        return Jwts.builder()
                .claim("ROLE", authList)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .setSubject(username)
                .setIssuer("MY")
                .setIssuedAt(new Date())
                .setNotBefore(new Date().from(Instant.now().plus(10, ChronoUnit.MINUTES)))
                .setExpiration(new Date().from(Instant.now().plus(10, ChronoUnit.DAYS)))
                .compact();
    }

    public String validationToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();


        return claims.getSubject();
    }

    public ArrayList tokenGetRoles(String token) {
        ArrayList<String> roles = new ArrayList<>();
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

      roles = claims.get("ROLE", ArrayList.class);
        return roles;
    }

}
