package com.mareedo.sample.braintree.payment.service.gateway;

import com.braintreegateway.BraintreeGateway;
import com.mareedo.sample.braintree.braintreePaymentGateway.service.payment.BraintreePaymentGatewayConstants;
import com.mareedo.sample.braintree.braintreePaymentGateway.service.payment.BraintreePaymentGatewayType;
import org.broadleafcommerce.common.payment.PaymentType;
import org.broadleafcommerce.common.payment.dto.AddressDTO;
import org.broadleafcommerce.common.payment.dto.PaymentRequestDTO;
import org.broadleafcommerce.common.payment.dto.PaymentResponseDTO;
import org.broadleafcommerce.common.payment.service.PaymentGatewayTransparentRedirectService;
import org.broadleafcommerce.common.vendor.service.exception.PaymentException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by maesi on 27/04/2016.
 */
@Service("blBraintreePaymentGatewayTransparentRedirectService")
public class BraintreePaymentGatewayTransparentRedirectServiceImpl implements PaymentGatewayTransparentRedirectService
{

    @Resource( name = "cspBraintreeGateway" )
    private BraintreeGateway gateway;

    @Resource( name = "blBraintreePaymentGatewayConfiguration" )
    protected BraintreePaymentGatewayConfiguration configuration;

    @PostConstruct
    public void init()
    {
        gateway = new BraintreeGateway( configuration.getEnvironment(), configuration.getMerchantId(),
                                        configuration.getPublicKey(), configuration.getPrivateKey() );
    }

    @Override
    public PaymentResponseDTO createAuthorizeForm( PaymentRequestDTO requestDTO )
                    throws PaymentException
    {
        return createCommonTRFields( requestDTO );
    }

    @Override
    public PaymentResponseDTO createAuthorizeAndCaptureForm( PaymentRequestDTO requestDTO )
                    throws PaymentException
    {
        return createCommonTRFields( requestDTO );
    }

    protected PaymentResponseDTO createCommonTRFields( PaymentRequestDTO requestDTO )
    {
        Assert.isTrue( requestDTO.getTransactionTotal() != null,
                       "The Transaction Total on the Payment Request DTO must not be null" );
        Assert.isTrue( requestDTO.getOrderId() != null, "The Order ID on the Payment Request DTO must not be null" );

        // Put The shipping, billing, and transaction amount fields as hidden
        // fields on the form
        // In a real implementation, the gateway will probably provide some API
        // to tokenize this information
        // which you can then put on your form as a secure token. For this
        // sample,
        // we will just place them as plain-text hidden fields on the form
        PaymentResponseDTO responseDTO = new PaymentResponseDTO( PaymentType.CREDIT_CARD,
                                                                 BraintreePaymentGatewayType.BRAINTREE_GATEWAY )
                        .responseMap( BraintreePaymentGatewayConstants.ORDER_ID, requestDTO.getOrderId() )
                        .responseMap( BraintreePaymentGatewayConstants.TRANSACTION_AMT,
                                      requestDTO.getTransactionTotal() )
                        .responseMap( BraintreePaymentGatewayConstants.TRANSPARENT_REDIRECT_URL,
                                      configuration.getTransparentRedirectUrl() );

        AddressDTO<PaymentRequestDTO> billTo = requestDTO.getBillTo();
        if ( billTo != null )
        {
            responseDTO.responseMap( BraintreePaymentGatewayConstants.BILLING_FIRST_NAME, billTo.getAddressFirstName() )
                       .responseMap( BraintreePaymentGatewayConstants.BILLING_LAST_NAME, billTo.getAddressLastName() )
                       .responseMap( BraintreePaymentGatewayConstants.BILLING_ADDRESS_LINE1, billTo.getAddressLine1() )
                       .responseMap( BraintreePaymentGatewayConstants.BILLING_ADDRESS_LINE2, billTo.getAddressLine2() )
                       .responseMap( BraintreePaymentGatewayConstants.BILLING_CITY, billTo.getAddressCityLocality() )
                       .responseMap( BraintreePaymentGatewayConstants.BILLING_STATE, billTo.getAddressStateRegion() )
                       .responseMap( BraintreePaymentGatewayConstants.BILLING_ZIP, billTo.getAddressPostalCode() )
                       .responseMap( BraintreePaymentGatewayConstants.BILLING_COUNTRY, billTo.getAddressCountryCode() );
        }

        AddressDTO<PaymentRequestDTO> shipTo = requestDTO.getShipTo();
        if ( shipTo != null )
        {
            responseDTO.responseMap( BraintreePaymentGatewayConstants.SHIPPING_FIRST_NAME,
                                     shipTo.getAddressFirstName() )
                       .responseMap( BraintreePaymentGatewayConstants.SHIPPING_LAST_NAME, shipTo.getAddressLastName() )
                       .responseMap( BraintreePaymentGatewayConstants.SHIPPING_ADDRESS_LINE1, shipTo.getAddressLine1() )
                       .responseMap( BraintreePaymentGatewayConstants.SHIPPING_ADDRESS_LINE2, shipTo.getAddressLine2() )
                       .responseMap( BraintreePaymentGatewayConstants.SHIPPING_CITY, shipTo.getAddressCityLocality() )
                       .responseMap( BraintreePaymentGatewayConstants.SHIPPING_STATE, shipTo.getAddressStateRegion() )
                       .responseMap( BraintreePaymentGatewayConstants.SHIPPING_ZIP, shipTo.getAddressPostalCode() )
                       .responseMap( BraintreePaymentGatewayConstants.SHIPPING_COUNTRY,
                                     shipTo.getAddressCountryCode() );
        }

        // Calls braintree API to retreive the client token.
        responseDTO.responseMap( BraintreePaymentGatewayConstants.BRAINTREE_CLIENT_TOKEN,
                                 gateway.clientToken().generate() );

        return responseDTO;

    }
}
