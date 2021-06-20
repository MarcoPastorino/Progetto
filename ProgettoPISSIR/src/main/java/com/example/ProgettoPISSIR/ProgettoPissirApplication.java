package com.example.ProgettoPISSIR;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//
//MainServer
//

@SpringBootApplication
public class ProgettoPissirApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProgettoPissirApplication.class, args);
//		SmartLocker1.main(null);
//		SmartLocker3.main(null);
		GestionePrenotazioni.main(null);
	}

}
