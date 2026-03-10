package com.trade.app.api.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.trade.app.api.gateway.filter.AuthFilter;


@Configuration
public class ApiGatewayConfiguration {
	
	 @Bean
	    public RouteLocator newroutes(RouteLocatorBuilder builder, AuthFilter authFilter) {
	        return builder.routes()
	            .route("login-service", r -> r.path("/api/login/**")
	                .uri("http://localhost:8080")) // No auth needed for login
	            .route("user-service", r -> r.path("/user/**")
	                .filters(f -> f.filter(authFilter))
	                .uri("http://localhost:8082")) // Protected route
	            .build();
	    }

}