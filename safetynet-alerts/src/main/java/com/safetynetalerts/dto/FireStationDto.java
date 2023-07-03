package com.safetynetalerts.dto;

import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class FireStationDto {

    private Set<String> addresses;

    private String stationNumber;


    public List<String> getAddresses() {return addresses.stream().toList();}


}
