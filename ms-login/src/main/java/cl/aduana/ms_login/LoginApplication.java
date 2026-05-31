package cl.aduana.ms_login;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"cl.aduana.ms_login", "cl.aduana.common"})
public class LoginApplication {
    public static void main(String[] args) {
        SpringApplication.run(LoginApplication.class, args);
    }
}