package com.retail.csb.security;

import java.util.HashMap;
import java.util.Map;

public class RouteAuth {

    private static final Map<String, Boolean> publicRoutes = new HashMap<>() {
        private static final long serialVersionUID = -1772378694552048840L;

        {
            put("/api/v1/auth/", true);
            put("/api/v1/pub/", true); // * Only requires api key
        }
    };

    public static boolean requireAuth(final String route) {
        return !publicRoutes.keySet().stream().anyMatch(rule -> route.contains(rule));
    }

}
