package com.mareedo.sample.braintree.config;

import com.braintreegateway.BraintreeGateway;
import com.mareedo.sample.braintree.payment.service.gateway.BraintreePaymentGatewayConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * Created by maesi on 26/04/2016.
 */
@Configuration
public class BraintreeConfig
{

    @Resource(name = "blBraintreePaymentGatewayConfiguration")
    protected BraintreePaymentGatewayConfiguration configuration;

    @Bean(name = "cspBraintreeGateway")
    public BraintreeGateway braintreeGateway() {
        return new BraintreeGateway(configuration.getEnvironment(), configuration.getMerchantId(),
                                    configuration.getPublicKey(), configuration.getPrivateKey());
    }

}
