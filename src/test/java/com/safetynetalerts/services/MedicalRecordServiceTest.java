package com.safetynetalerts.services;

import com.safetynetalerts.controller.exception.BadResourceException;
import com.safetynetalerts.controller.exception.ResourceAlreadyExistsException;
import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.MedicalRecordDto;
import com.safetynetalerts.dto.PersonDto;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.repository.MedicalRecordRepository;
import com.safetynetalerts.service.MedicalRecordServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordServiceTest {

    @InjectMocks
	private MedicalRecordServiceImpl service;

    @Mock
	private MedicalRecordRepository repository;

	private MedicalRecord medicalRecord;

	private MedicalRecordDto medicalRecordDto;

    private List<PersonDto> persons;

	private final String firstName = "Jean";

	private final String lastName = "Doe";

	private final String birthDate = "04/05/2000";

	private List<MedicalRecord> medicalRecords;

	private List<MedicalRecordDto> medicalRecordDtos;

	@BeforeEach
	public void setUp() {
		medicalRecord = new MedicalRecord.MedicalRecordBuilder().firstName(firstName).lastName(lastName).birthDate(birthDate).build();
		medicalRecordDto = new MedicalRecordDto.MedicalRecordDtoBuilder().firstName(firstName).lastName(lastName).birthDate(birthDate).build();
        PersonDto personDto = new PersonDto.PersonDtoBuilder().firstName(firstName).lastName(lastName).build();
		this.persons = List.of(personDto);
		this.medicalRecords = List.of(medicalRecord);
		this.medicalRecordDtos = new ArrayList<>(List.of(medicalRecordDto));
	}

	@Test
	void getMedicalRecordByFullNameShouldReturnAMedicalRecordDto() throws IOException, ResourceNotFoundException, BadResourceException {
		String vFirstName = "John";
		String vLastName = "Boyd";
		List<String> medications = new ArrayList<>();
		medications.add("paracétamol");
		List<String> allergies = new ArrayList<>();
		allergies.add("lactose");
		MedicalRecord medicalRecord = new MedicalRecord.MedicalRecordBuilder().firstName(vFirstName).lastName(vLastName).birthDate("01/01/2001").medications(medications).allergies(allergies).build();
		List<MedicalRecord> medicalRecords = new ArrayList<>();
		medicalRecords.add(medicalRecord);

		when(this.repository.getAllMedicalRecords()).thenReturn(medicalRecords);
		MedicalRecordDto medicalRecordsToCompare = this.service.getMedicalRecordByFullName(vFirstName, vLastName);

		assertEquals(medicalRecord.getFirstName(), medicalRecordsToCompare.getFirstName());
		assertEquals(medicalRecord.getLastName(), medicalRecordsToCompare.getLastName());
	}

	@Test
	public void getMedicalRecordByFullNameShouldThrowBadResourceException() throws ResourceNotFoundException, BadResourceException {
		BadResourceException badResourceException = Assertions.assertThrows(BadResourceException.class, () -> this.service.getMedicalRecordByFullName(null, lastName));

		Assertions.assertEquals(HttpStatus.BAD_REQUEST, badResourceException.getStatus());
	}

	@Test
	void getAgeOfPersonTest() throws IOException, ResourceNotFoundException, BadResourceException {
		MedicalRecord medicalRecord = new MedicalRecord();
		medicalRecord.setFirstName("John");
		medicalRecord.setLastName("Dubois");
		medicalRecord.setBirthDate("01/01/2000");
		List<MedicalRecord> medicalRecords = new ArrayList<>();
		medicalRecords.add(medicalRecord);

		when(this.repository.getAllMedicalRecords()).thenReturn(medicalRecords);
		Integer ageToCompare = this.service.getAgeOfPerson(medicalRecord.getFirstName(), medicalRecord.getLastName());
		assertEquals(24, ageToCompare);
	}

	@Test
	void getAllMedicalRecordsTest() throws IOException {
		MedicalRecord medicalRecord = new MedicalRecord();
		MedicalRecord medicalRecord2 = new MedicalRecord();
		MedicalRecord medicalRecord3 = new MedicalRecord();
		List<MedicalRecord> medicalRecords = new ArrayList<>();
		medicalRecords.add(medicalRecord);
		medicalRecords.add(medicalRecord2);
		medicalRecords.add(medicalRecord3);

		List<MedicalRecordDto> medicalRecordDtos = new ArrayList<>();
		for (MedicalRecord m : medicalRecords) {
			MedicalRecordDto medicalRecordDto = new MedicalRecordDto.MedicalRecordDtoBuilder().firstName(m.getFirstName()).lastName(m.getLastName()).birthDate(m.getBirthDate()).allergies(m.getAllergies()).medications(m.getMedications()).build();
			medicalRecordDtos.add(medicalRecordDto);
		}

		when(this.repository.getAllMedicalRecords()).thenReturn(medicalRecords);
		List<MedicalRecordDto> medicalRecordsToCompare = this.service.getAllMedicalRecords();
		assertEquals(medicalRecordDtos.get(0).getFirstName(), medicalRecordsToCompare.get(0).getFirstName());
		assertEquals(medicalRecordDtos.get(0).getLastName(), medicalRecordsToCompare.get(0).getLastName());
	}

	@Test
	public void getMedicalRecordByUnderageTest() throws IOException {
		MedicalRecord m = new MedicalRecord();
		m.setFirstName("Jean");
		m.setLastName("Dubois");
		m.setBirthDate("05/05/2023");
		List<MedicalRecord> medicalRecords = new ArrayList<>();
		medicalRecords.add(m);

		when(this.repository.getAllMedicalRecords()).thenReturn(medicalRecords);
		MedicalRecord mToCompare = this.service.getMedicalRecordByUnderage(m.getFirstName(), m.getLastName());
		assertEquals(m, mToCompare);
	}

	@Test
	public void countAllPersonsTest () throws IOException {
		String address = "108 allée des pins";
		// création de ma map
		Map<String, Integer> mapPersons = new HashMap<>();
		mapPersons.put("mineurs", 1);
		mapPersons.put("majeurs", 1);
		String firstName1 = "Marc";
		String firstName2 = "Jean";
		String lastName1 = "Dubois";
		String lastName2 = "Dubois";
		String birthDate1 = "01/01/2000";
		String birthDate2 = "01/01/2021";

		// création d'une liste de medicalRecords

		//création de medical record et ajout à la liste
		MedicalRecord medicalRecord1 = new MedicalRecord.MedicalRecordBuilder().firstName(firstName1).lastName(lastName1).birthDate(birthDate1).build();
		MedicalRecord medicalRecord2 = new MedicalRecord.MedicalRecordBuilder().firstName(firstName2).lastName(lastName2).birthDate(birthDate2).build();
		MedicalRecordDto m1 = new MedicalRecordDto.MedicalRecordDtoBuilder().firstName(firstName1).lastName(lastName1).birthDate(birthDate1).build();
		MedicalRecordDto m2 = new MedicalRecordDto.MedicalRecordDtoBuilder().firstName(firstName2).lastName(lastName2).birthDate(birthDate2).build();
		List<MedicalRecord> medicalRecords = List.of(medicalRecord1, medicalRecord2);
		List<MedicalRecordDto> medicalRecordDtoList = List.of(m1, m2);

		// création de personnes et ajout à une liste de personnes
		PersonDto p1 = new PersonDto.PersonDtoBuilder().firstName("Marc").lastName("Dubois").address(address).build();
		PersonDto p2 = new PersonDto.PersonDtoBuilder().firstName("Jean").lastName("Dubois").address(address).build();
		List<PersonDto> personDtoList = List.of(p1, p2);


		Mockito.when(this.repository.getAllMedicalRecords()).thenReturn(medicalRecords);
		Map<String, Integer> mapPersonsToCompare = this.service.countAllPersons(personDtoList);

		assertEquals(mapPersons, mapPersonsToCompare);
	}

	@Test
	public void isUnderagedTest () throws IOException {
		boolean isUnderaged = true;
		String firstName = "Jean";
		String lastName = "Dubois";
		MedicalRecord medicalRecord = new MedicalRecord();
		medicalRecord.setLastName(lastName);
		medicalRecord.setFirstName(firstName);
		medicalRecord.setBirthDate("08/12/2020");
		List<MedicalRecord> medicalRecords = new ArrayList<>();
		medicalRecords.add(medicalRecord);


		when(this.repository.getAllMedicalRecords()).thenReturn(medicalRecords);
		boolean isUnderagedToCompare = this.service.isUnderaged(firstName, lastName);

		assertEquals(isUnderaged, isUnderagedToCompare);
	}

	@Test
	public void createMedicalRecordShouldCreateAMedicalRecord () throws ResourceNotFoundException, ResourceAlreadyExistsException {
		when(this.repository.createMedicalRecord(medicalRecord)).thenReturn(medicalRecord);
		MedicalRecordDto medicalRecordToCompare = this.service.createMedicalRecord(medicalRecordDto);

		assertEquals(medicalRecordDto, medicalRecordToCompare);
	}

	@Test
	public void createMedicalRecordShouldThrowBadResourceExceptionIfTheMedicalRecordIsNotProvided() {
		ResourceNotFoundException resourceException = new ResourceNotFoundException();
		String message = "Medical record not found";
		resourceException.setMessage(message);
		resourceException.setStatus(HttpStatus.NOT_FOUND);

		ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> this.service.createMedicalRecord(null), message);

		Assertions.assertEquals(resourceException, exception);
		Assertions.assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
	}

	@Test
	public void createMedicalRecordShouldThrowResourceAlreadyExistsExceptionIfTheMedicalRecordExists() {
		ResourceAlreadyExistsException resourceException = new ResourceAlreadyExistsException();
		String message = "Le dossier médical existe déjà";
		resourceException.setMessage(message);
		resourceException.setStatus(HttpStatus.CONFLICT);

		Mockito.when(this.repository.getAllMedicalRecords()).thenReturn(this.medicalRecords);
		ResourceAlreadyExistsException exception = Assertions.assertThrows(ResourceAlreadyExistsException.class, () -> this.service.createMedicalRecord(this.medicalRecordDto), message);

		Assertions.assertEquals(resourceException, exception);
		Assertions.assertEquals(exception.getStatus(), HttpStatus.CONFLICT);
		Assertions.assertEquals(exception.getMessage(), message);
	}

	@Test
	public void updateMedicalRecordShouldReturnAOfMedicalRecordDto () throws ResourceNotFoundException, IOException, BadResourceException {
		MedicalRecord medicalRecord = new MedicalRecord.MedicalRecordBuilder()
				.firstName(firstName)
				.lastName(lastName)
				.birthDate(birthDate)
				.build();

		when(this.repository.getAllMedicalRecords()).thenReturn(medicalRecords);
		when(this.repository.saveMedicalRecord(this.medicalRecord, medicalRecord)).thenReturn(medicalRecord);
		MedicalRecordDto medicalRecordToCompare = this.service.updateMedicalRecord(this.medicalRecordDto);

		assertEquals(this.medicalRecordDto, medicalRecordToCompare);
	}

	@Test
	public void updateMedicalRecordShouldThrowBadResourceException () throws ResourceNotFoundException {
		String message = "The medical record is not provided";

		BadResourceException exception = Assertions.assertThrows(BadResourceException.class, () -> this.service.updateMedicalRecord(null), message);

		Assertions.assertEquals(message, exception.getMessage());
		Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
	}

	@Test
	public void updateMedicalRecordShouldThrowResourceNotFoundException () throws ResourceNotFoundException {
		String message = "this medical record doesn't exist";
		MedicalRecordDto mr = new MedicalRecordDto.MedicalRecordDtoBuilder().firstName("Bertrand").lastName("Dubois").birthDate("05/05/1989").build();

		when(this.repository.getAllMedicalRecords()).thenReturn(this.medicalRecords);
		ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> this.service.updateMedicalRecord(mr), message);

		Assertions.assertEquals(message, exception.getMessage());
		Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
	}


	@Test
	public void deleteMedicalRecordShouldDeleteAMedicalRecord() throws ResourceNotFoundException, BadResourceException {
		Mockito.when(this.repository.getAllMedicalRecords()).thenReturn(this.medicalRecords);
		this.service.deleteMedicalRecordByFullName(firstName, lastName);

		verify(this.repository).deleteMedicalRecord(medicalRecord);
	}

	@Test
	public void getAllMedicalRecordByListOfPersonsShouldReturnAListOfMedicalRecordsDto () throws ResourceNotFoundException, BadResourceException {
		Mockito.when(this.repository.getAllMedicalRecords()).thenReturn(this.medicalRecords);
		List<MedicalRecordDto> medicalRecordsToCompare = this.service.getAllMedicalRecordByListOfPersons(persons);

		Assertions.assertEquals(this.medicalRecordDtos, medicalRecordsToCompare);
	}

	@Test
	public void convertDtoToModelShouldConvertDtoToModel() {
		MedicalRecord medicalRecordToCompare = this.service.convertDtoToModel(this.medicalRecordDto);

		Assertions.assertEquals(this.medicalRecord, medicalRecordToCompare);
	}

	@Test
	public void convertModelToDtoShouldReturnADtoObject() {
		MedicalRecordDto medicalRecordToCompare = this.service.convertModelToDto(this.medicalRecord);

		Assertions.assertEquals(this.medicalRecordDto, medicalRecordToCompare);
	}

	@Test
	public void updateMedicalRecordShouldUpdateAMedicalRecord() throws IOException, ResourceNotFoundException, BadResourceException {
		MedicalRecord newMedicalRecord = new MedicalRecord.MedicalRecordBuilder()
				.firstName(firstName)
				.lastName(lastName)
				.birthDate(birthDate)
				.build();

		Mockito.when(this.repository.getAllMedicalRecords()).thenReturn(this.medicalRecords);
		Mockito.when(this.repository.saveMedicalRecord(medicalRecord, newMedicalRecord)).thenReturn(newMedicalRecord);
		MedicalRecordDto medicalRecordToCompare = this.service.updateMedicalRecord(medicalRecordDto);

		Assertions.assertEquals(this.medicalRecordDto, medicalRecordToCompare);
	}

	@Test
	public void toDtoListShouldReturnAListOfDto() {
		List<MedicalRecordDto> medicalRecordToCompare = this.service.toDtoList(this.medicalRecords);

		Assertions.assertEquals(this.medicalRecordDtos, medicalRecordToCompare);
	}
}
