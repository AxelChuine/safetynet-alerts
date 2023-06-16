package com.safetynetalerts.service;

import com.safetynetalerts.dto.FireStationDto;
import com.safetynetalerts.models.FireStation;

import java.io.IOException;
import java.util.List;

public interface IFireStationService {

	List<FireStation> getAllFireStations() throws IOException;

	void createFirestation(FireStation pFirestation);

	FireStationDto convertToFireStationDto(FireStation pFirestation);
}
