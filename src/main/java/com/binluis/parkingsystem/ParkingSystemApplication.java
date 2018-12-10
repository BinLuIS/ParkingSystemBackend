package com.binluis.parkingsystem;

import 	org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.annotation.PostConstruct;
import java.util.TimeZone;
@EntityScan(basePackageClasses = {
		ParkingSystemApplication.class,
		Jsr310JpaConverters.class
})

@SpringBootApplication
public class ParkingSystemApplication {

	@PostConstruct
	void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}


	public static void main(String[] args) {
		SpringApplication.run(ParkingSystemApplication.class, args);
	}
}
