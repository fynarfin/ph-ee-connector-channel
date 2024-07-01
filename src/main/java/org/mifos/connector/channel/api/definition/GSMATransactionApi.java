package org.mifos.connector.channel.api.definition;

import static org.mifos.connector.channel.camel.config.CamelProperties.CLIENTCORRELATIONID;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.mifos.connector.common.gsma.dto.GsmaTransfer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface GSMATransactionApi {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Accepted: Transaction id generated", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n"
                    + "    \"transactionId\": \"c3ce2b92-85e1-42b1-a71f-08f2ee44d42b\"\n" + "}"))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "Invalid Headers", value = "{\n" + "    \"errorCategory\": \"Validation\",\n"
                            + "    \"errorCode\": \"error.msg.header.validation.errors\",\n"
                            + "    \"errorDescription\": \"The headers are invalid\",\n"
                            + "    \"developerMessage\": \"The headers are invalid\",\n"
                            + "    \"defaultUserMessage\": \"The headers are invalid\",\n" + "    \"errorParameters\": null,\n"
                            + "    \"errors\": [\n" + "        {\n" + "            \"errorCategory\": \"Validation\",\n"
                            + "            \"errorCode\": \"error.msg.schema.platform.tenant.id.cannot.be.null.or.empty\",\n"
                            + "            \"errorDescription\": \"Platform Tenant Id cannot be null or empty\",\n"
                            + "            \"errorParameters\": null\n" + "        },\n" + "        {\n"
                            + "            \"errorCategory\": \"Validation\",\n"
                            + "            \"errorCode\": \"error.msg.schema.platform.tenant.id.length.is.invalid\",\n"
                            + "            \"errorDescription\": \"Platform Tenant Id length is invalid\",\n"
                            + "            \"errorParameters\": [\n" + "                {\n"
                            + "                    \"key\": \"Platform-TenantId\",\n" + "                    \"value\": \"20\"\n"
                            + "                }\n" + "            ]\n" + "        }\n" + "    ]\n" + "}"),
                    @ExampleObject(name = "Invalid Request Body", value = "{\n" + "    \"errorCategory\": \"Validation\",\n"
                            + "    \"errorCode\": \"error.msg.schema.validation.errors\",\n"
                            + "    \"errorDescription\": \"The request is invalid\",\n"
                            + "    \"developerMessage\": \"The request is invalid\",\n"
                            + "    \"defaultUserMessage\": \"The request is invalid\",\n" + "    \"errorParameters\": null,\n"
                            + "    \"errors\": [\n" + "        {\n" + "            \"errorCategory\": \"Validation\",\n"
                            + "            \"errorCode\": \"error.msg.schema.payer.cannot.be.null.or.empty\",\n"
                            + "            \"errorDescription\": \"Payer cannot be null or empty\",\n"
                            + "            \"errorParameters\": null\n" + "        },\n" + "        {\n"
                            + "            \"errorCategory\": \"Validation\",\n"
                            + "            \"errorCode\": \"error.msg.schema.payee.cannot.be.null.or.empty\",\n"
                            + "            \"errorDescription\": \"Payee cannot be null or empty\",\n"
                            + "            \"errorParameters\": null\n" + "        },\n" + "        {\n"
                            + "            \"errorCategory\": \"Validation\",\n"
                            + "            \"errorCode\": \"error.msg.schema.amount.cannot.be.null.or.empty\",\n"
                            + "            \"errorDescription\": \"Amount cannot be null or empty\",\n"
                            + "            \"errorParameters\": null\n" + "        }\n" + "    ]\n" + "}") })) })
    @PostMapping("/channel/gsma/transaction")
    ResponseEntity<?> gsmatransaction(@RequestBody GsmaTransfer requestBody,
            @RequestHeader(value = CLIENTCORRELATIONID, required = false) String correlationId,
            @RequestHeader(value = "amsName") String amsName, @RequestHeader(value = "accountHoldingInstitutionId") String accountHoldId,
            @RequestHeader(value = "X-CallbackURL") String callbackURL) throws JsonProcessingException;
}
