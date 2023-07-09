package com.retail.csb.controller;

import java.util.concurrent.CompletableFuture;

import javax.validation.Valid;

import com.retail.csb.common.api.SendApiResponse;
import com.retail.csb.model.vm.UserLogin;
import com.retail.csb.model.vm.customer.CustomerSignUp;
import com.retail.csb.service.CustomerService;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @Async
    @PostMapping("/auth/customer/signup")
    public CompletableFuture<ResponseEntity<?>> customerSignUp(@Valid @RequestBody CustomerSignUp customer) {
        final var createdUser = customerService.signUp(customer);
        return SendApiResponse.dispatch(createdUser);
    }

    @Async
    @PostMapping("/auth/customer/login")
    public CompletableFuture<ResponseEntity<?>> customerLogin(@Valid @RequestBody UserLogin customer) {
        final var token = customerService.login(customer);
        return SendApiResponse.dispatch(token);
    }

}
