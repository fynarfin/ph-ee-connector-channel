package org.mifos.connector.channel.validator;

import org.mifos.connector.common.channel.dto.TransactionChannelRequestDTO;
import org.mifos.connector.channel.model.TransferErrorDTO;
import org.mifos.connector.common.gsma.dto.GsmaTransfer;
import org.mifos.connector.common.gsma.dto.Party;

public class ChannelValidator {

    public static TransferErrorDTO validateTransfer(TransactionChannelRequestDTO requestBody){
        TransferErrorDTO errorDTO = new TransferErrorDTO();

        if (requestBody.getPayer() == null || requestBody.getPayer().getPartyIdInfo() == null ||
                requestBody.getPayer().getPartyIdInfo().getPartyIdType() == null ||
                requestBody.getPayer().getPartyIdInfo().getPartyIdentifier() == null) {
            errorDTO.addError("payer", "Payer information is missing or incomplete.");
        }

        if (requestBody.getPayee() == null || requestBody.getPayee().getPartyIdInfo() == null ||
                requestBody.getPayee().getPartyIdInfo().getPartyIdType() == null ||
                requestBody.getPayee().getPartyIdInfo().getPartyIdentifier() == null) {
            errorDTO.addError("payee", "Payee information is missing or incomplete.");
        }

        if (requestBody.getAmount() == null || requestBody.getAmount().getAmount() == null ||
                requestBody.getAmount().getCurrency() == null) {
            errorDTO.addError("amount", "Amount information is missing or incomplete.");
        } else {
            double amount = Double.parseDouble(requestBody.getAmount().getAmount());
            if (String.valueOf(amount).contains(".") && String.valueOf(amount).split("\\.")[1].length() > 4) {
                errorDTO.addError("amount", "Amount should not have more than 4 decimal places.");
            }

            String currency = requestBody.getAmount().getCurrency();
            if (currency.length() != 3) {
                errorDTO.addError("amount", "Currency should be of length 3.");
            }
        }
        return errorDTO.getErrors().isEmpty() ? null : errorDTO;
    }

    public static TransferErrorDTO validateTransaction(GsmaTransfer requestBody){
        TransferErrorDTO errorDTO = new TransferErrorDTO();

        if (requestBody.getRequestingOrganisationTransactionReference() == null || requestBody.getRequestingOrganisationTransactionReference().isEmpty()) {
            errorDTO.addError("requestingOrganisationTransactionReference", "Requesting Organisation Transaction Reference is missing or empty.");
        }

        if (requestBody.getSubType() == null || requestBody.getSubType().isEmpty()) {
            errorDTO.addError("subType", "SubType is missing or empty.");
        }

        if (requestBody.getType() == null || requestBody.getType().isEmpty()) {
            errorDTO.addError("type", "Type is missing or empty.");
        }

        if (requestBody.getAmount() == null || requestBody.getAmount().isEmpty()) {
            errorDTO.addError("amount", "Amount is missing or empty.");
        } else {
            try {
                double amount = Double.parseDouble(requestBody.getAmount());
                if (String.valueOf(amount).contains(".") && String.valueOf(amount).split("\\.")[1].length() > 4) {
                    errorDTO.addError("amount", "Amount should not have more than 4 decimal places.");
                }
            } catch (NumberFormatException e) {
                errorDTO.addError("amount", "Amount is not a valid number.");
            }
        }

        if (requestBody.getCurrency() == null || requestBody.getCurrency().isEmpty()) {
            errorDTO.addError("currency", "Currency is missing or empty.");
        } else if (requestBody.getCurrency().length() != 3) {
            errorDTO.addError("currency", "Currency should be of length 3.");
        }

        if (requestBody.getDescriptionText() == null || requestBody.getDescriptionText().isEmpty()) {
            errorDTO.addError("descriptionText", "Description Text is missing or empty.");
        }

        if (requestBody.getRequestDate() == null || requestBody.getRequestDate().isEmpty()) {
            errorDTO.addError("requestDate", "Request Date is missing or empty.");
        }

        if (requestBody.getCustomData() == null || requestBody.getCustomData().isEmpty()) {
            errorDTO.addError("customData", "Custom Data is missing or empty.");
        }

        if (requestBody.getPayer() == null || requestBody.getPayer().isEmpty()) {
            errorDTO.addError("payer", "Payer information is missing or empty.");
        } else {
            for (Party payer : requestBody.getPayer()) {
                if (payer.getPartyIdType() == null || payer.getPartyIdType().isEmpty()) {
                    errorDTO.addError("payer.partyIdType", "Payer Party ID Type is missing or empty.");
                }
                if (payer.getPartyIdIdentifier() == null || payer.getPartyIdIdentifier().isEmpty()) {
                    errorDTO.addError("payer.partyIdIdentifier", "Payer Party ID Identifier is missing or empty.");
                }
            }
        }

        if (requestBody.getPayee() == null || requestBody.getPayee().isEmpty()) {
            errorDTO.addError("payee", "Payee information is missing or empty.");
        } else {
            for (Party payee : requestBody.getPayee()) {
                if (payee.getPartyIdType() == null || payee.getPartyIdType().isEmpty()) {
                    errorDTO.addError("payee.partyIdType", "Payee Party ID Type is missing or empty.");
                }
                if (payee.getPartyIdIdentifier() == null || payee.getPartyIdIdentifier().isEmpty()) {
                    errorDTO.addError("payee.partyIdIdentifier", "Payee Party ID Identifier is missing or empty.");
                }
            }
        }

        return errorDTO.getErrors().isEmpty() ? null : errorDTO;
    }
}
