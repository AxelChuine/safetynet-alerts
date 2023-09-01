package com.safetynetalerts.service.impl;

import com.safetynetalerts.dto.PersonMedicalRecordDto;
import com.safetynetalerts.dto.PhoneAlertDto;
import com.safetynetalerts.dto.SimplePersonDto;
import com.safetynetalerts.dto.StationNumberDto;
import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.service.IFireStationService;
import com.safetynetalerts.service.IMedicalRecordService;
import com.safetynetalerts.service.IPersonFirestationService;
import com.safetynetalerts.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FireStationServiceImpl implements IFireStationService {

	@Autowired
	private Utils utils;

	@Autowired
	private IMedicalRecordService medicalRecordService;

	List<FireStation> fireStations = new ArrayList<>();

	public List<FireStation> getAllFireStations() throws IOException {
		this.fireStations = this.utils.getAllFirestations();
		return fireStations;
	}

	public void createFirestation(FireStation pFirestation) {
		this.fireStations.add(pFirestation);
	}

	@Override
	public List<PersonMedicalRecordDto> getPersonsAndMedicalRecordsByFirestation(List<String> stations) {
		return null;
	}


	/**
	 *
	 * @Author Axel
	 * @param pPerson
	 * @param pMedicalRecord
	 * @return personMedicalRecordDto
	 * @throws IOException
	 * use the person in parameter and his medical record to create an object personMedicalRecordDto.
	 */
	@Override
	public PersonMedicalRecordDto convertToPersonMedicalRecord(Person pPerson, MedicalRecord pMedicalRecord) throws IOException {
		PersonMedicalRecordDto personMedicalRecordDto = new PersonMedicalRecordDto();
		personMedicalRecordDto.setFirstName(pPerson.firstName);
		personMedicalRecordDto.setLastName(pPerson.lastName);
		personMedicalRecordDto.setAge(this.medicalRecordService.getAgeOfPerson(pPerson.firstName, pPerson.lastName));
		personMedicalRecordDto.setPhone(pPerson.phone);
		personMedicalRecordDto.setMedications(pMedicalRecord.getMedications());
		personMedicalRecordDto.setAllergies(pMedicalRecord.getAllergies());
		return personMedicalRecordDto;
	}

	@Override
	public List<FireStation> getFireStationsByStationNumber(String stationNumber) throws IOException {
		List<FireStation> firestations = utils.getAllFirestations().stream()
				.filter(firestation -> firestation.getStationNumber().equals(stationNumber))
				.collect(Collectors.toList());
		return firestations;
	}

	@Override
	public SimplePersonDto createSimplePersonDto(Person pPerson) {
		SimplePersonDto simplePersonDto = new SimplePersonDto(pPerson.firstName, pPerson.lastName, pPerson.address, pPerson.phone);
		return simplePersonDto;
	}

	@Override
	public StationNumberDto createStationNumberDto(List<Person> persons) throws IOException {
		List<SimplePersonDto> simplePersons = new ArrayList<>();
		for (Person p : persons) {
			SimplePersonDto simplePersonDto = new SimplePersonDto(p.firstName, p.lastName, p.address, p.phone);
			simplePersons.add(simplePersonDto);
		}
		StationNumberDto stationNumberDto = new StationNumberDto();
		stationNumberDto.setPersons(simplePersons);
		Map<String, Integer> mapPersons = this.medicalRecordService.countAllPersons(simplePersons);
		stationNumberDto.setAdult(mapPersons.get("majeurs"));
		stationNumberDto.setUnderaged(mapPersons.get("mineurs"));
		return stationNumberDto;
	}




}
