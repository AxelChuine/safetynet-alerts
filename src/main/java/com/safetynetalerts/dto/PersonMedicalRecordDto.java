package com.safetynetalerts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonMedicalRecordDto {

    private String firstName;

    private String lastName;

    private String phone;

    private Integer age;

    private List<String> medications;

    private List<String> allergies;
}
