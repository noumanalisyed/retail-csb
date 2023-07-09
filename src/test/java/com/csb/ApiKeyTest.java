package com.csb;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.retail.csb.common.Utils;

import org.junit.jupiter.api.Test;

public class ApiKeyTest {

    @Test
    void apiKeyGenerateTest() {
        var apiKey = Utils.generateApiKey();
        System.out.println(apiKey);
        assertEquals(36, apiKey.length());
    }
}
