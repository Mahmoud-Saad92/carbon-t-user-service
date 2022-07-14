package com.bazinga.eg.userservice.service;

import com.bazinga.eg.userservice.dto.UserDto;

public interface UserService {

    UserDto createUser(UserDto dto, String clientType);

    UserDto getUserById(Long id);
}
