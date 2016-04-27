package com.mareedo.sample.braintree.braintreePaymentGateway.web.processor;

import com.mareedo.sample.braintree.braintreePaymentGateway.service.payment.BraintreePaymentGatewayConstants;
import org.broadleafcommerce.common.payment.dto.PaymentResponseDTO;
import org.broadleafcommerce.common.payment.service.PaymentGatewayConfiguration;
import org.broadleafcommerce.common.payment.service.PaymentGatewayTransparentRedirectService;
import org.broadleafcommerce.common.web.payment.processor.AbstractTRCreditCardExtensionHandler;
import org.broadleafcommerce.common.web.payment.processor.TRCreditCardExtensionManager;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by maesi on 27/04/2016.
 */
@Service("blBraintreePaymentGatewayTRExtensionHandler")
public class BraintreePaymentGatewayTRExtensionHandler extends AbstractTRCreditCardExtensionHandler
{

    public static final String FORM_ACTION_URL = BraintreePaymentGatewayConstants.TRANSPARENT_REDIRECT_URL;
    public static final String FORM_HIDDEN_PARAMS = "FORM_HIDDEN_PARAMS";

    @Resource(name = "blTRCreditCardExtensionManager")
    protected TRCreditCardExtensionManager extensionManager;

    @Resource(name = "blBraintreePaymentGatewayTransparentRedirectService")
    protected PaymentGatewayTransparentRedirectService transparentRedirectService;

    @Resource(name = "blBraintreePaymentGatewayConfiguration")
    protected PaymentGatewayConfiguration configuration;

    @PostConstruct
    public void init() {
        if (isEnabled()) {
            extensionManager.registerHandler(this);
        }
    }

    @Override
    public String getFormActionURLKey() {
        return FORM_ACTION_URL;
    }

    @Override
    public String getHiddenParamsKey() {
        return FORM_HIDDEN_PARAMS;
    }

    @Override
    public PaymentGatewayConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public PaymentGatewayTransparentRedirectService getTransparentRedirectService() {
        return transparentRedirectService;
    }

    @Override
    public void populateFormParameters( Map<String, Map<String, String>> formParameters, PaymentResponseDTO responseDTO) {
        String actionUrl = (String) responseDTO.getResponseMap().get(BraintreePaymentGatewayConstants.TRANSPARENT_REDIRECT_URL);
        Map<String, String> actionValue = new HashMap<String, String>();
        actionValue.put(getFormActionURLKey(), actionUrl);
        formParameters.put(getFormActionURLKey(), actionValue);

        Map<String, String> hiddenFields = new HashMap<String, String>();
        hiddenFields.put(BraintreePaymentGatewayConstants.TRANSACTION_AMT,
                         responseDTO.getResponseMap().get(BraintreePaymentGatewayConstants.TRANSACTION_AMT).toString());
        hiddenFields.put(BraintreePaymentGatewayConstants.ORDER_ID,
                         responseDTO.getResponseMap().get(BraintreePaymentGatewayConstants.ORDER_ID).toString());

        if (responseDTO.getResponseMap().get(BraintreePaymentGatewayConstants.BILLING_FIRST_NAME) != null) {
            hiddenFields.put(BraintreePaymentGatewayConstants.BILLING_FIRST_NAME,
                             responseDTO.getResponseMap().get(BraintreePaymentGatewayConstants.BILLING_FIRST_NAME).toString());
        }

        if (responseDTO.getResponseMap().get(BraintreePaymentGatewayConstants.BILLING_LAST_NAME) != null) {
            hiddenFields.put(BraintreePaymentGatewayConstants.BILLING_LAST_NAME,
                             responseDTO.getResponseMap().get(BraintreePaymentGatewayConstants.BILLING_LAST_NAME).toString());
        }

        if (responseDTO.getResponseMap().get(BraintreePaymentGatewayConstants.BILLING_ADDRESS_LINE1) != null) {
            hiddenFields.put(BraintreePaymentGatewayConstants.BILLING_ADDRESS_LINE1,
                             responseDTO.getResponseMap().get(BraintreePaymentGatewayConstants.BILLING_ADDRESS_LINE1).toString());
        }

        if (responseDTO.getResponseMap().get(BraintreePaymentGatewayConstants.BILLING_ADDRESS_LINE2) != null) {
            hiddenFields.put(BraintreePaymentGatewayConstants.BILLING_ADDRESS_LINE2,
                             responseDTO.getResponseMap().get(BraintreePaymentGatewayConstants.BILLING_ADDRESS_LINE2).toString());
        }

        if (responseDTO.getResponseMap().get(BraintreePaymentGatewayConstants.BILLING_CITY) != null) {
            hiddenFields.put(BraintreePaymentGatewayConstants.BILLING_CITY,
                             responseDTO.getResponseMap().get(BraintreePaymentGatewayConstants.BILLING_CITY).toString());
        }

        if (responseDTO.getResponseMap().get(BraintreePaymentGatewayConstants.BILLING_STATE) != null) {
            hiddenFields.put(BraintreePaymentGatewayConstants.BILLING_STATE,
                             responseDTO.getResponseMap().get(BraintreePaymentGatewayConstants.BILLING_STATE).toString());
        }

        if (responseDTO.getResponseMap().get(BraintreePaymentGatewayConstants.BILLING_ZIP) != null) {
            hiddenFields.put(BraintreePaymentGatewayConstants.BILLING_ZIP,
                             responseDTO.getResponseMap().get(BraintreePaymentGatewayConstants.BILLING_ZIP).toString());
        }

        if (responseDTO.getResponseMap().get(BraintreePaymentGatewayConstants.BILLING_COUNTRY) != null) {
            hiddenFields.put(BraintreePaymentGatewayConstants.BILLING_COUNTRY,
                             responseDTO.getResponseMap().get(BraintreePaymentGatewayConstants.BILLING_COUNTRY).toString());
        }

        if (responseDTO.getResponseMap().get(BraintreePaymentGatewayConstants.SHIPPING_FIRST_NAME) != null) {
            hiddenFields.put(BraintreePaymentGatewayConstants.SHIPPING_FIRST_NAME,
                             responseDTO.getResponseMap().get(BraintreePaymentGatewayConstants.SHIPPING_FIRST_NAME).toString());
        }

        if (responseDTO.getResponseMap().get(BraintreePaymentGatewayConstants.SHIPPING_LAST_NAME) != null) {
            hiddenFields.put(BraintreePaymentGatewayConstants.SHIPPING_LAST_NAME,
                             responseDTO.getResponseMap().get(BraintreePaymentGatewayConstants.SHIPPING_LAST_NAME).toString());
        }

        if (responseDTO.getResponseMap().get(BraintreePaymentGatewayConstants.SHIPPING_ADDRESS_LINE1) != null) {
            hiddenFields.put(BraintreePaymentGatewayConstants.SHIPPING_ADDRESS_LINE1,
                             responseDTO.getResponseMap().get(BraintreePaymentGatewayConstants.SHIPPING_ADDRESS_LINE1).toString());
        }

        if (responseDTO.getResponseMap().get(BraintreePaymentGatewayConstants.SHIPPING_ADDRESS_LINE2) != null) {
            hiddenFields.put(BraintreePaymentGatewayConstants.SHIPPING_ADDRESS_LINE2,
                             responseDTO.getResponseMap().get(BraintreePaymentGatewayConstants.SHIPPING_ADDRESS_LINE2).toString());
        }

        if (responseDTO.getResponseMap().get(BraintreePaymentGatewayConstants.SHIPPING_CITY) != null) {
            hiddenFields.put(BraintreePaymentGatewayConstants.SHIPPING_CITY,
                             responseDTO.getResponseMap().get(BraintreePaymentGatewayConstants.SHIPPING_CITY).toString());
        }

        if (responseDTO.getResponseMap().get(BraintreePaymentGatewayConstants.SHIPPING_STATE) != null) {
            hiddenFields.put(BraintreePaymentGatewayConstants.SHIPPING_STATE,
                             responseDTO.getResponseMap().get(BraintreePaymentGatewayConstants.SHIPPING_STATE).toString());
        }

        if (responseDTO.getResponseMap().get(BraintreePaymentGatewayConstants.SHIPPING_ZIP) != null) {
            hiddenFields.put(BraintreePaymentGatewayConstants.SHIPPING_ZIP,
                             responseDTO.getResponseMap().get(BraintreePaymentGatewayConstants.SHIPPING_ZIP).toString());
        }

        if (responseDTO.getResponseMap().get(BraintreePaymentGatewayConstants.SHIPPING_COUNTRY) != null) {
            hiddenFields.put(BraintreePaymentGatewayConstants.SHIPPING_COUNTRY,
                             responseDTO.getResponseMap().get(BraintreePaymentGatewayConstants.SHIPPING_COUNTRY).toString());
        }

        if (responseDTO.getResponseMap().get(BraintreePaymentGatewayConstants.BRAINTREE_CLIENT_TOKEN) != null) {
            hiddenFields.put(BraintreePaymentGatewayConstants.BRAINTREE_CLIENT_TOKEN,
                             responseDTO.getResponseMap().get(BraintreePaymentGatewayConstants.BRAINTREE_CLIENT_TOKEN).toString());
        }

        formParameters.put(getHiddenParamsKey(), hiddenFields);
    }
}