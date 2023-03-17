package com.safetynetalerts.service;

import java.io.IOException;
import java.util.List;

import com.safetynetalerts.models.Person;

public interface PersonService {

	List<Person> getAllPersonsByFireStation(String stationNumber) throws IOException;

}
