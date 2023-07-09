package com.retail.csb.repository.cache;

import com.retail.csb.model.cache.PublicApiKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PublicApiKeyRepository {

    private static final String HASH_KEY = "csb:business_public_api_key";

    @Autowired
    RedisTemplate<String, Object> template;

    public boolean savePublicApiKey(PublicApiKey apiKey) {
        template.opsForHash().put(HASH_KEY, apiKey.getApiKey(), apiKey);
        return true;
    }

    public PublicApiKey getPublicApiKey(final String apiKey) {
        return (PublicApiKey) template.opsForHash().get(HASH_KEY, apiKey);
    }
}
