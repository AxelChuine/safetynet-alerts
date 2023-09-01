package com.safetynetalerts.controller;

import com.safetynetalerts.service.IFireStationService;
import com.safetynetalerts.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
public class FireStationControllerTest {


    @Autowired
    private FireStationController controller;

    @MockBean
    private IFireStationService service;

    @MockBean
    private Utils utils;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }



}
