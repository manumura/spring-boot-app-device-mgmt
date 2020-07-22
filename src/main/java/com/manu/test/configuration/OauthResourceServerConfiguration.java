package com.manu.test.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class OauthResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(OauthResourceServerConfiguration.class);

    @Value("${secured.endpoints}")
    private String[] securedEndpoints;

    @Value("${open.endpoints}")
    private String[] openEndpoints;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        logger.debug("secured endpoints: {}", securedEndpoints);
        logger.debug("open endpoints: {}", openEndpoints);

        http
                .authorizeRequests()
                .antMatchers(openEndpoints).permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers(securedEndpoints).authenticated();
        http.cors().disable();
        http.csrf().disable();
        // To prevent clickjacking attacks
        // https://stackoverflow.com/questions/28647136/how-to-disable-x-frame-options-response-header-in-spring-security
        http.headers().frameOptions().disable();
        // anonymous().disable()
        // .and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.tokenExtractor(new Oauth2TokenExtractor());
    }
}