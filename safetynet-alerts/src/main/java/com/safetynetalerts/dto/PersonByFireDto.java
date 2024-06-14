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
public class PersonByFireDto {
    private String firstName;
    private String lastName;
    private String cellNumber;
    private Integer age;
    List<String> medications;
    List<String> allergies;
}
