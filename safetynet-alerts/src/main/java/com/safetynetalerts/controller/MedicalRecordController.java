package com.safetynetalerts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.safetynetalerts.service.IMedicalRecordService;

@RestController
public class MedicalRecordController {

	@Autowired
	private IMedicalRecordService service;

	/*
	 * @GetMapping("/personInfo") public List<MedicalRecord>
	 * getPersonInfo(@RequestParam("firstName") String pFirstName,
	 * 
	 * @RequestParam("lastName") String pLastName) {
	 * 
	 * }
	 */
}
