package com.retail.csb.controller;

import java.util.concurrent.CompletableFuture;

import com.retail.csb.common.api.SendApiResponse;
import com.retail.csb.constant.SecurityConstants;
import com.retail.csb.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    UserService userService;

    @Async
    @GetMapping
    public CompletableFuture<ResponseEntity<?>> getAUser(
            @RequestHeader(SecurityConstants.TOKEN_HEADER) final String authToken) {
        final var user = userService.getAUser(authToken);
        return SendApiResponse.dispatch(user);
    }



}
