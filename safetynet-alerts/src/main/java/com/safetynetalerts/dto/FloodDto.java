package com.safetynetalerts.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FloodDto {
    private String address;

    private List<PersonMedicalRecordDto> persons;
}
