package com.alticelabs.ccp.exagon.dummyorchestrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION,
                classes = TestComponent.class),
        basePackages = { "com.alticelabs.ccp.exagon"}
)
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}