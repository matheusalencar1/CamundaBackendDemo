package com.camunda.model.service;

import com.camunda.client.response.CreateProcessResponse;
import com.camunda.configuration.properties.CamundaProperties;
import com.camunda.model.entity.User;
import com.camunda.model.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    private UserRepository userRepository;
    private CamundaService camundaService;
    private CamundaProperties camundaProperties;

    public UserService(UserRepository userRepository,
                       CamundaService camundaService,
                       CamundaProperties camundaProperties) {
        this.userRepository = userRepository;
        this.camundaService = camundaService;
        this.camundaProperties = camundaProperties;
    }

    public User save(User user) {
        User savedUser = userRepository.save(user);
        createUserProcess(savedUser);
        log.info(savedUser.toString());
        return savedUser;
    }

    public User update(User user) {
        User savedUser = userRepository.save(user);
        completeEditTask(savedUser.getId());
        log.info(savedUser.toString());
        return savedUser;
    }

    private void completeEditTask(Long idUser) {
        String taskId = camundaService.getTaskInstanceId(idUser);
        camundaService.completeTask(taskId, null);
    }

    private void createUserProcess(User savedUser) {
        Map<String, String> variables = new HashMap<>();
        variables.put(camundaProperties.getUserIdVariable(), String.valueOf(savedUser.getId()));
        CreateProcessResponse processInstance
                = camundaService.createProcessInstance(camundaProperties.getUserProcessDefinitionId(), variables);
        log.info(processInstance.toString());
    }


    public boolean delete(Long id) {
        Optional<User> userToBeDeleted = userRepository.findById(id);
        boolean delete = userToBeDeleted.isPresent();
        if (delete) {
            User user = userToBeDeleted.get();
            userRepository.delete(user);
            completeReviewTask(user.getId(), Boolean.TRUE);
        }
        return delete;
    }

    public void reject(Long id) {
        Optional<User> userToBeRejected = userRepository.findById(id);
        boolean reject = userToBeRejected.isPresent();
        if (reject) {
            completeReviewTask(id, Boolean.FALSE);
        }
    }

    private void completeReviewTask(Long idUser, Boolean isApproved) {
        String taskId = camundaService.getTaskInstanceId(idUser);
        Map<String, String> variables = new HashMap<>();
        variables.put("approved", isApproved.toString());
        variables.put(camundaProperties.getUserIdVariable(), String.valueOf(idUser));
        camundaService.completeTask(taskId, variables);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
