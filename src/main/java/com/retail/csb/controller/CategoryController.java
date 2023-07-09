package com.retail.csb.controller;

import java.util.concurrent.CompletableFuture;

import com.retail.csb.common.api.SendApiResponse;
import com.retail.csb.constant.SecurityConstants;
import com.retail.csb.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @Async
    @GetMapping("/pub/category")
    public CompletableFuture<ResponseEntity<?>> getAllCategories(
            @RequestHeader(value = SecurityConstants.X_API_KEY, required = true) final String apiKey,
            @RequestParam(value = "page", required = false, defaultValue = "0") final Integer page,
            @RequestParam(value = "size", required = false) final Integer pageSize,
            @RequestParam(value = "sortBy", required = false, defaultValue = "updatedAt") final String sortBy,
            @RequestParam(value = "sortDirection", required = false, defaultValue = "ASC") final String sortDirection) {
        final var categories = categoryService.getAllCategories(apiKey, page, pageSize, sortBy, sortDirection);
        return SendApiResponse.dispatch(categories);
    }

    @Async
    @GetMapping("/pub/category/{id}")
    public CompletableFuture<ResponseEntity<?>> getACategory(
            @RequestHeader(value = SecurityConstants.X_API_KEY, required = true) final String apiKey,
            @PathVariable(value = "id", required = true) Integer id) {
        final var category = categoryService.getACategory(apiKey, id);
        return SendApiResponse.dispatch(category);
    }
}
