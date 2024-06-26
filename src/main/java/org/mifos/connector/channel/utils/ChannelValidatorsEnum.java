package org.mifos.connector.channel.utils;

import org.mifos.connector.common.exception.PaymentHubErrorCategory;
import org.mifos.connector.common.validation.ValidationCodeType;

public enum ChannelValidatorsEnum implements ValidationCodeType{

    INVALID_PLATFORM_TENANT_ID_LENGTH(
            "error.msg.schema.platform.tenant.id.length.is.invalid",
            "Platform Tenant Id cannot length is invalid"),
    INVALID_PLATFORM_TENANT_ID(
            "error.msg.schema.platform.tenant.id.cannot.be.null.or.empty",
            "Platform Tenant Id cannot be null or empty"),

    INVALID_CURRENCY("error.msg.schema.currency.cannot.be.null.or.empty", "Currency cannot be null or empty"), INVALID_CURRENCY_LENGTH(
            "error.msg.schema.currency.length.is.invalid", "Currency length is invalid"),

    INVALID_AMOUNT("error.msg.schema.amount.cannot.be.null.or.empty", "Amount cannot be null or empty"), INVALID_NEGATIVE_AMOUNT(
            "error.msg.schema.amount.cannot.be.negative",
            "Amount cannot be negative"), INVALID_PAYEE_FUNCTIONAL_ID("error.msg.schema.payee.functional.id.cannot.be.null.or.empty",
            "PayeeFunctional Id cannot be null or empty"), INVALID_PAYEE_FUNCTIONAL_ID_LENGTH(
            "error.msg.schema.payee.functional.id.length.is.invalid",
            "PayeeFunctional Id length is invalid"), INVALID_NARRATION_LENGTH(
            "error.msg.schema.narration.length.is.invalid", "Narration is invalid"), INVALID_LENGTH(
            "error.msg.schema.length.is.invalid", "Length is invalid"), INVALID_LIST(
            "error.msg.schema.list.is.invalid", "List is invalid"), INVALID_MAX_LENGTH(
            "error.msg.schema.max.length.cannot.exceed.50.words",
            "Cannot exceed 50 words"), VOUCHER_LIFECYCLE_VALIDATION_ERROR(
            "error.msg.voucher.lifecycle.validation.errors",
            "Voucher lifecycle validation failed"), INVALID_SERIAL_NUMBER(
            "error.msg.schema.serial.number.cannot.be.null.or.empty",
            "Serial number cannot be null or empty"), INVALID_SERIAL_NUMBER_LENGTH(
            "error.msg.schema.serial.number.length.is.invalid",
            "Serial number length is invalid"), INVALID_STATUS(
            "error.msg.schema.status.cannot.be.null.or.empty",
            "Status cannot be null or empty"), INVALID_STATUS_LENGTH(
            "error.msg.schema.status.length.is.invalid",
            "Status length is invalid"), REDEEM_VOUCHER_VALIDATION_ERROR(
            "error.msg.redeem.voucher.validation.errors",
            "Redeem voucher validation failed"), INVALID_AGENT_ID(
            "error.msg.schema.agent.id.cannot.be.null.or.empty",
            "Agent Id cannot be null or empty"), INVALID_AGENT_ID_LENGTH(
            "error.msg.schema.agent.id.length.is.invalid",
            "Agent Id length is invalid"), INVALID_VOUCHER_SERIAL_NUMBER(
            "error.msg.schema.voucher.serial.number.cannot.be.null.or.empty",
            "Voucher serial number cannot be null or empty"), INVALID_VOUCHER_SERIAL_NUMBER_LENGTH(
            "error.msg.schema.voucher.serial.number.length.is.invalid",
            "Voucher serial number length is invalid"), INVALID_VOUCHER_SECRET_NUMBER(
            "error.msg.schema.voucher.secret.number.cannot.be.null.or.empty",
            "Voucher secret number cannot be null or empty"), INVALID_VOUCHER_SECRET_NUMBER_LENGTH(
            "error.msg.schema.voucher.secret.number.length.is.invalid",
            "Voucher secret number length is invalid"), HEADER_VALIDATION_ERROR(
            "error.msg.header.validation.errors", "The headers are invalid"), INVALID_PROGRAM_ID_LENGTH(
            "error.msg.schema.program.id.length.is.invalid",
            "Program Id length is invalid");

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
