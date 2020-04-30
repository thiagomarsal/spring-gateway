package com.example.gateway.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.net.URL;
import java.util.Map;

@Getter
@Setter
@ConfigurationProperties
@Configuration
public class Properties {

    private Map<String, URL> environmentMap;
}