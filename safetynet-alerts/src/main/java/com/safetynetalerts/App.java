package com.safetynetalerts;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;

@SpringBootApplication
public class App {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(App.class, args);
		String path = "src/main/resources/data/data.json";
		byte[] file = Files.readAllBytes(new File(path).toPath());
		JsonIterator iter = JsonIterator.parse(file);
		Any any = iter.readAny();
		Any personAny = any.get("persons");

		Any medicalAny = any.get("medicalrecords");
		medicalAny.forEach(medicalRecord -> {
			medicalRecord.get("firstName").toString().concat(medicalRecord.get("lastName").toString())
					.concat(medicalRecord.get("birthdate").toString());

			Any medications = medicalRecord.get("medications");

			Any allergies = medicalRecord.get("allergies");
		});
	}

}
