package com.safetynetalerts.service;

import com.safetynetalerts.controller.exception.BadResourceException;
import com.safetynetalerts.controller.exception.ResourceAlreadyExistsException;
import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.*;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.repository.PersonRepositoryImpl;
import com.safetynetalerts.utils.Data;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl {

    private final MedicalRecordServiceImpl medicalRecordService;

    private final PersonRepositoryImpl repository;

    public PersonServiceImpl(MedicalRecordServiceImpl medicalRecordService, PersonRepositoryImpl repository) {
        this.medicalRecordService = medicalRecordService;
        this.repository = repository;
    }


    
    public List<Person> getAllPersonsByCity(String pCity) throws Exception {
        List<Person> persons = this.repository.getAllPersons();
        List<Person> personsToReturn = new ArrayList<>();
        for (Person p : persons) {
            if (p.city.equals(pCity)) {
                personsToReturn.add(p);
            }
        }
        return personsToReturn;
    }

    
    public List<String> getAllEmailAddressesByCity(String city) throws Exception {
        if (Objects.isNull(city)) {
            throw new BadResourceException("this city doesn't exist");
        }
        List<Person> persons = this.getAllPersonsByCity(city);
        List<String> emailAddresses = new ArrayList<>();
        for (Person p : persons) {
            if (Objects.equals(p.city, city)) {
                Optional<String> optionalEmail = emailAddresses.stream().filter(e -> Objects.equals(e, p.email)).findFirst();
                if (!optionalEmail.isPresent()) {
                    emailAddresses.add(p.email);
                }
            }
        }
        if (emailAddresses.isEmpty()) {
            throw new ResourceNotFoundException("No email address found");
        }
        return emailAddresses;
    }

    public PersonDto getPersonByFullName(String pFirstName, String pLastName) throws ResourceNotFoundException, BadResourceException {
        if (Objects.isNull(pFirstName) || Objects.isNull(pLastName)) {
            throw new BadResourceException("One or two parameter(s) is are missing");
        }
        Person person;
        List<Person> persons = this.repository.getAllPersons();
        PersonDto personDto;
        Optional<Person> personOptional = persons.stream().filter(p -> Objects.equals(p.getFirstName(), pFirstName) && Objects.equals(p.getLastName(), pLastName)).findFirst();
        if (personOptional.isPresent()) {
            person = personOptional.get();
            personDto = new PersonDto
                    .PersonDtoBuilder()
                    .firstName(person.firstName)
                    .lastName(person.lastName)
                    .address(person.address)
                    .city(person.city)
                    .zip(person.zip)
                    .phone(person.phone)
                    .email(person.email)
                    .build();
        } else {
            throw new ResourceNotFoundException("La personne s'appelant " + pFirstName + " " + pLastName + " n'existe pas.");
        }
        return personDto;
    }

    public List<PersonDto> getPersonsByAddress(String address) throws ResourceNotFoundException, BadResourceException {
        if (Objects.isNull(address)) {
            throw new BadResourceException("No person found at this address");
        }
        List<PersonDto> persons = convertToDtoList(this.repository.getAllPersons());
        List<PersonDto> personsByAddress;
        personsByAddress = persons.stream().filter(p -> Objects.equals(p.address, address))
                .collect(Collectors.toList());
        if (personsByAddress.isEmpty()) {
            throw new ResourceNotFoundException("The people you are looking for don't exist.");
        }
        return personsByAddress;
    }


    
    public List<PersonInfo> getPersonInfo(String lastName) throws ResourceNotFoundException, IOException, BadResourceException {
        List<PersonDto> personDtos = this.getAllPersons().stream().filter(personDto -> Objects.equals(personDto.lastName, lastName)).toList();
        List<MedicalRecordDto> medicalRecordDtos = medicalRecordService.getAllMedicalRecordByListOfPersons(personDtos);
        List<PersonInfo> specificPersonInfos = new ArrayList<>();
        for (PersonDto p : personDtos) {
            for (MedicalRecordDto medicalRecordDto : medicalRecordDtos) {
                PersonInfo specificPersonInfo = new PersonInfo();
                specificPersonInfo.setFirstName(medicalRecordDto.getFirstName());
                specificPersonInfo.setLastName(medicalRecordDto.getLastName());
                specificPersonInfo.setEmail(p.email);
                specificPersonInfo.setAge(this.medicalRecordService.getAgeOfPerson(medicalRecordDto.getFirstName(), medicalRecordDto.getLastName()));
                specificPersonInfo.setAllergies(medicalRecordDto.getAllergies());
                specificPersonInfo.setMedications(medicalRecordDto.getMedications());
                specificPersonInfos.add(specificPersonInfo);
            }
        }
        return specificPersonInfos;
    }

    
    public PersonDto convertToPersonDto(Person person) throws ResourceNotFoundException {
        if (Objects.isNull(person)) {
            throw new ResourceNotFoundException("person not found exception");
        }
        return new PersonDto.PersonDtoBuilder()
                .firstName(person.firstName)
                .lastName(person.lastName)
                .address(person.address)
                .city(person.city)
                .zip(person.zip)
                .phone(person.phone)
                .email(person.email)
                .build();
    }

    
    public Person convertToPerson(PersonDto pPersonDto) throws ResourceNotFoundException {
        if (Objects.isNull(pPersonDto)) {
            throw new ResourceNotFoundException("person not found exception");
        }
        return new Person.PersonBuilder()
                .firstName(pPersonDto.firstName)
                .lastName(pPersonDto.lastName)
                .address(pPersonDto.address)
                .city(pPersonDto.city)
                .zip(pPersonDto.zip)
                .phone(pPersonDto.phone)
                .email(pPersonDto.email)
                .build();
    }

    
    public PersonDto updatePerson(PersonDto personDto) throws BadResourceException, ResourceNotFoundException, ResourceAlreadyExistsException {
        PersonDto personToModify = this.getPersonByFullName(personDto.firstName, personDto.lastName);
        return convertToPersonDto(this.repository.savePerson(convertToPerson(personToModify), convertToPerson(personDto)));
    }

    
    public List<PersonDto> getPersonByLastName(String lastName) {
        return convertToDtoList(this.repository.getAllPersons().stream().filter(p -> Objects.equals(p.lastName, lastName)).collect(Collectors.toList()));
    }


    
    public List<ChildAlertDto> getChildByAddress(String address) throws IOException, ResourceNotFoundException, BadResourceException {
        if (Objects.isNull(address)) {
            throw new BadResourceException("address not provided exception");
        }
        List<ChildAlertDto> childrenAlertDto = new ArrayList<>();
        List<PersonDto> peopleByAddress = this.getPersonsByAddress(address);
        for (PersonDto p : peopleByAddress) {
            if (this.medicalRecordService.isUnderaged(p.firstName, p.lastName)) {
                ChildAlertDto childAlertDto = new ChildAlertDto(p.firstName, p.lastName, this.medicalRecordService.getAgeOfPerson(p.firstName, p.lastName), this.getFamilyMembers(peopleByAddress, p.lastName));
                childrenAlertDto.add(childAlertDto);
            }
        }
        if (childrenAlertDto.isEmpty()) {
            throw new ResourceNotFoundException("No child at this address found");
        }
        return childrenAlertDto;
    }

    public List<PersonDto> getFamilyMembers(List<PersonDto> pFamilyMember, String pLastName) {
        List<PersonDto> familyMember = pFamilyMember.stream().filter(p -> Objects.equals(p.lastName, pLastName)).collect(Collectors.toList());
        return familyMember;
    }

    
    public List<PersonDto> getAllPersons() {
        List<Person> persons = this.repository.getAllPersons();
        List<PersonDto> personsToReturn = new ArrayList<>();
        for (Person p : persons) {
            PersonDto personDto = new PersonDto
                    .PersonDtoBuilder()
                    .firstName(p.firstName)
                    .lastName(p.lastName)
                    .address(p.address)
                    .city(p.city)
                    .zip(p.zip)
                    .phone(p.phone)
                    .email(p.email)
                    .build();
            personsToReturn.add(personDto);
        }
        return personsToReturn;
    }

    
public PersonDto addPerson(PersonDto person) throws ResourceAlreadyExistsException, ResourceNotFoundException {
    if (this.repository.getAllPersons().contains(convertToPerson(person))) {
        throw new ResourceAlreadyExistsException("Person already exists.");
    }
    this.repository.savePerson(convertToPerson(person), convertToPerson(person));
        return person;
    }


    
    public void deletePerson(String firstName, String lastName) throws ResourceNotFoundException {
        List<Person> persons = repository.getAllPersons();
        Person person = null;
        for (Person p : persons) {
            if (Objects.equals(p.firstName, firstName) && Objects.equals(p.lastName, lastName)) {
                person = p;
            }
        }
        if (Objects.isNull(person)) {
            throw new ResourceNotFoundException("La personne " + firstName + " " + lastName +" n'existe pas.");
        }
        persons.remove(person);
        this.repository.save(persons);
    }

    
    public SimplePersonDto convertToSimplePersonDto(Person pPerson) {
        return new SimplePersonDto(pPerson.firstName, pPerson.lastName, pPerson.address, pPerson.phone);
    }

    
    public List<SimplePersonDto> convertToSimplePersonDtoList(List<Person> pPersons) {
        List<SimplePersonDto> simplePersonDtos = new ArrayList<>();
        for (Person p : pPersons) {
            simplePersonDtos.add(this.convertToSimplePersonDto(p));
        }
        return simplePersonDtos;
    }

    
    public List<PersonDto> convertToDtoList(List<Person> pPersons) {
        List<PersonDto> personDtos = new ArrayList<>();
        for (Person p : pPersons) {
            PersonDto personDto = new PersonDto.PersonDtoBuilder()
                    .firstName(p.firstName)
                    .lastName(p.lastName)
                    .address(p.address)
                    .city(p.city)
                    .zip(p.zip)
                    .email(p.email)
                    .phone(p.phone).build();
            personDtos.add(personDto);
        }
        return personDtos;
    }

}
