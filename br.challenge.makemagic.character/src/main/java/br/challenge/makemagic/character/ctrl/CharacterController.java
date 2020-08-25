//**********************************************************************
// This is intended to solve a challenge to create a REST API to offers
// CRUD functionalities for Harry Potter's characters.
// **********************************************************************
package br.challenge.makemagic.character.ctrl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.challenge.makemagic.character.exception.handling.ErrorMessage;
import br.challenge.makemagic.character.model.entity.CharacterEntity;
import br.challenge.makemagic.character.repository.CharacterRepository;

/**
 * This class is responsible to give support for REST API for CRUD
 * functionalities for {@link CharacterEntity Character}.
 */
@Controller
@RequestMapping(path = "/v1/public/character")
public class CharacterController
{
    private Logger LOG = LoggerFactory.getLogger(CharacterController.class);

    private static final String HOUSE_ERROR_MESSAGE = "The house Id is not valid: %s";

    @Autowired
    private HouseRestClient houseRestClient;

    @Autowired
    private CharacterRepository repository;

    /**
     * Returns a list of all characters or a list of characters related to a house
     * Id.
     *
     * @param house The Id of the house, it can be null
     * @return The ResponseEntity with a list of characters and HttpStatus OK
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<CharacterEntity>> getCharacter(@RequestParam(required = false) String house)
    {
	Iterable<CharacterEntity> characters = null;

	if (house != null)
	{
	    LOG.debug("Find characters by house {0}", house);

	    characters = repository.findByHouse(house);
	} else
	{
	    LOG.debug("Find all characters", "");

	    characters = repository.findAll();
	}

	return new ResponseEntity<>(characters, HttpStatus.OK);
    }

    /**
     * Creates a character with the values specified in the parameter.
     *
     * @param character The CharacterEntity with the character informations
     * @return The ResponseEntity with the character created and HttpStatus Created.
     *         If the request body is null then a Not Acceptable status is returned.
     *         If the Id of the house is invalid, then a Bad Request status is
     *         returned
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCharacter(@RequestBody(required = false) CharacterEntity character)
    {
	if (character == null)
	{
	    LOG.debug("Payload is null", "");

	    return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
	}

	return addCharacter(character);
    }

    /**
     * Updates a character with the values specified in the parameter.
     *
     * @param character The CharacterEntity with the character informations
     * @param id        The Id of the character that will be updated
     * @return The ResponseEntity with the character updated and HttpStatus OK. If
     *         the request body is null or the Id is invalid, then a Not Acceptable
     *         status is returned. If the Id of the house is invalid, then a Bad
     *         Request status is returned
     */
    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> patchCharacter(@RequestBody(required = false) CharacterEntity character,
	    @PathVariable String id)
    {
	if (character == null || id == null)
	{
	    LOG.debug("Payload or character Id is null", "");

	    return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
	}

	return updateCharacter(character, id);
    }

    /**
     * Updates a character with the values specified in the parameter.
     *
     * @param character The CharacterEntity with the character informations
     * @param id        The Id of the character that will be updated
     * @return The ResponseEntity with the character updated and HttpStatus OK. If
     *         the request body is null or the Id is invalid, then a Not Acceptable
     *         status is returned. If the Id of the house is invalid, then a Bad
     *         Request status is returned
     */
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> putCharacter(@RequestBody(required = false) CharacterEntity character,
	    @PathVariable String id)
    {
	if (character == null || id == null)
	{
	    LOG.debug("Payload or character Id is null", "");

	    return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
	}

	return updateCharacter(character, id);
    }

    /**
     * Deletes a character with the Id specified.
     *
     * @param id The Id of the character that will be deleted
     * @return The ResponseEntity with No Content status. If the Id is invalid, then
     *         a Not Acceptable status is returned. If there is no character with
     *         the Id, then a Not Found status is returned
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteCharacterById(@PathVariable String id)
    {
	if (id == null)
	{
	    LOG.debug("Character Id is null", "");

	    return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
	}

	return deleteCharacter(id);
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
    private ResponseEntity<?> addCharacter(CharacterEntity character)
    {
	if (!houseRestClient.verifyHouseId(character.getHouse()))
	{
	    ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST,
		    String.format(HOUSE_ERROR_MESSAGE, character.getHouse()));

	    return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
	}

	LOG.debug("Save character {0}", character);

	CharacterEntity characterSaved = repository.save(character);

	return new ResponseEntity<CharacterEntity>(characterSaved, HttpStatus.CREATED);
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
    private ResponseEntity<?> updateCharacter(CharacterEntity character, String id)
    {
	try
	{
	    character.setId(Long.valueOf(id));
	} catch (NumberFormatException e)
	{
	    LOG.debug("Invalid Id {0}", id);

	    return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
	}

	if (!houseRestClient.verifyHouseId(character.getHouse()))
	{
	    ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST,
		    String.format(HOUSE_ERROR_MESSAGE, character.getHouse()));

	    return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
	}

	LOG.debug("Save character {0}", character);

	CharacterEntity characterSaved = repository.save(character);

	return new ResponseEntity<CharacterEntity>(characterSaved, HttpStatus.OK);
    }

    /**
     * Deletes the character from the storage using the CharacterRepository.
     *
     * @param id The Id of the character
     * @return The ResponseEntity with No Content status. If the Id is invalid, then
     *         a Not Acceptable status is returned. If there is no character with
     *         the Id, then a Not Found status is returned
     */
    private ResponseEntity<?> deleteCharacter(String id)
    {
	try
	{
	    repository.deleteById(Long.valueOf(id));
	} catch (NumberFormatException e)
	{
	    LOG.debug("Invalid Id {0}", id);

	    return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
	} catch (EmptyResultDataAccessException e)
	{
	    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	} catch (Exception e)
	{
	    LOG.debug("Exception {0}", e);
	}

	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
