package com.retail.csb.security;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.retail.csb.common.Utils;
import com.retail.csb.constant.SecurityConstants;
import com.retail.csb.model.User;
import com.retail.csb.model.vm.UserJWTToken;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

public class JWTUtils {

    private static Algorithm algorithm = Algorithm.HMAC256(SecurityConstants.JWT_SECRET);
    private static JWTVerifier verifier = JWT.require(algorithm).withSubject(SecurityConstants.TOKEN_HEADER)
            .withIssuer(SecurityConstants.TOKEN_ISSUER).build();
    private static ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public static String createJWT(final User user) {
        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put("alg", "HS256");
        headers.put("typ", "JWT");

        Date nowDate = new Date();
        Date expireDate = Utils.getAfterDate(nowDate, 0, 0, 1, 0, 0, 0); // * Expires in 1 day

        // * Create user token
        final var userToken = new UserJWTToken();
        userToken.setUserId(user.getId());
        userToken.setUsername(user.getUsername());
        userToken.setBusinessId(user.getBusiness().getId());
        userToken.setUserType(user.getUserType());

        final var userTokenJson = objectMapper.writeValueAsString(userToken);

        final var userTokenJsonBase64 = Base64.getEncoder()
                .encodeToString(userTokenJson.getBytes(StandardCharsets.UTF_8));

        return JWT.create().withIssuer(SecurityConstants.TOKEN_ISSUER).withHeader(headers)
                .withClaim("user", userTokenJsonBase64).withAudience(SecurityConstants.TOKEN_AUDIENCE)
                .withSubject(SecurityConstants.TOKEN_HEADER).withIssuedAt(nowDate).withExpiresAt(expireDate)
                .sign(algorithm);
    }

    public static boolean verifyToken(String token) {
        try {
            verifier.verify(tokenSplitter(token));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @SneakyThrows
    public static UserJWTToken getUserFromToken(String token) {
        var decodedToken = verifier.verify(tokenSplitter(token));
        var decodedUser = new String(Base64.getDecoder().decode(decodedToken.getClaim("user").asString()),
                StandardCharsets.UTF_8);
        var userDecodedDetails = objectMapper.readValue(decodedUser, UserJWTToken.class);
        return userDecodedDetails;
    }

    private static final String tokenSplitter(final String token) {
        return token.split(SecurityConstants.TOKEN_PREFIX)[1];
    }
}
