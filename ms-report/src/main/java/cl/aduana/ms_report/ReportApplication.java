package cl.aduana.ms_report;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"cl.aduana.ms_report","cl.aduana.common"})
@EnableFeignClients(basePackages = {"cl.aduana.common.client"})
public class ReportApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReportApplication.class, args);
	}

}