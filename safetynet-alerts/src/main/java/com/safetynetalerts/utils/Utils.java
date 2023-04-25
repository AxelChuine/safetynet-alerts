package com.safetynetalerts.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Service
public class Utils {

	private String path = "src/main/resources/data/data.json";

	private byte[] file;

	private JsonIterator iter;

	private List<Person> persons;

	private Any any;

	private Any personAny;

	private Any fireStationAny;

	private Map<String, FireStation> firestationMap;

	private List<MedicalRecord> medicalRecords;

	private Any medicalAny;

	private Any medications;

	private Any allergies;

	public List<FireStation> getFireStations() throws IOException {
		this.file = Files.readAllBytes(new File(path).toPath());
		this.iter = JsonIterator.parse(file);
		this.any = iter.readAny();

		this.firestationMap = new HashMap<>();
		this.fireStationAny = any.get("firestations");
		this.fireStationAny.forEach(anyStation -> {
			firestationMap.compute(anyStation.get("station").toString(),
					(k, v) -> v == null
							? new FireStation(anyStation.get("station").toString())
									.addAddress(anyStation.get("address").toString())
							: v.addAddress(anyStation.get("address").toString()));
		});

		List<FireStation> fireStations = firestationMap.values().stream().collect(Collectors.toList());
		return fireStations;
	}

	// FIXME: créer une méthode de lecture qui range les données dans les list.

	public List<Person> getAllPersons() throws IOException {
		this.file = Files.readAllBytes(new File(path).toPath());
		this.iter = JsonIterator.parse(file);
		this.any = iter.readAny();

		this.persons = new ArrayList<>();
		this.personAny = any.get("persons");
		this.personAny.forEach(a -> persons.add(new Person.PersonBuilder().firstName(a.get("firstName").toString())
				.address(a.get("address").toString()).city(a.get("city").toString())
				.lastName(a.get("lastName").toString()).phone(a.get("phone").toString()).zip(a.get("zip").toString())
				.email(a.get("email").toString()).build()));

		persons.forEach(
				p -> p.firstName.concat(p.lastName).concat(p.address).concat(p.city).concat(p.phone).concat(p.zip));
		return persons;
	}

	/*
	 * public List<MedicalRecord> getMedicalRecordByFullName(String firstName,
	 * String LastName) throws IOException { // FIXME this.medicalRecords = new
	 * ArrayList<>(); this.medicalAny = any.get("medicalrecords");
	 * this.medicalAny.forEach(medicalRecord -> medicalRecords.add( new
	 * MedicalRecord().getPerson().setFirstName(
	 * medicalRecord.get("firstName")).toString().concat(medicalRecord.get(
	 * "lastName").toString()) .concat(medicalRecord.get("birthdate").toString()
	 * ))); return this.medicalRecords; }
	 */

	// FIXME: Object mapper mapstruct
	public List<MedicalRecord> getAllMedicalRecords() throws IOException {
		// return Data.getAllMedicalRecords();
		this.medicalRecords = new ArrayList<>();
		this.file = Files.readAllBytes(new File(path).toPath());
		this.iter = JsonIterator.parse(file);
		this.any = iter.readAny();

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
			this.medicalRecords.add(medicalRecord);
		}
		return this.medicalRecords;

	}

}
