//**********************************************************************
// This is intended to solve a challenge to create a REST API to offers
// CRUD functionalities for Harry Potter's characters.
// **********************************************************************
package br.challenge.makemagic.character.ctrl;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.challenge.makemagic.character.dto.CharacterEntityDto;
import br.challenge.makemagic.character.exception.handling.ErrorMessage;
import br.challenge.makemagic.character.service.CharacterService;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is responsible to give support for REST API for CRUD
 * functionalities for {@link CharacterEntityDto Character}.
 */
@Controller
@Slf4j
@RequestMapping(path = "/v1/character")
public class CharacterController
{
    private static final String HOUSE_ERROR_MESSAGE = "The house Id is not valid";

    @Autowired
    private CharacterService characterService;

    /**
     * Returns a list of all characters or a list of characters related to a house
     * Id.
     *
     * @param house The Id of the house, it can be null
     * @return The ResponseEntity with a list of characters and HttpStatus OK
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<CharacterEntityDto>> getCharacter(@RequestParam(required = false) String house)
    {
	Iterable<CharacterEntityDto> characters = null;

	if (house != null)
	{
	    log.debug("Find characters by house {}", house);

	    characters = characterService.findByHouse(house);
	} else
	{
	    log.debug("Find all characters {}", "");

	    characters = characterService.findAll();
	}

	return new ResponseEntity<>(characters, HttpStatus.OK);
    }

    /**
     * Creates a character with the values specified in the parameter.
     *
     * @param characterEntityDto The CharacterEntity with the character informations
     * @return The ResponseEntity with the character created and HttpStatus Created.
     *         If the request body is null then a Not Acceptable status is returned.
     *         If the Id of the house is invalid, then a Bad Request status is
     *         returned
     */
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCharacter(@RequestBody @Valid CharacterEntityDto characterEntityDto)
    {
	if (!characterService.verifyHouseId(characterEntityDto.getHouse()))
	{
	    ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST,
		    String.format(HOUSE_ERROR_MESSAGE, characterEntityDto.getHouse()));

	    return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
	}

	log.debug("Save character {}", characterEntityDto);

	CharacterEntityDto characterSaved = characterService.addCharacter(characterEntityDto);

	return new ResponseEntity<>(characterSaved, HttpStatus.CREATED);
    }

    /**
     * Updates a character with the values specified in the parameter.
     *
     * @param characterEntityDto The CharacterEntity with the character informations
     * @param id                 The Id of the character that will be updated
     * @return The ResponseEntity with the character updated and HttpStatus OK. If
     *         the request body is null or the Id is invalid, then a Not Acceptable
     *         status is returned. If the Id of the house is invalid, then a Bad
     *         Request status is returned
     */
    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> patchCharacter(@RequestBody @Valid CharacterEntityDto characterEntityDto,
	    @PathVariable String id)
    {
	if (id == null)
	{
	    log.debug("Character Id is null {}", "");

	    return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
	}

	if (!characterService.verifyHouseId(characterEntityDto.getHouse()))
	{
	    ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, HOUSE_ERROR_MESSAGE);

	    return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
	}

	log.debug("Save character {}", characterEntityDto);

	CharacterEntityDto characterSaved = characterService.updateCharacter(characterEntityDto, id);

	if (characterSaved == null)
	{
	    return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
	}

	return new ResponseEntity<>(characterSaved, HttpStatus.OK);
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
	    log.debug("Character Id is null {}", "");

	    return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
	}

	if (characterService.deleteCharacter(id))
	{
	    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
