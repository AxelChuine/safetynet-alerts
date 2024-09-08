package com.safetynetalerts.service;

import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.*;

import java.io.IOException;
import java.util.List;

public interface IPersonMedicalRecordsService {
    FireDto getAllConcernedPersonsAndTheirInfosByFire(String address) throws ResourceNotFoundException, IOException;

    List<PersonInfo> getAllPersonInformations(String lastName) throws ResourceNotFoundException, IOException;

    List<PersonByFireDto> convertToPersonByFireDtoList(List<PersonDto> pPersons, List<MedicalRecordDto> pMedicalRecords) throws IOException, ResourceNotFoundException;
}
