package com.ai.st.microservice.quality;

import com.ai.st.microservice.quality.modules.shared.domain.Service;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@EnableAutoConfiguration
@EnableFeignClients(basePackages = { "com.ai.st.microservice.common.clients" })
@EnableEurekaClient
@ComponentScan(value = { "com.ai.st.microservice.common.business",
        "com.ai.st.microservice.quality" }, includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Service.class))
public class StMicroserviceProductsApplication {

    public static void main(String[] args) {
        SpringApplication.run(StMicroserviceProductsApplication.class, args);
    }

}