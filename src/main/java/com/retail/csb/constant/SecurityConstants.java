package com.retail.csb.constant;

public class SecurityConstants {
    // todo use application properties for this using this for dev testing
    public static final String JWT_SECRET = "n2r5u8x/A%D*G-KaPdSgVkYp3s6v9y$B&E(H+MbQeThWmZq4t7w!z%C*F-J@NcRf";

    // * JWT token defaults
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "csb-api";
    public static final String TOKEN_AUDIENCE = "csb-app";
    public static final String X_API_KEY = "x-api-key";

    private SecurityConstants() {
        throw new IllegalStateException("Cannot create instance of static util class");
    }
}
