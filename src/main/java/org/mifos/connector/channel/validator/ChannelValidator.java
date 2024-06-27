package org.mifos.connector.channel.validator;

import org.mifos.connector.common.channel.dto.PhErrorDTO;
import org.mifos.connector.common.channel.dto.TransactionChannelRequestDTO;
import org.mifos.connector.channel.model.TransferErrorDTO;
import org.mifos.connector.common.exception.PaymentHubErrorCategory;
import org.mifos.connector.common.gsma.dto.GsmaTransfer;
import org.mifos.connector.common.validation.ValidatorBuilder;
import org.mifos.connector.channel.utils.ChannelValidatorsEnum;
import static org.mifos.connector.common.exception.PaymentHubError.ExtValidationError;

public class ChannelValidator {

    private static final String resource = "ChannelValidator";
    private static final String payer = "payer";
    private static final String partyIdInfo = "partyIdInfo";
    private static final String partyIdType = "partyIdType";
    private static final String partyIdentifier = "partyIdentifier";
    private static final String payee = "payee";
    private static final String amount = "amount";
    private static final String amount_value = "amount";
    private static final String currency = "currency";
    private static final int expectedCurrencyLength = 3;

    public static PhErrorDTO validateTransfer(TransactionChannelRequestDTO request){
        final ValidatorBuilder validatorBuilder = new ValidatorBuilder();

        // validating payer
        validatorBuilder.reset().resource(resource).parameter(payer).value(request.getPayer())
                .isNullWithFailureCode(ChannelValidatorsEnum.INVALID_PAYER);

        // validating payee
        validatorBuilder.reset().resource(resource).parameter(payee).value(request.getPayee())
                .isNullWithFailureCode(ChannelValidatorsEnum.INVALID_PAYEE);

        if(request.getPayer()!=null){
            // validating payer party id info
            validatorBuilder.reset().resource(resource).parameter(partyIdInfo).value(request.getPayer().getPartyIdInfo())
                    .isNullWithFailureCode(ChannelValidatorsEnum.INVALID_PAYER_PARTY_ID_INFO);

            if(request.getPayer().getPartyIdInfo()!=null){
                // validating payer party id type
                validatorBuilder.reset().resource(resource).parameter(partyIdType).value(request.getPayer().getPartyIdInfo().getPartyIdType())
                        .isNullWithFailureCode(ChannelValidatorsEnum.INVALID_PAYER_PARTY_ID_TYPE);

                // validating payer party identifier
                validatorBuilder.reset().resource(resource).parameter(partyIdentifier).value(request.getPayer().getPartyIdInfo().getPartyIdentifier())
                        .isNullWithFailureCode(ChannelValidatorsEnum.INVALID_PAYER_PARTY_IDENTIFIER);
            }
        }

        if(request.getPayee()!=null){
            // validating payee party id info
            validatorBuilder.reset().resource(resource).parameter(partyIdInfo).value(request.getPayee().getPartyIdInfo())
                    .isNullWithFailureCode(ChannelValidatorsEnum.INVALID_PAYEE_PARTY_ID_INFO);

            if(request.getPayee().getPartyIdInfo()!=null){
                // validating payee party id type
                validatorBuilder.reset().resource(resource).parameter(partyIdType).value(request.getPayee().getPartyIdInfo().getPartyIdType())
                        .isNullWithFailureCode(ChannelValidatorsEnum.INVALID_PAYEE_PARTY_ID_TYPE);

                // validating payee party identifier
                validatorBuilder.reset().resource(resource).parameter(partyIdentifier).value(request.getPayee().getPartyIdInfo().getPartyIdentifier())
                        .isNullWithFailureCode(ChannelValidatorsEnum.INVALID_PAYEE_PARTY_IDENTIFIER);
            }
        }

        // validating amount
        validatorBuilder.reset().resource(resource).parameter(amount).value(request.getAmount())
                .isNullWithFailureCode(ChannelValidatorsEnum.INVALID_AMOUNT);

        if(request.getAmount()!=null){
            // validating the amount field inside the amount field.
            validatorBuilder.reset().resource(resource).parameter(amount_value).value(request.getAmount().getAmount())
                    .isNullWithFailureCode(ChannelValidatorsEnum.INVALID_AMOUNT_AMOUNT)
                    .validateBigDecimalFieldNotNegativeWithFailureCode(ChannelValidatorsEnum.INVALID_NEGATIVE_AMOUNT);

            //validating amount currency
            validatorBuilder.reset().resource(resource).parameter(currency).value(request.getAmount().getCurrency())
                    .isNullWithFailureCode(ChannelValidatorsEnum.INVALID_CURRENCY)
                    .validateFieldMaxLengthWithFailureCodeAndErrorParams(expectedCurrencyLength,ChannelValidatorsEnum.INVALID_CURRENCY_LENGTH);
        }

        if (validatorBuilder.hasError()) {
            validatorBuilder.errorCategory(PaymentHubErrorCategory.Validation.toString())
                    .errorCode(ChannelValidatorsEnum.TRANSFER_SCHEMA_VALIDATION_ERROR.getCode())
                    .errorDescription(ChannelValidatorsEnum.TRANSFER_SCHEMA_VALIDATION_ERROR.getMessage())
                    .developerMessage(ChannelValidatorsEnum.TRANSFER_SCHEMA_VALIDATION_ERROR.getMessage())
                    .defaultUserMessage(ChannelValidatorsEnum.TRANSFER_SCHEMA_VALIDATION_ERROR.getMessage());

            PhErrorDTO.PhErrorDTOBuilder phErrorDTOBuilder = new PhErrorDTO.PhErrorDTOBuilder(ExtValidationError.getErrorCode());
            phErrorDTOBuilder.fromValidatorBuilder(validatorBuilder);
            return phErrorDTOBuilder.build();
        }

        return null;
    }

    public static TransferErrorDTO validateTransaction(GsmaTransfer requestBody){
        return null;
    }
}
