package com.safetynetalerts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
public class PersonDto {
    public final String firstName;
    public final String lastName;
    public final String address;
    public final String city;
    public final String zip;
    public final String phone;
    public final String email;

    public static class PersonDtoBuilder {
        private String firstName;
        private String lastName;
        private String address;
        private String city;
        private String zip;
        private String phone;
        private String email;

        public PersonDtoBuilder() {}

        public PersonDto.PersonDtoBuilder firstName(String pFirstName) {
            this.firstName = pFirstName;
            return this;
        }

        public PersonDto.PersonDtoBuilder lastName(String pLastName) {
            this.lastName = pLastName;
            return this;
        }

        public PersonDto.PersonDtoBuilder address(String pAddress) {
            this.address = pAddress;
            return this;
        }

        public PersonDto.PersonDtoBuilder city(String pCity) {
            this.city = pCity;
            return this;
        }

        public PersonDto.PersonDtoBuilder zip(String pZip) {
            this.zip = pZip;
            return this;
        }

        public PersonDto.PersonDtoBuilder phone(String pPhone) {
            this.phone = pPhone;
            return this;
        }

        public PersonDto.PersonDtoBuilder email(String pEmail) {
            this.email = pEmail;
            return this;
        }

        public PersonDto build() {
            return new PersonDto(firstName, lastName, address, city, zip, phone, email);
        }
    }

    private PersonDto(String firstName, String lastName, String address, String city, String zip,
                      String phone, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.zip = zip;
        this.phone = phone;
        this.email = email;
    }
}
