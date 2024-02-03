package pl.marek.securityjwt.exception;

import org.springframework.context.MessageSource;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.marek.securityjwt.utils.ExceptionMessage;

import java.util.Locale;

@RestControllerAdvice
public class ExceptionHandler {

    private static final String RESOURCE_NOT_FOUND = "Exception.resource.not.found";
    private static final String OPTIMISTIC_LOCK = "Exception.different.version";

    private final MessageSource messageSource;

    public ExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionMessage> handleResourceNotFoundException(Exception ex, Locale locale) {
        String errorMessage = messageSource.getMessage(RESOURCE_NOT_FOUND, null, locale);
        return new ResponseEntity<>(new ExceptionMessage(errorMessage), HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ExceptionMessage> handleOptimisticLockingFailureException(Exception ex, Locale locale) {
        String errorMessage = messageSource.getMessage(OPTIMISTIC_LOCK, null, locale);
        return new ResponseEntity<>(new ExceptionMessage(errorMessage), HttpStatus.CONFLICT);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(RestException.class)
    public ResponseEntity<ExceptionMessage> handleIllegalArgument(RestException ex, Locale locale) {
        String errorMessage = messageSource.getMessage(ex.getMessage(), null, locale);
        return new ResponseEntity<>(new ExceptionMessage(errorMessage), HttpStatus.BAD_REQUEST);
    }
}