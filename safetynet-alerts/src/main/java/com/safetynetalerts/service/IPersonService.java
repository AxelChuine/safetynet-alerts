package com.safetynetalerts.service;

import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.ChildAlertDto;
import com.safetynetalerts.dto.PersonDto;
import com.safetynetalerts.dto.SimplePersonDto;
import com.safetynetalerts.models.Person;

import java.io.IOException;
import java.util.List;

public interface IPersonService {


	List<Person> getAllPersonsByCity(String pCity) throws Exception;

	List<String> getAllEmailAddressesByCity(String pCity) throws Exception;

	PersonDto getPersonByFullName(String pFirstName, String pLastName) throws Exception;

	List<ChildAlertDto> getChildByAddress(String pAddress) throws IOException;

	List<Person> getFamilyMembers(List<Person> pFamilyMember, String pLastName);

	List<Person> getAllPersons() throws IOException;

	PersonDto addPerson(PersonDto pPerson) throws IOException;

	PersonDto updatePerson(String pAddress, String pFirstName, String pLastName) throws IOException, ResourceNotFoundException, Exception;

	void deletePerson(String firstName, String lastName);

	SimplePersonDto convertToSimplePersonDto(Person personToChange);

	List<SimplePersonDto> convertToDtoList (List<Person> pPersons);


	List<Person> getPersonsByAddress(String address);
}
