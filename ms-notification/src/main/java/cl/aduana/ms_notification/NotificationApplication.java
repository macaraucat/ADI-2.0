package cl.aduana.ms_notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"cl.aduana.ms_notification","cl.aduana.common"})
@EnableFeignClients(basePackages = "cl.aduana.common.client")
public class NotificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationApplication.class, args);
	}

}