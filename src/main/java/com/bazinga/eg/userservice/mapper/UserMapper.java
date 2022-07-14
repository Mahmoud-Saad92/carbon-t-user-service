package com.bazinga.eg.userservice.mapper;

import com.bazinga.eg.userservice.dto.UserDto;
import com.bazinga.eg.userservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class UserMapper implements GenericMapper<User, UserDto> {

    @Mapping(target = "contact", ignore = true)
    public abstract UserDto toDto(User entity);

    @Mapping(target = "id", ignore = true)
    public abstract User toEntity(UserDto dto);
}
