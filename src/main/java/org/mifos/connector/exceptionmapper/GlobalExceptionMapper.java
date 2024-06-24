package org.mifos.connector.exceptionmapper;

import static org.mifos.connector.channel.utils.ConnectorChannelEnum.INTERNAL_SERVER_OCCURRED;
import static org.mifos.connector.channel.utils.ConnectorChannelEnum.PROCESS_DEFINITION_NOT_FOUND;

import io.camunda.zeebe.client.api.command.ClientStatusException;
import org.mifos.connector.channel.model.GlobalExceptionMapperDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionMapper {

    @ExceptionHandler(ClientStatusException.class)
    public ResponseEntity<GlobalExceptionMapperDTO> handleClientStatusException(ClientStatusException ex) {
        GlobalExceptionMapperDTO exceptionMapperDTO = new GlobalExceptionMapperDTO(PROCESS_DEFINITION_NOT_FOUND.getValue(),
                PROCESS_DEFINITION_NOT_FOUND.getMessage());
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(exceptionMapperDTO);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GlobalExceptionMapperDTO> handleException(Exception ex) {
        GlobalExceptionMapperDTO exceptionMapperDTO = new GlobalExceptionMapperDTO(INTERNAL_SERVER_OCCURRED.getValue(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionMapperDTO);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Map<String, String>> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        if (ex.getHeaderName().equals("Platform-TenantId")) {
            String message = "Required request header 'Platform-TenantId' for method parameter type String is not present";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", message));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap("message", ex.getMessage()));
    }
}
