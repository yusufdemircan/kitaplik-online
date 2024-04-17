package com.onlinekitaplik.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Value("${book-service}")
    private String bookServiceUrl;

    @Value("${library-service}")
    private String libraryServiceUrl;

    @Bean
    public RouteLocator customLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("book-service", r -> r.path("/v1/book/**")
                        .filters(f->f.addRequestHeader("yedy","yusuf"))
                        .uri(bookServiceUrl))
                .route("library-service", r -> r.path("/v1/library/**")
                        .uri(libraryServiceUrl)).build();
    }

}
