package com.mobicoolsoft.electronic.store.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static net.bytebuddy.matcher.ElementMatchers.is;

@SpringBootTest(classes = ReadFileTest.class)
public class ReadFileTest {

    private Path workingDir;

    @Before
    public void init() {
        this.workingDir = Path.of("", "src/test/");
    }

    @Test
    public void read() throws IOException, IOException {
        Path file = this.workingDir.resolve("sandy.txt");
        String content = Files.readString(file);
        Assertions.assertEquals(content, "sandy");
    }
}
