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
		List<Person> persons = utils.getPersons();
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

	public Person getPersonByFullName(String pFirstName, String pLastName) {
		Integer count = 0;
		Person person = new Person.PersonBuilder().build();
		List<Person> persons = this.utils.getPersons();
		Optional<Person> personOptional = persons.stream().filter(p -> Objects.equals(p.firstName, pFirstName) && Objects.equals(p.lastName, pLastName)).findFirst();
		if (personOptional.isPresent()) {
			person = personOptional.get();
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
	public void updatePerson(String pAddress, String pFirstName, String pLastName) throws IOException {
		Person person = this.getPersonByFullName(pFirstName, pLastName);
		Person modifiedPerson = new Person.PersonBuilder().firstName(person.firstName).lastName(person.lastName).address(pAddress).city(person.city).
				zip(person.zip).phone(person.phone).email(person.email).build();
		Integer index = 0;
		if (this.utils.getPersons().stream().anyMatch(p -> Objects.equals(p.firstName, pFirstName) && Objects.equals(p.lastName, pLastName))) {
			for (Person p : this.utils.getPersons()) {
				if (Objects.equals(p.firstName, pFirstName) && Objects.equals(p.lastName, pLastName)) {
					index = this.utils.getPersons().indexOf(p);
				}
			}
			this.utils.getPersons().remove(this.utils.getPersons().get(index));
			this.utils.getPersons().add(modifiedPerson);
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
		List<Person> persons = this.utils.getPersons();
		Integer index = 0;
		for (Person p : persons) {
			if (Objects.equals(p.firstName, firstName) && Objects.equals(p.lastName, lastName)) {
				index = persons.indexOf(p);
			}
		}
		persons.remove(index);
		this.utils.getPersons().addAll(persons);
	}

}
