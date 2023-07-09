package com.retail.csb.Interface;

import com.retail.csb.model.customer.SmsRequest;

public interface SmsSender {
    void sendSms(SmsRequest smsRequest);


}
