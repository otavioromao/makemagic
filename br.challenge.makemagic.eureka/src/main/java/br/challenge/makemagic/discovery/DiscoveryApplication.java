//**********************************************************************
// This is intended to solve a challenge to create a REST API to offers
// CRUD functionalities for Harry Potter's characters.
// **********************************************************************
package br.challenge.makemagic.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * This class is responsible to start the application by Sprint Boot.
 */
@SpringBootApplication
@EnableEurekaServer
public class DiscoveryApplication
{
    public static void main(String[] args)
    {
	SpringApplication.run(DiscoveryApplication.class, args);
    }
}
