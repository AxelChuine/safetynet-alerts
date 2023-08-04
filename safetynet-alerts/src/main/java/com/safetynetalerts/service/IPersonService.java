package com.safetynetalerts.service;

import com.safetynetalerts.dto.ChildAlertDto;
import com.safetynetalerts.dto.PersonDto;
import com.safetynetalerts.dto.SimplePersonDto;
import com.safetynetalerts.models.Person;

import java.io.IOException;
import java.util.List;

public interface IPersonService {


	List<Person> getAllPersonsByCity(String pCity) throws Exception;

	List<String> getAllEmailAddressesByCity(String pCity) throws Exception;

	Person getPersonByFullName(String pFirstName, String pLastName) throws IOException;


	// FIXME: à tester
	List<ChildAlertDto> getChildByAddress(String pAddress) throws IOException;


	// FIXME: à tester
	List<Person> getFamilyMembers(List<Person> pFamilyMember, String pLastName);

	List<Person> getAllPersons() throws IOException;

	void addPerson(PersonDto pPerson) throws IOException;

	void updatePerson(String pAddress, String pFirstName, String pLastName) throws IOException;

	List<PersonDto> convertToListDto(List<Person> pPersons);

	void deletePerson(String firstName, String lastName);

	SimplePersonDto convertToSimplePersonDto(Person personToChange);

	PersonDto convertToPersonDto(Person pPerson);
}
