//**********************************************************************
// This is intended to solve a challenge to create a REST API to offers
// CRUD functionalities for Harry Potter's characters.
// **********************************************************************
package br.challenge.makemagic.character.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * This class defines the character information.
 */
@Entity(name = "characters")
public class CharacterEntity
{
    /**
     * Specifies the Id for the character.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Specifies the name for the character.
     */
    private String name;

    /**
     * Specifies the role for the character.
     */
    private String role;

    /**
     * Specifies the name of school for the character.
     */
    private String school;

    /**
     * Specifies the Id of the house for the character.
     */
    private String house;

    /**
     * Specifies the patronus for the character.
     */
    private String patronus;

    /**
     * Creates a CharacterEntity with empty values.
     */
    public CharacterEntity()
    {
    }

    /**
     * Creates a CharacterEntity with the parameters.
     *
     * @param name     The name of the character
     * @param role     The role of the character
     * @param school   The name of the school of the character
     * @param house    The Id of the house of the character
     * @param patronus The name of the patronus of the character
     */
    public CharacterEntity(String name, String role, String school, String house, String patronus)
    {
	this.name = name;
	this.role = role;
	this.school = school;
	this.house = house;
	this.patronus = patronus;
    }

    /**
     * @return the id
     */
    public Long getId()
    {
	return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id)
    {
	this.id = id;
    }

    /**
     * @return the name
     */
    public String getName()
    {
	return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
	this.name = name;
    }

    /**
     * @return the role
     */
    public String getRole()
    {
	return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(String role)
    {
	this.role = role;
    }

    /**
     * @return the school
     */
    public String getSchool()
    {
	return school;
    }

    /**
     * @param school the school to set
     */
    public void setSchool(String school)
    {
	this.school = school;
    }

    /**
     * @return the house
     */
    public String getHouse()
    {
	return house;
    }

    /**
     * @param house the house to set
     */
    public void setHouse(String house)
    {
	this.house = house;
    }

    /**
     * @return the patronus
     */
    public String getPatronus()
    {
	return patronus;
    }

    /**
     * @param patronus the patronus to set
     */
    public void setPatronus(String patronus)
    {
	this.patronus = patronus;
    }
}
