package com.camunda.web.controller;

import com.camunda.exception.BadRequestException;
import com.camunda.model.entity.User;
import com.camunda.web.representation.UserRepresentation;
import com.camunda.model.service.UserService;
import com.camunda.web.response.ListRestResponse;
import com.camunda.web.response.RestResponse;
import com.camunda.web.util.ListResponseBuilder;
import com.camunda.web.util.RestResponseBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("users")
public class UserController {

    private RestResponseBuilder restResponseBuilder;
    private ListResponseBuilder listResponseBuilder;
    private UserService userService;
    private ModelMapper modelMapper;

    public UserController(RestResponseBuilder restResponseBuilder,
                          ListResponseBuilder listResponseBuilder,
                          UserService userService,
                          ModelMapper modelMapper) {
        this.restResponseBuilder = restResponseBuilder;
        this.listResponseBuilder = listResponseBuilder;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(value = "{id}")
    public RestResponse<UserRepresentation> findById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if (!user.isPresent()) {
            throw new BadRequestException("User not found");
        }
        return restResponseBuilder.success(modelMapper.map(user.get(), UserRepresentation.class));
    }

    @GetMapping
    public ListRestResponse findAllUsers() {
        List<User> users = userService.findAll();
        List<UserRepresentation> response = users.stream()
                .map(user -> modelMapper.map(user, UserRepresentation.class))
                .collect(Collectors.toList());
        return listResponseBuilder.success(response);
    }

    @PostMapping
    public RestResponse<UserRepresentation> createUser(@RequestBody UserRepresentation user) {
        User createdUser = this.saveUser(user);
        return createUserResponse(createdUser);
    }

    private User saveUser(UserRepresentation user) {
        return userService.save(modelMapper.map(user, User.class));
    }

    @PutMapping
    public RestResponse<UserRepresentation> updateUser(@RequestBody UserRepresentation user) {
        User updatedUser = update(user);
        return createUserResponse(updatedUser);
    }

    private User update(UserRepresentation user) {
        return userService.update(modelMapper.map(user, User.class));
    }

    @PutMapping(value = "reject/{userId}")
    public RestResponse<UserRepresentation> rejectUser(@PathVariable Long userId) {
        userService.reject(userId);
        return restResponseBuilder.success(null);
    }

    private RestResponse<UserRepresentation> createUserResponse(User user) {
        return restResponseBuilder.success(modelMapper.map(user, UserRepresentation.class));
    }

    @DeleteMapping(value = "{id}")
    public RestResponse<UserRepresentation> deleteUser(@PathVariable Long id) {
        if (!userService.delete(id)) {
            throw new BadRequestException(String.format("User %d can't be deleted", id));
        }
        return restResponseBuilder.success(null);
    }

}
