package org.mifos.connector.channel.api.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.zeebe.client.api.command.ClientStatusException;
import io.grpc.Status;
import java.util.Collections;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.mifos.connector.channel.api.definition.GSMATransactionApi;
import org.mifos.connector.channel.gsma_api.GsmaP2PResponseDto;
import org.mifos.connector.channel.service.ValidateHeaders;
import org.mifos.connector.channel.utils.HeaderConstants;
import org.mifos.connector.channel.utils.Headers;
import org.mifos.connector.channel.utils.SpringWrapperUtil;
import org.mifos.connector.channel.validator.ChannelValidator;
import org.mifos.connector.channel.validator.HeaderValidator;
import org.mifos.connector.common.channel.dto.PhErrorDTO;
import org.mifos.connector.common.gsma.dto.GsmaTransfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GSMATransactionApiController implements GSMATransactionApi {

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    ObjectMapper objectMapper;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    @ValidateHeaders(requiredHeaders = { HeaderConstants.amsName, HeaderConstants.accountHoldingInstitutionId,
            HeaderConstants.X_CallbackURL,
            HeaderConstants.CLIENTCORRELATIONID }, validatorClass = HeaderValidator.class, validationFunction = "validateGsmaTransaction")
    public ResponseEntity<?> gsmatransaction(GsmaTransfer requestBody, String correlationId, String amsName, String accountHoldId,
            String callbackURL) throws JsonProcessingException {

        try {
            PhErrorDTO phErrorDTO = ChannelValidator.validateTransaction(requestBody);
            if (phErrorDTO != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(phErrorDTO);
            }
        } catch (NullPointerException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("Error message", "Internal Server Error"));
        }

        Headers headers = new Headers.HeaderBuilder().addHeader("X-CorrelationID", correlationId).addHeader("amsName", amsName)
                .addHeader("accountHoldingInstitutionId", accountHoldId).addHeader("X-CallbackURL", callbackURL).build();
        Exchange exchange = SpringWrapperUtil.getDefaultWrappedExchange(producerTemplate.getCamelContext(), headers,
                objectMapper.writeValueAsString(requestBody));
        producerTemplate.send("direct:post-gsma-transaction", exchange);

        Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        if (cause instanceof ClientStatusException) {
            throw new ClientStatusException(Status.FAILED_PRECONDITION, cause);
        }

        String body = exchange.getIn().getBody(String.class);
        GsmaP2PResponseDto responseDto = objectMapper.readValue(body, GsmaP2PResponseDto.class);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseDto);
    }
}
