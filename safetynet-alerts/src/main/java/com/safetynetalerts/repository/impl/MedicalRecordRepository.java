package com.safetynetalerts.repository.impl;

import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.repository.IMedicalRecordRepository;
import com.safetynetalerts.utils.Data;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MedicalRecordRepository implements IMedicalRecordRepository {

    private final Data data;

    public MedicalRecordRepository(Data data) {
        this.data = data;
    }

    @Override
    public List<MedicalRecord> getAllMedicalRecords() {
        return new ArrayList<>(this.data.getAllMedicalRecords());
    }

    @Override
    public MedicalRecord createMedicalRecord(MedicalRecord medicalRecord) {
        return this.data.createMedicalRecord(medicalRecord);
    }

    @Override
    public void deleteMedicalRecord(MedicalRecord medicalRecord) {
        this.data.deleteMedicalRecord(medicalRecord);
    }

    @Override
    public MedicalRecord saveMedicalRecord(MedicalRecord medicalRecord, MedicalRecord newMedicalRecord) {
        return this.data.saveMedicalRecord(medicalRecord, newMedicalRecord);
    }
}
