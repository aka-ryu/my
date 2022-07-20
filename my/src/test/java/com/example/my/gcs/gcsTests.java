package com.example.my.gcs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

@SpringBootTest
public class gcsTests {

    @Value("gs://uhas2002/myProject")
    private Resource gcsFile;


    @Test
    public String readGcsFile() throws IOException {

        System.out.println("ddd");
        System.out.println(gcsFile);
        System.out.println("ddd");
        return StreamUtils.copyToString(
                this.gcsFile.getInputStream(),
                Charset.defaultCharset()) + "\n";
    }

}
