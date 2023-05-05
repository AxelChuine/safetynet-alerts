package com.safetynetalerts.service.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.service.IMedicalRecordService;
import com.safetynetalerts.utils.Utils;

@Service
public class MedicalRecordServiceImpl implements IMedicalRecordService {

	@Autowired
	private Utils utils;

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
	public Map countAllPersons(List<Person> pPersons) throws IOException {
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

	/**
	 * @param pFirstName
	 * @param pLastName
	 * @return a list of medical records according to the parameters
	 */
	public List<MedicalRecord> getMedicalRecordByFullName(String pFirstName, String pLastName) throws IOException {
		List<MedicalRecord> records = this.utils.getAllMedicalRecords();
		List<MedicalRecord> recordsToReturn = new ArrayList<>();
		int count = 0;
		while (count < records.size()) {
			if (records.get(count).getFirstName().equals(pFirstName)
					&& records.get(count).getLastName().equals(pLastName)) {
				recordsToReturn.add(records.get(count));
			}
			count++;
		}
		return recordsToReturn;
	}

	/**
	 * @param pFirstName
	 * @param pLastName
	 * @return a list of age of all the people called the same.
	 */
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

	/**
	 * @param pFirstName
	 * @param pLastName
	 * @return true if the person is underaged
	 */
	@Override
	public boolean isUnderaged(String pFirstName, String pLastName) throws IOException {
		boolean isUnderaged = false;
		MedicalRecord medicalRecord = this.getMedicalRecordByUnderage(pFirstName, pLastName);
		if (this.isUnderaged(medicalRecord.getBirthDate())) {
			isUnderaged = true;
		}
		return isUnderaged;
	}

	/**
	 * @return a medical record of a person if underaged
	 */
	@Override
	public MedicalRecord getMedicalRecordByUnderage(String pFirstName, String pLastName) throws IOException {
		MedicalRecord medicalRecord = null;
		Optional<MedicalRecord> medicalRecordOptional = this.getAllMedicalRecords().stream()
				.filter(m -> Objects.equals(m.getFirstName(), pFirstName) && Objects.equals(m.getLastName(), pLastName))
				.findFirst();
		if (medicalRecordOptional.isPresent()) {
			medicalRecord = medicalRecordOptional.get();
		}
		return medicalRecord;
	}

	/**
	 * @return retrieve all medical records
	 */
	@Override
	public List<MedicalRecord> getAllMedicalRecords() throws IOException {
		return this.utils.getAllMedicalRecords();
	}
}
