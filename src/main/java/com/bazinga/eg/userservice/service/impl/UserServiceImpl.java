package com.bazinga.eg.userservice.service.impl;

import com.bazinga.eg.userservice.dto.ContactDto;
import com.bazinga.eg.userservice.dto.UserDto;
import com.bazinga.eg.userservice.entity.User;
import com.bazinga.eg.userservice.exception.model.EntityNotFoundException;
import com.bazinga.eg.userservice.exception.model.EntityPersistException;
import com.bazinga.eg.userservice.mapper.UserMapper;
import com.bazinga.eg.userservice.repository.UserRepository;
import com.bazinga.eg.userservice.service.GenericService;
import com.bazinga.eg.userservice.service.UserService;
import com.bazinga.eg.userservice.service.client.Client;
import com.bazinga.eg.userservice.service.client.ContactFeignClientInterface;
import com.bazinga.eg.userservice.service.client.FunctionalClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class UserServiceImpl extends GenericService<User, UserDto> implements UserService {

    private final UserRepository userRepository;

    private final Map<String, Client> implementations;

    private final ContactFeignClientInterface feignClient;

    private final FunctionalClient<ContactDto, String, Client> client = (dto, clientType, map) -> map.get(clientType).createContact(dto);

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, Map<String, Client> implementations, ContactFeignClientInterface feignClient) {
        super(userMapper);
        this.userRepository = userRepository;
        this.implementations = implementations;
        this.feignClient = feignClient;
    }

    @Override
    @Transactional
    public UserDto createUser(UserDto dto, String clientType) {
        User user = mapToEntity(dto);

        try {
            user = userRepository.save(user);
        } catch (Exception e) {
            throw new EntityPersistException("user could not be created", e);
        }

        ContactDto contactDto;

        if ("feign".equals(clientType)) {
            contactDto = dto.getContact();
            contactDto.setUserId(user.getId());

            feignClient.createContact(contactDto);
        } else {
            contactDto = createContact(user.getId(), dto.getContact(), clientType);
        }

        UserDto userDto = mapToDto(user);

        userDto.setContact(contactDto);

        return userDto;
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("resource not found"));

        return mapToDto(user);
    }

    private ContactDto createContact(Long userId, ContactDto dto, String clientType) {
        dto.setUserId(userId);
        return client.apply(dto, clientType, implementations);
    }
}
