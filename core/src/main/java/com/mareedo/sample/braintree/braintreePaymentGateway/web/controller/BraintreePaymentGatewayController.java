package com.mareedo.sample.braintree.braintreePaymentGateway.web.controller;

import com.mareedo.sample.braintree.braintreePaymentGateway.service.payment.BraintreePaymentGatewayConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.common.payment.dto.PaymentResponseDTO;
import org.broadleafcommerce.common.payment.service.PaymentGatewayConfiguration;
import org.broadleafcommerce.common.payment.service.PaymentGatewayWebResponseService;
import org.broadleafcommerce.common.vendor.service.exception.PaymentException;
import org.broadleafcommerce.common.web.payment.controller.PaymentGatewayAbstractController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by maesi on 27/04/2016.
 */
@Controller("blBraintreePaymentGatewayController")
@RequestMapping("/" + BraintreePaymentGatewayController.GATEWAY_CONTEXT_KEY)
public class BraintreePaymentGatewayController extends PaymentGatewayAbstractController
{

    protected static final Log LOG = LogFactory.getLog( BraintreePaymentGatewayController.class);
    protected static final String GATEWAY_CONTEXT_KEY = "braintree-checkout";

    @Resource(name = "blBraintreePaymentGatewayWebResponseService")
    protected PaymentGatewayWebResponseService paymentGatewayWebResponseService;

    @Resource(name = "blBraintreePaymentGatewayConfiguration")
    protected PaymentGatewayConfiguration paymentGatewayConfiguration;

    @Override
    public void handleProcessingException(Exception e, RedirectAttributes redirectAttributes) throws PaymentException {
        LOG.error(
                        "A Processing Exception Occurred for " + GATEWAY_CONTEXT_KEY + ". Adding Error to Redirect Attributes.",
                        e);

        redirectAttributes.addAttribute(PAYMENT_PROCESSING_ERROR, getProcessingErrorMessage());
    }

    @Override
    public void handleUnsuccessfulTransaction( Model model, RedirectAttributes redirectAttributes,
                                               PaymentResponseDTO responseDTO) throws PaymentException
    {
        if (LOG.isTraceEnabled()) {
            LOG.trace("The Transaction was unsuccessful for " + GATEWAY_CONTEXT_KEY
                                      + ". Adding Errors to Redirect Attributes.");
        }
        redirectAttributes.addAttribute(PAYMENT_PROCESSING_ERROR,
                                        responseDTO.getResponseMap().get( BraintreePaymentGatewayConstants.RESULT_MESSAGE));
    }

    @Override
    public String getGatewayContextKey() {
        return GATEWAY_CONTEXT_KEY;
    }

    @Override
    public PaymentGatewayWebResponseService getWebResponseService() {
        return paymentGatewayWebResponseService;
    }

    @Override
    public PaymentGatewayConfiguration getConfiguration() {
        return paymentGatewayConfiguration;
    }

    @Override
    @RequestMapping(value = "/process", method = RequestMethod.POST)
    public String returnEndpoint( Model model, HttpServletRequest request, RedirectAttributes redirectAttributes,
                                  Map<String, String> pathVars) throws PaymentException
    {
        return super.process(model, request, redirectAttributes);
    }

    @Override
    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String errorEndpoint(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes,
                                Map<String, String> pathVars) throws PaymentException {
        redirectAttributes.addAttribute(PAYMENT_PROCESSING_ERROR, request.getParameter(PAYMENT_PROCESSING_ERROR));
        return getOrderReviewRedirect();
    }
}