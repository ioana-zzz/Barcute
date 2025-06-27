package org.example.RestServices;

import jakarta.annotation.Resource;

import jakarta.persistence.Entity;
import org.example.RestServices.UserController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan(basePackages = {
        "org.example", "org.example.RestServices", "org.example.Repository"
})
@EntityScan(basePackages = {
        "org.example"
})
@Resource(name = "application.properties", type = org.springframework.core.io.Resource.class)

@SpringBootApplication
public class StartRestServices {

    public static void main(String[] args) {
        System.out.println("Starting server ...");
        org.springframework.boot.SpringApplication.run(StartRestServices.class, args);
        System.out.println("Server started ...");
    }
}
