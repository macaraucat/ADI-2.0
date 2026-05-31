package cl.aduana.ms_vehicle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"cl.aduana.ms_vehicle", "cl.aduana.common"})
public class VehicleApplication {

	public static void main(String[] args) {
		SpringApplication.run(VehicleApplication.class, args);
	}

}