package com.safetynetalerts.service.impl;

import com.safetynetalerts.controller.exception.ResourceAlreadyExistsException;
import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.ChildAlertDto;
import com.safetynetalerts.dto.PersonDto;
import com.safetynetalerts.dto.PersonInfo;
import com.safetynetalerts.dto.SimplePersonDto;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.repository.IPersonRepository;
import com.safetynetalerts.service.IMedicalRecordService;
import com.safetynetalerts.service.IPersonService;
import com.safetynetalerts.utils.Data;
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
    private Data data;

    @Autowired
    private IMedicalRecordService medicalRecordService;

    private final IPersonRepository repository;

    public PersonServiceImpl (IPersonRepository repository) {
        this.repository = repository;
    }


    @Override
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

    @Override
    public List<String> getAllEmailAddressesByCity(String pCity) throws Exception {
        List<Person> persons = this.getAllPersonsByCity(pCity);
        List<String> emailAddresses = new ArrayList<>();
        for (Person p : persons) {
            if (Objects.equals(p.city, pCity)) {
                Optional<String> optionalEmail = emailAddresses.stream().filter(e -> Objects.equals(e, p.email)).findFirst();
                if (!optionalEmail.isPresent()) {
                    emailAddresses.add(p.email);
                }
            }
        }
        return emailAddresses;
    }

    public PersonDto getPersonByFullName(String pFirstName, String pLastName) throws ResourceNotFoundException {
        Person person;
        List<Person> persons = this.repository.getAllPersons();
        PersonDto personDto = null;
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

    public List<PersonDto> getPersonsByAddress(String pAddress) throws ResourceNotFoundException {
        List<PersonDto> persons = convertToPersonDtoList(this.repository.getAllPersons());
        List<PersonDto> personsByAddress = new ArrayList<>();
        personsByAddress = persons.stream().filter(p -> Objects.equals(p.address, pAddress))
                .collect(Collectors.toList());
        return personsByAddress;
    }

    @Override
    public PersonInfo getPersonInfo(String lastName) {
        return null;
    }

    @Override
    public PersonInfo createPersonInfo(Person person) {
        return null;
    }

    @Override
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

    @Override
    public List<PersonDto> convertToPersonDtoList(List<Person> persons) throws ResourceNotFoundException {
        if (Objects.isNull(persons)) {
            throw new ResourceNotFoundException("persons not found exception");
        }
        List<PersonDto> personsDto = new ArrayList<>();
        for (Person person : persons) {
            PersonDto personDto = this.convertToPersonDto(person);
            personsDto.add(personDto);
        }
        return personsDto;
    }

    @Override
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


    @Override
    public List<ChildAlertDto> getChildByAddress(String pAddress) throws IOException, ResourceNotFoundException {
        List<ChildAlertDto> childrenAlertDto = new ArrayList<>();
        List<PersonDto> peopleByAddress = this.getPersonsByAddress(pAddress);
        for (PersonDto p : peopleByAddress) {
            if (this.medicalRecordService.isUnderaged(p.firstName, p.lastName)) {
                ChildAlertDto childAlertDto = new ChildAlertDto(p.firstName, p.lastName, this.medicalRecordService.getAgeOfPerson(p.firstName, p.lastName), this.getFamilyMembers(peopleByAddress, p.lastName));
                childrenAlertDto.add(childAlertDto);
            }
        }
        return childrenAlertDto;
    }

    public List<PersonDto> getFamilyMembers(List<PersonDto> pFamilyMember, String pLastName) {
        List<PersonDto> familyMember = new ArrayList<>();
        familyMember = pFamilyMember.stream().filter(p -> Objects.equals(p.lastName, pLastName)).collect(Collectors.toList());
        return familyMember;
    }

    @Override
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

    @Override
public PersonDto addPerson(PersonDto pPerson) throws ResourceAlreadyExistsException {
        PersonDto personDto = pPerson;
        if (this.repository.getAllPersons().stream().anyMatch(p -> Objects.equals(p.firstName, pPerson.getFirstName()) && Objects.equals(p.lastName, pPerson.getLastName()))) {
            throw new ResourceAlreadyExistsException("La personne que vous essayez de créer existe déjà.");
        }
        Person person = new Person.PersonBuilder().firstName(pPerson.getFirstName()).lastName(pPerson.getLastName()).address(pPerson.getAddress()).city(pPerson.getCity()).zip(pPerson.getZip()).phone(pPerson.getPhone()).email(pPerson.getEmail()).build();
        this.repository.getAllPersons().add(person);
        return pPerson;
    }

    @Override
    public PersonDto updatePerson(String pAddress, String pFirstName, String pLastName) throws ResourceNotFoundException {
        PersonDto personDto = this.getPersonByFullName(pFirstName, pLastName);
        if (Objects.isNull(personDto.getFirstName()) || Objects.isNull(personDto.getLastName())) {
            String resource = "person" + " " + pFirstName + " " + pLastName;
            throw new ResourceNotFoundException(resource);
        }
        Person modifiedPerson = new Person.PersonBuilder().firstName(personDto.getFirstName()).lastName(personDto.getLastName()).address(pAddress).city(personDto.getCity()).
                zip(personDto.getZip()).phone(personDto.getPhone()).email(personDto.getEmail()).build();
        Integer index = 0;
        for (Person p : repository.getAllPersons()) {
            if (Objects.equals(p.firstName, pFirstName) && Objects.equals(p.lastName, pLastName)) {
                index = repository.getAllPersons().indexOf(p);
            }
        }
        this.repository.getAllPersons().remove(repository.getAllPersons().get(index));
        repository.getAllPersons().add(modifiedPerson);

        return personDto;
    }


    @Override
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
        data.setPersons(persons);
    }

    @Override
    public SimplePersonDto convertToSimplePersonDto(Person pPerson) {
        return new SimplePersonDto(pPerson.firstName, pPerson.lastName, pPerson.address, pPerson.phone);
    }

    @Override
    public List<SimplePersonDto> convertToDtoList(List<Person> pPersons) {
        List<SimplePersonDto> simplePersonDtos = new ArrayList<>();
        for (Person p : pPersons) {
            simplePersonDtos.add(this.convertToSimplePersonDto(p));
        }
        return simplePersonDtos;
    }

}
