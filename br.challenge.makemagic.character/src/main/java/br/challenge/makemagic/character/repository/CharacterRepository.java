//**********************************************************************
// This is intended to solve a challenge to create a REST API to offers
// CRUD functionalities for Harry Potter's characters.
// **********************************************************************
package br.challenge.makemagic.character.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.challenge.makemagic.character.model.entity.CharacterEntity;

/**
 * This interface is responsible to give support for CRUD functionalities for
 * {@link CharacterEntity Character}.
 */
public interface CharacterRepository extends CrudRepository<CharacterEntity, Long>
{
    /**
     * Finds the characters that are related to a house.
     *
     * @param house The Id of the character's house
     * @return A Iterable of CharacterEntity
     */
    default Iterable<CharacterEntity> findByHouse(String house)
    {
	List<CharacterEntity> charactersByHouse = new ArrayList<>();

	Iterable<CharacterEntity> characters = findAll();
	for (CharacterEntity characterEntity : characters)
	{
	    if (characterEntity.getHouse().equals(house))
	    {
		charactersByHouse.add(characterEntity);
	    }
	}

	return charactersByHouse;
    }
}
