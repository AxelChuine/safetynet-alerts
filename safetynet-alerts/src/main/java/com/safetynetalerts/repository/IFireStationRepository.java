package com.safetynetalerts.repository;

import com.safetynetalerts.models.FireStation;

import java.util.List;

public interface IFireStationRepository {

    List<FireStation> getAllFireStations();
    FireStation createFireStation(FireStation fireStation);
}
