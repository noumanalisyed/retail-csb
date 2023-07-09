package com.retail.csb.service;

import com.retail.csb.common.Utils;
import com.retail.csb.common.api.APIDataObject;
import com.retail.csb.constant.Constants;
import com.retail.csb.model.cache.PublicApiKey;
import com.retail.csb.repository.BrandRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BrandService extends ApiAccessService<PublicApiKey> {

    @Autowired
    BrandRepository brandRepository;

    public APIDataObject getAllBrands(final String apiKey, final Integer page) {
        try {
            var accessKey = getAccessDetails(apiKey); // * Get api key from cache

            final var brands = brandRepository.findAllByBusinessId(accessKey.getBusinessId(),
                    Utils.defaultPage(page, Constants.pageSize));
            return APIDataObject.builder().error(false).data(brands).build();
        } catch (final Exception e) {
            log.error("{}", e.getMessage());
            return APIDataObject.builder().error(true).data("Invalid api keys.").build();
        }
    }

    public APIDataObject getABrand(final String apiKey, final Integer id) {
        try {
            var accessKey = getAccessDetails(apiKey); // * Get api key from cache

            final var brand = brandRepository.findByIdAndBusinessId(id, accessKey.getBusinessId());
            return APIDataObject.builder().error(false).data(brand).build();
        } catch (final Exception e) {
            log.error("{}", e.getMessage());
            return APIDataObject.builder().error(true).data("Invalid api keys.").build();
        }
    }
}
