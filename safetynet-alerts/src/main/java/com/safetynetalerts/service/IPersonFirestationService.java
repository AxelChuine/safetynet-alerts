package com.safetynetalerts.service;

import com.safetynetalerts.dto.PhoneAlertDto;
import com.safetynetalerts.dto.SimplePersonDto;
import com.safetynetalerts.dto.StationNumberDto;

import java.io.IOException;
import java.util.List;

public interface IPersonFirestationService {
    List<SimplePersonDto> getAllPersonsByFireStation(String stationNumber) throws IOException;

    PhoneAlertDto getCellNumbers (String pStationNumber) throws IOException;

    StationNumberDto getHeadCountByFirestation(String pStationNumber) throws IOException;
}
