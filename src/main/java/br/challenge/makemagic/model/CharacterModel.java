//**********************************************************************
// **********************************************************************
package br.challenge.makemagic.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "characters")
public class CharacterModel {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String role;
    private String school;
    private String house;
    private String patronus;

    public CharacterModel() {
    }

    public CharacterModel(String name, String role, String school, String house, String patronus) {
	this.name = name;
	this.role = role;
	this.school = school;
	this.house = house;
	this.patronus = patronus;
    }

    /**
     * @return the id
     */
    public Long getId() {
	return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
	this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * @return the role
     */
    public String getRole() {
	return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(String role) {
	this.role = role;
    }

    /**
     * @return the school
     */
    public String getSchool() {
	return school;
    }

    /**
     * @param school the school to set
     */
    public void setSchool(String school) {
	this.school = school;
    }

    /**
     * @return the house
     */
    public String getHouse() {
	return house;
    }

    /**
     * @param house the house to set
     */
    public void setHouse(String house) {
	this.house = house;
    }

    /**
     * @return the patronus
     */
    public String getPatronus() {
	return patronus;
    }

    /**
     * @param patronus the patronus to set
     */
    public void setPatronus(String patronus) {
	this.patronus = patronus;
    }
}
