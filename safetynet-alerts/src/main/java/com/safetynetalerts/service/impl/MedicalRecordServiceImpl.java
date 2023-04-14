package com.safetynetalerts.service.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.service.IMedicalRecordService;
import com.safetynetalerts.service.IPersonService;
import com.safetynetalerts.utils.Utils;

@Service
public class MedicalRecordServiceImpl implements IMedicalRecordService {

	@Autowired
	private Utils utils;

	@Autowired
	private IPersonService personService;

	@Override
	public List<MedicalRecord> getAllMedicalRecords(List<Person> pPersons) throws IOException {
		List<MedicalRecord> medicalRecords = utils.getAllMedicalRecords();
		List<MedicalRecord> medicalRecordsToReturn = new ArrayList<>();
		for (Person p : pPersons) {
			for (MedicalRecord m : medicalRecords) {
				if (p.firstName.equals(m.getFirstName()) && p.lastName.equals(m.getLastName())) {
					medicalRecordsToReturn.add(m);
				}
			}
		}
		return medicalRecordsToReturn;
	}

	@Override
	public boolean isUnderaged(String pBirthDate) {
		boolean vRet = false;
		DateTimeFormatter dateOfBirth = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDate birthDate = LocalDate.parse(pBirthDate, dateOfBirth);
		LocalDate instant = LocalDate.now();
		Period period = Period.between(birthDate, instant);
		if (period.getYears() < 18) {
			vRet = true;
		}
		return vRet;
	}

	@Override
	public Map countAllPersons(List<Person> pPersons) {
		Map<String, Integer> persons = new HashMap<>();
		Integer adults = 0;
		Integer underaged = 0;
		List<MedicalRecord> m1 = this.utils.getAllMedicalRecords();
		for (Person p : pPersons) {
			for (MedicalRecord m : m1) {
				if (!(p.firstName.equals(m.getFirstName()) && p.lastName.equals(m.getLastName()))) {
					m1.remove(m);
				}
				if (!this.isUnderaged(m.getBirthDate())) {
					adults++;
					persons.put("majeurs", adults);
				} else {
					underaged++;
					persons.put("mineurs", underaged);
				}
			}
		}
		return persons;
	}

	public List<MedicalRecord> getMedicalRecordByFullName(String pFirstName, String pLastName) {
		List<MedicalRecord> records = this.utils.getAllMedicalRecords();
		List<MedicalRecord> recordsToReturn = new ArrayList<>();
		Integer count = 0;
		while (count < records.size()) {
			if (records.get(count).getFirstName().equals(pFirstName)
					&& records.get(count).getLastName().equals(pLastName)) {
				recordsToReturn.add(records.get(count));
			}
		}
		return recordsToReturn;
	}

	@Override
	public List<Integer> getAgeOfPerson(String firstName, String lastName) throws IOException {
		List<Integer> ages = new ArrayList<>();
		String v1 = "";
		for (MedicalRecord m : this.getMedicalRecordByFullName(firstName, lastName)) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			LocalDate birthDate = LocalDate.parse(m.getBirthDate(), formatter);
			LocalDate instant = LocalDate.now();
			Period vAge = Period.between(birthDate, instant);
			v1 = vAge.toString();
			Integer age = Integer.parseInt(v1);
			ages.add(age);
		}
		return ages;
	}
}
