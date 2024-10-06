package com.safetynetalerts.repository;

import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.utils.Data;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FireStationRepository {

    private final Data data;

    public FireStationRepository(Data data) {
        this.data = data;
    }

    
    public List<FireStation> getAllFireStations() {
        return new ArrayList<>(this.data.getAllFireStations());
    }

    
    public FireStation createFireStation(FireStation fireStation) {
        return this.data.createFireStation(fireStation);
    }

    
    public FireStation save(FireStation oldFirestation, FireStation newFirestation) {
        return this.data.saveFirestation(oldFirestation, newFirestation);
    }

    
    public void deleteFireStation(FireStation fireStation) {
        this.data.deleteFireStation(fireStation);
    }

}
