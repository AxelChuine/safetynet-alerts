package com.safetynetalerts.service;

import java.io.IOException;
import java.util.List;

import com.safetynetalerts.models.FireStation;

public interface IFireStationService {

	List<FireStation> getAllFireStations() throws IOException;

}
