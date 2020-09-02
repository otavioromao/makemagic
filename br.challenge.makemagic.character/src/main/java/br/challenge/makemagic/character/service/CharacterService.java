//**********************************************************************
// This is intended to solve a challenge to create a REST API to offers
// CRUD functionalities for Harry Potter's characters.
// **********************************************************************
package br.challenge.makemagic.character.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import br.challenge.makemagic.character.ctrl.HouseRestClient;
import br.challenge.makemagic.core.model.CharacterEntity;
import br.challenge.makemagic.core.repository.CharacterRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is responsible to give support for services for
 * {@link CharacterEntity Character}.
 */
@Service
@Slf4j
//@RequiredArgsConstructor(onConstructor = @Autowired)
public class CharacterService
{
    private static final String HOUSE_ERROR_MESSAGE = "The house Id is not valid: %s";

    @Autowired
    private HouseRestClient houseRestClient;

    @Autowired
    private CharacterRepository repository;

    /**
     * Returns a list of characters related to a house Id.
     *
     * @param house The Id of the house, it can be null
     * @return The ResponseEntity with a list of characters and HttpStatus OK
     */
    public Iterable<CharacterEntity> findByHouse(String house)
    {
	return repository.findByHouse(house);
    }

    /**
     * Returns a list of all characters Id.
     *
     * @param house The Id of the house, it can be null
     * @return The ResponseEntity with a list of characters and HttpStatus OK
     */
    public Iterable<CharacterEntity> findAll()
    {
	return repository.findAll();
    }

    /**
     * Saves the character in the storage using the CharacterRepository.
     *
     * @param character The CharacterEntity with the character informations
     * @return The ResponseEntity with the character created and HttpStatus created.
     *         If the request body is null then a Not Acceptable status is returned.
     *         If the Id of the house is invalid, then a Bad Request status is
     *         returned
     */
    public CharacterEntity addCharacter(CharacterEntity character)
    {
	if (StringUtils.isEmpty(character))
	{
	    return null;
	}

	log.debug("Save character {0}", character);

	if (verifyHouseId(character.getHouse()))
	{
	    return repository.save(character);
	}
	return null;
    }

    /**
     * Saves the character in the storage using the CharacterRepository.
     *
     * @param character The CharacterEntity with the character informations
     * @param id        The Id of the character
     * @return The ResponseEntity with the character updated and HttpStatus OK. If
     *         the request body is null or the Id is invalid, then a Not Acceptable
     *         status is returned. If the Id of the house is invalid, then a Bad
     *         Request status is returned
     */
    public CharacterEntity updateCharacter(CharacterEntity character, String id)
    {
	if (StringUtils.isEmpty(character) || StringUtils.isEmpty(id))
	{
	    return null;
	}

	character.setId(id);

//	if (!houseRestClient.verifyHouseId(character.getHouse()))
//	{
//	    ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST,
//		    String.format(HOUSE_ERROR_MESSAGE, character.getHouse()));
//
//	    return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
//	}

	log.debug("Save character {0}", character);

	if (verifyHouseId(character.getHouse()))
	{
	    return repository.save(character);
	}
	return null;
    }

    /**
     * Deletes the character from the storage using the CharacterRepository.
     *
     * @param id The Id of the character
     * @return True when the delete was executed, otherwise returns false
     */
    public boolean deleteCharacter(String id)
    {
	if (StringUtils.isEmpty(id))
	{
	    return false;
	}

	try
	{
	    repository.deleteById(id);
	} catch (EmptyResultDataAccessException e)
	{
	    return false;
	}

	return true;
    }

    public boolean verifyHouseId(String id)
    {
	return houseRestClient.verifyHouseId(id);
    }
}
