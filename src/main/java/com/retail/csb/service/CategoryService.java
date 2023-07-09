package com.retail.csb.service;

import com.retail.csb.common.Utils;
import com.retail.csb.common.api.APIDataObject;
import com.retail.csb.constant.Constants;
import com.retail.csb.model.cache.PublicApiKey;
import com.retail.csb.repository.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CategoryService extends ApiAccessService<PublicApiKey> {

    @Autowired
    CategoryRepository categoryRepository;

    public APIDataObject getAllCategories(final String apiKey, final Integer page, final Integer pageSize,
            final String sortBy, final String sortDirection) {
        try {
            var accessKey = getAccessDetails(apiKey); // * Get api key from cache

            final var categories = categoryRepository.findAllByBusinessId(accessKey.getBusinessId(),
                    Utils.defaultPage(page, pageSize == null ? Constants.pageSize : pageSize, sortBy, sortDirection));
            return APIDataObject.builder().error(false).data(categories).build();
        } catch (final Exception e) {
            log.error("{}", e.getMessage());
            return APIDataObject.builder().error(true).data("Invalid api keys.").build();
        }
    }

    public APIDataObject getACategory(final String apiKey, final Integer id) {
        try {
            var accessKey = getAccessDetails(apiKey); // * Get api key from cache

            final var category = categoryRepository.findByIdAndBusinessId(id, accessKey.getBusinessId());
            return APIDataObject.builder().error(false).data(category).build();
        } catch (final Exception e) {
            log.error("{}", e.getMessage());
            return APIDataObject.builder().error(true).data("Invalid api keys.").build();
        }
    }
}
