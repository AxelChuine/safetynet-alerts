package com.safetynetalerts.repository.impl;

import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.repository.IFireStationRepository;
import com.safetynetalerts.utils.Data;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FireStationRepository implements IFireStationRepository {

    private final Data data;

    public FireStationRepository(Data data) {
        this.data = data;
    }

    @Override
    public List<FireStation> getAllFireStations() {
        return new ArrayList<>(this.data.getAllFireStations());
    }

    @Override
    public FireStation createFireStation(FireStation fireStation) {
        return this.data.createFireStation(fireStation);
    }

    @Override
    public FireStation save(FireStation oldFirestation, FireStation newFirestation) {
        return this.data.saveFirestation(oldFirestation, newFirestation);
    }

}
