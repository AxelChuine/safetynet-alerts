package com.safetynetalerts;

import com.safetynetalerts.utils.Utils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class SafetynetAlertsApplicationTests {

	@Autowired
	private App app;

	@MockBean
	private Utils utils;

	@Test
	void contextLoads() {
	}

}
