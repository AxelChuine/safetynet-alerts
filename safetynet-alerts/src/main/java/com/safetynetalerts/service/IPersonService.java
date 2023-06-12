package com.safetynetalerts.service;

import com.safetynetalerts.dto.ChildAlertDto;
import com.safetynetalerts.dto.PersonDto;
import com.safetynetalerts.models.Person;

import java.io.IOException;
import java.util.List;

public interface IPersonService {

	List<Person> getAllPersonsByFireStation(String stationNumber) throws IOException;

	List<Person> getAllPersonsByCity(String pCity) throws Exception;

	List<String> getAllEmailAddressesByCity(String pCity) throws Exception;

	List<String> getPersonInformation(String pFirstName, String pLastName);

	Person getPersonByFullName(String pFirstName, String pLastName) throws IOException;


	// FIXME: à tester
	List<ChildAlertDto> getChildByAddress(String pAddress) throws IOException;


	// FIXME: à tester
	List<Person> getFamilyMembers(List<Person> pFamilyMember, String pLastName);

	List<Person> getAllPersons() throws IOException;

	void addPerson(PersonDto pPerson) throws IOException;

	void updatePerson(String pAddress, String pFirstName, String pLastName) throws IOException;

	PersonDto convertToPersonDto(Person pPerson);

	List<PersonDto> convertToListDto(List<Person> pPersons);

}
