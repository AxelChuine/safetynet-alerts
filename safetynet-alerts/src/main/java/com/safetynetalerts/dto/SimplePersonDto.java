package com.safetynetalerts.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SimplePersonDto {
    private String firstName;

    private String lastName;

    private String address;

    private String phone;

}
