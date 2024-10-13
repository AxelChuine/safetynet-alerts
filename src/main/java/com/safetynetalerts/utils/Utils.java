package com.safetynetalerts.utils;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;
import lombok.Getter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Component
public class Utils {

	private final Data data;

    public Utils(Data data) {
        this.data = data;
    }


    public final List<FireStation> getAllFirestations() throws IOException {
		String path = "src/main/resources/data/data.json";
		List<FireStation> fireStations;
		Any any = this.readFile(path);

		Map<String, FireStation> firestationMap = new HashMap<>();
		Any fireStationAny = any.get("firestations");
		fireStationAny.forEach(anyStation -> {
			firestationMap.compute(anyStation.get("station").toString(),
					(k, v) -> v == null
							? new FireStation(anyStation.get("station").toString())
									.addAddress(anyStation.get("address").toString())
							: v.addAddress(anyStation.get("address").toString()));
		});

		fireStations = firestationMap.values().stream().toList();
		return fireStations;
	}


	public final List<Person> getAllPeople() throws IOException {
		String path = "src/main/resources/data/data.json";
		List<Person> persons = new ArrayList<>();
		Any any = this.readFile(path);

		Any personAny = any.get("persons");
		personAny.forEach(a -> persons.add(new Person.PersonBuilder().firstName(a.get("firstName").toString())
				.address(a.get("address").toString()).city(a.get("city").toString())
				.lastName(a.get("lastName").toString()).phone(a.get("phone").toString()).zip(a.get("zip").toString())
				.email(a.get("email").toString()).build()));

		persons.forEach(
				p -> p.firstName.concat(p.lastName).concat(p.address).concat(p.city).concat(p.phone).concat(p.zip));
		return persons;
	}

	public final List<MedicalRecord> getAllMedicalRecords() throws IOException {
		String path = "src/main/resources/data/data.json";
		List<MedicalRecord> medicalRecords = new ArrayList<>();
		Any any = this.readFile(path);

		Any medicalAny = any.get("medicalrecords");

		for (Any medical : medicalAny) {
			List<String> medications = new ArrayList<>();
			if (medical.get("medications").size() > 0) {
				medical.get("medications").forEach(m -> medications.add(m != null ? m.toString() : " "));
			}
			List<String> allergies = new ArrayList<>();
			if (medical.get("allergies").size() > 0) {
				medical.get("allergies").forEach(a -> allergies.add(a != null ? a.toString() : " "));
			}
			MedicalRecord medicalRecord = new MedicalRecord.MedicalRecordBuilder()
					.firstName(medical.get("firstName").toString()).lastName(medical.get("lastName").toString())
					.birthDate(medical.get("birthdate").toString()).allergies(allergies).medications(medications)
					.build();
			medicalRecords.add(medicalRecord);
		}
		return medicalRecords;

	}

	@EventListener(ApplicationReadyEvent.class)
	public void OnStartUp() throws IOException {
		data.setPersons(this.getAllPeople());
		data.setFirestations(this.getAllFirestations());
		data.setMedicalRecords(this.getAllMedicalRecords());
	}

	public Any readFile(String path) throws IOException {
		byte[] file = Files.readAllBytes(new File(path).toPath());
		JsonIterator iter = JsonIterator.parse(file);
		return iter.readAny();
	}
}
