package com.retail.csb.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.retail.csb.model.vm.Checkout;
import com.retail.csb.repository.VariationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.retail.csb.common.api.APIDataObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CheckoutService {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Autowired
    VariationRepository variationRepository;

    @Autowired
    ObjectMapper objectMapper;

    public APIDataObject processCheckout(final JsonNode cart) throws StripeException {
        try {
            var checkout = objectMapper.treeToValue(cart, Checkout.class);

            // * Map the real values from the data store, this is required for actual price
            // * verification and calculating the actual value of the cart
            final var trueValuedOrder = checkout.getOrders().values().stream().map(orderObject -> {
                var variation = variationRepository.findById(orderObject.getVariationId());
                orderObject.setUnitPriceIncTax(variation.get().getSellPriceIncTax());
                return orderObject;
            }).collect(Collectors.toList());

            final var totalOrderValue = trueValuedOrder.stream()
                    .mapToDouble(orderItem -> orderItem.getUnitPriceIncTax() * orderItem.getQuantity())
                    .reduce(0.00, (subTotal, price) -> subTotal + price);

            checkout.setOrderTotal(
                    (BigDecimal.valueOf(totalOrderValue).setScale(2, RoundingMode.HALF_UP).floatValue()));
            checkout.setOrderItems(trueValuedOrder.size());

            Stripe.apiKey = stripeSecretKey;

            // * Converting to cents and cast it to long value
            final var params = PaymentIntentCreateParams.builder().setCurrency("usd")
                    .setAmount((long) (checkout.getOrderTotal() * 100L)).build();

            final var intent = PaymentIntent.create(params);

            Map<String, Object> response = new HashMap<>();
            response.put("client_secret", intent.getClientSecret());
            response.put("checkout", checkout);
            return APIDataObject.builder().error(false).data(response).build();
        } catch (final Exception e) {
            log.error("{}", e);
            return APIDataObject.builder().error(true).data("Invalid checkout details").build();
        }
    }

}
