package com.zhuooo.workflow;

import com.zhuooo.jdbc.annotations.DoScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableScheduling
@EnableWebMvc
@DoScan
@ComponentScan(basePackages = {"com.zhuooo"})
public class WFApplication {
    public static void main(String[] args) {
        SpringApplication.run(WFApplication.class, args);
    }
}
