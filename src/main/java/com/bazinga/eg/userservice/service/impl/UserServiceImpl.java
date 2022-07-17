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
import com.bazinga.eg.userservice.utils.UserContextHolder;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Slf4j
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
    @CircuitBreaker(name = "contactService")
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

//            randomlyRunLong();

            feignClient.createContact(contactDto);
        } else {
            contactDto = createContact(user.getId(), dto.getContact(), clientType);
        }

        UserDto userDto = mapToDto(user);

        userDto.setContact(contactDto);

        return userDto;
    }

    @Override
    @CircuitBreaker(name = "userService", fallbackMethod = "buildFallbackUserDto")
    @Retry(name = "retryUserService", fallbackMethod = "buildFallbackUserDto")
    @Bulkhead(name = "bulkheadUserService", fallbackMethod = "buildFallbackUserDto", type = Bulkhead.Type.THREADPOOL)
    public UserDto getUserById(Long id) {
        log.debug("getLicensesByOrganization Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
        randomlyRunLong();
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("resource not found"));

        return mapToDto(user);
    }

    private ContactDto createContact(Long userId, ContactDto dto, String clientType) {
        dto.setUserId(userId);
        return client.apply(dto, clientType, implementations);
    }

    @SuppressWarnings("unused")
    public UserDto buildFallbackUserDto(Throwable cause) {
        log.error(cause.getMessage());
        return UserDto
                .builder()
                .message("Sorry no user information currently available")
                .build();
    }

    /*
     * Following code is just to simulate the getUserById() method running into slow or timed out database query
     * */

    Supplier<Integer> randomNum = () -> new Random().nextInt((3 - 1) + 1) + 1;
    Predicate<Integer> sleepCondition = (e) -> e.equals(3);

    private void randomlyRunLong() {
        if (sleepCondition.test(randomNum.get())) sleep();
    }

    private void sleep() {
        try {
            log.info("Thread goes to sleep");
            Thread.sleep(6000);
//            throw new TimeoutException();
        } catch (InterruptedException e) {
            log.error("Time out exception, {}", e.getMessage());
        }
    }
}
