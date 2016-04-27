package com.mareedo.sample.braintree.payment.service.gateway;

import com.braintreegateway.Environment;
import org.broadleafcommerce.common.payment.service.PaymentGatewayConfiguration;

/**
 * Created by maesi on 26/04/2016.
 */
public interface BraintreePaymentGatewayConfiguration extends PaymentGatewayConfiguration
{

    Environment getEnvironment();

    String getMerchantId();

    String getPublicKey();

    String getPrivateKey();

    String getTransparentRedirectUrl();

    String getTransparentRedirectReturnUrl();
}
