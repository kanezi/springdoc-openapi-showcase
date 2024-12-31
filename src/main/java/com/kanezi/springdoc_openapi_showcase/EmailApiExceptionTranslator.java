package com.kanezi.springdoc_openapi_showcase;

import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.stream.Collectors;

/**
 * We are NOT using lombok @Value annotation for default constructor
 * because lombok does not work with AspectJ without additional configuration
 * <p>
 * 1. javac compiles your .java files to .class files with lombok (generating methods, etc.)
 * 2. aspectj regenerates your classes from the .java files without lombok
 */
@RestControllerAdvice(assignableTypes = EmailApiController.class)
public class EmailApiExceptionTranslator {
    private final ErrorAttributes errorAttributes;

    public EmailApiExceptionTranslator(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }


    /**
     * translates validations exceptions (contained in binding result)
     * to apis error json format
     *
     * @param request web request interceptor
     * @return http status 400 with errorText body containing request body validation problems
     */
    // tag::rest-controller-advice-openapi[]
    @ExceptionHandler(exception = BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<EmailApi.EmailError> processValidationException(WebRequest request) {
        String bidingErrors = ((BindException) errorAttributes.getError(request))
                .getBindingResult()
                .getAllErrors()
                .stream()
                .map(oe -> (oe instanceof FieldError fe)
                        ? fe.getField() + ": " + fe.getDefaultMessage()
                        : oe.getDefaultMessage()
                )
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new EmailApi.EmailError(bidingErrors));
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ResponseEntity<EmailApi.EmailError> processServerErrors(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new EmailApi.EmailError(e.getLocalizedMessage()));
    }
    // end::rest-controller-advice-openapi[]
}
