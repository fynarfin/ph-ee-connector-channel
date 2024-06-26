package org.mifos.connector.channel.model;
import java.util.ArrayList;
import java.util.List;

public class TransferErrorDTO {
    private List<TransferValidationError> errors;

    public TransferErrorDTO() {
        this.errors = new ArrayList<>();
    }

    public void addError(String field, String message) {
        errors.add(new TransferValidationError(field, message));
    }

    public List<TransferValidationError> getErrors() {
        return errors;
    }

}
