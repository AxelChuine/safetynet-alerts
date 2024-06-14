package com.safetynetalerts.service;

import com.safetynetalerts.controller.exception.ResourceAlreadyExistsException;
import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.ChildAlertDto;
import com.safetynetalerts.dto.PersonDto;
import com.safetynetalerts.dto.PersonInfo;
import com.safetynetalerts.dto.SimplePersonDto;
import com.safetynetalerts.models.Person;

import java.io.IOException;
import java.util.List;

public interface IPersonService {


	List<Person> getAllPersonsByCity(String pCity) throws Exception;

	List<String> getAllEmailAddressesByCity(String pCity) throws Exception;

	PersonDto getPersonByFullName(String pFirstName, String pLastName) throws Exception;

	List<ChildAlertDto> getChildByAddress(String pAddress) throws IOException, ResourceNotFoundException;

	List<PersonDto> getFamilyMembers(List<PersonDto> pFamilyMember, String pLastName);

	List<PersonDto> getAllPersons() throws IOException;

	PersonDto addPerson(PersonDto pPerson) throws ResourceAlreadyExistsException;

	PersonDto updatePerson(String pAddress, String pFirstName, String pLastName) throws IOException, ResourceNotFoundException, Exception;

	void deletePerson(String firstName, String lastName) throws ResourceNotFoundException;

	SimplePersonDto convertToSimplePersonDto(Person personToChange);

	List<SimplePersonDto> convertToDtoList (List<Person> pPersons);


	List<PersonDto> getPersonsByAddress(String address);

	PersonInfo getPersonInfo(String lastName);

	PersonInfo createPersonInfo(Person person);
}
