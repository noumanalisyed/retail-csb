package com.retail.csb.model.cache;

import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(timeToLive = 2629800, value = "csb:business_public_api_key") // * 2629800seconds in a month
public class PublicApiKey {
    private Integer businessId;
    private String apiKey;
}
