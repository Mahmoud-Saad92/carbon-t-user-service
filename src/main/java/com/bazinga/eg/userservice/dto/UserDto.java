package com.bazinga.eg.userservice.dto;

import lombok.*;

@EqualsAndHashCode
@Getter @Setter
@ToString
@Builder(toBuilder = true)
public class UserDto {
    private String username;
    private String email;
    private ContactDto contact;
}
