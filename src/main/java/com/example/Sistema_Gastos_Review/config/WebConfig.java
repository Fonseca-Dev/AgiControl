package com.example.Sistema_Gastos_Review.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // todas as rotas
                        .allowedOrigins(
                                "https://fonseca-dev.github.io",
                                "http://localhost:5500",
                                "http://localhost:3000",
                                "https://sistema-gastos-review-six.vercel.app/", // ADICIONE SEU FRONT-END VERCEL
                                "https://sistema-gastos-review-4dhf.vercel.app/",
                                "https://site-sistema-gastos.vercel.app/"
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowCredentials(true);

            }
        };
    }
}
