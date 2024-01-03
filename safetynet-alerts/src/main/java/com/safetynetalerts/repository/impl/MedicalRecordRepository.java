package com.safetynetalerts.repository.impl;

import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.repository.IMedicalRecordRepository;
import com.safetynetalerts.utils.Data;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MedicalRecordRepository implements IMedicalRecordRepository {

    private final Data data;

    public MedicalRecordRepository(Data data) {
        this.data = data;
    }

    @Override
    public List<MedicalRecord> getAllMedicalRecords() {
        return this.data.getMedicalRecords();
    }
}
