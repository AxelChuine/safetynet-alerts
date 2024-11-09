package com.safetynetalerts.service;

import com.safetynetalerts.exception.BadResourceException;
import com.safetynetalerts.exception.ResourceAlreadyExistsException;
import com.safetynetalerts.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.*;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.repository.PersonRepositoryImpl;
import com.safetynetalerts.utils.mapper.MapperPerson;
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

    private final MapperPerson mapper;

    public PersonServiceImpl(MedicalRecordServiceImpl medicalRecordService, PersonRepositoryImpl repository, MapperPerson mapper) {
        this.medicalRecordService = medicalRecordService;
        this.repository = repository;
        this.mapper = mapper;
    }


    
    public List<Person> getAllPersonsByCity(String pCity) throws Exception {
        List<Person> persons = this.repository.getAllPersons();
        List<Person> personsToReturn = new ArrayList<>();
        for (Person p : persons) {
            if (p.getCity().equals(pCity)) {
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
            if (Objects.equals(p.getCity(), city)) {
                Optional<String> optionalEmail = emailAddresses.stream().filter(e -> Objects.equals(e, p.getEmail())).findFirst();
                if (!optionalEmail.isPresent()) {
                    emailAddresses.add(p.getEmail());
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
        Optional<PersonDto> personOptional = this.getAllPersons().stream().filter(p -> Objects.equals(p.getFirstName(), pFirstName) && Objects.equals(p.getLastName(), pLastName)).findFirst();
        if (personOptional.isPresent()) {
            return personOptional.get();
        } else {
            throw new ResourceNotFoundException("La personne s'appelant " + pFirstName + " " + pLastName + " n'existe pas.");
        }
    }

    public List<PersonDto> getPersonsByAddress(String address) throws ResourceNotFoundException, BadResourceException {
        if (Objects.isNull(address)) {
            throw new BadResourceException("No person found at this address");
        }
        List<PersonDto> personsByAddress = this.getAllPersons().stream().filter(p -> Objects.equals(p.address, address))
                .collect(Collectors.toList());;
        if (personsByAddress.isEmpty()) {
            throw new ResourceNotFoundException("The people you are looking for don't exist.");
        }
        return personsByAddress;
    }


    
    public List<PersonInfo> getPersonInfo(String lastName) throws ResourceNotFoundException, IOException, BadResourceException {
        List<PersonDto> personDtoList = this.getAllPersons().stream().filter(personDto -> Objects.equals(personDto.lastName, lastName)).toList();
        List<MedicalRecordDto> medicalRecordDtoList = medicalRecordService.getAllMedicalRecordByListOfPersons(personDtoList);
        List<PersonInfo> specificPersonInfos = new ArrayList<>();
        for (PersonDto p : personDtoList) {
            for (MedicalRecordDto medicalRecordDto : medicalRecordDtoList) {
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

    
    public PersonDto updatePerson(PersonDto personDto) throws BadResourceException, ResourceNotFoundException, ResourceAlreadyExistsException {
        PersonDto personToModify = this.getPersonByFullName(personDto.firstName, personDto.lastName);
        return this.mapper.toPersonDto(this.repository.savePerson(this.mapper.toPerson(personToModify), this.mapper.toPerson(personDto)));
    }

    
    public List<PersonDto> getPersonByLastName(String lastName) throws BadResourceException {
        return this.mapper.toPersonDtoList(this.repository.getAllPersons().stream().filter(p -> Objects.equals(p.getLastName(), lastName)).collect(Collectors.toList()));
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

    public List<PersonDto> getFamilyMembers(List<PersonDto> familyMember, String pLastName) {
        return familyMember.stream().filter(p -> Objects.equals(p.lastName, pLastName)).collect(Collectors.toList());
    }

    
    public List<PersonDto> getAllPersons() throws BadResourceException {
        return this.mapper.toPersonDtoList(this.repository.getAllPersons());
    }

    
public PersonDto addPerson(PersonDto personDto) throws ResourceAlreadyExistsException, BadResourceException, ResourceNotFoundException {
    if (Objects.isNull(personDto)) {
       throw new BadResourceException("The person was not provided");
    }
    Optional<PersonDto> optionalPersonDto = this.getAllPersons().stream().filter(p -> Objects.equals(p.firstName, personDto.firstName) && Objects.equals(p.lastName, personDto.lastName)).findFirst();
    if (optionalPersonDto.isPresent()) {
        throw new ResourceAlreadyExistsException("Person already exists.");
    }
    this.repository.savePerson(this.mapper.toPerson(personDto), this.mapper.toPerson(personDto));
    return personDto;
}


    
    public void deletePerson(String firstName, String lastName) throws ResourceNotFoundException {
        List<Person> persons = repository.getAllPersons();
        Person person = null;
        for (Person p : persons) {
            if (Objects.equals(p.getFirstName(), firstName) && Objects.equals(p.getLastName(), lastName)) {
                person = p;
            }
        }
        if (Objects.isNull(person)) {
            throw new ResourceNotFoundException("La personne " + firstName + " " + lastName +" n'existe pas.");
        }
        persons.remove(person);
        this.repository.save(persons);
    }

    
    public SimplePersonDto convertToSimplePersonDto(Person person) {
        return new SimplePersonDto(person.getFirstName(), person.getLastName(), person.getAddress(), person.getPhone());
    }

    
    public List<SimplePersonDto> convertToSimplePersonDtoList(List<Person> pPersons) {
        List<SimplePersonDto> simplePersonDtos = new ArrayList<>();
        for (Person p : pPersons) {
            simplePersonDtos.add(this.convertToSimplePersonDto(p));
        }
        return simplePersonDtos;
    }

    public PersonDto savePerson(PersonDto personDto) throws ResourceAlreadyExistsException, BadResourceException {
        if (Objects.isNull(personDto)) {
            throw new BadResourceException("No person provided");
        }
        Optional<PersonDto> optionalPersonDto = this.getAllPersons().stream().filter(p -> Objects.equals(p.firstName, personDto.firstName) && Objects.equals(p.lastName, personDto.lastName)).findFirst();
        if (optionalPersonDto.isPresent()) {
            throw new ResourceAlreadyExistsException("This person already exists");
        }
        return this.mapper.toPersonDto(this.repository.savePerson(this.mapper.toPerson(personDto)));
    }
}
