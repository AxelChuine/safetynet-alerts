package com.safetynetalerts.models;

import java.util.List;

import com.jsoniter.any.Any;

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
	private List<Any> allergies;
	private List<Any> medications;

	public static class MedicalRecordBuilder {
		private String firstName;
		private String lastName;
		private String birthDate;
		private List<Any> allergies;
		private List<Any> medications;

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

		public MedicalRecordBuilder allergies(List<Any> pAllergies) {
			this.allergies = pAllergies;
			return this;
		}

		public MedicalRecordBuilder medications(List<Any> pMedications) {
			this.medications = pMedications;
			return this;
		}

		public MedicalRecord build() {
			return new MedicalRecord(firstName, lastName, birthDate, allergies, medications);
		}

	}

	private MedicalRecord(String pFirstName, String pLastName, String pBirthDate, List<Any> pAllergies,
			List<Any> pMedications) {
		this.firstName = pFirstName;
		this.lastName = pLastName;
		this.birthDate = pBirthDate;
		this.allergies = pAllergies;
		this.medications = pMedications;
	}

}
