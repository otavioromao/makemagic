//**********************************************************************
// This is intended to solve a challenge to create a REST API to offers
// CRUD functionalities for Harry Potter's characters.
// **********************************************************************
package br.challenge.makemagic.character.exception.handling;

import org.springframework.http.HttpStatus;

/**
 * This class is a definition for error messages.
 */
public class ErrorMessage
{
    /**
     * Specifies the HttpStatus error code for the error message.
     */
    private HttpStatus errorCode;

    /**
     * Specifies the message for the error.
     */
    private String message;

    /**
     * Creates a ErrorMessage with parameters.
     *
     * @param errorCode The HttpStatus error code
     * @param message   The message for the error
     */
    public ErrorMessage(HttpStatus errorCode, String message)
    {
	this.errorCode = errorCode;
	this.message = message;
    }

    /**
     * @return the errorCode
     */
    public HttpStatus getErrorCode()
    {
	return errorCode;
    }

    /**
     * @param errorCode the errorCode to set
     */
    public void setErrorCode(HttpStatus errorCode)
    {
	this.errorCode = errorCode;
    }

    /**
     * @return the message
     */
    public String getMessage()
    {
	return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message)
    {
	this.message = message;
    }
}
