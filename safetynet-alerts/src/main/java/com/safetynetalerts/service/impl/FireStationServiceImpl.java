package com.safetynetalerts.service.impl;

import com.safetynetalerts.dto.FireStationDto;
import com.safetynetalerts.dto.PersonMedicalRecordDto;
import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.service.IFireStationService;
import com.safetynetalerts.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FireStationServiceImpl implements IFireStationService {

	@Autowired
	private Utils utils;

	List<FireStation> fireStations = new ArrayList<>();

	public List<FireStation> getAllFireStations() throws IOException {
		this.fireStations = this.utils.getAllFirestations();
		return fireStations;
	}

	public void createFirestation(FireStation pFirestation) {
		this.fireStations.add(pFirestation);
	}

	@Override
	public FireStationDto convertToFireStationDto(FireStation pFirestation) {
		return null;
	}

	@Override
	public List<PersonMedicalRecordDto> getPersonsAndMedicalRecordsByFirestation(List<String> stations) {
		return null;
	}

	public List<FireStation> getFireStationsByStationNumber(String stationNumber) throws IOException {
		List<FireStation> firestations = utils.getAllFirestations().stream()
				.filter(firestation -> firestation.getStationNumber().equals(stationNumber))
				.collect(Collectors.toList());
		return firestations;
	}

}
