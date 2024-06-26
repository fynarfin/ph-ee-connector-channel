package org.mifos.connector.channel.model;

public class TransferValidationError {
    private String field;
    private String message;

    public TransferValidationError(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }
}
