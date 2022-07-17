package com.bazinga.eg.userservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@EqualsAndHashCode
@Getter @Setter
@ToString
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private String username;
    private String email;
    private ContactDto contact;
    private String message;
}
