package org.mifos.connector.channel.api.implementation;

import static org.mifos.connector.channel.camel.config.CamelProperties.BATCH_ID;
import static org.mifos.connector.channel.camel.config.CamelProperties.CLIENTCORRELATIONID;
import static org.mifos.connector.channel.camel.config.CamelProperties.REGISTERING_INSTITUTION_ID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.zeebe.client.api.command.ClientStatusException;
import io.grpc.Status;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.mifos.connector.channel.api.definition.TransferApi;
import org.mifos.connector.channel.gsma_api.GsmaP2PResponseDto;
import org.mifos.connector.channel.service.ValidateHeaders;
import org.mifos.connector.channel.utils.HeaderConstants;
import org.mifos.connector.channel.utils.Headers;
import org.mifos.connector.channel.utils.SpringWrapperUtil;
import org.mifos.connector.channel.validator.HeaderValidator;
import org.mifos.connector.common.channel.dto.TransactionChannelRequestDTO;
import org.mifos.connector.common.channel.dto.TransactionStatusResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
public class TransferApiController implements TransferApi {

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    ObjectMapper objectMapper;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    @ValidateHeaders(requiredHeaders = { HeaderConstants.Platform_TenantId}, validatorClass = HeaderValidator.class, validationFunction = "validateTransfer")
    public ResponseEntity<?> transfer(String tenant, String batchId, String correlationId, String registeringInstitutionId,
                                          TransactionChannelRequestDTO requestBody) throws JsonProcessingException {

        if (tenant == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "platform tenant id cannot be null"));
        }

        if (tenant.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "platform tenant id cannot be empty"));
        }

        if (tenant.length() > 20) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "platform tenant id length cannot be greater than 20 letters"));
        }

        if(!tenant.equals("gorilla")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "enter a valid platform tenant id"));
        }

        Headers headers = new Headers.HeaderBuilder().addHeader("Platform-TenantId", tenant).addHeader(BATCH_ID, batchId)
                .addHeader(CLIENTCORRELATIONID, correlationId).addHeader(REGISTERING_INSTITUTION_ID, registeringInstitutionId).build();
        Exchange exchange = SpringWrapperUtil.getDefaultWrappedExchange(producerTemplate.getCamelContext(), headers,
                objectMapper.writeValueAsString(requestBody));
        logger.info("Client correlation id: " + correlationId);
        logger.info("Batch id: " + batchId);
        producerTemplate.send("direct:post-transfer", exchange);
        Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        if (cause instanceof ClientStatusException) {
            throw new ClientStatusException(Status.FAILED_PRECONDITION, cause);
        }

        String responseBody = exchange.getIn().getBody(String.class);
        GsmaP2PResponseDto responseDto=objectMapper.readValue(responseBody, GsmaP2PResponseDto.class);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseDto);
    }

    @Override
    public TransactionStatusResponseDTO transferId(String transactionId, String tenant) throws JsonProcessingException {
        Headers headers = new Headers.HeaderBuilder().addHeader("Platform-TenantId", tenant).addHeader("transactionId", transactionId)
                .build();
        Exchange exchange = SpringWrapperUtil.getDefaultWrappedExchange(producerTemplate.getCamelContext(), headers, null);
        producerTemplate.send("direct:get-transfer-transaction-id", exchange);

        String body = exchange.getIn().getBody(String.class);
        return objectMapper.readValue(body, TransactionStatusResponseDTO.class);
    }
}
