package com.retail.csb.model.vm;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JWTToken {
    private String token;
}
