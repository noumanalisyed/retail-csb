package com.retail.csb.controller;

import java.util.concurrent.CompletableFuture;

import com.retail.csb.common.api.SendApiResponse;
import com.retail.csb.constant.SecurityConstants;
import com.retail.csb.service.BrandService;

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
public class BrandController {

    @Autowired
    BrandService brandService;

    @Async
    @GetMapping("/pub/brand")
    public CompletableFuture<ResponseEntity<?>> getAllBrands(
            @RequestHeader(value = SecurityConstants.X_API_KEY, required = true) final String apiKey,
            @RequestParam(value = "page", required = false, defaultValue = "0") final String page) {
        final var brands = brandService.getAllBrands(apiKey, Integer.valueOf(page));
        return SendApiResponse.dispatch(brands);
    }

    @Async
    @GetMapping("/pub/brand/{id}")
    public CompletableFuture<ResponseEntity<?>> getABrand(
            @RequestHeader(value = SecurityConstants.X_API_KEY, required = true) final String apiKey,
            @PathVariable(value = "id", required = true) Integer id) {
        final var brand = brandService.getABrand(apiKey, id);
        return SendApiResponse.dispatch(brand);

    }
}
