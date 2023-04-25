package com.safetynetalerts.models;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MedicalRecord {
	private String firstName;
	private String lastName;
	private String birthDate;
	private List<String> allergies;
	private List<String> medications;

	public static class MedicalRecordBuilder {
		private String firstName;
		private String lastName;
		private String birthDate;
		private List<String> allergies;
		private List<String> medications;

		public MedicalRecordBuilder() {

		}

		public MedicalRecordBuilder firstName(String pFirstName) {
			this.firstName = pFirstName;
			return this;
		}

		public MedicalRecordBuilder lastName(String pLastName) {
			this.lastName = pLastName;
			return this;
		}

		public MedicalRecordBuilder birthDate(String pBirthDate) {
			this.birthDate = pBirthDate;
			return this;
		}

		public MedicalRecordBuilder allergies(List<String> pAllergies) {
			this.allergies = pAllergies;
			return this;
		}

		public MedicalRecordBuilder medications(List<String> pMedications) {
			this.medications = pMedications;
			return this;
		}

		public MedicalRecord build() {
			return new MedicalRecord(firstName, lastName, birthDate, allergies, medications);
		}

	}

	/*
	 * public static Class MedicalRecords { private List<MedicalRecord>
	 * medicalReocrds;
	 * 
	 * public List<MedicalRecord> getMedicalRecords() { return getMedicalRecords();
	 * }
	 * 
	 * public void setMedicalRecords(List<MedicalRecord> pMedicalRecords) {
	 * medicalReocrds = pMedicalRecords; }
	 * 
	 * }
	 */

	private MedicalRecord(String pFirstName, String pLastName, String pBirthDate, List<String> pAllergies,
			List<String> pMedications) {
		this.firstName = pFirstName;
		this.lastName = pLastName;
		this.birthDate = pBirthDate;
		this.allergies = pAllergies;
		this.medications = pMedications;
	}

}