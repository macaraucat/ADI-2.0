package cl.aduana.ms_travel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"cl.aduana.ms_travel","cl.aduana.common"})
@EnableFeignClients(basePackages = "cl.aduana.common.client")
public class TravelApplication {
    public static void main(String[] args) { 
		SpringApplication.run(TravelApplication.class, args); 
	}
}