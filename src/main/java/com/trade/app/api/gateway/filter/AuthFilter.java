package com.trade.app.api.gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.trade.app.auth_lib.util.JwtUtil;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter implements GatewayFilter, Ordered {

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = (ServerHttpRequest) exchange.getRequest();
		String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		String path = request.getURI().getPath();
		if (request.getMethod().name().equals("OPTIONS")) {
			return chain.filter(exchange);
		}
		if (path.startsWith("/api/login") || path.startsWith("/api/signup")) {
			return chain.filter(exchange);
		}

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return unauthorized(exchange);
		}

		String token = authHeader.substring(7); // remove "Bearer "

		Claims claims = jwtUtil.validateToken(token);
		if (claims == null) {
			return unauthorized(exchange);
		}

		// Optional: Add claims as headers for downstream services
		ServerHttpRequest modifiedRequest = request.mutate().header("X-User-Id", claims.getSubject()).build();

		return chain.filter(exchange.mutate().request(modifiedRequest).build());
	}

	private Mono<Void> unauthorized(ServerWebExchange exchange) {
		exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
		return exchange.getResponse().setComplete();
	}

	@Override
	public int getOrder() {
		return -1;
	}
}
