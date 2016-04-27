package com.mareedo.sample.braintree.payment.service.gateway;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.ValidationError;
import com.mareedo.sample.braintree.braintreePaymentGateway.service.payment.BraintreePaymentGatewayType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.common.payment.PaymentTransactionType;
import org.broadleafcommerce.common.payment.PaymentType;
import org.broadleafcommerce.common.payment.dto.PaymentRequestDTO;
import org.broadleafcommerce.common.payment.dto.PaymentResponseDTO;
import org.broadleafcommerce.common.payment.service.PaymentGatewayRollbackService;
import org.broadleafcommerce.common.vendor.service.exception.PaymentException;
import org.broadleafcommerce.core.order.service.OrderService;
import org.broadleafcommerce.core.payment.domain.OrderPayment;
import org.broadleafcommerce.core.payment.domain.PaymentTransaction;
import org.broadleafcommerce.core.payment.service.OrderPaymentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by maesi on 26/04/2016.
 */
@Service("blBraintreePaymentGatewayRollbackService")
public class BraintreePaymentGatewayRollbackServiceImpl implements PaymentGatewayRollbackService
{
    protected static final Log LOG = LogFactory.getLog( BraintreePaymentGatewayRollbackServiceImpl.class);

    @Resource(name = "cspBraintreeGateway")
    private BraintreeGateway gateway;

    @Resource(name = "blOrderPaymentService")
    private OrderPaymentService orderPaymentService;

    @Resource(name = "blOrderService")
    private OrderService orderService;

    @Override
    public PaymentResponseDTO rollbackAuthorize( PaymentRequestDTO transactionToBeRolledBack) throws PaymentException
    {
        return this.voidTransaction(transactionToBeRolledBack);
    }

    @Override
    public PaymentResponseDTO rollbackCapture(PaymentRequestDTO transactionToBeRolledBack) throws PaymentException {
        throw new PaymentException("The Rollback Capture method is not supported for this module");
    }

    @Override
    public PaymentResponseDTO rollbackAuthorizeAndCapture(PaymentRequestDTO transactionToBeRolledBack)
                    throws PaymentException {
        return this.voidTransaction(transactionToBeRolledBack);
    }

    @Override
    public PaymentResponseDTO rollbackRefund(PaymentRequestDTO transactionToBeRolledBack) throws PaymentException {
        throw new PaymentException("The Rollback Refund method is not supported for this module");
    }

    private PaymentResponseDTO voidTransaction(PaymentRequestDTO transactionToBeRolledBack) {
        if (LOG.isTraceEnabled()) {
            LOG.trace("Braintree Payment Gateway - Rolling back authorize transaction with amount: "
                                      + transactionToBeRolledBack.getTransactionTotal());
        }

        boolean success = true;
        for (OrderPayment orderPayment : orderPaymentService.readPaymentsForOrder(
                        orderService.findOrderById(Long.valueOf(transactionToBeRolledBack.getOrderId())))) {
            for (PaymentTransaction paymentTransaction : orderPayment.getTransactions()) {
                String gatewayTransactionId = paymentTransaction.getAdditionalFields().get("GATEWAY_TRANSACTION_ID");
                if (gatewayTransactionId != null) {
                    Result<Transaction> result = gateway.transaction().voidTransaction( gatewayTransactionId);

                    if (!result.isSuccess()) {
                        for (ValidationError braintreeError : result.getErrors().getAllDeepValidationErrors()) {
                            LOG.error(braintreeError.getMessage());
                        }
                        success = false;

                    }
                }
            }
        }

        return new PaymentResponseDTO( PaymentType.CREDIT_CARD, BraintreePaymentGatewayType.BRAINTREE_GATEWAY)
                        .rawResponse("rollback authorize - successful").successful(success)
                        .paymentTransactionType( PaymentTransactionType.VOID)
                        .amount(new Money( transactionToBeRolledBack.getTransactionTotal()));
    }
}
