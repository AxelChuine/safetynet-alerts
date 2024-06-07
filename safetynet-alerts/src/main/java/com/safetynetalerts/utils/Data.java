package com.safetynetalerts.utils;

import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.jetty.http.MetaData;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Component
public class Data {

	private List<MedicalRecord> medicalRecords = new ArrayList<>();

	private List<Person> persons = new ArrayList<>();

	private List<FireStation> firestations = new ArrayList<>();

	public Collection<FireStation> getAllFireStations () {
		return this.firestations;
	}

	public FireStation createFireStation(FireStation fireStation) {
  		List<FireStation> newFireStations = new ArrayList<>(this.firestations);
		newFireStations.add(fireStation);
		this.firestations = newFireStations;
		return fireStation;
	}

	public List<MedicalRecord> getAllMedicalRecords() {
		return this.medicalRecords;
	}

	public MedicalRecord createMedicalRecord(MedicalRecord medicalRecord) {
		List<MedicalRecord> medicalRecords = new ArrayList<>(this.medicalRecords);
		medicalRecords.add(medicalRecord);
		this.medicalRecords = medicalRecords;
		return medicalRecord;
	}

	public List<Person> getAllPersons() {
		return this.persons;
	}

	public Person createPerson(Person person) {
		List<Person> newPersons = new ArrayList<>(this.persons);
		newPersons.add(person);
		this.persons = newPersons;
		return person;
	}
}
