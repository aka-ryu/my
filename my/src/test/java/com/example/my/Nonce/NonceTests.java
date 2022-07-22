package com.example.my.Nonce;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class NonceTests {

    @Test
    public void nonceTest() {

        for (int i = 0; i <100; i++) {

        int authNo = (int)(Math.random() * (999999 - 100000 + 1)) + 100000;
        System.out.println(authNo);
        }
    }
}
