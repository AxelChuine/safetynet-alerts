package com.safetynetalerts.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Data {

	private static Person person;

	private static List<MedicalRecord> medicalRecords;

	private static MedicalRecord medicalRecord;

	private static List<Person> persons;

	private static List<FireStation> firestations;

	private static String path = "src/main/resources/data/data.json";

	private static ObjectMapper mapper;

	@JsonSetter("persons")
	public static Person getAllPersons() throws StreamReadException, DatabindException, IOException {
		person = mapper.readValue(new File(path), Person.class);
		return person;
	}

	@JsonSetter("medicalrecords")
	@JsonIgnoreProperties({ "persons", "firestations" })
	public static List<MedicalRecord> getAllMedicalRecords() throws IOException {
		medicalRecords = new ArrayList<>();
		mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		medicalRecord = mapper.readValue(new File(path), MedicalRecord.class);
		medicalRecords.add(medicalRecord);
		return medicalRecords;
	}
}
