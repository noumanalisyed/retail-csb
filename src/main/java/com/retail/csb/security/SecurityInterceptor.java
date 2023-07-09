package com.retail.csb.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.retail.csb.common.api.RestAPIResponse;
import com.retail.csb.constant.SecurityConstants;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.SneakyThrows;

@Component
@WebFilter(value = "/api/v1/*")
public class SecurityInterceptor implements HandlerInterceptor, Filter {

    @Override
    @SneakyThrows
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        // * If the request is for auth then allow it without authorization else check
        // * the authorization headers
        if (RouteAuth.requireAuth(request.getRequestURI())) {
            final var contentType = request.getContentType();
            final var authHeader = request.getHeader("Authorization");
            if (!verifyHeaders(contentType) || !requestHasAuthorizationToken(authHeader)) {
                writeErrorResponse(response);
                return false;
            }

            // * Verify the JWT itself
            if (!JWTUtils.verifyToken(authHeader)) {
                writeErrorResponse(response);
                return false;
            }
        }
        return true;
    }

    /**
     * Check is the content type header is set and is correct
     *
     * @param contentType
     * @return True if property exists and of right type else False
     */
    private boolean verifyHeaders(final String contentType) {
        return contentType != null && contentType.equals("application/json");
    }

    /**
     * Ensure that auth headers exist in request and have the valid auth prefix
     *
     * @param authHeader
     * @return True if property exists and of right type else False
     */
    private boolean requestHasAuthorizationToken(final String authHeader) {
        return authHeader != null && authHeader.startsWith(SecurityConstants.TOKEN_PREFIX);
    }

    /**
     * Write the error response by adding correct content type and error message.
     *
     * @param response Intercepted Response object
     */
    @SneakyThrows
    private void writeErrorResponse(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.addHeader("Content-Type", "application/json");
        response.getWriter().write(RestAPIResponse.restFailureToString(HttpStatus.FORBIDDEN, "Invalid token."));
    }

    /**
     * Add headers to all the responses
     *
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("X-CSB-Stage", "development");
        httpServletResponse.setHeader("X-CSB-API-Version", "v1-2020");
        chain.doFilter(request, response);

    }
}
