package com.retail.csb.model.customer;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SmsRequest {
    @NotBlank
    private final String phoneNumber; // destination
    @NotBlank
    private final String message; //the message

    public SmsRequest(@JsonProperty("phoneNumber") String phoneNumber, @JsonProperty("message") String message) {
        this.phoneNumber = phoneNumber;
        this.message = message;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "SmsRequest{" +
            "phoneNumber='" + phoneNumber + '\'' +
            ", message='" + message + '\'' +
            '}';
    }
}
