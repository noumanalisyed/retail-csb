package com.retail.csb.service;

import java.security.SecureRandom;
import java.sql.Timestamp;

import com.retail.csb.common.Utils;
import com.retail.csb.common.api.APIDataObject;
import com.retail.csb.model.Business;
import com.retail.csb.model.User;
import com.retail.csb.model.customer.Contact;
import com.retail.csb.model.customer.CustomerAuth;
import com.retail.csb.model.vm.UserLogin;
import com.retail.csb.model.vm.customer.CustomerSignUp;
import com.retail.csb.repository.ContactRepository;
import com.retail.csb.repository.CustomerRepository;
import com.retail.csb.security.BCryptPasswordUtils;
import com.retail.csb.security.JWTUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    BCryptPasswordUtils bCryptPasswordUtils;

    @Autowired
    ObjectMapper mapper; // * Json mapper

    public APIDataObject signUp(final CustomerSignUp customer) {
        try {
            var contact = new Contact();
            Utils.Copier.copy(customer, contact); // * Map the necessary fields to contact object
            contact.setCreatedBy(customer.getBusinessId());
            contact.setContactStatus("active");
            // * Default is customer if specified then add that
            contact.setType(
                    customer.getType() == null || customer.getType().isEmpty() ? "customer" : customer.getType());
            contact.setMobile(
                    customer.getMobile() == null || customer.getMobile().isEmpty() ? "" : customer.getMobile());

            contact.setAddressLine1(
                customer.getAddressLine1() == null || customer.getAddressLine1().isEmpty() ? "No place to go" : customer.getAddressLine1());

            // * Set dates for contact creation
            setTimeStampAndDates(contact);

            final var savedCustomer = contactRepository.save(contact);

            var customerAuth = new CustomerAuth();
            customerAuth.setUsername(customer.getUsername());
            customerAuth.setPassword(bCryptPasswordUtils.encoder().encode(customer.getPassword()));
            customerAuth.setContactId(savedCustomer.getId());

            customerRepository.save(customerAuth);

            return APIDataObject.builder().error(false).data(savedCustomer).build();
        } catch (Exception e) {
            return APIDataObject.builder().error(true).data(e.getLocalizedMessage()).build();
        }
    }

    public APIDataObject randomPassword(int len) {

        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#%&$";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < len; i++) {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }
        return APIDataObject.builder().error(false).data(sb.toString()).build();
    }

    public APIDataObject login(final UserLogin customer) {
        log.info("Login request by customer: {}", customer.getUsername());
        try {
            final var customerDetail = customerRepository.findByUsername(customer.getUsername());
            final var contactDetails = contactRepository.findById(customerDetail.getContactId()).get();
            final var isCustomerValid = BCryptPasswordUtils.verifyPassword(customer.getPassword(),
                    customerDetail.getPassword());

            // * Check is user provided with right information if not throw exception
            if (!isCustomerValid) {
                throw new Exception();
            }

            // * Making a user model and assigning only necessary fields
            final var user = User.builder().id(customerDetail.getId()).username(customerDetail.getUsername())
                    .business(Business.builder().id(contactDetails.getBusinessId()).build())
                    .userType(contactDetails.getType()).build();

            // * Make the final object with customer's token and its details
            final var customerDetailsAndToken = mapper.createObjectNode().put("token", JWTUtils.createJWT(user))
                    .set("detail", mapper.valueToTree(contactDetails));
            return APIDataObject.builder().error(false).data(customerDetailsAndToken).build();
        } catch (Exception e) {
            log.error("Login request for customer: {} cannot be completed because: {}", customer.getUsername(),
                    e.getMessage());
            return APIDataObject.builder().error(true).data("Invalid user details.").build();
        }
    }

    public APIDataObject loginWithoutVerification(String username){
        try {
            final var customerDetail = customerRepository.findByUsername(username);
            final var contactDetails = contactRepository.findById(customerDetail.getContactId()).get();

            // * Making a user model and assigning only necessary fields
            final var user = User.builder().id(customerDetail.getId()).username(customerDetail.getUsername())
                .business(Business.builder().id(contactDetails.getBusinessId()).build())
                .userType(contactDetails.getType()).build();

            // * Make the final object with customer's token and its details
            final var customerDetailsAndToken = mapper.createObjectNode().put("token", JWTUtils.createJWT(user))
                .set("detail", mapper.valueToTree(contactDetails));
            return APIDataObject.builder().error(false).data(customerDetailsAndToken).build();
        } catch (Exception e) {
            log.error("Login request for customer: {} cannot be completed because: {}", username,
                e.getMessage());
            return APIDataObject.builder().error(true).data("Invalid user details.").build();
        }
    }

    //Check if an account is already registered against a provided email
    public APIDataObject emailRegistered(String emailAddress){
        try {
            return APIDataObject.builder().error(false)
                .data(contactRepository.findByEmail(emailAddress)).build();

        }catch (final Exception e){
            log.error("{}", e.getMessage());
            return APIDataObject.builder().error(true).data("No account registered against provided email").build();
        }

    }

    private void setTimeStampAndDates(final Contact contact) {
        final var timestamp = new Timestamp(System.currentTimeMillis());
        contact.setCreatedAt(timestamp);
        contact.setUpdatedAt(timestamp);
    }
}
