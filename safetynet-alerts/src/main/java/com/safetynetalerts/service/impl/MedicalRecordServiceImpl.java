package com.safetynetalerts.service.impl;

import com.safetynetalerts.dto.MedicalRecordDto;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.service.IMedicalRecordService;
import com.safetynetalerts.utils.Data;
import com.safetynetalerts.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class MedicalRecordServiceImpl implements IMedicalRecordService {

	@Autowired
	private Utils utils;

	@Autowired
	private Data data;


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


	/**
	 * @Author Axel
	 * @param pPersons
	 * @return a map counting the persons underaged and not.
	 * @throws IOException
	 */
	@Override
	public Map countAllPersons(List<Person> pPersons) throws IOException {
		Map<String, Integer> persons = new HashMap<>();
		Integer adults = 0;
		Integer underaged = 0;
		List<Integer> index = new ArrayList<>();
		List<MedicalRecord> m1 = this.data.getMedicalRecords();
		for (Person p : pPersons) {
			for (MedicalRecord m : m1) {
				if (p.getFirstName().equals(m.getFirstName()) && p.getLastName().equals(m.getLastName())) {
					if (!this.isUnderaged(m.getBirthDate())) {
						adults++;
						persons.put("majeurs", adults);
					} else {
						underaged++;
						persons.put("mineurs", underaged);
					}
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
	public MedicalRecord getMedicalRecordByFullName(String pFirstName, String pLastName) throws IOException {
		List<MedicalRecord> records = this.data.getMedicalRecords();
		MedicalRecord medicalRecord = new MedicalRecord.MedicalRecordBuilder().build();
		int count = 0;
		while (count < records.size()) {
			if (records.get(count).getFirstName().equals(pFirstName)
					&& records.get(count).getLastName().equals(pLastName)) {
				medicalRecord.setFirstName(records.get(count).getFirstName());
				medicalRecord.setLastName(records.get(count).getLastName());
				medicalRecord.setBirthDate(records.get(count).getBirthDate());
				medicalRecord.setMedications(records.get(count).getMedications());
				medicalRecord.setAllergies(records.get(count).getAllergies());
			}
			count++;
		}
		return medicalRecord;
	}

	/**
	 *
	 * @param firstName
	 * @param lastName
	 * @return
	 * @throws IOException
	 */
	@Override
	public Integer getAgeOfPerson(String firstName, String lastName) throws IOException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDate birthDate = LocalDate.parse(this.getMedicalRecordByFullName(firstName, lastName).getBirthDate(), formatter);
		LocalDate instant = LocalDate.now();
		Integer age = Period.between(birthDate, instant).getYears();
		return age;
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
		Optional<MedicalRecord> medicalRecordOptional = this.data.getMedicalRecords().stream()
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
	public List<MedicalRecordDto> getAllMedicalRecords() throws IOException {
		List<MedicalRecord> medicalRecords = this.data.getMedicalRecords();
		List<MedicalRecordDto> medicalRecordDtos = new ArrayList<>();
		for (MedicalRecord m : medicalRecords) {
			MedicalRecordDto medicalRecordDto = new MedicalRecordDto.MedicalRecordDtoBuilder().firstName(m.getFirstName()).lastName(m.getLastName()).birthDate(m.getBirthDate()).allergies(m.getAllergies()).medications(m.getMedications()).build();
			medicalRecordDtos.add(medicalRecordDto);
		}
		return medicalRecordDtos;
	}

	@Override
	public void updateMedicalRecord(String firstName, String lastName, String allergie) throws IOException {
		Integer index = 0;
		Integer indexOfElement = 0;
		for (MedicalRecord m  : this.data.getMedicalRecords()) {
			if (Objects.equals(m.getFirstName(), firstName) && Objects.equals(m.getLastName(), lastName)) {
				m.getAllergies().add(allergie);
				indexOfElement = index;
			}
			index ++;
		}
		this.data.getMedicalRecords().get(indexOfElement).getAllergies().add(allergie);
	}

	@Override
	public void createMedicalRecord(MedicalRecordDto pMedicalRecord) {
		MedicalRecord medicalRecord = new MedicalRecord.MedicalRecordBuilder().firstName(pMedicalRecord.getFirstName()).lastName(pMedicalRecord.getLastName()).birthDate(pMedicalRecord.getBirthDate()).medications(pMedicalRecord.getMedications()).allergies(pMedicalRecord.getAllergies()).build();
		this.data.getMedicalRecords().add(medicalRecord);
	}

	@Override
	public void deleteMedicalRecordByFullName(String firstName, String lastName) {
		Integer index = 0;
		Integer indexOfElement = 0;
		for (MedicalRecord medicalRecord : this.data.getMedicalRecords()) {
			if (Objects.equals(medicalRecord.getFirstName(), firstName) && Objects.equals(medicalRecord.getLastName(), lastName)) {
				indexOfElement = index;
			}
			index++;
		}
		this.data.getMedicalRecords().remove(indexOfElement);
	}
}
