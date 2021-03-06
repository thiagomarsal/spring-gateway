package com.example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@SpringBootApplication
public class SpringProxyGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringProxyGatewayApplication.class, args);
    }
}