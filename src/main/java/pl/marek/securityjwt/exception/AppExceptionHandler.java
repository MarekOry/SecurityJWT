package pl.marek.securityjwt.exception;

import org.springframework.context.MessageSource;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

@RestControllerAdvice
public class AppExceptionHandler {

    private static final String RESOURCE_NOT_FOUND = "Exception.resource.not.found";
    private static final String OPTIMISTIC_LOCK = "Exception.different.version";
    private static final String EMAIL_PROBLEM = "Exception.email.problem";
    private static final String UNEXPECTED_EXCEPTION = "Exception.unexpected";


    private final MessageSource messageSource;

    public AppExceptionHandler(MessageSource messageSource)  {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Message> handleResourceNotFoundException(Exception ex, Locale locale) {
        String exceptionMessage = messageSource.getMessage(RESOURCE_NOT_FOUND, null, locale);
        return new ResponseEntity<>(new Message(exceptionMessage), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<Message> handleOptimisticLockingFailureException(Exception ex, Locale locale) {
        String exceptionMessage = messageSource.getMessage(OPTIMISTIC_LOCK, null, locale);
        return new ResponseEntity<>(new Message(exceptionMessage), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RestException.class)
    public ResponseEntity<Message> handleIllegalArgument(RestException ex, Locale locale) {
        String exceptionMessage = messageSource.getMessage(ex.getMessage(), null, locale);
        return new ResponseEntity<>(new Message(exceptionMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailException.class)
    public ResponseEntity<Message> handleEmailProblemException(Exception ex, Locale locale) {
        String errorMessage = messageSource.getMessage(EMAIL_PROBLEM, null, locale);
        return new ResponseEntity<>(new Message(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Message> handleExceptions(Exception ex, Locale locale) {
        String errorMessage = messageSource.getMessage(UNEXPECTED_EXCEPTION, null, locale);
        ex.printStackTrace();
        return new ResponseEntity<>(new Message(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}