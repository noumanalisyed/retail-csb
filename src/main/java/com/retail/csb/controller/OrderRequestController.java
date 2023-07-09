package com.retail.csb.controller;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.validation.Valid;

import com.retail.csb.common.api.SendApiResponse;
import com.retail.csb.constant.SecurityConstants;
import com.retail.csb.model.cart.OrderRequest;
import com.retail.csb.service.OrderRequestService;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderRequestController {

    @Autowired
    OrderRequestService orderRequestService;

    @Async
    @PostMapping("/order")
    public CompletableFuture<ResponseEntity<?>> newOrderRequest(
            @RequestHeader(SecurityConstants.TOKEN_HEADER) final String authToken,
            @Valid @RequestBody final OrderRequest orderRequest) {
        final var response = orderRequestService.newOrderRequest(authToken, orderRequest);
        return SendApiResponse.dispatch(response);
    }

    @Async
    @GetMapping(value = "/pub/order/{id}")
    public CompletableFuture<ResponseEntity<?>> getAGuestOrder(
            @RequestHeader(value = SecurityConstants.X_API_KEY, required = true) final String apiKey,
            @PathVariable(value = "id", required = true) final Long orderId) {
        final var response = orderRequestService.getAGuestOrder(apiKey, orderId);
        return SendApiResponse.dispatch(response);
    }

    @Async
    @PostMapping(value = "/pub/order")
    public CompletableFuture<ResponseEntity<?>> newGuestOrderRequest(
            @RequestHeader(value = SecurityConstants.X_API_KEY, required = true) final String apiKey,
            @Valid @RequestBody final JsonNode orderRequest) {
        final var response = orderRequestService.newGuestOrderRequest(apiKey, orderRequest);
        return SendApiResponse.dispatch(response);
    }

    @Async
    @PatchMapping("/pub/order/verify/{id}/{verificationCode}")
    public CompletableFuture<ResponseEntity<?>> verifyGuestOrderRequest(
            @RequestHeader(value = SecurityConstants.X_API_KEY, required = true) final String apiKey,
            @PathVariable(value = "id", required = true) final Long orderId,
            @PathVariable(value = "verificationCode", required = true) final String verificationCode) {
        final var response = orderRequestService.verifyGuestOrderRequest(apiKey, orderId, verificationCode);
        return SendApiResponse.dispatch(response);
    }

    @Async
    @GetMapping("/order/{id}")
    public CompletableFuture<ResponseEntity<?>> getAnOrder(
            @RequestHeader(SecurityConstants.TOKEN_HEADER) final String authToken,
            @PathVariable(value = "id", required = true) final Long orderId) {
        final var response = orderRequestService.getAnOrder(authToken, orderId);
        return SendApiResponse.dispatch(response);
    }

    @Async
    @PatchMapping("/order/{id}")
    public CompletableFuture<ResponseEntity<?>> updateAnOrder(
            @RequestHeader(SecurityConstants.TOKEN_HEADER) final String authToken,
            @PathVariable(value = "id", required = true) final Long orderId,
            @RequestBody final Map<String, Object> orderUpdateResource) {
        final var response = orderRequestService.updateAnOrder(authToken, orderId, orderUpdateResource);
        return SendApiResponse.dispatch(response);
    }

    @Async
    @GetMapping("/order")
    public CompletableFuture<ResponseEntity<?>> getAllOrders(
            @RequestHeader(SecurityConstants.TOKEN_HEADER) final String authToken,
            @RequestParam(value = "page", required = false, defaultValue = "0") final Integer page) {
        final var response = orderRequestService.getAllOrders(authToken, page);
        return SendApiResponse.dispatch(response);
    }

}
