package com.safetynetalerts.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.safetynetalerts.service.IMedicalRecordService;
import com.safetynetalerts.utils.Utils;

@SpringBootTest
public class MedicalRecordDtoTest {

	@MockBean
	private Utils utils;

	@MockBean
	private IMedicalRecordService service;

	@Autowired
	private MedicalRecordDto dto;

}
