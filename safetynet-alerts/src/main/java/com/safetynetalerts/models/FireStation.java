package com.safetynetalerts.models;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@NoArgsConstructor
@Getter
@Setter
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

	public String getStationNumber() {
		return stationNumber;
	}

	public Set<String> getAddresses() {
		return addresses.stream().collect(Collectors.toSet());
	}

	@Override
	public String toString() {
		return stationNumber.concat(": ") + String.join(", ", addresses);
	}

}
