//**********************************************************************
// This is intended to solve a challenge to create a REST API to offers
// CRUD functionalities for Harry Potter's characters.
// **********************************************************************
package br.challenge.makemagic.character.ctrl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import br.challenge.makemagic.character.model.entity.HouseEntity;
import reactor.core.publisher.Mono;

/**
 * This class is responsible retrieve the house from potterapi service.
 */
@Component
public class HouseRestClient
{
    private Logger LOG = LoggerFactory.getLogger(HouseRestClient.class);

    private static final String API_URI = "{id}?key={key}";

    @Value("${potterapi_key}")
    private String potterapi_key;

    @Value("${potterapi_houses_uri}")
    private String potterapi_houses_uri;

    private WebClient webClient = WebClient.builder().build();

    /**
     * Verify at www.potterapi.com if the house Id is valid.
     *
     * @param id The house Id
     * @return true when the house Id is valid, otherwise returns false.
     */
    public Boolean verifyHouseId(String id)
    {
	LOG.debug("Find the house by Id {0}", id);

	if (id == null)
	{
	    return false;
	}

	String url = potterapi_houses_uri;
	if (!url.endsWith("/"))
	{
	    url += "/";
	}

	try
	{
	    Mono<HouseEntity[]> bodyToMonoResult = webClient.get().uri(url + API_URI, id, potterapi_key).retrieve()
		    .bodyToMono(HouseEntity[].class);

	    HouseEntity[] house = bodyToMonoResult.block();

	    LOG.debug("Find the house by Id {0}", id);

	    if (house != null && house.length > 0)
	    {
		return true;
	    }
	} catch (Exception e)
	{
	    LOG.debug("Exception thrown {0}", e);
	}

	return false;
    }
}
