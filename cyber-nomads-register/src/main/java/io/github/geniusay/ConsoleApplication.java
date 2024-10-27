package io.github.geniusay;

import javafx.application.Application;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description
 * @Author welsir
 * @Date 2024/10/27 14:05
 */
@SpringBootApplication
public class ConsoleApplication implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        Application.launch(MainApp.class, args);
    }
}
