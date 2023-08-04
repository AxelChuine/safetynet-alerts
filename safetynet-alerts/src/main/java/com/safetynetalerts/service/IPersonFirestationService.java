package com.safetynetalerts.service;

import com.safetynetalerts.dto.SimplePersonDto;

import java.io.IOException;
import java.util.List;

public interface IPersonFirestationService {
    List<SimplePersonDto> getAllPersonsByFireStation(String stationNumber) throws IOException;
}
