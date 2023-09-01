package com.safetynetalerts.service.impl;

import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.ChildAlertDto;
import com.safetynetalerts.dto.PersonDto;
import com.safetynetalerts.dto.SimplePersonDto;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.service.IMedicalRecordService;
import com.safetynetalerts.service.IPersonService;
import com.safetynetalerts.utils.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
	private Data data;

	@Autowired
	private IMedicalRecordService medicalRecordService;


	@Override
	public List<Person> getAllPersonsByCity(String pCity) throws Exception {
		List<Person> persons = data.getPersons();
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

	public Person getPersonByFullName(String pFirstName, String pLastName) {
		Integer count = 0;
		Person person = new Person.PersonBuilder().build();
		List<Person> persons = data.getPersons();
		Optional<Person> personOptional = persons.stream().filter(p -> Objects.equals(p.firstName, pFirstName) && Objects.equals(p.lastName, pLastName)).findFirst();
		if (personOptional.isPresent()) {
			person = personOptional.get();
		}
		return person;
	}

	public List<Person> getPersonsByAddress(String pAddress) throws IOException {
		List<Person> personsByAddress = data.getPersons();
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
		List<Person> peopleByAddress = this.getPersonsByAddress(pAddress);
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
	public List<Person> getAllPersons() {
		return data.getPersons();
	}

	@Override
	public void addPerson(PersonDto pPerson) {
		data.getPersons().add(new Person.PersonBuilder().firstName(pPerson.getFirstName()).
				lastName(pPerson.getLastName()).address(pPerson.getAddress()).city(pPerson.getCity()).zip(pPerson.getZip())
				.phone(pPerson.getPhone()).email(pPerson.getEmail()).build());
	}

	@Override
	public void updatePerson(String pAddress, String pFirstName, String pLastName) throws Exception {
		Person person = this.getPersonByFullName(pFirstName, pLastName);
		if (Objects.isNull(person)) {
			throw new ResourceNotFoundException(new String(pFirstName + " " + pLastName), HttpStatus.NOT_FOUND);
		}
		Person modifiedPerson = new Person.PersonBuilder().firstName(person.firstName).lastName(person.lastName).address(pAddress).city(person.city).
				zip(person.zip).phone(person.phone).email(person.email).build();
		Integer index = 0;
			if (data.getPersons().stream().anyMatch(p -> Objects.equals(p.firstName, pFirstName) && Objects.equals(p.lastName, pLastName))) {
				for (Person p : data.getPersons()) {
					if (Objects.equals(p.firstName, pFirstName) && Objects.equals(p.lastName, pLastName)) {
						index = data.getPersons().indexOf(p);
					}
				}
				this.data.getPersons().remove(data.getPersons().get(index));
				data.getPersons().add(modifiedPerson);
		}
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

	@Override
	public void deletePerson(String firstName, String lastName) {
		List<Person> persons = data.getPersons();
		Person person = null;
		for (Person p : persons) {
			if (Objects.equals(p.firstName, firstName) && Objects.equals(p.lastName, lastName)) {
				person = p;
			}
		}
		persons.remove(person);
		data.setPersons(persons);
	}

	@Override
	public SimplePersonDto convertToSimplePersonDto(Person pPerson) {
		SimplePersonDto person = new SimplePersonDto(pPerson.firstName, pPerson.lastName, pPerson.address, pPerson.phone);
		return person;
	}

}
