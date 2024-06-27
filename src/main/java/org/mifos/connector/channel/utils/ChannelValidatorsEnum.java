package org.mifos.connector.channel.utils;

import org.mifos.connector.common.exception.PaymentHubErrorCategory;
import org.mifos.connector.common.validation.ValidationCodeType;

public enum ChannelValidatorsEnum implements ValidationCodeType{

    INVALID_PLATFORM_TENANT_ID_LENGTH("error.msg.schema.platform.tenant.id.length.is.invalid", "Platform Tenant Id cannot length is invalid"),
    INVALID_PLATFORM_TENANT_ID("error.msg.schema.platform.tenant.id.cannot.be.null.or.empty", "Platform Tenant Id cannot be null or empty"),

    TRANSFER_SCHEMA_VALIDATION_ERROR("error.msg.schema.validation.errors", "The request is invalid"),

    INVALID_PAYER("error.msg.schema.payer.cannot.be.null.or.empty","Payer cannot be null or empty"),
    INVALID_PAYEE("error.msg.schema.payee.cannot.be.null.or.empty","Payee cannot be null or empty"),

    INVALID_PAYER_PARTY_ID_INFO("error.msg.schema.payer.party.id.info.cannot.be.null.or.empty","Payer party Id Info cannot be null or empty"),
    INVALID_PAYEE_PARTY_ID_INFO("error.msg.schema.payee.party.id.info.cannot.be.null.or.empty","Payee party Id Info cannot be null or empty"),
    INVALID_PAYER_PARTY_IDENTIFIER("error.msg.schema.payer.party.identifier.cannot.be.null.or.empty","Payer party identifier cannot be null or empty"),
    INVALID_PAYEE_PARTY_IDENTIFIER("error.msg.schema.payee.party.identifier.cannot.be.null.or.empty","Payee party identifier cannot be null or empty"),
    INVALID_PAYER_PARTY_ID_TYPE("error.msg.schema.payer.party.id.type.cannot.be.null.or.empty","Payer party Id Type cannot be null or empty"),
    INVALID_PAYEE_PARTY_ID_TYPE("error.msg.schema.payee.party.id.type.cannot.be.null.or.empty","Payee party Id Type cannot be null or empty"),

    INVALID_AMOUNT("error.msg.schema.amount.cannot.be.null.or.empty", "Amount cannot be null or empty"),
    INVALID_AMOUNT_AMOUNT("error.msg.schema.amount.amount.cannot.be.null.or.empty","Amount amount cannot be null or empty"),
    INVALID_NEGATIVE_AMOUNT("error.msg.schema.amount.cannot.be.negative", "Amount cannot be negative"),
    INVALID_CURRENCY("error.msg.schema.currency.cannot.be.null.or.empty", "Currency cannot be null or empty"),
    INVALID_CURRENCY_LENGTH("error.msg.schema.currency.length.is.invalid", "Currency length is invalid"),

    HEADER_VALIDATION_ERROR("error.msg.header.validation.errors", "The headers are invalid");

    private final String code;
    private final String category;
    private final String message;

    ChannelValidatorsEnum(String code, String message) {
        this.code = code;
        this.category = PaymentHubErrorCategory.Validation.toString();
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getCategory() {
        return this.category;
    }

    public String getMessage() {
        return message;
    }
}
