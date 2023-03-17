package com.safetynetalerts.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.service.MedicalRecordService;
import com.safetynetalerts.utils.Utils;

@Service
public class MedicationServiceImpl implements MedicalRecordService {

	@Autowired
	private Utils utils;

	@Override
	public List<MedicalRecord> getAllMedicalRecords() throws IOException {
		return null;
	}

	/**
	 * @return a list of the object passed in the string as a parameter
	 * @param pParam
	 *
	 * @Example: Any medications = medicalRecord.get("medications"); if
	 *           (medications.size() > 0) { medications.forEach(a ->
	 *           System.out.println(a.toString())); }
	 */

}
