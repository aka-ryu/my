//package com.example.my.security.cookie;
//
//import lombok.extern.log4j.Log4j2;
//import org.springframework.stereotype.Service;
//
//import javax.servlet.http.Cookie;
//
//@Log4j2
//@Service
//public class CookieProvider {
//
//    public Cookie accessCookie(String token) {
//        Cookie accessCookie = new Cookie("accessToken", token);
//        accessCookie.setPath("/");
//        accessCookie.setMaxAge(60*30);
////        accessCookie.setHttpOnly(true);
//
//
//        return accessCookie;
//    }
//
//    public Cookie refreshCookie(String token) {
//        Cookie refreshCookie = new Cookie("refreshToken", token);
//        refreshCookie.setPath("/");
//        refreshCookie.setMaxAge(60*60*24*10);
////        refreshCookie.setHttpOnly(true);
//
//        return refreshCookie;
//    }
//
//    public Cookie logged(String userid) {
//        Cookie loggedCookie = new Cookie("logged", userid);
//        loggedCookie.setPath("/");
//        loggedCookie.setMaxAge(60*30);
////        loggedCookie.setHttpOnly(true);
//
//        return loggedCookie;
//    }
//}
