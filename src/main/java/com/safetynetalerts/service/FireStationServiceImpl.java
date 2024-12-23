package com.safetynetalerts.service;

import com.safetynetalerts.exception.BadResourceException;
import com.safetynetalerts.exception.ResourceAlreadyExistsException;
import com.safetynetalerts.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.FireStationDto;
import com.safetynetalerts.dto.PersonMedicalRecordDto;
import com.safetynetalerts.models.FireStation;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.repository.FireStationRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class FireStationServiceImpl {

	private final MedicalRecordServiceImpl medicalRecordService;

	private final FireStationRepository repository;

    public FireStationServiceImpl(MedicalRecordServiceImpl medicalRecordService, FireStationRepository repository) {
        this.medicalRecordService = medicalRecordService;
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
	 * @param person
	 * @param pMedicalRecord
	 * @return personMedicalRecordDto
	 * @throws IOException
	 * use the person in parameter and his medical record to create an object personMedicalRecordDto.
	 */
	
	public PersonMedicalRecordDto convertToPersonMedicalRecord(Person person, MedicalRecord pMedicalRecord) throws IOException, ResourceNotFoundException, BadResourceException {
		PersonMedicalRecordDto personMedicalRecordDto = new PersonMedicalRecordDto();
		personMedicalRecordDto.setFirstName(person.getFirstName());
		personMedicalRecordDto.setLastName(person.getLastName());
		personMedicalRecordDto.setAge(this.medicalRecordService.getAgeOfPerson(person.getFirstName(), person.getLastName()));
		personMedicalRecordDto.setPhone(person.getPhone());
		personMedicalRecordDto.setMedications(pMedicalRecord.getMedications());
		personMedicalRecordDto.setAllergies(pMedicalRecord.getAllergies());
		return personMedicalRecordDto;
	}

	
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

	
	public FireStationDto convertToDto(FireStation fireStation) throws ResourceNotFoundException {
		if (Objects.isNull(fireStation)) {
			throw new ResourceNotFoundException("this firestation does not exist");
		}
		return new FireStationDto(new HashSet<>(fireStation.getAddresses()), fireStation.getStationNumber());
	}

	
	public FireStation save(FireStation oldFirestation, FireStation newFirestation) {
		return this.repository.save(oldFirestation, newFirestation);
	}

	
	public FireStationDto updateFireStationByAddress(FireStationDto fireStationDto) throws ResourceNotFoundException, IOException, BadResourceException {
		if (Objects.isNull(fireStationDto)) {
			throw new BadResourceException("No firestation provided");
		}
		Optional<FireStation> optionalFireStation = this.repository.getAllFireStations().stream().filter(fs -> Objects.equals(fs.getStationNumber(), fireStationDto.getStationNumber())).findFirst();
		if (optionalFireStation.isEmpty()) {
			throw new ResourceNotFoundException("this address is not covered by any firestation");
		}
		FireStation oldFirestation = optionalFireStation.get();
        return convertToDto(this.repository.save(oldFirestation, convertDtoToModel(fireStationDto)));
	}

	
	public FireStationDto updateAddressesByFireStation(FireStationDto fireStationDto, String address) {
		FireStationDto oldFireStation = fireStationDto;
		List<String> addresses = new ArrayList<>();
		addresses.addAll(fireStationDto.getAddresses());
		addresses.add(address);
		FireStationDto newFireStation = new FireStationDto(new HashSet<>(addresses), fireStationDto.getStationNumber());
		this.save(convertDtoToModel(oldFireStation), convertDtoToModel(newFireStation));
		return newFireStation;
	}

	
	public FireStation convertDtoToModel(FireStationDto fireStationDto) {
		return new FireStation(new HashSet<>(fireStationDto.getAddresses()), fireStationDto.getStationNumber());
	}

	
	public FireStationDto deleteAddressOfFireStation(FireStationDto fireStationDto, String address) throws ResourceNotFoundException {
        List<String> addresses = new ArrayList<>(fireStationDto.getAddresses());
		addresses.remove(address);
		FireStationDto newFireStationDto = new FireStationDto(new HashSet<>(addresses), fireStationDto.getStationNumber());
        return convertToDto(this.save(convertDtoToModel(fireStationDto), convertDtoToModel(newFireStationDto)));
	}

	
	public void deleteFirestation(FireStationDto fireStationDto) throws IOException, ResourceNotFoundException {
		if (this.getAllFireStations().stream().anyMatch(fs -> Objects.equals(fireStationDto.getStationNumber(), fs.getStationNumber()))) {
			this.repository.deleteFireStation(convertDtoToModel(fireStationDto));
		} else {
			throw new ResourceNotFoundException("this firestation doesn't exists");
		}
	}
}
