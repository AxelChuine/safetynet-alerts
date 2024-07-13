package com.safetynetalerts.repository;

import com.safetynetalerts.models.MedicalRecord;

import java.util.List;

public interface IMedicalRecordRepository {
    List<MedicalRecord> getAllMedicalRecords();

    MedicalRecord createMedicalRecord(MedicalRecord medicalRecord);

    void deleteMedicalRecord(MedicalRecord medicalRecord);
}
