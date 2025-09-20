//package com.troc.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig {
//
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**") // toutes les routes
//                        .allowedOrigins("http://localhost:3000") // ton frontend Next.js
//                        .allowedMethods("GET","POST","PUT","PATCH","DELETE","OPTIONS")
//                        .allowCredentials(true)
//                        .allowedHeaders("*");
//            }
//        };
//    }
//}
