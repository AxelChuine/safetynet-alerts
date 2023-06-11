package com.safetynetalerts.service.impl;

import com.safetynetalerts.dto.ChildAlertDto;
import com.safetynetalerts.dto.PersonDto;
import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.service.IMedicalRecordService;
import com.safetynetalerts.service.IPersonService;
import com.safetynetalerts.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl implements IPersonService {

	@Autowired
	private FireStationServiceImpl fireStationService;

	@Autowired
	private Utils utils;

	@Autowired
	private IMedicalRecordService medicalRecordService;

	/**
	 * 
	 * @param stationNumber
	 * @return list of persons concerned by a firestation
	 * @throws IOException
	 */

	public List<Person> getAllPersonsByFireStation(String stationNumber) throws IOException {
		List<Person> persons = utils.getAllPeople();
		List<Person> personsByFirestation = new ArrayList<>();
		List<FireStation> firestations = fireStationService.getFireStationsByStationNumber(stationNumber);
		for (FireStation firestation : firestations) {
			for (Person person : persons) {
				if (firestation.getAddresses().contains(person.getAddress())) {
					if (!personsByFirestation.contains(person)) {
						personsByFirestation.add(person);
					}
				}
			}
			/*
			 * personsByFirestation .addAll(persons.stream().filter(person ->
			 * person.getAddress().equals(firestation.getAddresses()))
			 * .collect(Collectors.toList()));
			 */
		}
		return personsByFirestation;
	}

	@Override
	public List<Person> getAllPersonsByCity(String pCity) throws Exception {
		List<Person> persons = this.utils.getAllPeople();
		List<Person> personsToReturn = new ArrayList<>();
		for (Person p : persons) {
			if (p.city.equals(pCity)) {
				personsToReturn.add(p);
			}
		}
		return personsToReturn;
	}

	@Override
	public List<String> getAllEmailAddressesByCity(String pCity) throws Exception {
		List<Person> persons = this.getAllPersonsByCity(pCity);
		List<String> emailAddresses = new ArrayList<>();
		for (Person p : persons) {
			if (p.city.equals(pCity)) {
				emailAddresses.add(p.email);
			}
		}
		return emailAddresses;
	}

	@Override
	public List<String> getPersonInformation(String pFirstName, String pLastName) {
		// TODO Auto-generated method stub
		return null;
	}

	public Person getPersonByFullName(String pFirstName, String pLastName) throws IOException {
		Integer count = 0;
		Person person = null;
		List<Person> persons = this.utils.getAllPeople();
		Optional<Person> personOptional = persons.stream().filter(p -> Objects.equals(p.firstName, pFirstName) && Objects.equals(p.lastName, pLastName)).findFirst();
		if (personOptional.isPresent()) {
			//person = personOptional;
		}
		return person;
	}

	public List<Person> getPersonByAddress(String pAddress) throws IOException {
		List<Person> personsByAddress = this.utils.getAllPeople();
		personsByAddress = personsByAddress.stream().filter(p -> Objects.equals(p.address, pAddress))
				.collect(Collectors.toList());
		return personsByAddress;
	}

	/**
	 *
	 * @param pAddress
	 * @return
	 * @throws IOException
	 */
	@Override
	public List<ChildAlertDto> getChildByAddress(String pAddress) throws IOException {
		List<ChildAlertDto> childrenAlertDto = new ArrayList<>();
		List<Person> peopleByAddress = this.getPersonByAddress(pAddress);
		for (Person p : peopleByAddress) {
			if (this.medicalRecordService.isUnderaged(p.firstName, p.lastName)) {
				ChildAlertDto childAlertDto = new ChildAlertDto();
				childAlertDto.setFirstName(p.firstName);
				childAlertDto.setLastName(p.lastName);
				childAlertDto.setAge(this.medicalRecordService.getAgeOfPerson(p.firstName, p.lastName));
				childAlertDto.setFamily(this.getFamilyMembers(peopleByAddress, p.lastName));
				childrenAlertDto.add(childAlertDto);
			}
		}
		return childrenAlertDto;
	}

	public List<Person> getFamilyMembers(List<Person> pFamilyMember, String pLastName) {
		List<Person> familyMember = new ArrayList<>();
		familyMember = pFamilyMember.stream().filter(p -> Objects.equals(p.lastName, pLastName)).collect(Collectors.toList());
		return familyMember;
	}

	@Override
	public List<Person> getAllPersons() throws IOException {
		return this.utils.getAllPeople();
	}

	@Override
	public void addPerson(PersonDto pPerson) throws IOException {
		this.utils.getPersons().add(new Person.PersonBuilder().firstName(pPerson.getFirstName()).
				lastName(pPerson.getLastName()).address(pPerson.getAddress()).city(pPerson.getCity()).zip(pPerson.getZip())
				.phone(pPerson.getPhone()).email(pPerson.getEmail()).build());
	}

	@Override
	public void updatePerson(String pAddress, String pFirstName, String pLastName) {
	}

	@Override
	public PersonDto convertToPersonDto(Person pPerson) {
		PersonDto person = new PersonDto(pPerson.firstName, pPerson.lastName, pPerson.address, pPerson.city, pPerson.zip, pPerson.phone, pPerson.email);
		return person;
	}

	@Override
	public List<PersonDto> convertToListDto(List<Person> pPersons) {
		List<PersonDto> persons = new ArrayList<>();
		for (Person p : pPersons) {
			PersonDto person = new PersonDto(p.firstName, p.lastName, p.address, p.city, p.zip, p.phone, p.email);
			persons.add(person);
		}
		return persons;
	}
}
