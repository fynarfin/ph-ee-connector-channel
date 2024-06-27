package org.mifos.connector.channel.validator;

import static org.mifos.connector.common.exception.PaymentHubError.ExtValidationError;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.mifos.connector.channel.utils.ChannelValidatorsEnum;
import org.mifos.connector.channel.utils.HeaderConstants;
import org.mifos.connector.common.channel.dto.PhErrorDTO;
import org.mifos.connector.common.exception.PaymentHubErrorCategory;
import org.mifos.connector.common.validation.ValidatorBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HeaderValidator {

    @Autowired
    private UnsupportedParameterValidation unsupportedParameterValidator;

    @Value("#{'${default_headers}'.split(',')}")
    private List<String> defaultHeader;

    private static final String resource = "channelValidator";

    public PhErrorDTO validateTransfer(Set<String> requiredHeaders, HttpServletRequest request) {
        final ValidatorBuilder validatorBuilder = new ValidatorBuilder();

        List<String> headers = getHeaderList(request);

        unsupportedParameterValidator.handleRequiredParameterValidation(headers, requiredHeaders, validatorBuilder);

        // Checks for Platform_TenantId
        validatorBuilder.validateFieldIsNullAndMaxLengthWithFailureCode(resource, HeaderConstants.Platform_TenantId,
                request.getHeader(HeaderConstants.Platform_TenantId), ChannelValidatorsEnum.INVALID_PLATFORM_TENANT_ID,
                20, ChannelValidatorsEnum.INVALID_PLATFORM_TENANT_ID_LENGTH);

        return handleValidationErrors(validatorBuilder);
    }

    private PhErrorDTO handleValidationErrors(ValidatorBuilder validatorBuilder) {
        if (validatorBuilder.hasError()) {
            validatorBuilder.errorCategory(PaymentHubErrorCategory.Validation.toString())
                    .errorCode(ChannelValidatorsEnum.HEADER_VALIDATION_ERROR.getCode())
                    .errorDescription(ChannelValidatorsEnum.HEADER_VALIDATION_ERROR.getMessage())
                    .developerMessage(ChannelValidatorsEnum.HEADER_VALIDATION_ERROR.getMessage())
                    .defaultUserMessage(ChannelValidatorsEnum.HEADER_VALIDATION_ERROR.getMessage());

            PhErrorDTO.PhErrorDTOBuilder phErrorDTOBuilder = new PhErrorDTO.PhErrorDTOBuilder(ExtValidationError.getErrorCode());
            phErrorDTOBuilder.fromValidatorBuilder(validatorBuilder);
            return phErrorDTOBuilder.build();
        }
        return null;
    }

    public List<String> getHeaderList(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaderNames();
        return Collections.list(request.getHeaderNames());
    }

}
