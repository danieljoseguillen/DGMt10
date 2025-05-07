package com.tarea10.dgmt10e03;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Dgmt10e03Application {

	public static void main(String[] args) {
		SpringApplication.run(Dgmt10e03Application.class, args);
	}

	// @Bean
	// CommandLineRunner initData(eService servicio){
	// 	return args -> {
	// 		servicio.agregar(new Empleado(null,"Jose","Jose@gmail.com",30000d,Estado.ACTIVO,Genero.MASCULINO));
	// 		servicio.agregar(new Empleado(null,"Pedro","pedrog@gmail.com",32000d,Estado.ACTIVO,Genero.MASCULINO));
	// 		servicio.agregar(new Empleado(null,"Maria","mcarolina@gmail.com",35000d,Estado.INACTIVO,Genero.FEMENINO));
	// 	};
	// }
}
