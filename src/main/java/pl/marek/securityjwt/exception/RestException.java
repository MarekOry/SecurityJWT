package pl.marek.securityjwt.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RestException extends RuntimeException{
    private final String exceptionMessage;
}