package com.safetynetalerts.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties
@EqualsAndHashCode
public class FireStation {
	private Set<String> addresses;
	private String stationNumber;

	public FireStation(String stationNumber) {
		this.addresses = new HashSet<>();
		this.stationNumber = stationNumber;
	}

	public FireStation addAddress(String address) {
		addresses.add(address);
		return this;
	}

	public List<String> getAddresses() {
		return addresses.stream().toList();
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FireStation fireStation = (FireStation) obj;
		return this.stationNumber.equals(fireStation.stationNumber);
	}

}
