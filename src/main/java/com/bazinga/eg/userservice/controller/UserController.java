package com.bazinga.eg.userservice.controller;

import com.bazinga.eg.userservice.dto.UserDto;
import com.bazinga.eg.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userServiceImpl;

    @Autowired
    public UserController(UserService userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto, @RequestParam(required = false) String clientType) {
        UserDto user = userServiceImpl.createUser(userDto, clientType);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto user = userServiceImpl.getUserById(id);

        return new ResponseEntity<>(user, HttpStatus.FOUND);
    }
}
