package com.safetynetalerts.service.impl;

import com.safetynetalerts.controller.exception.ResourceAlreadyExistsException;
import com.safetynetalerts.controller.exception.ResourceNotFoundException;
import com.safetynetalerts.dto.MedicalRecordDto;
import com.safetynetalerts.dto.PersonDto;
import com.safetynetalerts.models.MedicalRecord;
import com.safetynetalerts.models.Person;
import com.safetynetalerts.repository.IMedicalRecordRepository;
import com.safetynetalerts.service.IMedicalRecordService;
import com.safetynetalerts.utils.Data;
import com.safetynetalerts.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class MedicalRecordServiceImpl implements IMedicalRecordService {

	@Autowired
	private Utils utils;

	@Autowired
	private Data data;
	
	private final IMedicalRecordRepository repository;

    public MedicalRecordServiceImpl(IMedicalRecordRepository repository) {
        this.repository = repository;
    }


    @Override
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


	/**
	 * @Author Axel
	 * @param pPersons
	 * @return a map counting the persons underaged and not.
	 * @throws IOException
	 */
	@Override
	public Map countAllPersons(List<Person> pPersons) throws IOException {
		Map<String, Integer> persons = new HashMap<>();
		Integer adults = 0;
		Integer underaged = 0;
		List<Integer> index = new ArrayList<>();
		List<MedicalRecord> m1 = this.repository.getAllMedicalRecords();
		for (Person p : pPersons) {
			for (MedicalRecord m : m1) {
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

	/**
	 * @param pFirstName
	 * @param pLastName
	 * @return a list of medical records according to the parameters
	 */
	public MedicalRecordDto getMedicalRecordByFullName(String pFirstName, String pLastName) throws ResourceNotFoundException {
		List<MedicalRecord> records = this.repository.getAllMedicalRecords();
		MedicalRecordDto medicalRecord = new MedicalRecordDto.MedicalRecordDtoBuilder().build();
		int count = 0;
		while (count < records.size()) {
			if (records.get(count).getFirstName().equals(pFirstName)
					&& records.get(count).getLastName().equals(pLastName)) {
				medicalRecord.setFirstName(records.get(count).getFirstName());
				medicalRecord.setLastName(records.get(count).getLastName());
				medicalRecord.setBirthDate(records.get(count).getBirthDate());
				medicalRecord.setMedications(records.get(count).getMedications());
				medicalRecord.setAllergies(records.get(count).getAllergies());
			}
			count++;
		}
		return medicalRecord;
	}

	/**
	 *
	 * @param firstName
	 * @param lastName
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@Override
	public Integer getAgeOfPerson(String firstName, String lastName) throws ResourceNotFoundException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDate birthDate = LocalDate.parse(this.getMedicalRecordByFullName(firstName, lastName).getBirthDate(), formatter);
		LocalDate instant = LocalDate.now();
		Integer age = Period.between(birthDate, instant).getYears();
		return age;
	}

	/**
	 * @param pFirstName
	 * @param pLastName
	 * @return true if the person is underaged
	 */
	@Override
	public boolean isUnderaged(String pFirstName, String pLastName) throws IOException {
		boolean isUnderaged = false;
		MedicalRecord medicalRecord = this.getMedicalRecordByUnderage(pFirstName, pLastName);
		if (this.isUnderaged(medicalRecord.getBirthDate())) {
			isUnderaged = true;
		}
		return isUnderaged;
	}

	/**
	 * @return a medical record of a person if underaged
	 */
	@Override
	public MedicalRecord getMedicalRecordByUnderage(String pFirstName, String pLastName) throws IOException {
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
	@Override
	public List<MedicalRecordDto> getAllMedicalRecords() throws IOException {
		List<MedicalRecord> medicalRecords = this.repository.getAllMedicalRecords();
		List<MedicalRecordDto> medicalRecordDtos = new ArrayList<>();
		for (MedicalRecord m : medicalRecords) {
			MedicalRecordDto medicalRecordDto = new MedicalRecordDto.MedicalRecordDtoBuilder().firstName(m.getFirstName()).lastName(m.getLastName()).birthDate(m.getBirthDate()).allergies(m.getAllergies()).medications(m.getMedications()).build();
			medicalRecordDtos.add(medicalRecordDto);
		}
		return medicalRecordDtos;
	}

	@Override
	public MedicalRecordDto updateMedicalRecord(MedicalRecordDto medicalRecordDto) throws ResourceNotFoundException, IOException {
		List<MedicalRecord> medicalRecords = this.repository.getAllMedicalRecords();
		MedicalRecordDto medicalRecordDto1 = new MedicalRecordDto.MedicalRecordDtoBuilder().build();
		MedicalRecord tempMR = convertDtoToModel(medicalRecordDto);
		for (MedicalRecord m : medicalRecords) {
			if (Objects.equals(medicalRecordDto.getFirstName(), m.getFirstName())
					&& Objects.equals(medicalRecordDto.getLastName(), m.getLastName())) {
				medicalRecordDto1 = convertModelToDto(this.repository.saveMedicalRecord(m, tempMR));
			} else {
				throw new ResourceNotFoundException("this medical record doesn't exist");
			}
		}
		return medicalRecordDto1;
	}

	@Override
	public MedicalRecordDto convertModelToDto(MedicalRecord medicalRecord) {
		return new MedicalRecordDto.MedicalRecordDtoBuilder()
				.firstName(medicalRecord.getFirstName())
				.lastName(medicalRecord.getLastName())
				.birthDate(medicalRecord.getBirthDate())
				.allergies(medicalRecord.getAllergies())
				.medications(medicalRecord.getMedications())
				.build();
	}

	@Override
	public MedicalRecordDto createMedicalRecord(MedicalRecordDto pMedicalRecord) throws ResourceNotFoundException, ResourceAlreadyExistsException {
        MedicalRecord medicalRecord = null;
		if (this.repository.getAllMedicalRecords().stream().anyMatch(m -> Objects.equals(m.getFirstName(), pMedicalRecord.getFirstName()) && Objects.equals(m.getLastName(), pMedicalRecord.getLastName()))) {
			throw new ResourceAlreadyExistsException("Le dossier médical existe déja");
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

	@Override
	public void deleteMedicalRecordByFullName(String firstName, String lastName) throws ResourceNotFoundException {
		this.repository.deleteMedicalRecord(convertDtoToModel(this.getMedicalRecordByFullName(firstName, lastName)));
	}

	@Override
	public MedicalRecord convertDtoToModel(MedicalRecordDto medicalRecordByFullName) {
		return new MedicalRecord.MedicalRecordBuilder()
				.firstName(medicalRecordByFullName.getFirstName())
				.lastName(medicalRecordByFullName.getLastName())
				.birthDate(medicalRecordByFullName.getBirthDate())
				.allergies(medicalRecordByFullName.getAllergies())
				.medications(medicalRecordByFullName.getMedications())
				.build();
	}

	@Override
	public List<MedicalRecordDto> getAllMedicalRecordByListOfPersons(List<PersonDto> personDtoList) throws ResourceNotFoundException {
		List<MedicalRecordDto> medicalRecordsToReturn = new ArrayList<>();
		for (PersonDto personDto : personDtoList) {
			MedicalRecordDto medicalRecordDto = this.getMedicalRecordByFullName(personDto.firstName, personDto.lastName);
			medicalRecordsToReturn.add(medicalRecordDto);
		}
		if (medicalRecordsToReturn.isEmpty()) {
			throw new ResourceNotFoundException("Medical Record not found");
		}
        return medicalRecordsToReturn;
	}
}
