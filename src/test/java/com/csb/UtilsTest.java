package com.csb;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.retail.csb.common.Utils;

import org.junit.jupiter.api.Test;

public class UtilsTest {

    @Test
    void snakeCaseToCamelCase() {
        var result = Utils.snakeCaseToCamelCase("order_status");
        assertEquals("orderStatus", result);
        System.out.println(result);
    }
}
