//**********************************************************************
// This is intended to solve a challenge to create a REST API to offers
// CRUD functionalities for Harry Potter's characters.
// **********************************************************************
package br.challenge.makemagic.character.ctrl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * This class is responsible to test {@link HouseRestClient}.
 */
@SpringBootTest
public class HouseRestClientTest
{
    private static final String HOUSE_ID_VALUE = "5a05e2b252f721a3cf2ea33f";

    @Autowired
    private HouseRestClient houseRestClient;

    @InjectMocks
    private String potterapi_key;

    @InjectMocks
    private String potterapi_houses_uri;

    @Test
    public void verifyHouseId_withValidId_shouldReturnTrue()
    {
	Boolean result = houseRestClient.verifyHouseId(HOUSE_ID_VALUE);

	assertTrue(result);
    }

    @Test
    public void verifyHouseId_withInvalidId_shouldReturnFalse()
    {
	Boolean result = houseRestClient.verifyHouseId(null);

	assertFalse(result);
    }
}
