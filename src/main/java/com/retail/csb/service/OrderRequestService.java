package com.retail.csb.service;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Map;

import javax.validation.Valid;

import com.retail.csb.common.Utils;
import com.retail.csb.common.api.APIDataObject;
import com.retail.csb.constant.Constants;
import com.retail.csb.model.cache.PublicApiKey;
import com.retail.csb.model.cart.OrderRequest;
import com.retail.csb.model.cart.OrderStatus;
import com.retail.csb.model.customer.Contact;
import com.retail.csb.repository.BusinessRepository;
import com.retail.csb.repository.ContactRepository;
import com.retail.csb.repository.OrderRequestRepository;
import com.retail.csb.security.JWTUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderRequestService extends ApiAccessService<PublicApiKey> {

    @Autowired
    OrderRequestRepository orderRequestRepository;

    @Autowired
    BusinessRepository businessRepository;

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ObjectMapper objectMapper;

    public APIDataObject newOrderRequest(final String authToken, @Valid OrderRequest orderRequest) {
        try {
            final var userDetail = JWTUtils.getUserFromToken(authToken);

            final var timestamp = new Timestamp(System.currentTimeMillis());
            orderRequest.setCreatedAt(timestamp);
            orderRequest.setUpdatedAt(timestamp);
            orderRequest.setCustomerId(userDetail.getUserId().longValue()); // * Assign user id as customer id

            final var order = orderRequestRepository.save(orderRequest);
            return APIDataObject.builder().error(false).data(order).build();
        } catch (final Exception e) {
            return APIDataObject.builder().error(true).data("Invalid user details.").build();
        }
    }

    public APIDataObject getAnOrder(final String authToken, final Long orderId) {
        try {
            final var userDetail = JWTUtils.getUserFromToken(authToken);

            final var order = orderRequestRepository.findById(orderId)
                    .orElseThrow(OrderRequestException::NoOrdersFoundException);

            // * If the customer id and user does not match then there is no order for this
            // * user
            if (!order.getCustomerId().equals(userDetail.getUserId().longValue())) {
                throw OrderRequestException.NoOrdersFoundException();
            }

            return APIDataObject.builder().error(false).data(order).build();
        } catch (final Exception e) {
            return APIDataObject.builder().error(true).data(e.getMessage()).build();
        }
    }

    public APIDataObject updateAnOrder(final String authToken, final Long orderId,
            final Map<String, Object> orderUpdateResource) {
        try {
            final var userDetail = JWTUtils.getUserFromToken(authToken);

            var order = orderRequestRepository.findById(orderId)
                    .orElseThrow(OrderRequestException::NoOrdersFoundException);

            // * If the customer id and user does not match then there is no order for this
            // * user
            if (!order.getCustomerId().equals(userDetail.getUserId().longValue())) {
                throw OrderRequestException.NoOrdersFoundException();
            }

            // * Remove id from request, we don't ever want to change the id.
            orderUpdateResource.remove("id");
            // * Support for patch request is really bad in spring; have to map the non null
            // * values with original values
            orderUpdateResource.forEach((key, value) -> {
                // * Use reflection to get field k on object and set it to value
                // * find field in the object class
                var formattedKey = Utils.snakeCaseToCamelCase(key);
                Field field = ReflectionUtils.findField(OrderRequest.class, formattedKey);
                field.setAccessible(true);

                // * Check if the field is of type enum
                // * ALWAYS USE EQUAL
                if (formattedKey.equals("orderStatus")) {
                    order.setOrderStatus(OrderStatus.valueOf(value.toString().toUpperCase()));
                } else {
                    ReflectionUtils.setField(field, order, value); // * set given field for defined object to value
                }
            });

            orderRequestRepository.save(order); // * Save the updated deal
            return APIDataObject.builder().error(false).data(order).build();
        } catch (final Exception e) {
            return APIDataObject.builder().error(true).data(e.getMessage()).build();
        }
    }

    public APIDataObject getAllOrders(final String authToken, final Integer page) {
        try {
            final var userDetail = JWTUtils.getUserFromToken(authToken);
            final var orders = orderRequestRepository.findAllByCustomerId(userDetail.getUserId().longValue(),
                    Utils.defaultPage(page, Constants.pageSize));
            return APIDataObject.builder().error(false).data(orders).build();
        } catch (final Exception e) {
            return APIDataObject.builder().error(true).data(e.getMessage()).build();
        }
    }

    public APIDataObject newGuestOrderRequest(final String apiKey, final JsonNode order) {
        try {
            final var orderRequest = objectMapper.treeToValue(order.get("order"), OrderRequest.class);

            final var accessKey = getAccessDetails(apiKey);
            final var business = businessRepository.findByApiKey(accessKey.getApiKey());

            var customer = objectMapper.treeToValue(order.get("customer"), Contact.class);
            var contact = new Contact();

            Utils.Copier.copy(customer, contact); // * Map the necessary fields to contact object

            if (contact.getMobile() == null || contact.getMobile().isEmpty() || contact.getEmail() == null
                    || contact.getEmail().isEmpty()) {
                throw OrderRequestException.InvalidOrderDetailsException();
            }

            // * This should be a guest employee from the business
            // todo this should be discussed
            contact.setCreatedBy(business.getId());
            contact.setBusinessId(business.getId());
            contact.setContactStatus("inactive");
            contact.setIsDefault(1);
            contact.setTotalRp(0);
            contact.setTotalRpUsed(0);
            contact.setTotalRpExpired(0);

            // * Default is customer if specified then add that
            contact.setType(
                    customer.getType() == null || customer.getType().isEmpty() ? "guest_customer" : customer.getType());

            // * Set dates for contact creation
            final var timestamp = new Timestamp(System.currentTimeMillis());
            contact.setCreatedAt(timestamp);
            contact.setUpdatedAt(timestamp);

            final var savedCustomer = contactRepository.save(contact);

            // * Generate random order confirmation code
            // * Shoot email and text message with this pin
            final var orderConfirmationPin = Utils.generateRandomCode(6, true);
            orderRequest.setOrderConfirmationPin(orderConfirmationPin);
            // * Generate order tracking id
            orderRequest.setOrderTrackingId(Utils.generateRandomCode(6, false));
            // * Set the order status to awaiting confirmation
            orderRequest.setOrderStatus(OrderStatus.AWAITING_CONFIRMATION);
            orderRequest.setCreatedAt(timestamp);
            orderRequest.setUpdatedAt(timestamp);

            // * Save the customer as a new contact
            orderRequest.setCustomerId(savedCustomer.getId().longValue()); // * Assign customer id as customer id

            final var guestOrder = orderRequestRepository.save(orderRequest);
            return APIDataObject.builder().error(false).data(guestOrder).build();
        } catch (final Exception e) {
            log.error("{}", e);
            return APIDataObject.builder().error(true).data(e.getMessage()).build();
        }
    }

    public APIDataObject verifyGuestOrderRequest(final String apiKey, final Long orderId,
            final String verificationCode) {
        try {
            final var order = orderRequestRepository.findById(orderId)
                    .orElseThrow(OrderRequestException::NoOrdersFoundException);

            if (!order.getOrderConfirmationPin().equals(verificationCode)) {
                throw OrderRequestException.InvalidOrderConfirmationPin();
            }

            final var timestamp = new Timestamp(System.currentTimeMillis());
            order.setOrderStatus(OrderStatus.RECEIVED);
            order.setUpdatedAt(timestamp);

            final var updatedOrder = orderRequestRepository.save(order);
            return APIDataObject.builder().error(false).data(updatedOrder).build();
        } catch (final Exception e) {
            return APIDataObject.builder().error(true).data(e.getMessage()).build();
        }
    }

    public APIDataObject getAGuestOrder(final String apiKey, final Long orderId) {
        try {
            final var accessKey = getAccessDetails(apiKey);

            if (accessKey == null) {
                throw OrderRequestException.NoOrdersFoundException();
            }

            final var order = orderRequestRepository.findById(orderId)
                    .orElseThrow(OrderRequestException::NoOrdersFoundException);

            return APIDataObject.builder().error(false).data(order).build();
        } catch (final Exception e) {
            return APIDataObject.builder().error(true).data(e.getMessage()).build();
        }
    }
}

final class OrderRequestException {
    public static Exception NoOrdersFoundException() {
        return new Exception("No orders found");
    }

    public static Exception InvalidOrderDetailsException() {
        return new Exception("Invalid order details");
    }

    public static Exception InvalidOrderConfirmationPin() {
        return new Exception("Invalid order confirmation pin");
    }
}
