package com.safetynetalerts.service;

import com.safetynetalerts.controller.exception.BadResourceException;
import com.safetynetalerts.controller.exception.ResourceAlreadyExistsException;
import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.MedicalRecordDto;
import com.safetynetalerts.dto.PersonDto;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.repository.MedicalRecordRepository;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class MedicalRecordServiceImpl {

	private final MedicalRecordRepository repository;

    public MedicalRecordServiceImpl(MedicalRecordRepository repository) {
        this.repository = repository;
    }


    
	public boolean isUnderaged(String pBirthDate) {
		boolean vRet = false;
		DateTimeFormatter dateOfBirth = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDate birthDate = LocalDate.parse(pBirthDate, dateOfBirth);
		LocalDate instant = LocalDate.now();
		Period period = Period.between(birthDate, instant);
		if (period.getYears() < 18) {
			vRet = true;
		}
		return vRet;
	}
	
	public Map<String, Integer> countAllPersons(List<PersonDto> personDtoList) throws IOException {
		Map<String, Integer> persons = new HashMap<>();
		Integer adults = 0;
		Integer underaged = 0;
		List<MedicalRecordDto> m1 = this.getAllMedicalRecords();
		for (PersonDto p : personDtoList) {
			for (MedicalRecordDto m : m1) {
				if (p.getFirstName().equals(m.getFirstName()) && p.getLastName().equals(m.getLastName())) {
					if (!this.isUnderaged(m.getBirthDate())) {
						adults++;
						persons.put("majeurs", adults);
					} else {
						underaged++;
						persons.put("mineurs", underaged);
					}
				}
			}
		}
		return persons;
	}

	public MedicalRecordDto getMedicalRecordByFullName(String firstName, String lastName) throws ResourceNotFoundException, BadResourceException {
		if (Objects.isNull(firstName) || Objects.isNull(lastName)) {
			throw new BadResourceException("The parameters provided are incorrect");
		}
		Optional<MedicalRecord> optionalMedicalRecord = this.repository.getAllMedicalRecords().stream().filter(mr -> mr.getFirstName().equals(firstName) && mr.getLastName().equals(lastName)).findFirst();
        return optionalMedicalRecord.map(this::convertModelToDto).orElse(null);
    }

	public List<MedicalRecordDto> toDtoList(List<MedicalRecord> records) {
		List<MedicalRecordDto> medicalRecordDtos = new ArrayList<>();
		for (MedicalRecord record : records) {
			MedicalRecordDto medicalRecordDto = this.convertModelToDto(record);
			medicalRecordDtos.add(medicalRecordDto);
		}
		return medicalRecordDtos;
	}
	
	public Integer getAgeOfPerson(String firstName, String lastName) throws ResourceNotFoundException, BadResourceException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDate birthDate = LocalDate.parse(this.getMedicalRecordByFullName(firstName, lastName).getBirthDate(), formatter);
		LocalDate instant = LocalDate.now();
		return  Period.between(birthDate, instant).getYears();
	}


	public boolean isUnderaged(String firstName, String lastName) throws IOException {
		boolean isUnderaged = false;
		MedicalRecord medicalRecord = this.getMedicalRecordByUnderage(firstName, lastName);
		if (this.isUnderaged(medicalRecord.getBirthDate())) {
			isUnderaged = true;
		}
		return isUnderaged;
	}
	
	public MedicalRecord getMedicalRecordByUnderage(String pFirstName, String pLastName) {
		MedicalRecord medicalRecord = null;
		Optional<MedicalRecord> medicalRecordOptional = this.repository.getAllMedicalRecords().stream()
				.filter(m -> Objects.equals(m.getFirstName(), pFirstName) && Objects.equals(m.getLastName(), pLastName))
				.findFirst();
		if (medicalRecordOptional.isPresent()) {
			medicalRecord = medicalRecordOptional.get();
		}
		return medicalRecord;
	}

	/**
	 * @return retrieve all medical records
	 */
	
	public List<MedicalRecordDto> getAllMedicalRecords() throws IOException {
		List<MedicalRecord> medicalRecords = this.repository.getAllMedicalRecords();
		List<MedicalRecordDto> medicalRecordDtos = new ArrayList<>();
		for (MedicalRecord m : medicalRecords) {
			MedicalRecordDto medicalRecordDto = new MedicalRecordDto.MedicalRecordDtoBuilder().firstName(m.getFirstName()).lastName(m.getLastName()).birthDate(m.getBirthDate()).allergies(m.getAllergies()).medications(m.getMedications()).build();
			medicalRecordDtos.add(medicalRecordDto);
		}
		return medicalRecordDtos;
	}

	
	public MedicalRecordDto updateMedicalRecord(MedicalRecordDto medicalRecordDto) throws ResourceNotFoundException, IOException, BadResourceException {
		if (Objects.isNull(medicalRecordDto)) {
			throw new BadResourceException("The medical record is not provided");
		}
		MedicalRecordDto medicalRecordDtoToReturn = this.getMedicalRecordByFullName(medicalRecordDto.getFirstName(), medicalRecordDto.getLastName());
		if (Objects.isNull(medicalRecordDtoToReturn)) {
			throw new ResourceNotFoundException("this medical record doesn't exist");
		}
		return this.convertModelToDto(this.repository.saveMedicalRecord(convertDtoToModel(medicalRecordDtoToReturn), convertDtoToModel(medicalRecordDto)));
	}

	
	public MedicalRecordDto convertModelToDto(MedicalRecord medicalRecord) {
		return new MedicalRecordDto.MedicalRecordDtoBuilder()
				.firstName(medicalRecord.getFirstName())
				.lastName(medicalRecord.getLastName())
				.birthDate(medicalRecord.getBirthDate())
				.allergies(medicalRecord.getAllergies())
				.medications(medicalRecord.getMedications())
				.build();
	}

	
	public MedicalRecordDto createMedicalRecord(MedicalRecordDto pMedicalRecord) throws ResourceNotFoundException, ResourceAlreadyExistsException {
        MedicalRecord medicalRecord;
		if (this.repository.getAllMedicalRecords().stream().anyMatch(m -> Objects.equals(m.getFirstName(), pMedicalRecord.getFirstName()) && Objects.equals(m.getLastName(), pMedicalRecord.getLastName()))) {
			throw new ResourceAlreadyExistsException("Le dossier médical existe déjà");
		}
		if (Objects.nonNull(pMedicalRecord)) {
			medicalRecord = new MedicalRecord
					.MedicalRecordBuilder()
					.firstName(pMedicalRecord.getFirstName())
					.lastName(pMedicalRecord.getLastName())
					.birthDate(pMedicalRecord.getBirthDate())
					.medications(pMedicalRecord.getMedications())
					.allergies(pMedicalRecord.getAllergies()).build();
		} else {
			throw new ResourceNotFoundException("Medical record not found");
		}
		this.repository.createMedicalRecord(medicalRecord);
		return pMedicalRecord;
	}

	
	public void deleteMedicalRecordByFullName(String firstName, String lastName) throws ResourceNotFoundException, BadResourceException {
		this.repository.deleteMedicalRecord(convertDtoToModel(this.getMedicalRecordByFullName(firstName, lastName)));
	}

	
	public MedicalRecord convertDtoToModel(MedicalRecordDto medicalRecordByFullName) {
		return new MedicalRecord.MedicalRecordBuilder()
				.firstName(medicalRecordByFullName.getFirstName())
				.lastName(medicalRecordByFullName.getLastName())
				.birthDate(medicalRecordByFullName.getBirthDate())
				.allergies(medicalRecordByFullName.getAllergies())
				.medications(medicalRecordByFullName.getMedications())
				.build();
	}

	
	public List<MedicalRecordDto> getAllMedicalRecordByListOfPersons(List<PersonDto> personDtoList) throws ResourceNotFoundException, BadResourceException, IOException {
		if (Objects.isNull(personDtoList)) {
			throw new BadResourceException("The medical records are not provided");
		}
		List<MedicalRecordDto> medicalRecordDtoList = this.getAllMedicalRecords();
		List<MedicalRecordDto> medicalRecordDtoListToReturn = new ArrayList<>();
		for (PersonDto personDto : personDtoList) {
			Optional<MedicalRecordDto> optionalMedicalRecordDto = medicalRecordDtoList.stream().filter(mr -> Objects.equals(mr.getFirstName(), personDto.getFirstName()) && Objects.equals(mr.getLastName(), personDto.getLastName())).findFirst();
			if (optionalMedicalRecordDto.isPresent()) {
				MedicalRecordDto medicalRecordDto = optionalMedicalRecordDto.get();
				medicalRecordDtoListToReturn.add(medicalRecordDto);
			}
		}
		if (medicalRecordDtoListToReturn.isEmpty()) {
			throw new ResourceNotFoundException("Medical Record not found");
		}
        return medicalRecordDtoListToReturn;
	}
}
