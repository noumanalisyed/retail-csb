package com.retail.csb.service;

import com.retail.csb.Interface.SmsSender;
import com.retail.csb.config.TwilioConfig;
import com.retail.csb.model.customer.SmsRequest;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("twilio")
public class TwilioSmsSender implements SmsSender {

    private final TwilioConfig twilioConfig;

    @Autowired
    public TwilioSmsSender(TwilioConfig twilioConfig) {
        this.twilioConfig = twilioConfig;
    }

    @Override
    public void sendSms(SmsRequest smsRequest){
        if (isPhoneNumberValid(smsRequest.getPhoneNumber())){

            PhoneNumber to = new PhoneNumber(smsRequest.getPhoneNumber());
            PhoneNumber from = new PhoneNumber(twilioConfig.getTrialNumber());
            String message = smsRequest.getMessage();
            MessageCreator creator = Message.creator(to, from, message);

            creator.create();
        }
        else {
            throw new IllegalArgumentException("Phone number {" + smsRequest.getPhoneNumber() + "} is not valid");
        }
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        return true;
    }
}
