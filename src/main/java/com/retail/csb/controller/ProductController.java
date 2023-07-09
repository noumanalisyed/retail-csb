package com.retail.csb.controller;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javax.websocket.server.PathParam;

import com.retail.csb.common.api.SendApiResponse;
import com.retail.csb.constant.SecurityConstants;
import com.retail.csb.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {

    @Autowired
    ProductService productService;

    @Async
    @GetMapping("/pub/product")
    public CompletableFuture<ResponseEntity<?>> getAllProducts(
            @RequestHeader(value = SecurityConstants.X_API_KEY, required = true) final String apiKey,
            @RequestParam(value = "page", required = false, defaultValue = "0") final Integer page,
            @RequestParam(value = "size", required = false) final Integer pageSize,
            @RequestParam(value = "sortBy", required = false, defaultValue = "updatedAt") final String sortBy,
            @RequestParam(value = "sortDirection", required = false, defaultValue = "ASC") final String sortDirection,
            @RequestParam(value = "q", required = false) final Optional<String> searchTerm) {
        final var products = productService.getAllPublicProducts(apiKey, page, pageSize, sortBy, sortDirection,
                searchTerm);
        return SendApiResponse.dispatch(products);
    }

    @Async
    @GetMapping("/pub/product/{id}")
    public CompletableFuture<ResponseEntity<?>> getAProduct(
            @RequestHeader(value = SecurityConstants.X_API_KEY, required = true) final String apiKey,
            @PathParam(value = "id") Integer id) {
        final var response = productService.getAProduct(apiKey, id);
        return SendApiResponse.dispatch(response);
    }

    @Async
    @GetMapping("/product")
    public CompletableFuture<ResponseEntity<?>> getAllProducts(
            @RequestHeader(SecurityConstants.TOKEN_HEADER) final String authToken,
            @RequestParam(value = "page", required = false, defaultValue = "0") final String page,
            @RequestParam(value = "brand", required = false, defaultValue = "") final String brand) {
        final var pageValue = Integer.valueOf(page);
        final var products = brand.isEmpty() ? productService.getAllProducts(authToken, pageValue)
                : productService.getAllProductsByBrand(authToken, brand, pageValue);
        return SendApiResponse.dispatch(products);
    }

    @Async
    @GetMapping("/product/search")
    public CompletableFuture<ResponseEntity<?>> getAllProducts(
            @RequestHeader(SecurityConstants.TOKEN_HEADER) final String authToken,
            @RequestParam(value = "page", required = false, defaultValue = "0") final Integer page,
            @RequestParam(value = "q", required = true, defaultValue = "") final String query) {
        final var products = productService.searchProducts(authToken, query, page);
        return SendApiResponse.dispatch(products);
    }
}
