package com.safetynetalerts.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class MedicalRecordDto {

    private String firstName;

    private String lastName;

    private String birthDate;

    private List<String> allergies;

    private List<String> medications;

    public static class MedicalRecordDtoBuilder {
        private String firstName;

        private String lastName;

        private String birthDate;

        private List<String> allergies;

        private List<String> medications;

        public MedicalRecordDtoBuilder firstName (String pFirstName) {
            this.firstName = pFirstName;
            return this;
        }

        public MedicalRecordDtoBuilder lastName (String pLastName) {
            this.lastName = pLastName;
            return this;
        }

        public MedicalRecordDtoBuilder birthDate (String pBirthDate) {
            this.birthDate = pBirthDate;
            return this;
        }

        public MedicalRecordDtoBuilder allergies (List<String> pAllergies) {
            this.allergies = pAllergies;
            return this;
        }

        public MedicalRecordDtoBuilder medications (List<String> pMedications) {
            this.medications = pMedications;
            return this;
        }

        public MedicalRecordDto build () {
            return new MedicalRecordDto(firstName, lastName, birthDate, allergies, medications);
        }
    }




    private MedicalRecordDto (String pFirstName, String pLastName, String pBirthDate, List<String> pAllergies, List<String> pMedications) {
        this.firstName = pFirstName;
        this.lastName = pLastName;
        this.birthDate = pBirthDate;
        this.allergies = pAllergies;
        this.medications = pMedications;
    }
}
