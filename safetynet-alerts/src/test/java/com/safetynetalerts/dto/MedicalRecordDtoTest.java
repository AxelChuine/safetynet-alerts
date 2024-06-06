package com.safetynetalerts.dto;

import com.safetynetalerts.service.IMedicalRecordService;
import com.safetynetalerts.utils.Utils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class MedicalRecordDtoTest {

	@MockBean
	private Utils utils;

	@MockBean
	private IMedicalRecordService service;

}
