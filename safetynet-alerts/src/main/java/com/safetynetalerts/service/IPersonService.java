package com.safetynetalerts.service;

import java.io.IOException;
import java.util.List;

import com.safetynetalerts.models.Person;

public interface IPersonService {

	List<Person> getAllPersonsByFireStation(String stationNumber) throws IOException;

	List<Person> getAllPersonsByCity(String pCity) throws Exception;

	List<String> getAllEmailAddressesByCity(String pCity) throws Exception;

	List<String> getPersonInformation(String pFirstName, String pLastName);

	List<Person> getPersonByFullName(String pFirstName, String pLastName) throws IOException;

}
