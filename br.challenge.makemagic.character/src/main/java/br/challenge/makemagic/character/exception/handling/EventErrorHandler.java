package br.challenge.makemagic.character.exception.handling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EventErrorHandler
{
    private static final String EMPTY_PAYLOAD_MESSAGE = "Payload is empty";

    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ErrorMessage> handle(MethodArgumentNotValidException exception)
    {
	List<ErrorMessage> errorMessages = new ArrayList<>();

	exception.getBindingResult().getFieldErrors().forEach(error ->
	{
	    String message = messageSource.getMessage(error, LocaleContextHolder.getLocale());

	    errorMessages.add(new ErrorMessage(HttpStatus.BAD_REQUEST, message));
	});

	return errorMessages;
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorMessage handle(HttpMessageNotReadableException exception)
    {
	String message = "";

	try
	{
	    if (exception.getHttpInputMessage().getBody().read() == -1)
	    {
		message = EMPTY_PAYLOAD_MESSAGE;
	    }
	} catch (IOException e)
	{
	    e.printStackTrace();
	}

	return new ErrorMessage(HttpStatus.BAD_REQUEST, message);
    }
}
