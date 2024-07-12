package com.safetynetalerts.service.impl;

import com.safetynetalerts.controller.exception.ResourceAlreadyExistsException;
import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.FireStationDto;
import com.safetynetalerts.dto.PersonMedicalRecordDto;
import com.safetynetalerts.dto.SimplePersonDto;
import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.repository.IFireStationRepository;
import com.safetynetalerts.service.IFireStationService;
import com.safetynetalerts.service.IMedicalRecordService;
import com.safetynetalerts.utils.Data;
import com.safetynetalerts.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class FireStationServiceImpl implements IFireStationService {

	@Autowired
	private Utils utils;

	@Autowired
	private Data data;

	@Autowired
	private IMedicalRecordService medicalRecordService;

	private final IFireStationRepository repository;


    public FireStationServiceImpl(IFireStationRepository repository) {
        this.repository = repository;
    }

    public List<FireStationDto> getAllFireStations() throws IOException {
		List<FireStation> firestationsToConvert = this.repository.getAllFireStations();
		List<FireStationDto> fireStationDtos = new ArrayList<>();
		for (FireStation fireStation : firestationsToConvert) {
			FireStationDto fireStationDto = new FireStationDto(new HashSet<>(fireStation.getAddresses()), fireStation.getStationNumber());
			fireStationDtos.add(fireStationDto);
		}
		return fireStationDtos;
	}

	//FIXME: erreur à la création
	public FireStationDto createFirestation(FireStationDto pFirestation) throws ResourceNotFoundException, IOException, ResourceAlreadyExistsException {
		if (this.getAllFireStations().stream().anyMatch(fs -> Objects.equals(fs.getStationNumber(), pFirestation.getStationNumber()))) {
			throw new ResourceAlreadyExistsException("this firestation already exists");
		}
		FireStation fireStation = new FireStation(new HashSet<>(pFirestation.getAddresses()), pFirestation.getStationNumber());
        return convertToDto(this.repository.createFireStation(fireStation));
	}




	/**
	 *
	 * @Author Axel
	 * @param pPerson
	 * @param pMedicalRecord
	 * @return personMedicalRecordDto
	 * @throws IOException
	 * use the person in parameter and his medical record to create an object personMedicalRecordDto.
	 */
	@Override
	public PersonMedicalRecordDto convertToPersonMedicalRecord(Person pPerson, MedicalRecord pMedicalRecord) throws IOException, ResourceNotFoundException {
		PersonMedicalRecordDto personMedicalRecordDto = new PersonMedicalRecordDto();
		personMedicalRecordDto.setFirstName(pPerson.firstName);
		personMedicalRecordDto.setLastName(pPerson.lastName);
		personMedicalRecordDto.setAge(this.medicalRecordService.getAgeOfPerson(pPerson.firstName, pPerson.lastName));
		personMedicalRecordDto.setPhone(pPerson.phone);
		personMedicalRecordDto.setMedications(pMedicalRecord.getMedications());
		personMedicalRecordDto.setAllergies(pMedicalRecord.getAllergies());
		return personMedicalRecordDto;
	}

	@Override
	public FireStation getFireStationsByStationNumber(String stationNumber) throws IOException {
		FireStation fireStation = new FireStation();
		Optional<FireStation> optionalFireStation = this.repository.getAllFireStations().stream()
				.filter(firestation -> Objects.equals(firestation.getStationNumber(), stationNumber))
				.findFirst();
		if (optionalFireStation.isPresent())  {
			fireStation = optionalFireStation.get();
		}
		return fireStation;
	}

	@Override
	public SimplePersonDto createSimplePersonDto(Person pPerson) {
		SimplePersonDto simplePersonDto = new SimplePersonDto(pPerson.firstName, pPerson.lastName, pPerson.address, pPerson.phone);
		return simplePersonDto;
	}

	@Override
	public String getStationNumberByAddress(String address) throws IOException {
		String stationNumber = null;
		List<FireStation> fireStations = this.repository.getAllFireStations();
		for (FireStation fireStation : fireStations) {
			if (fireStation.getAddresses().contains(address)) {
				stationNumber = fireStation.getStationNumber();
			}
		}
		return stationNumber;
	}

	@Override
	public FireStationDto convertToDto(FireStation fireStation) throws ResourceNotFoundException {
		if (Objects.isNull(fireStation)) {
			throw new ResourceNotFoundException("this firestation does not exist");
		}
		return new FireStationDto(new HashSet<>(fireStation.getAddresses()), fireStation.getStationNumber());
	}

	@Override
	public FireStation save(FireStation oldFirestation, FireStation newFirestation) {
		return this.repository.save(oldFirestation, newFirestation);
	}

	@Override
	public FireStationDto updateFireStationByAddress(String address, String stationNumber) throws ResourceNotFoundException, IOException {
		Optional<FireStation> optionalFireStation = this.repository.getAllFireStations().stream().filter(fs -> fs.getAddresses().contains(address)).findFirst();
		if (optionalFireStation.isEmpty()) {
			throw new ResourceNotFoundException("this address is not covered by any firestation");
		}
		FireStation oldFirestation = optionalFireStation.get();
        List<String> addresses = new ArrayList<>(oldFirestation.getAddresses());
		addresses.remove(address);
		oldFirestation.setAddresses(new HashSet<>(addresses));
		FireStation newFirestation = this.getFireStationsByStationNumber(stationNumber);
		if (Objects.isNull(newFirestation)) {
			throw new ResourceNotFoundException("this firestation does not exist");
		}
		addresses = new ArrayList<>(newFirestation.getAddresses());
		addresses.add(address);
		newFirestation.setAddresses(new HashSet<>(addresses));
		this.repository.save(oldFirestation, newFirestation);
		return convertToDto(newFirestation);
	}

	@Override
	public FireStationDto updateAddressesByFireStation(FireStationDto fireStationDto, String address) {
		FireStationDto oldFireStation = fireStationDto;
		List<String> addresses = new ArrayList<>();
		addresses.addAll(fireStationDto.getAddresses());
		addresses.add(address);
		FireStationDto newFireStation = new FireStationDto(new HashSet<>(addresses), fireStationDto.getStationNumber());
		this.save(convertDtoToModel(oldFireStation), convertDtoToModel(newFireStation));
		return newFireStation;
	}

	@Override
	public FireStation convertDtoToModel(FireStationDto fireStationDto) {
		return new FireStation(new HashSet<>(fireStationDto.getAddresses()), fireStationDto.getStationNumber());
	}

	@Override
	public FireStationDto deleteAddressOfFireStation(FireStationDto fireStationDto, String address) throws ResourceNotFoundException {
        List<String> addresses = new ArrayList<>(fireStationDto.getAddresses());
		addresses.remove(address);
		FireStationDto newFireStationDto = new FireStationDto(new HashSet<>(addresses), fireStationDto.getStationNumber());
        return convertToDto(this.save(convertDtoToModel(fireStationDto), convertDtoToModel(newFireStationDto)));
	}
}
