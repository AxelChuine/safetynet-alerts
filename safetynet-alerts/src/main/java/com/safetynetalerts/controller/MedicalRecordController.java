package com.safetynetalerts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

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

}
