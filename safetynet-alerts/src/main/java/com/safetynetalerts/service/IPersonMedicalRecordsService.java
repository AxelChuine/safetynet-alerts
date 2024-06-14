package com.safetynetalerts.service;

import com.safetynetalerts.dto.FireDto;

public interface IPersonMedicalRecordsService {
    FireDto getAllConcernedPersonsAndTheirInfosByFire();
}
