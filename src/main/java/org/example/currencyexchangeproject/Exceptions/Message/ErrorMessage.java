package org.example.currencyexchangeproject.Exceptions.Message;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorMessage {
    String message;

    public ErrorMessage(String message) {
        this.message = message;
    }

}
