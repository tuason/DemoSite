package com.mareedo.sample.braintree.payment.service.gateway;

import com.braintreegateway.Environment;
import com.mareedo.sample.braintree.braintreePaymentGateway.service.payment.BraintreePaymentGatewayType;
import org.broadleafcommerce.common.payment.PaymentGatewayType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by maesi on 26/04/2016.
 */

@Service("blBraintreePaymentGatewayConfiguration")
public class BraintreePaymentGatewayConfigurationImpl implements BraintreePaymentGatewayConfiguration
{

    @Value("${braintree.environment}")
    private Environment environment;

    @Value("${braintree.merchant.id}")
    private String merchantId;

    @Value("${braintree.public.key}")
    private String publicKey;

    @Value("${braintree.private.key}")
    private String privateKey;

    protected int failureReportingThreshold = 1;

    protected boolean performAuthorizeAndCapture = true;

    @Override
    public Environment getEnvironment() {
        return environment;
    }

    @Override
    public String getMerchantId() {
        return merchantId;
    }

    @Override
    public String getPrivateKey() {
        return privateKey;
    }

    @Override
    public String getPublicKey() {
        return publicKey;
    }

    @Override
    public String getTransparentRedirectUrl() {
        return "/braintree-checkout/process";
    }

    @Override
    public String getTransparentRedirectReturnUrl() {
        return "/braintree-checkout/return";
    }

    @Override
    public boolean isPerformAuthorizeAndCapture() {
        return performAuthorizeAndCapture;
    }

    @Override
    public void setPerformAuthorizeAndCapture(boolean performAuthorizeAndCapture) {
        this.performAuthorizeAndCapture = performAuthorizeAndCapture;
    }

    @Override
    public int getFailureReportingThreshold() {
        return failureReportingThreshold;
    }

    @Override
    public void setFailureReportingThreshold(int failureReportingThreshold) {
        this.failureReportingThreshold = failureReportingThreshold;
    }

    @Override
    public boolean handlesAuthorize() {
        return true;
    }

    @Override
    public boolean handlesCapture() {
        return false;
    }

    @Override
    public boolean handlesAuthorizeAndCapture() {
        return true;
    }

    @Override
    public boolean handlesReverseAuthorize() {
        return false;
    }

    @Override
    public boolean handlesVoid() {
        return false;
    }

    @Override
    public boolean handlesRefund() {
        return false;
    }

    @Override
    public boolean handlesPartialCapture() {
        return false;
    }

    @Override
    public boolean handlesMultipleShipment() {
        return false;
    }

    @Override
    public boolean handlesRecurringPayment() {
        return false;
    }

    @Override
    public boolean handlesSavedCustomerPayment() {
        return false;
    }

    @Override
    public boolean handlesMultiplePayments() {
        return false;
    }

    @Override
    public PaymentGatewayType getGatewayType() {
        return BraintreePaymentGatewayType.BRAINTREE_GATEWAY;
    }

}
