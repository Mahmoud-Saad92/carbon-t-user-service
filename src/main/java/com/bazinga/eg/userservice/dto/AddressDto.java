package com.bazinga.eg.userservice.dto;

import lombok.*;

@EqualsAndHashCode
@Getter
@Setter
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    private String country;
    private String city;
    private String street;
    private Integer houseNumber;
}