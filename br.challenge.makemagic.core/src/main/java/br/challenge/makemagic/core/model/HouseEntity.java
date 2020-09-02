//**********************************************************************
// This is intended to solve a challenge to create a REST API to offers
// CRUD functionalities for Harry Potter's characters.
// **********************************************************************
package br.challenge.makemagic.core.model;

/**
 * This class Specifies the house information.
 */
public class HouseEntity
{
    /**
     * Specifies the Id for the house.
     */
    private String _id;

    /**
     * Specifies the name for the house.
     */
    private String name;

    /**
     * Specifies the mascot for the house.
     */
    private String mascot;

    /**
     * Specifies the head for the house.
     */
    private String headOfHouse;

    /**
     * Specifies the ghost for the house.
     */
    private String houseGhost;

    /**
     * Specifies the foundr for the house.
     */
    private String founder;

    /**
     * Specifies a information for the house.
     */
    private String __v;

    /**
     * Specifies the name for school for the house.
     */
    private String school;

    /**
     * Specifies the members for the house.
     */
    private Object[] members;

    /**
     * Specifies the values for the house.
     */
    private Object[] values;

    /**
     * Specifies the colors for the house.
     */
    private Object[] colors;

    /**
     * @return the id
     */
    public String get_id()
    {
	return _id;
    }

    /**
     * @param id the id to set
     */
    public void set_id(String id)
    {
	this._id = id;
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
     * @return the mascot
     */
    public String getMascot()
    {
	return mascot;
    }

    /**
     * @param mascot the mascot to set
     */
    public void setMascot(String mascot)
    {
	this.mascot = mascot;
    }

    /**
     * @return the headOfHouse
     */
    public String getHeadOfHouse()
    {
	return headOfHouse;
    }

    /**
     * @param headOfHouse the headOfHouse to set
     */
    public void setHeadOfHouse(String headOfHouse)
    {
	this.headOfHouse = headOfHouse;
    }

    /**
     * @return the houseGhost
     */
    public String getHouseGhost()
    {
	return houseGhost;
    }

    /**
     * @param houseGhost the houseGhost to set
     */
    public void setHouseGhost(String houseGhost)
    {
	this.houseGhost = houseGhost;
    }

    /**
     * @return the founder
     */
    public String getFounder()
    {
	return founder;
    }

    /**
     * @param founder the founder to set
     */
    public void setFounder(String founder)
    {
	this.founder = founder;
    }

    /**
     * @return the flag
     */
    public String get__v()
    {
	return __v;
    }

    /**
     * @param flag the flag to set
     */
    public void set__v(String flag)
    {
	this.__v = flag;
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
     * @return the members
     */
    public Object[] getMembers()
    {
	return members;
    }

    /**
     * @param members the members to set
     */
    public void setMembers(Object[] members)
    {
	this.members = members;
    }

    /**
     * @return the values
     */
    public Object[] getValues()
    {
	return values;
    }

    /**
     * @param values the values to set
     */
    public void setValues(Object[] values)
    {
	this.values = values;
    }

    /**
     * @return the colors
     */
    public Object[] getColors()
    {
	return colors;
    }

    /**
     * @param colors the colors to set
     */
    public void setColors(Object[] colors)
    {
	this.colors = colors;
    }
}
