//**********************************************************************
// This is intended to solve a challenge to create a REST API to offers
// CRUD functionalities for Harry Potter's characters.
// **********************************************************************
package br.challenge.makemagic.character.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import br.challenge.makemagic.character.ctrl.HouseRestClient;
import br.challenge.makemagic.character.dto.CharacterEntityDto;
import br.challenge.makemagic.core.model.CharacterEntity;
import br.challenge.makemagic.core.repository.CharacterRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is responsible to give support for services for
 * {@link CharacterEntityDto Character}.
 */
@Service
@Slf4j
//@RequiredArgsConstructor(onConstructor = @Autowired)
public class CharacterService
{
    @Autowired
    private HouseRestClient houseRestClient;

    @Autowired
    private CharacterRepository repository;

    private ModelMapper modelMapper = new ModelMapper();

    /**
     * Returns a list of characters related to a house Id.
     *
     * @param house The Id of the house, it can be null
     * @return The ResponseEntity with a list of characters and HttpStatus OK
     */
    public Iterable<CharacterEntityDto> findByHouse(String house)
    {
	return mapCharacterEntityList(repository.findByHouse(house));
    }

    /**
     * Returns a list of all characters Id.
     *
     * @param house The Id of the house, it can be null
     * @return The ResponseEntity with a list of characters and HttpStatus OK
     */
    public Iterable<CharacterEntityDto> findAll()
    {
	return mapCharacterEntityList(repository.findAll());
    }

    /**
     * Saves the character in the storage using the CharacterRepository.
     *
     * @param characterEntityDto The CharacterEntity with the character informations
     * @return The ResponseEntity with the character created and HttpStatus created.
     *         If the request body is null then a Not Acceptable status is returned.
     *         If the Id of the house is invalid, then a Bad Request status is
     *         returned
     */
    public CharacterEntityDto addCharacter(CharacterEntityDto characterEntityDto)
    {
	if (StringUtils.isEmpty(characterEntityDto))
	{
	    return null;
	}

	log.debug("Save character {}", characterEntityDto);

	if (verifyHouseId(characterEntityDto.getHouse()))
	{
	    CharacterEntity characterEntity = mapCharacterEntityDtoToCharacterEntity(characterEntityDto);

	    characterEntity = repository.save(characterEntity);

	    return mapCharacterEntityToCharacterEntityDto(characterEntity);
	}

	return null;
    }

    /**
     * Saves the character in the storage using the CharacterRepository.
     *
     * @param characterEntityDto The CharacterEntity with the character informations
     * @param id                 The Id of the character
     * @return The ResponseEntity with the character updated and HttpStatus OK. If
     *         the request body is null or the Id is invalid, then a Not Acceptable
     *         status is returned. If the Id of the house is invalid, then a Bad
     *         Request status is returned
     */
    public CharacterEntityDto updateCharacter(CharacterEntityDto characterEntityDto, String id)
    {
	if (StringUtils.isEmpty(characterEntityDto) || StringUtils.isEmpty(id))
	{
	    return null;
	}

	characterEntityDto.setId(id);

//	if (!houseRestClient.verifyHouseId(character.getHouse()))
//	{
//	    ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST,
//		    String.format(HOUSE_ERROR_MESSAGE, character.getHouse()));
//
//	    return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
//	}

	log.debug("Save character {0}", characterEntityDto);

	if (verifyHouseId(characterEntityDto.getHouse()))
	{
	    CharacterEntity characterEntity = mapCharacterEntityDtoToCharacterEntity(characterEntityDto);

	    characterEntity = repository.save(characterEntity);

	    return mapCharacterEntityToCharacterEntityDto(characterEntity);
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

    /**
     * @param characterEntityDtoList
     * @param characterEntityList
     */
    private List<CharacterEntityDto> mapCharacterEntityList(Iterable<CharacterEntity> characterEntityList)
    {
	List<CharacterEntityDto> characterEntityDtoList = new ArrayList<>();

	characterEntityList.forEach(characterEntity ->
	{
	    CharacterEntityDto characterEntityDto = modelMapper.map(characterEntity, CharacterEntityDto.class);

	    characterEntityDtoList.add(characterEntityDto);
	});

	return characterEntityDtoList;
    }

    /**
     * @param characterEntity
     */
    private CharacterEntityDto mapCharacterEntityToCharacterEntityDto(CharacterEntity characterEntity)
    {
	return modelMapper.map(characterEntity, CharacterEntityDto.class);
    }

    /**
     * @param characterEntityDto
     */
    private CharacterEntity mapCharacterEntityDtoToCharacterEntity(CharacterEntityDto characterEntityDto)
    {
	return modelMapper.map(characterEntityDto, CharacterEntity.class);
    }
}
