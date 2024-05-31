package com.safetynetalerts.utils;

import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
public class Data {

	private List<MedicalRecord> medicalRecords = new ArrayList<>();

	private List<Person> persons = new ArrayList<>();

	private List<FireStation> firestations = new ArrayList<>();


	public FireStation createFireStation(FireStation fireStation) {
		List<FireStation> result = new ArrayList<>();
		result.addAll(this.firestations);
		result.add(fireStation);
		this.firestations = result;
		return fireStation;
	}
}
