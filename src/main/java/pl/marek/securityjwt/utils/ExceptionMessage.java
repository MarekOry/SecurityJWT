package pl.marek.securityjwt.utils;

import lombok.Getter;

import java.util.List;

@Getter
public class ExceptionMessage {
    private final String message;
    private List<String> detailedMessages;

    public ExceptionMessage(String message) {
        this.message = message;
    }

    public ExceptionMessage(String message, List<String> detailedMessages) {
        this.message = message;
        this.detailedMessages = detailedMessages;
    }
}