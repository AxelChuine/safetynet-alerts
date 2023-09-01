package com.safetynetalerts.service.impl;

import com.safetynetalerts.dto.SimplePersonDto;
import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.service.IFireStationService;
import com.safetynetalerts.service.IPersonFirestationService;
import com.safetynetalerts.service.IPersonService;
import com.safetynetalerts.utils.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PersonFirestationServiceImpl implements IPersonFirestationService {

    @Autowired
    private IFireStationService fireStationService;
    @Autowired
    private Data data;

    @Autowired
    private IPersonService personService;


    public List<SimplePersonDto> getAllPersonsByFireStation(String stationNumber) throws IOException {
        List<Person> persons = data.getPersons();
        List<SimplePersonDto> personsByFirestation = new ArrayList<>();
        List<FireStation> firestations = fireStationService.getFireStationsByStationNumber(stationNumber);
        for (FireStation firestation : firestations) {
            for (Person person : persons) {
                if (firestation.getAddresses().contains(person.getAddress())) {
                    if (!personsByFirestation.contains(person)) {
                        personsByFirestation.add(this.personService.convertToSimplePersonDto(person));
                    }
                }
            }
        }
        return personsByFirestation;
    }
}
