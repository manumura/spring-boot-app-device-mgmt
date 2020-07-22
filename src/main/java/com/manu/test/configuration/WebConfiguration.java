package com.manu.test.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

/**
 * @author emmanuel.mura
 *
 */
@Configuration
@EnableWebMvc
public class WebConfiguration implements WebMvcConfigurer {

	public static final Logger logger = LoggerFactory.getLogger(WebConfiguration.class);

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/test/**").addResourceLocations("classpath:/public/", "classpath:/static/");

		// Swagger enable
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

		// super.addResourceHandlers(registry);
	}

	@Bean
	public ViewResolver viewResolver() {
		UrlBasedViewResolver viewResolver = new UrlBasedViewResolver();
		viewResolver.setViewClass(InternalResourceView.class);
		return viewResolver;
	}

	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		logger.debug("configureAsyncSupport");
		configurer.setDefaultTimeout(-1);
		configurer.setTaskExecutor(asyncTaskExecutor());
	}

	@Bean
	public AsyncTaskExecutor asyncTaskExecutor() {
		logger.debug("asyncTaskExecutor");
		return new SimpleAsyncTaskExecutor("async");
	}

	@Bean
	public CustomBasicAuthorizationInterceptor customBasicAuthorizationInterceptor() {
		return new CustomBasicAuthorizationInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(customBasicAuthorizationInterceptor()).addPathPatterns("/internal/**");
	}

}
