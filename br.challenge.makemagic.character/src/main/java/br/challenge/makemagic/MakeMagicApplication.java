//**********************************************************************
// This is intended to solve a challenge to create a REST API to offers
// CRUD functionalities for Harry Potter's characters.
// **********************************************************************
package br.challenge.makemagic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * This class is responsible to start the application by Sprint Boot.
 */
@SpringBootApplication
@EntityScan(
{ "br.challenge.makemagic.core.model" })
@EnableJpaRepositories(
{ "br.challenge.makemagic.core.repository" })
public class MakeMagicApplication
{
    public static void main(String[] args)
    {
	SpringApplication.run(MakeMagicApplication.class, args);
    }
}
