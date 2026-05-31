package cl.aduana.ms_risk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"cl.aduana.ms_risk","cl.aduana.common"})
@EnableFeignClients(basePackages = "cl.aduana.common.client")
public class RiskApplication {

	public static void main(String[] args) {
		SpringApplication.run(RiskApplication.class, args);
	}

}