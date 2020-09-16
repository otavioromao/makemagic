//**********************************************************************
// This is intended to solve a challenge to create a REST API to offers
// CRUD functionalities for Harry Potter's characters.
// **********************************************************************
package br.challenge.makemagic.character.ctrl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import br.challenge.makemagic.character.dto.CharacterEntityDto;
import br.challenge.makemagic.character.exception.handling.ErrorMessage;
import br.challenge.makemagic.character.service.CharacterService;

/**
 * This class is responsible to test {@link CharacterController}.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CharacterControllerTest
{
    private static final String CHARACTER_ENTRY_POINT = "http://localhost:%s/v1/character";
    private static final String CHARACTER_ENTRY_POINT_FOR_GET = "http://localhost:%s/v1/character?house={house}";
    private static final String CHARACTER_ENTRY_POINT_FOR_UPDATE_DELETE = "http://localhost:%s/v1/character/%s";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String HANNAH_ABBOTT_NAME_VALUE = "Hannah Abbott";
    private static final String HARRY_POTTER_NAME_VALUE = "Harry Potter";
    private static final String HOUSE_ERROR_MESSAGE = "The house Id is not valid";
    private static final String HOUSE_VALUE = "5a05e2b252f721a3cf2ea33f";
    private static final String HOUSE_NAME_VALUE = "Hufflepuff";
    private static final String HOUSE_KEY = "house";
    private static final String ID_VALUE = "1";
    private static final String INVALID_ID_VALUE = "invalidId";
    private static final String NAME_KEY = "name";
    private static final String PATRONUS_KEY = "patronus";
    private static final String PATRONUS_VALUE = "stag";
    private static final String PATRONUS_TEST_VALUE = "patronusTest";
    private static final String ROLE_KEY = "role";
    private static final String SCHOOL_KEY = "school";
    private static final String SCHOOL_NAME_VALUE = "Hogwarts School of Witchcraft and Wizardry";
    private static final String SCHOOL_TEST_VALUE = "schoolTest";
    private static final String STUDENT_VALUE = "student";

    @Mock
    private CharacterService characterService;

    @InjectMocks
    private CharacterController characterController = new CharacterController();

    @LocalServerPort
    private int localServerPort;

    private RestTemplate restTemplate = new RestTemplate();

    @Test
    void getCharacterModel_withTwoCharacters_shoudReturnTwoCharacters()
    {
	CharacterEntityDto characterOne = createCharacterEntity(HANNAH_ABBOTT_NAME_VALUE, STUDENT_VALUE,
		SCHOOL_NAME_VALUE, HOUSE_NAME_VALUE, PATRONUS_VALUE);
	CharacterEntityDto characterTwo = createCharacterEntity(HARRY_POTTER_NAME_VALUE, STUDENT_VALUE,
		SCHOOL_NAME_VALUE, HOUSE_VALUE, PATRONUS_VALUE);

	List<CharacterEntityDto> characters = new ArrayList<>();
	characters.add(characterOne);
	characters.add(characterTwo);

	when(characterService.findAll()).thenReturn(characters);

	ResponseEntity<Iterable<CharacterEntityDto>> response = characterController.getCharacter(null);

	assertEquals(HttpStatus.OK, response.getStatusCode());
	assertNotNull(response.getBody());
	assertIterableEquals(response.getBody(), characters);
    }

    @Test
    void getCharacterModel_withHouseParam_shoudReturnOneCharacter()
    {
	CharacterEntityDto characterOne = createCharacterEntity(HANNAH_ABBOTT_NAME_VALUE, STUDENT_VALUE,
		SCHOOL_NAME_VALUE, HOUSE_NAME_VALUE, PATRONUS_VALUE);
	CharacterEntityDto characterTwo = createCharacterEntity(HARRY_POTTER_NAME_VALUE, STUDENT_VALUE,
		SCHOOL_NAME_VALUE, HOUSE_VALUE, PATRONUS_VALUE);

	List<CharacterEntityDto> characters = new ArrayList<>();
	characters.add(characterOne);
	characters.add(characterTwo);

	when(characterService.findByHouse(HOUSE_VALUE)).thenReturn(characters);

	ResponseEntity<Iterable<CharacterEntityDto>> response = characterController.getCharacter(HOUSE_VALUE);

	assertEquals(HttpStatus.OK, response.getStatusCode());
	assertNotNull(response.getBody());
	assertIterableEquals(response.getBody(), characters);
    }

    @Test
    void createCharacter_withValidInput_shoudReturnCharacterSaved()
    {
	CharacterEntityDto character = createCharacterEntity(HARRY_POTTER_NAME_VALUE, STUDENT_VALUE, SCHOOL_NAME_VALUE,
		HOUSE_VALUE, PATRONUS_VALUE);

	when(characterService.addCharacter(character)).thenReturn(character);
	when(characterService.verifyHouseId(HOUSE_VALUE)).thenReturn(true);

	@SuppressWarnings("unchecked")
	ResponseEntity<CharacterEntityDto> response = (ResponseEntity<CharacterEntityDto>) characterController
		.createCharacter(character);

	assertEquals(HttpStatus.CREATED, response.getStatusCode());
	assertNotNull(response.getBody());
	assertEquals(response.getBody(), character);
    }

    @Test
    void createCharacter_withoutHouseId_shoudReturnBadRequest()
    {
	ResponseEntity<?> response = characterController.createCharacter(new CharacterEntityDto());

	ErrorMessage errorMessage = (ErrorMessage) response.getBody();

	assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	assertEquals(HOUSE_ERROR_MESSAGE, errorMessage.getMessage());
    }

    @Test
    void patchCharacter_withValidInput_shoudReturnCharacterSaved()
    {
	CharacterEntityDto character = createCharacterEntity(HARRY_POTTER_NAME_VALUE, STUDENT_VALUE, SCHOOL_NAME_VALUE,
		HOUSE_VALUE, PATRONUS_VALUE);

	when(characterService.updateCharacter(any(CharacterEntityDto.class), anyString())).thenReturn(character);
	when(characterService.verifyHouseId(HOUSE_VALUE)).thenReturn(true);

	@SuppressWarnings("unchecked")
	ResponseEntity<CharacterEntityDto> response = (ResponseEntity<CharacterEntityDto>) characterController
		.patchCharacter(character, ID_VALUE);

	assertEquals(HttpStatus.OK, response.getStatusCode());
	assertNotNull(response.getBody());
	assertEquals(response.getBody(), character);
    }

    @Test
    void patchCharacter_withInvalidId_shoudReturnNotAcceptable()
    {
	CharacterEntityDto character = createCharacterEntity(HARRY_POTTER_NAME_VALUE, STUDENT_VALUE, SCHOOL_NAME_VALUE,
		HOUSE_VALUE, PATRONUS_VALUE);

	when(characterService.updateCharacter(any(CharacterEntityDto.class), anyString())).thenReturn(null);
	when(characterService.verifyHouseId(HOUSE_VALUE)).thenReturn(true);

	ResponseEntity<?> response = characterController.patchCharacter(character, INVALID_ID_VALUE);

	assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
	assertNull(response.getBody());
    }

    @Test
    void patchCharacter_withNullId_shoudReturnNotAcceptable()
    {
	CharacterEntityDto character = createCharacterEntity(HARRY_POTTER_NAME_VALUE, STUDENT_VALUE, SCHOOL_NAME_VALUE,
		HOUSE_VALUE, PATRONUS_VALUE);

	ResponseEntity<?> response = characterController.patchCharacter(character, null);

	assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
	assertNull(response.getBody());
    }

    @Test
    void patchCharacter_withoutHouseId_shoudReturnBadRequest()
    {
	ResponseEntity<?> response = characterController.patchCharacter(new CharacterEntityDto(), ID_VALUE);

	ErrorMessage errorMessage = (ErrorMessage) response.getBody();

	assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	assertEquals(HOUSE_ERROR_MESSAGE, errorMessage.getMessage());
    }

    @Test
    void deleteCharacter_withValidId_shoudReturnNoContent()
    {
	String id = ID_VALUE;

	when(characterService.deleteCharacter(anyString())).thenReturn(true);

	@SuppressWarnings("unchecked")
	ResponseEntity<CharacterEntityDto> response = (ResponseEntity<CharacterEntityDto>) characterController
		.deleteCharacterById(id);

	assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	assertNull(response.getBody());
    }

    @Test
    void deleteCharacter_whenIdIsNull_shoudReturnNotAcceptable()
    {
	ResponseEntity<?> response = characterController.deleteCharacterById(null);

	assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
	assertNull(response.getBody());
    }

    @Test
    void deleteCharacter_withInvalidId_shoudReturnNotFound()
    {
	ResponseEntity<?> response = characterController.deleteCharacterById(INVALID_ID_VALUE);

	assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	assertNull(response.getBody());
    }

    @Test
    void testEndPoints_withValidInpust_shouldCreateUpdateAndDeleteCharacter()
	    throws ClientProtocolException, IOException, JSONException
    {
	CharacterEntityDto characterCreated = createCharacterForIntegrationTest();

	CharacterEntityDto characterEntityDto = getCharacterByHouseForIntegrationTest(characterCreated.getId());

	assertEquals(characterCreated.getId(), characterEntityDto.getId());

	patchCharacterForIntegrationTest(characterCreated);

	deleteCharacterForIntegrationTest(characterCreated);
    }

    private CharacterEntityDto createCharacterEntity(String name, String role, String school, String house,
	    String patronus)
    {
	return CharacterEntityDto.builder().name(name).role(role).school(school).house(house).patronus(patronus)
		.build();
    }

    private CharacterEntityDto createCharacterForIntegrationTest()
    {
	final String uri = String.format(CHARACTER_ENTRY_POINT, localServerPort);

	Map<String, Object> map = new HashMap<>();
	map.put(NAME_KEY, HARRY_POTTER_NAME_VALUE);
	map.put(ROLE_KEY, STUDENT_VALUE);
	map.put(SCHOOL_KEY, SCHOOL_NAME_VALUE);
	map.put(HOUSE_KEY, HOUSE_VALUE);
	map.put(PATRONUS_KEY, PATRONUS_VALUE);

	HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map);

	ResponseEntity<?> response = restTemplate.postForEntity(uri, entity, CharacterEntityDto.class);

	assertNotNull(response);

	@SuppressWarnings("unchecked")
	ResponseEntity<CharacterEntityDto> responseEntity = (ResponseEntity<CharacterEntityDto>) response;

	assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
	assertNotNull(responseEntity.getBody());
	assertNotNull(responseEntity.getBody().getId());

	return responseEntity.getBody();
    }

    private void patchCharacterForIntegrationTest(CharacterEntityDto CharacterEntityDto)
	    throws JSONException, ClientProtocolException, IOException
    {
	final String uri = String.format(CHARACTER_ENTRY_POINT_FOR_UPDATE_DELETE, localServerPort,
		CharacterEntityDto.getId());

	JSONObject characterPayload = new JSONObject();
	characterPayload.put(NAME_KEY, HARRY_POTTER_NAME_VALUE);
	characterPayload.put(ROLE_KEY, STUDENT_VALUE);
	characterPayload.put(SCHOOL_KEY, SCHOOL_TEST_VALUE);
	characterPayload.put(HOUSE_KEY, HOUSE_VALUE);
	characterPayload.put(PATRONUS_KEY, PATRONUS_TEST_VALUE);

	HttpClient client = HttpClients.createDefault();

	HttpPatch patchRequest = new HttpPatch(uri);

	patchRequest.addHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

	StringEntity stringData = new StringEntity(characterPayload.toString());

	patchRequest.setEntity(stringData);

	HttpResponse response = client.execute(patchRequest);

	assertNotNull(response);
	assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
    }

    private CharacterEntityDto getCharacterByHouseForIntegrationTest(String characterId)
    {
	String uri = String.format(CHARACTER_ENTRY_POINT_FOR_GET, localServerPort);

	Map<String, String> params = new HashMap<String, String>();
	params.put(HOUSE_KEY, HOUSE_VALUE);

	RestTemplate restTemplate = new RestTemplate();

	Object[] response = restTemplate.getForObject(uri, CharacterEntityDto[].class, params);

	assertNotNull(response);
	assertThat(response.length > 0);

	for (Object character : response)
	{
	    CharacterEntityDto characterEntityDto = (CharacterEntityDto) character;
	    if (characterEntityDto.getId().equals(characterId))
	    {
		return characterEntityDto;
	    }
	}

	return ((CharacterEntityDto) response[response.length - 1]);
    }

    private void deleteCharacterForIntegrationTest(CharacterEntityDto CharacterEntityDto)
    {
	final String uri = String.format(CHARACTER_ENTRY_POINT_FOR_UPDATE_DELETE, localServerPort,
		CharacterEntityDto.getId());

	restTemplate.delete(uri, CharacterEntityDto.getId());
    }
}
