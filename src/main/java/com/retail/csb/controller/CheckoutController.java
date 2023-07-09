package com.retail.csb.controller;

import java.util.concurrent.CompletableFuture;

import com.retail.csb.common.api.SendApiResponse;
import com.retail.csb.service.CheckoutService;
import com.fasterxml.jackson.databind.JsonNode;
import com.stripe.exception.StripeException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class CheckoutController {

    @Autowired
    CheckoutService checkoutService;

    @Async
    @PostMapping("/pub/checkout")
    public CompletableFuture<ResponseEntity<?>> processCheckout(@RequestBody JsonNode cart) throws StripeException {
        final var response = checkoutService.processCheckout(cart);
        return SendApiResponse.dispatch(response);
    }
}
