package com.safetynetalerts.utils;

import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;
import lombok.Getter;
import lombok.Setter;
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
}
