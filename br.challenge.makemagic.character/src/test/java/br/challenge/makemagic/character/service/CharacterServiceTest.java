//**********************************************************************
// This is intended to solve a challenge to create a REST API to offers
// CRUD functionalities for Harry Potter's characters.
// **********************************************************************
package br.challenge.makemagic.character.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.dao.EmptyResultDataAccessException;

import br.challenge.makemagic.character.ctrl.HouseRestClient;
import br.challenge.makemagic.character.dto.CharacterEntityDto;
import br.challenge.makemagic.core.model.CharacterEntity;
import br.challenge.makemagic.core.repository.CharacterRepository;

/**
 * This class is responsible to test {@link CharacterService}.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CharacterServiceTest
{
    private static final String HANNAH_ABBOTT_NAME_VALUE = "Hannah Abbott";
    private static final String HARRY_POTTER_NAME_VALUE = "Harry Potter";
    private static final String HOUSE_VALUE = "5a05e2b252f721a3cf2ea33f";
    private static final String HOUSE_NAME_VALUE = "Hufflepuff";
    private static final String ID_VALUE = "1";
    private static final String INVALID_ID_VALUE = "invalidId";
    private static final String PATRONUS_VALUE = "stag";
    private static final String SCHOOL_NAME_VALUE = "Hogwarts School of Witchcraft and Wizardry";
    private static final String STUDENT_VALUE = "student";

    @Mock
    private CharacterRepository characterRepository;

    @Mock
    private HouseRestClient houseRestClient;

//  @Autowired
    @InjectMocks
    private CharacterService characterService;
    // private CharacterService characterService = new CharacterService();

    @LocalServerPort
    private int localServerPort;

    @Test
    void findAll_withTwoCharacters_shoudReturnTwoCharacters()
    {
	CharacterEntity characterOne = createCharacterEntity(HANNAH_ABBOTT_NAME_VALUE, STUDENT_VALUE, SCHOOL_NAME_VALUE,
		HOUSE_NAME_VALUE, PATRONUS_VALUE);
	CharacterEntity characterTwo = createCharacterEntity(HARRY_POTTER_NAME_VALUE, STUDENT_VALUE, SCHOOL_NAME_VALUE,
		HOUSE_VALUE, PATRONUS_VALUE);

	List<CharacterEntity> characters = new ArrayList<>();
	characters.add(characterOne);
	characters.add(characterTwo);

	when(characterRepository.findAll()).thenReturn(characters);

	Iterable<CharacterEntityDto> response = characterService.findAll();

	assertNotNull(response);
	assertIterableEquals(response, characters);
    }

    @Test
    void findByHouse_withHouseParam_shoudReturnOneCharacter()
    {
	CharacterEntity characterOne = createCharacterEntity(HANNAH_ABBOTT_NAME_VALUE, STUDENT_VALUE, SCHOOL_NAME_VALUE,
		HOUSE_NAME_VALUE, PATRONUS_VALUE);
	CharacterEntity characterTwo = createCharacterEntity(HARRY_POTTER_NAME_VALUE, STUDENT_VALUE, SCHOOL_NAME_VALUE,
		HOUSE_VALUE, PATRONUS_VALUE);

	List<CharacterEntity> characters = new ArrayList<>();
	characters.add(characterOne);
	characters.add(characterTwo);

	when(characterRepository.findByHouse(HOUSE_VALUE)).thenReturn(characters);

	Iterable<CharacterEntityDto> response = characterService.findByHouse(HOUSE_VALUE);

	assertNotNull(response);
	assertIterableEquals(response, characters);
    }

    @Test
    void addCharacter_withValidInput_shoudReturnCharacterSaved()
    {
	CharacterEntity character = createCharacterEntity(HARRY_POTTER_NAME_VALUE, STUDENT_VALUE, SCHOOL_NAME_VALUE,
		HOUSE_VALUE, PATRONUS_VALUE);

	when(characterRepository.save(character)).thenReturn(character);
	when(houseRestClient.verifyHouseId(HOUSE_VALUE)).thenReturn(true);

	CharacterEntityDto characterEntityDto = createCharacterEntityDto(HARRY_POTTER_NAME_VALUE, STUDENT_VALUE,
		SCHOOL_NAME_VALUE, HOUSE_VALUE, PATRONUS_VALUE);

	CharacterEntityDto response = (CharacterEntityDto) characterService.addCharacter(characterEntityDto);

	assertNotNull(response);
	assertEquals(response, character);
    }

    @Test
    void addCharacter_withNullBody_shoudReturnNotAcceptable()
    {
	CharacterEntityDto response = characterService.addCharacter(null);

	assertNull(response);
    }

    @Test
    void updateCharacter_withValidInput_shoudReturnCharacterSaved()
    {
	CharacterEntity character = createCharacterEntity(HARRY_POTTER_NAME_VALUE, STUDENT_VALUE, SCHOOL_NAME_VALUE,
		HOUSE_VALUE, PATRONUS_VALUE);

	CharacterEntity characterExpected = createCharacterEntity(HARRY_POTTER_NAME_VALUE, STUDENT_VALUE,
		SCHOOL_NAME_VALUE, HOUSE_VALUE, PATRONUS_VALUE);
	characterExpected.setId(ID_VALUE);

	when(characterRepository.save(character)).thenReturn(characterExpected);
	when(houseRestClient.verifyHouseId(HOUSE_VALUE)).thenReturn(true);

	CharacterEntityDto characterEntityDto = createCharacterEntityDto(HARRY_POTTER_NAME_VALUE, STUDENT_VALUE,
		SCHOOL_NAME_VALUE, HOUSE_VALUE, PATRONUS_VALUE);

	CharacterEntityDto response = (CharacterEntityDto) characterService.updateCharacter(characterEntityDto,
		ID_VALUE);

	assertNotNull(response);
	assertEquals(response, characterExpected);
	assertEquals(response.getId(), ID_VALUE);
    }

    @Test
    void updateCharacter_withInvalidId_shoudReturnNotAcceptable()
    {
	CharacterEntityDto characterEntityDto = createCharacterEntityDto(HARRY_POTTER_NAME_VALUE, STUDENT_VALUE,
		SCHOOL_NAME_VALUE, HOUSE_VALUE, PATRONUS_VALUE);

	CharacterEntityDto response = characterService.updateCharacter(characterEntityDto, INVALID_ID_VALUE);

	assertNull(response);
    }

    @Test
    void updateCharacter_withNullId_shoudReturnNotAcceptable()
    {
	CharacterEntityDto characterEntityDto = createCharacterEntityDto(HARRY_POTTER_NAME_VALUE, STUDENT_VALUE,
		SCHOOL_NAME_VALUE, HOUSE_VALUE, PATRONUS_VALUE);

	CharacterEntityDto response = characterService.updateCharacter(characterEntityDto, null);

	assertNull(response);
    }

    @Test
    void updateCharacter_withNullBody_shoudReturnNotAcceptable()
    {
	CharacterEntityDto response = characterService.updateCharacter(null, ID_VALUE);

	assertNull(response);
    }

    @Test
    void deleteCharacter_withValidId_shoudReturnNoContent()
    {
	String id = ID_VALUE;

	Mockito.doNothing().when(characterRepository).deleteById(id);

	boolean characterDeleted = characterService.deleteCharacter(id);

	assertEquals(characterDeleted, true);
    }

    @Test
    void deleteCharacter_whenIdIsNull_shoudReturnNotAcceptable()
    {
	boolean characterDeleted = characterService.deleteCharacter(null);

	assertEquals(characterDeleted, false);
    }

    @Test
    void deleteCharacter_withInvalidId_shoudReturnNotFound()
    {
	Mockito.doThrow(EmptyResultDataAccessException.class).when(characterRepository).deleteById(anyString());

	boolean characterDeleted = characterService.deleteCharacter(INVALID_ID_VALUE);

	assertEquals(characterDeleted, false);
    }

    private CharacterEntity createCharacterEntity(String name, String role, String school, String house,
	    String patronus)
    {
	return CharacterEntity.builder().name(name).role(role).school(school).house(house).patronus(patronus).build();
    }

    private CharacterEntityDto createCharacterEntityDto(String name, String role, String school, String house,
	    String patronus)
    {
	return CharacterEntityDto.builder().name(name).role(role).school(school).house(house).patronus(patronus)
		.build();
    }
}
