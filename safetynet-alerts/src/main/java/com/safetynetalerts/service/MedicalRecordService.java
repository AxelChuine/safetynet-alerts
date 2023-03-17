package com.safetynetalerts.service;

import java.io.IOException;
import java.util.List;

import com.safetynetalerts.models.MedicalRecord;

public interface MedicalRecordService {
	public List<MedicalRecord> getAllMedicalRecords() throws IOException;
}
