package com.safetynetalerts.service.impl;

import com.safetynetalerts.dto.FireStationDto;
import com.safetynetalerts.dto.PersonMedicalRecordDto;
import com.safetynetalerts.dto.SimplePersonDto;
import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.repository.IFireStationRepository;
import com.safetynetalerts.service.IFireStationService;
import com.safetynetalerts.service.IMedicalRecordService;
import com.safetynetalerts.utils.Data;
import com.safetynetalerts.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class FireStationServiceImpl implements IFireStationService {

	@Autowired
	private Utils utils;

	@Autowired
	private Data data;

	@Autowired
	private IMedicalRecordService medicalRecordService;

	private final IFireStationRepository repository;

	List<FireStation> fireStations = new ArrayList<>();

    public FireStationServiceImpl(IFireStationRepository repository) {
        this.repository = repository;
    }

    public List<FireStationDto> getAllFireStations() throws IOException {
		List<FireStationDto> fireStationDtos = new ArrayList<>();
		for (FireStation fireStation : this.repository.getAllFireStations()) {
			FireStationDto fireStationDto = new FireStationDto(new HashSet<>(fireStation.getAddresses()), fireStation.getStationNumber());
			fireStationDtos.add(fireStationDto);
		}
		return fireStationDtos;
	}

	public void createFirestation(FireStationDto pFirestation) {
		FireStation fireStation = new FireStation(new HashSet<>(pFirestation.getAddresses()), pFirestation.getStationNumber());
		this.repository.getAllFireStations().add(fireStation);
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
	public FireStation getFireStationsByStationNumber(String stationNumber) throws IOException {
		FireStation fireStation = new FireStation();
		Optional<FireStation> optionalFireStation = this.repository.getAllFireStations().stream()
				.filter(firestation -> Objects.equals(firestation.getStationNumber(), stationNumber))
				.findFirst();
		if (optionalFireStation.isPresent())  {
			fireStation = optionalFireStation.get();
		}
		return fireStation;
	}

	@Override
	public SimplePersonDto createSimplePersonDto(Person pPerson) {
		SimplePersonDto simplePersonDto = new SimplePersonDto(pPerson.firstName, pPerson.lastName, pPerson.address, pPerson.phone);
		return simplePersonDto;
	}
}
