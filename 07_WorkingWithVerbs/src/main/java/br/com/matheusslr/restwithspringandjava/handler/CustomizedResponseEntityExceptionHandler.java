package br.com.matheusslr.restwithspringandjava.handler;

import br.com.matheusslr.restwithspringandjava.exceptions.ExceptionResponse;
import br.com.matheusslr.restwithspringandjava.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest request){
        return new ResponseEntity<>(
                ExceptionResponse.builder()
                        .timestamp(new Date())
                        .message(ex.getMessage())
                        .details(request.getDescription(false))
                        .build(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleNotFoundExceptions(Exception ex, WebRequest request){
        return new ResponseEntity<>(
                ExceptionResponse.builder()
                        .timestamp(new Date())
                        .message(ex.getMessage())
                        .details(request.getDescription(false))
                        .build(),HttpStatus.NOT_FOUND);
    }

}
