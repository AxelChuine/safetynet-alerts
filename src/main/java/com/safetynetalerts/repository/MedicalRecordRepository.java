package com.safetynetalerts.repository;

import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.utils.Data;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MedicalRecordRepository {

    private final Data data;

    public MedicalRecordRepository(Data data) {
        this.data = data;
    }

    
    public List<MedicalRecord> getAllMedicalRecords() {
        return new ArrayList<>(this.data.getAllMedicalRecords());
    }

    
    public MedicalRecord createMedicalRecord(MedicalRecord medicalRecord) {
        return this.data.createMedicalRecord(medicalRecord);
    }

    
    public void deleteMedicalRecord(MedicalRecord medicalRecord) {
        this.data.deleteMedicalRecord(medicalRecord);
    }

    
    public MedicalRecord saveMedicalRecord(MedicalRecord medicalRecord, MedicalRecord newMedicalRecord) {
        return this.data.saveMedicalRecord(medicalRecord, newMedicalRecord);
    }
}
