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
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import br.challenge.makemagic.character.model.entity.CharacterEntity;
import br.challenge.makemagic.character.repository.CharacterRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CharacterControllerTest
{
    private static final String CHARACTER_ENTRY_POINT = "http://localhost:%s/v1/public/character";
    private static final String CHARACTER_ENTRY_POINT_FOR_GET = "http://localhost:%s/v1/public/character?house={house}";
    private static final String CHARACTER_ENTRY_POINT_FOR_UPDATE_DELETE = "http://localhost:%s/v1/public/character/%s";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String HANNAH_ABBOTT_NAME_VALUE = "Hannah Abbott";
    private static final String HARRY_POTTER_NAME_VALUE = "Harry Potter";
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
    private CharacterRepository repository;

    @Mock
    private HouseRestClient houseRestClient;

    @InjectMocks
    private CharacterController characterController = new CharacterController();

    @LocalServerPort
    private int localServerPort;

    private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void getCharacterModel_withTwoCharacters_shoudReturnTwoCharacters()
    {
	CharacterEntity characterOne = createCharacterEntity(HANNAH_ABBOTT_NAME_VALUE, STUDENT_VALUE, SCHOOL_NAME_VALUE,
		HOUSE_NAME_VALUE, PATRONUS_VALUE);
	CharacterEntity characterTwo = createCharacterEntity(HARRY_POTTER_NAME_VALUE, STUDENT_VALUE, SCHOOL_NAME_VALUE,
		HOUSE_VALUE, PATRONUS_VALUE);

	List<CharacterEntity> characters = new ArrayList<>();
	characters.add(characterOne);
	characters.add(characterTwo);

	when(repository.findAll()).thenReturn(characters);

	ResponseEntity<Iterable<CharacterEntity>> response = characterController.getCharacter(null);

	assertEquals(response.getStatusCode(), HttpStatus.OK);
	assertNotNull(response.getBody());
	assertIterableEquals(response.getBody(), characters);
    }

    @Test
    public void getCharacterModel_withHouseParam_shoudReturnOneCharacter()
    {
	CharacterEntity characterOne = createCharacterEntity(HANNAH_ABBOTT_NAME_VALUE, STUDENT_VALUE, SCHOOL_NAME_VALUE,
		HOUSE_NAME_VALUE, PATRONUS_VALUE);
	CharacterEntity characterTwo = createCharacterEntity(HARRY_POTTER_NAME_VALUE, STUDENT_VALUE, SCHOOL_NAME_VALUE,
		HOUSE_VALUE, PATRONUS_VALUE);

	List<CharacterEntity> characters = new ArrayList<>();
	characters.add(characterOne);
	characters.add(characterTwo);

	when(repository.findByHouse(HOUSE_VALUE)).thenReturn(characters);

	ResponseEntity<Iterable<CharacterEntity>> response = characterController.getCharacter(HOUSE_VALUE);

	assertEquals(response.getStatusCode(), HttpStatus.OK);
	assertNotNull(response.getBody());
	assertIterableEquals(response.getBody(), characters);
    }

    @Test
    public void createCharacter_withValidInput_shoudReturnCharacterSaved()
    {
	CharacterEntity character = createCharacterEntity(HARRY_POTTER_NAME_VALUE, STUDENT_VALUE, SCHOOL_NAME_VALUE,
		HOUSE_VALUE, PATRONUS_VALUE);

	when(repository.save(character)).thenReturn(character);
	when(houseRestClient.verifyHouseId(HOUSE_VALUE)).thenReturn(true);

	@SuppressWarnings("unchecked")
	ResponseEntity<CharacterEntity> response = (ResponseEntity<CharacterEntity>) characterController
		.createCharacter(character);

	assertEquals(response.getStatusCode(), HttpStatus.CREATED);
	assertNotNull(response.getBody());
	assertEquals(response.getBody(), character);
    }

    @Test
    public void createCharacter_withNullBody_shoudReturnNotAcceptable()
    {
	ResponseEntity<?> response = characterController.createCharacter(null);

	assertEquals(response.getStatusCode(), HttpStatus.NOT_ACCEPTABLE);
	assertNull(response.getBody());
    }

    @Test
    public void patchCharacter_withValidInput_shoudReturnCharacterSaved()
    {
	CharacterEntity character = createCharacterEntity(HARRY_POTTER_NAME_VALUE, STUDENT_VALUE, SCHOOL_NAME_VALUE,
		HOUSE_VALUE, PATRONUS_VALUE);

	when(repository.save(character)).thenReturn(character);
	when(houseRestClient.verifyHouseId(HOUSE_VALUE)).thenReturn(true);

	@SuppressWarnings("unchecked")
	ResponseEntity<CharacterEntity> response = (ResponseEntity<CharacterEntity>) characterController
		.patchCharacter(character, ID_VALUE);

	assertEquals(response.getStatusCode(), HttpStatus.OK);
	assertNotNull(response.getBody());
	assertEquals(response.getBody(), character);
	assertEquals(response.getBody().getId(), Long.valueOf(ID_VALUE));
    }

    @Test
    public void patchCharacter_withInvalidId_shoudReturnNotAcceptable()
    {
	CharacterEntity character = createCharacterEntity(HARRY_POTTER_NAME_VALUE, STUDENT_VALUE, SCHOOL_NAME_VALUE,
		HOUSE_VALUE, PATRONUS_VALUE);

	ResponseEntity<?> response = characterController.patchCharacter(character, INVALID_ID_VALUE);

	assertEquals(response.getStatusCode(), HttpStatus.NOT_ACCEPTABLE);
	assertNull(response.getBody());
    }

    @Test
    public void patchCharacter_withNullId_shoudReturnNotAcceptable()
    {
	CharacterEntity character = createCharacterEntity(HARRY_POTTER_NAME_VALUE, STUDENT_VALUE, SCHOOL_NAME_VALUE,
		HOUSE_VALUE, PATRONUS_VALUE);

	ResponseEntity<?> response = characterController.patchCharacter(character, null);

	assertEquals(response.getStatusCode(), HttpStatus.NOT_ACCEPTABLE);
	assertNull(response.getBody());
    }

    @Test
    public void patchCharacter_withNullBody_shoudReturnNotAcceptable()
    {
	ResponseEntity<?> response = characterController.patchCharacter(null, ID_VALUE);

	assertEquals(response.getStatusCode(), HttpStatus.NOT_ACCEPTABLE);
	assertNull(response.getBody());
    }

    @Test
    public void putCharacter_withValidInput_shoudReturnCharacterSaved()
    {
	CharacterEntity character = createCharacterEntity(HARRY_POTTER_NAME_VALUE, STUDENT_VALUE, SCHOOL_NAME_VALUE,
		HOUSE_VALUE, PATRONUS_VALUE);

	when(repository.save(character)).thenReturn(character);
	when(houseRestClient.verifyHouseId(HOUSE_VALUE)).thenReturn(true);

	@SuppressWarnings("unchecked")
	ResponseEntity<CharacterEntity> response = (ResponseEntity<CharacterEntity>) characterController
		.putCharacter(character, ID_VALUE);

	assertEquals(response.getStatusCode(), HttpStatus.OK);
	assertNotNull(response.getBody());
	assertEquals(response.getBody(), character);
	assertEquals(response.getBody().getId(), Long.valueOf(ID_VALUE));
    }

    @Test
    public void putCharacter_withInvalidId_shoudReturnNotAcceptable()
    {
	CharacterEntity character = createCharacterEntity(HARRY_POTTER_NAME_VALUE, STUDENT_VALUE, SCHOOL_NAME_VALUE,
		HOUSE_VALUE, PATRONUS_VALUE);

	ResponseEntity<?> response = characterController.putCharacter(character, INVALID_ID_VALUE);

	assertEquals(response.getStatusCode(), HttpStatus.NOT_ACCEPTABLE);
	assertNull(response.getBody());
    }

    @Test
    public void putCharacter_withNullId_shoudReturnNotAcceptable()
    {
	CharacterEntity character = createCharacterEntity(HARRY_POTTER_NAME_VALUE, STUDENT_VALUE, SCHOOL_NAME_VALUE,
		HOUSE_VALUE, PATRONUS_VALUE);

	ResponseEntity<?> response = characterController.putCharacter(character, null);

	assertEquals(response.getStatusCode(), HttpStatus.NOT_ACCEPTABLE);
	assertNull(response.getBody());
    }

    @Test
    public void putCharacter_withNullBody_shoudReturnNotAcceptable()
    {
	ResponseEntity<?> response = characterController.putCharacter(null, ID_VALUE);

	assertEquals(response.getStatusCode(), HttpStatus.NOT_ACCEPTABLE);
	assertNull(response.getBody());
    }

    @Test
    public void deleteCharacter_withValidId_shoudReturnNoContent()
    {
	String id = ID_VALUE;

	Mockito.doNothing().when(repository).deleteById(Long.valueOf(id));

	@SuppressWarnings("unchecked")
	ResponseEntity<CharacterEntity> response = (ResponseEntity<CharacterEntity>) characterController
		.deleteCharacterById(id);

	assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);
	assertNull(response.getBody());
    }

    @Test
    public void deleteCharacter_whenIdIsNull_shoudReturnNotAcceptable()
    {
	ResponseEntity<?> response = characterController.deleteCharacterById(null);

	assertEquals(response.getStatusCode(), HttpStatus.NOT_ACCEPTABLE);
	assertNull(response.getBody());
    }

    @Test
    public void deleteCharacter_withInvalidId_shoudReturnNotFound()
    {
	ResponseEntity<?> response = characterController.deleteCharacterById(INVALID_ID_VALUE);

	assertEquals(response.getStatusCode(), HttpStatus.NOT_ACCEPTABLE);
	assertNull(response.getBody());
    }

    @Test
    public void testEndPoints_withValidInpust_shouldCreateUpdateAndDeleteCharacter()
	    throws ClientProtocolException, IOException, JSONException
    {
	CharacterEntity characterCreated = createCharacterForIntegrationTest();

	CharacterEntity characterEntity = getCharacterByHouseForIntegrationTest();

	assertEquals(characterCreated.getId(), characterEntity.getId());

	patchCharacterForIntegrationTest(characterCreated);

	putCharacterForIntegrationTest(characterCreated);

	deleteCharacterForIntegrationTest(characterCreated);
    }

    private CharacterEntity createCharacterEntity(String name, String role, String school, String house,
	    String patronus)
    {
	return new CharacterEntity(name, role, school, house, patronus);
    }

    private CharacterEntity createCharacterForIntegrationTest()
    {
	final String uri = String.format(CHARACTER_ENTRY_POINT, localServerPort);

	Map<String, Object> map = new HashMap<>();
	map.put(NAME_KEY, HARRY_POTTER_NAME_VALUE);
	map.put(ROLE_KEY, STUDENT_VALUE);
	map.put(SCHOOL_KEY, SCHOOL_NAME_VALUE);
	map.put(HOUSE_KEY, HOUSE_VALUE);
	map.put(PATRONUS_KEY, PATRONUS_VALUE);

	HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map);

	ResponseEntity<?> response = restTemplate.postForEntity(uri, entity, CharacterEntity.class);

	assertNotNull(response);

	@SuppressWarnings("unchecked")
	ResponseEntity<CharacterEntity> responseEntity = (ResponseEntity<CharacterEntity>) response;

	assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
	assertNotNull(responseEntity.getBody());
	assertNotNull(responseEntity.getBody().getId());

	return responseEntity.getBody();
    }

    private void patchCharacterForIntegrationTest(CharacterEntity characterEntity)
	    throws JSONException, ClientProtocolException, IOException
    {
	final String uri = String.format(CHARACTER_ENTRY_POINT_FOR_UPDATE_DELETE, localServerPort,
		characterEntity.getId());

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
	assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.OK.value());
    }

    private void putCharacterForIntegrationTest(CharacterEntity characterEntity)
	    throws ClientProtocolException, IOException, JSONException
    {
	final String uri = String.format(CHARACTER_ENTRY_POINT_FOR_UPDATE_DELETE, localServerPort,
		characterEntity.getId());

	JSONObject characterPayload = new JSONObject();
	characterPayload.put(NAME_KEY, HARRY_POTTER_NAME_VALUE);
	characterPayload.put(ROLE_KEY, STUDENT_VALUE);
	characterPayload.put(SCHOOL_KEY, SCHOOL_TEST_VALUE);
	characterPayload.put(HOUSE_KEY, HOUSE_VALUE);
	characterPayload.put(PATRONUS_KEY, PATRONUS_VALUE);

	HttpClient client = HttpClients.createDefault();

	HttpPut putRequest = new HttpPut(uri);

	putRequest.addHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

	StringEntity stringData = new StringEntity(characterPayload.toString());

	putRequest.setEntity(stringData);

	HttpResponse response = client.execute(putRequest);

	assertNotNull(response);
	assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.OK.value());
    }

    public CharacterEntity getCharacterByHouseForIntegrationTest()
    {
	String uri = String.format(CHARACTER_ENTRY_POINT_FOR_GET, localServerPort);

	Map<String, String> params = new HashMap<String, String>();
	params.put(HOUSE_KEY, HOUSE_VALUE);

	RestTemplate restTemplate = new RestTemplate();

	Object[] response = restTemplate.getForObject(uri, CharacterEntity[].class, params);

	assertNotNull(response);
	assertThat(response.length > 0);

	return ((CharacterEntity) response[response.length - 1]);
    }

    private void deleteCharacterForIntegrationTest(CharacterEntity characterEntity)
    {
	final String uri = String.format(CHARACTER_ENTRY_POINT_FOR_UPDATE_DELETE, localServerPort,
		characterEntity.getId());

	restTemplate.delete(uri, characterEntity.getId());
    }
}
