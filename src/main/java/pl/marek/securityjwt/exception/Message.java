package pl.marek.securityjwt.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class Message {
    private final String message;
    private List<String> detailedMessages;

    public Message(String message) {
        this.message = message;
    }

    public Message(String message, List<String> detailedMessages) {
        this.message = message;
        this.detailedMessages = detailedMessages;
    }
}