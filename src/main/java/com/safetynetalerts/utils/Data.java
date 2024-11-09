package com.safetynetalerts.utils;

import com.safetynetalerts.exception.BadResourceException;
import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Component
public class Data {

	private List<MedicalRecord> medicalRecords = new ArrayList<>();

	private List<Person> persons = new ArrayList<>();

	private List<FireStation> firestations = new ArrayList<>();

	// zone firestation

	public Collection<FireStation> getAllFireStations () {
		return this.firestations;
	}

	public FireStation createFireStation(FireStation fireStation) {
  		List<FireStation> newFireStations = new ArrayList<>(this.firestations);
		newFireStations.add(fireStation);
		this.firestations = newFireStations;
		return fireStation;
	}

	public void deleteFireStation (FireStation fireStation) {
		List<FireStation> tempFireStation = new ArrayList<>(this.getAllFireStations());
		tempFireStation.remove(fireStation);
		this.firestations = new ArrayList<>(tempFireStation);
	}

	//zone medicalrecords

	public List<MedicalRecord> getAllMedicalRecords() {
		return this.medicalRecords;
	}

	public MedicalRecord createMedicalRecord(MedicalRecord medicalRecord) {
		List<MedicalRecord> medicalRecords = new ArrayList<>(this.medicalRecords);
		medicalRecords.add(medicalRecord);
		this.medicalRecords = medicalRecords;
		return medicalRecord;
	}

	//zone persons

	public List<Person> getAllPersons() {
		return this.persons;
	}

	public Person createPerson(Person person) {
		List<Person> newPersons = new ArrayList<>(this.persons);
		newPersons.add(person);
		this.persons = newPersons;
		return person;
	}

	public FireStation saveFirestation(FireStation oldFirestation, FireStation newFirestation) {
		FireStation fireStation = newFirestation;
		List<FireStation> newFireStations = new ArrayList<>(this.firestations);
		newFireStations.remove(oldFirestation);
		newFireStations.add(fireStation);
		this.firestations = newFireStations;
		return fireStation;
	}

	public Person savePerson(Person person, Person person1) {
        List<Person> newPersons = new ArrayList<>(this.persons);
		newPersons.remove(person);
		newPersons.add(person1);
		this.persons = newPersons;
		return person1;
	}

	public void deleteMedicalRecord(MedicalRecord medicalRecord) {
		List<MedicalRecord> medicalRecords1 = new ArrayList<>(this.medicalRecords);
		medicalRecords1.remove(medicalRecord);
		this.medicalRecords = medicalRecords1;
	}

	public MedicalRecord saveMedicalRecord(MedicalRecord medicalRecord, MedicalRecord newMedicalRecord) {
        List<MedicalRecord> medicalRecords = new ArrayList<>(this.medicalRecords);
		medicalRecords.remove(medicalRecord);
		medicalRecords.add(newMedicalRecord);
		this.medicalRecords = medicalRecords;
		return newMedicalRecord;
	}

	public List<Person> saveListOfPerPersons(List<Person> persons) {
		return this.persons = persons;
	}

	public Person savePerson(Person person) throws BadResourceException {
		if (Objects.isNull(person)) {
			throw new BadResourceException("No person provided");
		}
		List<Person> newPersons = new ArrayList<>(this.persons);
		newPersons.add(person);
		this.persons = newPersons;
		return person;
	}
}
