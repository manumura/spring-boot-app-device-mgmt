package com.manu.test.configuration;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class Oauth2TokenExtractor implements TokenExtractor {

    private static final Logger logger = LoggerFactory.getLogger(CustomBasicAuthorizationInterceptor.class);

    @Override
    public Authentication extract(HttpServletRequest request) {
        String tokenValue = this.extractToken(request);
        if (tokenValue != null) {
            return new PreAuthenticatedAuthenticationToken(tokenValue, StringUtils.EMPTY);
        } else {
            return null;
        }
    }

    private String extractToken(HttpServletRequest request) {
        String token = this.extractHeaderToken(request);
        if (token == null) {
            logger.debug("Token not found in headers. Trying request parameters.");
            token = request.getParameter("access_token");
            if (token == null) {
                logger.debug("Token not found in request parameters.  Not an OAuth2 request.");
            } else {
                request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE, "Bearer");
            }
        }

        return token;
    }

    private String extractHeaderToken(HttpServletRequest request) {

        Enumeration headers = request.getHeaders("Authorization");

        if (headers == null || !headers.hasMoreElements()) {
            return null;
        }

        String value = headers.nextElement().toString();

        while (value == null || !value.toLowerCase().startsWith("Bearer".toLowerCase())) {
            if (!headers.hasMoreElements()) {
                return null;
            }

            value = headers.nextElement().toString();
        }
        logger.debug("Authorization value: {}", value);

        String authHeaderValue = value.substring("Bearer".length()).trim();
        request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE, "Bearer");

        int commaIndex = authHeaderValue.indexOf(44);
        if (commaIndex > 0) {
            authHeaderValue = authHeaderValue.substring(0, commaIndex);
        }

        logger.debug("Token value: {}", authHeaderValue);
        return authHeaderValue;
    }
}
