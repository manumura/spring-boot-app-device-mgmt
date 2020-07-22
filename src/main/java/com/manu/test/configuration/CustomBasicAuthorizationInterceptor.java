package com.manu.test.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;

public class CustomBasicAuthorizationInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(CustomBasicAuthorizationInterceptor.class);

    @Value("${security.oauth2.client.client-id}")
    private String basicAuthUsername;

    @Value("${security.oauth2.client.client-secret}")
    private String basicAuthPassword;

    /*
     **The method returns a boolean value – which tells Spring if the request should be further processed by a handler (true) or not (false).
     * set the request attribute sample -
     * request.setAttribute("webuser", accessTokenResponseData);
     *
     **/
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {

        if (!request.getRequestURI().contains("internal")) {
            return true;
        }

        final String headerValue = request.getHeader("Authorization");

        if (headerValue == null) {
//            throw new DefinedErrorException(ErrorEnum.BASIC_AUTH_REQUIRED, "Basic Authentication necessary");
            throw new RuntimeException("Basic Authentication necessary");
        }

        if (!headerValue.startsWith("Basic")) {
//            throw new DefinedErrorException(ErrorEnum.BASIC_AUTH_REQUIRED, "Basic Authentication necessary");
            throw new RuntimeException("Basic Authentication necessary");
        }

        final String validHeaderValue = "Basic " + base64Encode((basicAuthUsername + ":" + basicAuthPassword).getBytes(Charset.forName("ISO_8859_1")));

        if (!validHeaderValue.equals(headerValue)) {
//            throw new DefinedErrorException(ErrorEnum.INCORRECT_CREDENTIALS, "Basic Authentication credentials are wrong");
            throw new RuntimeException("Basic Authentication credentials are wrong");
        }

        logger.debug("headerValue: {}", headerValue);

        return true;
    }

    /*
    This method is called immediately after the request is processed by HandlerAdapter, but before generating a view
     */
    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView) {
        return;
    }

    /*
    When the view is successfully generated, we can use this hook to do things like gather additional statistics related to the request
     */
    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler, Exception ex) {
        return;
    }

    private String base64Encode(byte[] bytes) {
        return java.util.Base64.getEncoder().encodeToString(bytes);
    }
}
