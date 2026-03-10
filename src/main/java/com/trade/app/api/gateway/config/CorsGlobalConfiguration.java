package com.trade.app.api.gateway.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;


@Configuration
public class CorsGlobalConfiguration {


    @Configuration
    public class CorsConfig {
    	@Bean
        public CorsWebFilter corsWebFilter() {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowCredentials(true); // Allow cookies and Authorization headers
            config.setAllowedOrigins(Arrays.asList("http://localhost:5173")); // Frontend origin
            config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            config.setAllowedHeaders(Arrays.asList("*")); // Or be specific (e.g. Authorization, Content-Type)

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", config); // Apply to all routes

            return new CorsWebFilter(source);
        }
    }
}
