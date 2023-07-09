package com.retail.csb.service;

import com.retail.csb.model.cache.PublicApiKey;
import com.retail.csb.repository.BusinessRepository;
import com.retail.csb.repository.cache.PublicApiKeyRepository;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ApiAccessService<T> {

    @Autowired
    PublicApiKeyRepository publicApiKeyRepository;

    @Autowired
    BusinessRepository businessRepository;

    @SuppressWarnings("unchecked")
    public T getAccessDetails(final String apiKey) {
        /*var accessKey = publicApiKeyRepository.getPublicApiKey(apiKey);
        if (accessKey == null) {
            // * If api key is not cached then query database and save that in cache
            final var businessDetails = businessRepository.findByApiKey(apiKey);
            accessKey = PublicApiKey.builder().apiKey(businessDetails.getApiKey()).businessId(businessDetails.getId())
                    .build();
            publicApiKeyRepository.savePublicApiKey(accessKey);
            log.info("Cached Api Key for Business Id: {}", accessKey.getBusinessId());
        }
        return (T) accessKey;*/
        final var businessDetails = businessRepository.findByApiKey(apiKey);
        var accessKey = PublicApiKey.builder().apiKey(businessDetails.getApiKey()).businessId(businessDetails.getId())
            .build();

        return (T) accessKey;
    }
}
