package com.safetynetalerts.services;

import com.safetynetalerts.controller.exception.BadResourceException;
import com.safetynetalerts.controller.exception.ResourceAlreadyExistsException;
import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.FireStationDto;
import com.safetynetalerts.dto.PersonMedicalRecordDto;
import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.repository.FireStationRepository;
import com.safetynetalerts.repository.PersonRepositoryImpl;
import com.safetynetalerts.service.FireStationServiceImpl;
import com.safetynetalerts.service.MedicalRecordServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FireStationServiceTest {

	@InjectMocks
	private FireStationServiceImpl service;

    @MockBean
	private PersonRepositoryImpl personRepository;

	@Mock
	private FireStationRepository repository;

	@Mock
	private MedicalRecordServiceImpl medicalRecordService;

	private String address = "18 rue jean moulin";

	private String stationNumber = "5";

	private Set<String> addresses;

	private FireStation fireStation;

	private FireStationDto fireStationDto;

	private List<FireStation> fireStations;



	@BeforeEach
	void setUp() {
		this.addresses = new HashSet<>();
		this.addresses.add(address);
		fireStation = new FireStation(this.addresses, stationNumber);
		this.fireStationDto = new FireStationDto(this.addresses, stationNumber);
		this.fireStations = new ArrayList<>();
		this.fireStations.add(fireStation);
	}

    @Test
	public void getAllFireStationsTest() throws IOException {
		String address = "1509 Culver St";
		List<FireStation> expectedFireStations = new ArrayList<>();
		FireStation f1 = new FireStation();
		f1.setStationNumber("2");
		Set<String> addresses = new HashSet<>();
		addresses.add(address);
		f1.setAddresses(addresses);
		expectedFireStations.add(f1);
		List<FireStationDto> expectedFirestationsToCompare = new ArrayList<>();

		for (FireStation firestation : expectedFireStations) {
			FireStationDto fireStationDto = new FireStationDto(new HashSet<>(firestation.getAddresses()), firestation.getStationNumber());
			expectedFirestationsToCompare.add(fireStationDto);
		}

		when(this.repository.getAllFireStations()).thenReturn(expectedFireStations);
		List<FireStationDto> stationsToCompare = this.service.getAllFireStations();
		assertEquals(expectedFirestationsToCompare, stationsToCompare);
	}

	@Test
	public void createFirestationAFireStation() throws ResourceNotFoundException, ResourceAlreadyExistsException, IOException {
		// GIVEN
		FireStationDto fireStationDto = new FireStationDto();
		fireStationDto.setStationNumber("17");
		Set<String> addresses = new HashSet<>();
		String address = "17 rue Général de gaulle";
		addresses.add(address);
		fireStationDto.setAddresses(addresses);
		FireStation fireStation = new FireStation(new HashSet<>(fireStationDto.getAddresses()), fireStationDto.getStationNumber());
		List<FireStation> firestations = new ArrayList<>();
		firestations.add(fireStation);

		// WHEN
		when(this.repository.getAllFireStations()).thenReturn(this.fireStations);
		when(this.repository.createFireStation(fireStation)).thenReturn(fireStation);
		this.service.createFirestation(fireStationDto);
		// THEN
		assertEquals(firestations.get(0).getStationNumber(), "17");
	}

	@Test
	public void createFirestationShouldThrowResourceAlreadyExistsException() throws ResourceNotFoundException, ResourceAlreadyExistsException, IOException {
		String message = "this firestation already exists";

		Mockito.when(this.repository.getAllFireStations()).thenReturn(this.fireStations);
		ResourceAlreadyExistsException exception = Assertions.assertThrows(ResourceAlreadyExistsException.class, () -> this.service.createFirestation(this.fireStationDto), message);

		Assertions.assertEquals(message, exception.getMessage());
		Assertions.assertEquals(HttpStatus.CONFLICT, exception.getStatus());
	}

	@Test
	public void getFireStationsByStationNumberTest () throws IOException {
		String stationNumber = "4";
		Set<String> addresses = new HashSet<>();
		String address = "47 rue du puit";
		addresses.add(address);
		FireStation fireStation = new FireStation(addresses, stationNumber);
		List<FireStation> firestations = new ArrayList<>();
		firestations.add(fireStation);


		when(this.repository.getAllFireStations()).thenReturn(firestations);
		FireStation fireStationToCompare = this.service.getFireStationsByStationNumber(stationNumber);

		assertEquals(fireStation, fireStationToCompare);
	}

	@Test
	public void getStationNumberByAddressShouldReturnANumberIfExists () throws IOException {
		String stationNUmber = "1";
		String address = "78 rue des lapins";
		Set<String> addresses = new HashSet<>();
		addresses.add(address);
		FireStation fireStation = new FireStation(addresses, stationNUmber);
		List<FireStation> firestations = new ArrayList<>();
		firestations.add(fireStation);

		Mockito.when(this.repository.getAllFireStations()).thenReturn(firestations);
		String stationNumberToCompare = this.service.getStationNumberByAddress(address);

		Assertions.assertEquals(stationNUmber, stationNumberToCompare);
	}

	@Test
	public void updateFireStationNumberForAddressShouldReturnAFireStation() throws ResourceNotFoundException, IOException, BadResourceException {
		FireStation oldFirestation = new FireStation(new HashSet<>(this.fireStation.getAddresses()), this.stationNumber);
		Set<String> addresses =  new HashSet<>(this.fireStation.getAddresses());
		addresses.add("98 rue du puit");
		oldFirestation.setAddresses((Set<String>) addresses);
		Mockito.when(this.repository.getAllFireStations()).thenReturn(this.fireStations);
		Mockito.when(this.repository.save(oldFirestation, this.fireStation)).thenReturn(this.fireStation);
		FireStationDto fireStationDtoToCompare = this.service.updateFireStationByAddress(fireStationDto);

		Assertions.assertEquals(this.fireStationDto, fireStationDtoToCompare);
	}

	@Test
	public void updateFirestationShouldThrowBadResourceException () throws BadResourceException {
		String message = "No firestation provided";

		BadResourceException exception = Assertions.assertThrows(BadResourceException.class, () -> this.service.updateFireStationByAddress(null), message);

		Assertions.assertEquals(message, exception.getMessage());
		Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
	}

	@Test
	public void updateFirestationShouldThrowResourceNotFoundException () throws ResourceNotFoundException {
		String message = "this address is not covered by any firestation";

		ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> this.service.updateFireStationByAddress(this.fireStationDto), message);

		Assertions.assertEquals(message, exception.getMessage());
		Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
	}

	@Test
	public void convertToDtoShouldConvertObjectToDto() throws ResourceNotFoundException {
		FireStationDto fireStationDtoToCompare = this.service.convertToDto(this.fireStation);

		Assertions.assertEquals(this.fireStationDto, fireStationDtoToCompare);
	}

	@Test
	public void convertToDtoShouldThrowResourceNotFoundException() throws ResourceNotFoundException {
		String message = "this firestation does not exist";

		ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> this.service.convertToDto(null), message);

		Assertions.assertEquals(message, exception.getMessage());
		Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
	}

	@Test
	public void saveFireStationShouldCreateNewFireStationIfNotExistsOrUpdateExistingOne () throws ResourceNotFoundException {
		FireStation oldFirestation = new FireStation(this.addresses, "4");

		Mockito.when(this.repository.save(oldFirestation, this.fireStation)).thenReturn(this.fireStation);
		FireStation fireStationToCompare = this.service.save(oldFirestation, this.fireStation);

		Assertions.assertEquals(this.fireStation, fireStationToCompare);
	}

	@Test
	public void updateAddressesByFireStationShouldReturnAFireStation() throws ResourceNotFoundException {
		FireStation newFireStation = new FireStation(this.stationNumber);

		Mockito.when(this.repository.save(this.fireStation, newFireStation)).thenReturn(this.fireStation);
		FireStationDto fireStationToCompare = this.service.updateAddressesByFireStation(this.fireStationDto, this.address);

		Assertions.assertEquals(this.fireStationDto, fireStationToCompare);
	}

	@Test
	public void convertDtoToModelShouldReturnAModelObject() {
		FireStation fireStationToCompare = this.service.convertDtoToModel(this.fireStationDto);

		Assertions.assertEquals(this.fireStation, fireStationToCompare);
	}

	@Test
	public void deleteAnAddressInFireStationAddressesShouldReturnAFireStationWithOneLessAddress() throws ResourceNotFoundException {
		this.addresses.clear();
		FireStation newFireStation = new FireStation(this.addresses, "5");
		FireStationDto newFireStationDto = new FireStationDto(this.addresses, "5");

		Mockito.when(this.repository.save(this.fireStation, newFireStation)).thenReturn(newFireStation);
		FireStationDto fireStationToCompare = this.service.deleteAddressOfFireStation(this.fireStationDto, this.address);

		Assertions.assertEquals(newFireStationDto, fireStationToCompare);
	}

	@Test
	public void deleteFireStationShouldDeleteFireStation() throws IOException, ResourceNotFoundException {
		Mockito.when(this.repository.getAllFireStations()).thenReturn(this.fireStations);
		this.service.deleteFirestation(this.fireStationDto);

		Mockito.verify(this.repository).deleteFireStation(fireStation);
	}

	@Test
	public void convertToPersonMedicalRecordShouldReturnAPersonMedicalRecordDto() throws BadResourceException, IOException, ResourceNotFoundException {
		String firstName = "Jean";
		String lastName = "Smith";
		String birthDate = "05/05/2000";
		Person person = new Person.PersonBuilder().firstName(firstName).lastName(lastName).phone("04").build();
		MedicalRecord medicalRecord = new MedicalRecord.MedicalRecordBuilder().firstName(firstName).lastName(lastName).birthDate(birthDate).build();
		PersonMedicalRecordDto personMedicalRecordDto = new PersonMedicalRecordDto(firstName, lastName, "04", 24, null, null);

		Mockito.when(this.medicalRecordService.getAgeOfPerson(firstName, lastName)).thenReturn(24);
		PersonMedicalRecordDto personMRToCompare = this.service.convertToPersonMedicalRecord(person, medicalRecord);

		Assertions.assertEquals(personMedicalRecordDto, personMRToCompare);
	}

	@Test
	public void deleteFirestationShouldThrowResourceNotFoundException() {
		String message = "this firestation doesn't exists";

		ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> this.service.deleteFirestation(this.fireStationDto), message);

		Assertions.assertEquals(message, exception.getMessage());
		Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
	}
}
