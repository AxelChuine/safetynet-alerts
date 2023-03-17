package com.safetynetalerts.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.service.FireStationService;
import com.safetynetalerts.utils.Utils;

@Service
public class FireStationServiceImpl implements FireStationService {

	@Autowired
	private Utils utils;

	public List<FireStation> getAllFireStations() throws IOException {
		return utils.getFireStations();
	}

	public List<FireStation> getFireStationsByStationNumber(String stationNumber) throws IOException {
		List<FireStation> firestations = utils.getFireStations().stream()
				.filter(firestation -> firestation.getStationNumber().equals(stationNumber))
				.collect(Collectors.toList());
		return firestations;
	}

}
