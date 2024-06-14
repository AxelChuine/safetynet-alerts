package com.safetynetalerts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class SpecificPersonInfo {
    private String firstName;
    private String lastName;
    private Integer age;
    private String email;
    private List<String> allergies;
    private List<String> medications;
}
