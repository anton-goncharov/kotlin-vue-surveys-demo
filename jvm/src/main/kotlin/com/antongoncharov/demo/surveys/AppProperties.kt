package com.antongoncharov.demo.surveys

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "app")
class AppProperties {

    lateinit var imageStore: Map<String, String>

}