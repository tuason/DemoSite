package com.mareedo.sample.braintree.payment.service.gateway;

import com.mareedo.sample.braintree.braintreePaymentGateway.service.payment.BraintreePaymentGatewayConstants;
import com.mareedo.sample.braintree.braintreePaymentGateway.service.payment.BraintreePaymentGatewayType;
import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.common.payment.PaymentTransactionType;
import org.broadleafcommerce.common.payment.PaymentType;
import org.broadleafcommerce.common.payment.dto.PaymentResponseDTO;
import org.broadleafcommerce.common.payment.service.PaymentGatewayWebResponsePrintService;
import org.broadleafcommerce.common.payment.service.PaymentGatewayWebResponseService;
import org.broadleafcommerce.common.vendor.service.exception.PaymentException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by maesi on 27/04/2016.
 */
@Service("blBraintreePaymentGatewayWebResponseService")
public class BraintreePaymentGatewayWebResponseServiceImpl implements PaymentGatewayWebResponseService
{

    @Resource(name = "blPaymentGatewayWebResponsePrintService")
    protected PaymentGatewayWebResponsePrintService webResponsePrintService;

    @Resource(name = "blBraintreePaymentGatewayConfiguration")
    protected BraintreePaymentGatewayConfiguration configuration;

    @Override
    public PaymentResponseDTO translateWebResponse(HttpServletRequest request) throws PaymentException
    {
        PaymentResponseDTO responseDTO = new PaymentResponseDTO( PaymentType.CREDIT_CARD,
                                                                 BraintreePaymentGatewayType.BRAINTREE_GATEWAY)
                        .rawResponse(webResponsePrintService.printRequest(request));

        Map<String, String[]> paramMap = request.getParameterMap();

        Money amount = Money.ZERO;
        if (paramMap.containsKey(BraintreePaymentGatewayConstants.TRANSACTION_AMT)) {
            String amt = paramMap.get(BraintreePaymentGatewayConstants.TRANSACTION_AMT)[0];
            amount = new Money( amt);
        }

        responseDTO.successful(true).completeCheckoutOnCallback(true).amount(amount)
                   .paymentTransactionType( PaymentTransactionType.UNCONFIRMED)
                   .orderId(paramMap.get(BraintreePaymentGatewayConstants.ORDER_ID)[0])
                   .responseMap(BraintreePaymentGatewayConstants.BRAINTREE_NONCE,
                                paramMap.get(BraintreePaymentGatewayConstants.BRAINTREE_NONCE)[0])
                   .billTo().addressFirstName(paramMap.get(BraintreePaymentGatewayConstants.BILLING_FIRST_NAME)[0])
                   .addressLastName(paramMap.get(BraintreePaymentGatewayConstants.BILLING_LAST_NAME)[0])
                   .addressLine1(paramMap.get(BraintreePaymentGatewayConstants.BILLING_ADDRESS_LINE1)[0])
                   .addressLine2(paramMap.get(BraintreePaymentGatewayConstants.BILLING_ADDRESS_LINE2)[0])
                   .addressCityLocality(paramMap.get(BraintreePaymentGatewayConstants.BILLING_CITY)[0])
                   .addressStateRegion(paramMap.get(BraintreePaymentGatewayConstants.BILLING_STATE)[0])
                   .addressPostalCode(paramMap.get(BraintreePaymentGatewayConstants.BILLING_ZIP)[0])
                   .addressCountryCode(paramMap.get( BraintreePaymentGatewayConstants.BILLING_COUNTRY)[0]).done();

        return responseDTO;

    }

}
