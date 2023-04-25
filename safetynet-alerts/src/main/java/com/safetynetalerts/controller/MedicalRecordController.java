package com.safetynetalerts.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.service.IMedicalRecordService;
import com.safetynetalerts.utils.Utils;

@RestController
public class MedicalRecordController {

	@Autowired
	private IMedicalRecordService service;

	@Autowired
	private Utils utils;

	/*
	 * @GetMapping("/personInfo") public List<MedicalRecord>
	 * getPersonInfo(@RequestParam("firstName") String pFirstName,
	 * 
	 * @RequestParam("lastName") String pLastName) {
	 * 
	 * }
	 */

	@GetMapping("medicalreocrds")
	public List<MedicalRecord> getAllMedicalRecords() throws IOException {
		List<MedicalRecord> medicalRecords = this.utils.getAllMedicalRecords();
		return medicalRecords;
	}
}
