package com.retail.csb.controller;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.validation.Valid;

import com.retail.csb.common.api.APIDataObject;
import com.retail.csb.common.api.SendApiResponse;
import com.retail.csb.model.User;
import com.retail.csb.model.customer.Contact;
import com.retail.csb.model.customer.SmsRequest;
import com.retail.csb.model.vm.*;
import com.retail.csb.service.*;

import com.retail.csb.model.vm.*;
import com.retail.csb.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    @Autowired
    JWTAuthService authService;

    @Autowired
    UserService userService;

    @Autowired
    CustomerService customerService;

    @Autowired
    ProductService productService;

    @Autowired
    private final TwilioSmsSender twilioSmsSender;

    public AuthController(TwilioSmsSender twilioSmsSender) {
        this.twilioSmsSender = twilioSmsSender;
    }

    @Async
    @PostMapping("/signup")
    public CompletableFuture<ResponseEntity<?>> signup(@Valid @RequestBody User newUser) {
        final var createdUser = authService.singUp(newUser);
        return SendApiResponse.dispatch(createdUser);
    }

    @Async
    @PostMapping("/login")
    public CompletableFuture<ResponseEntity<?>> login(@RequestBody UserLogin user) {
        final var token = authService.login(user);
        return SendApiResponse.dispatch(token);
    }

    @Async
    @GetMapping("/validate/{emailAddress}")
    public CompletableFuture<ResponseEntity<?>> emailRegistered(@PathVariable String emailAddress){

        return SendApiResponse.dispatch(customerService.emailRegistered(emailAddress));

    }

    @Async
    @GetMapping("/validate/username/{username}")
    public CompletableFuture<ResponseEntity<?>> usernameRegistered(@PathVariable String username){
        return SendApiResponse.dispatch(userService.usernameRegistered(username));
    }

    @Async
    @GetMapping("/retrieve/username/{contactId}")
    public CompletableFuture<ResponseEntity<?>> retrieveUsername(@PathVariable Integer contactId){
        return SendApiResponse.dispatch(userService.findUsername(contactId));
    }

    @Async
    @GetMapping("/retrieve/{contactId}")
    public CompletableFuture<ResponseEntity<?>> retrieveUser(@PathVariable String contactId){
        return SendApiResponse.dispatch(userService.fetchContact2(contactId));
    }

    @Async
    @PostMapping("/edit/shipping/{email}")
    public CompletableFuture<ResponseEntity<?>> editShipping(@PathVariable String email, @Valid@RequestBody ShippingData address){
        Contact userToEdit = userService.fetchContact(email);
        userToEdit.setAddressLine2(address.getAddress_Line());
        userToEdit.setCity(address.getCity());
        userToEdit.setState(address.getState());
        userService.saveUpdates(userToEdit);
        String message = "The details were updated";
        return SendApiResponse.dispatch(APIDataObject.builder().error(false).data(Map.of("Message", message)).build());
    }

    @Async
    @PostMapping("/edit/personal/{email}")
    public CompletableFuture<ResponseEntity<?>> editPersonal(@PathVariable String email, @Valid@RequestBody UserPersonalDetails details){
        Contact userToEdit = userService.fetchContact(email);
        userToEdit.setName(details.getName());
        userToEdit.setMobile(details.getPhone());
        userToEdit.setEmail(details.getEmail());
        System.out.println("Edited User is "+userToEdit);
        userService.saveUpdates(userToEdit);
        String message = "The details were updated";
        return SendApiResponse.dispatch(APIDataObject.builder().error(false).data(Map.of("Message", message)).build());
    }

    @Async
    @PostMapping("/edit/credentials/{id}")
    public CompletableFuture<ResponseEntity<?>> editCredentials(@PathVariable String id, @Valid@RequestBody UserCredentialsUpdate creds){
        String message;
        if (creds.getPassword()!= null){
            if (userService.comparePassword(creds.getOldpassword(),id)){
                userService.changePassword(creds.getPassword(),id);
            }
            else{
                message = "Incorrect Old password";
                return SendApiResponse.dispatch(APIDataObject.builder().error(false).data(Map.of("Message", message)).build());
            }
        }
        if (creds.getUsername()!= null) {
            userService.changeUsername(creds.getUsername(), id);
        }
        String message2 = "The details were updated";
        return SendApiResponse.dispatch(APIDataObject.builder().error(false).data(Map.of("Message", message2)).build());
    }

    @Async
    @PostMapping("/edit/billing/{email}")
    public CompletableFuture<ResponseEntity<?>> editBilling(@PathVariable String email, @Valid@RequestBody ShippingData address){
        Contact userToEdit = userService.fetchContact(email);
        userToEdit.setAddressLine1(address.getAddress_Line());
        userToEdit.setCity(address.getCity());
        userToEdit.setState(address.getState());
        userService.saveUpdates(userToEdit);
        String message = "The details were updated";
        return SendApiResponse.dispatch(APIDataObject.builder().error(false).data(Map.of("Message", message)).build());
    }

    @Async
    @PostMapping("/add/review")
    public CompletableFuture<ResponseEntity<?>> addReview(@Valid@RequestBody ReviewData review){
        System.out.println("This is review=====>"+review);
        productService.addReview(review);
        String message = "The details were updated";
        return SendApiResponse.dispatch(APIDataObject.builder().error(false).data(Map.of("Message", message)).build());
    }

    @Async
    @GetMapping("/get/review/{pId}")
    public CompletableFuture<ResponseEntity<?>> getReview(@PathVariable Long pId){
        return SendApiResponse.dispatch(productService.getReview(pId));
    }

    @Async
    @GetMapping("/temp-pass")
    public CompletableFuture<ResponseEntity<?>> createRandomPassword(){

        return SendApiResponse.dispatch(customerService.randomPassword(7));

    }

    @Async
    @GetMapping("/change/password/{password}/{contactId}")
    public CompletableFuture<ResponseEntity<?>>
        ChangePassword(@PathVariable String password,
                       @PathVariable String contactId){
        /*authService.changePassword(password,contactId);*/
        userService.changePassword(password,contactId);
        return SendApiResponse.dispatch(APIDataObject.builder().error(false).data(Map.of("message",password)).build());
    }

    @Async
    @GetMapping("/validate/OTP/{username}")
    public CompletableFuture<ResponseEntity<?>> OTPVerification(@PathVariable String username){
        userService.NotValidOtp(username);
        System.out.println("User id"+username);
        int OTP=((int)(Math.floor (1000 + Math.random () * 9000)));
        userService.saveOTP(username,OTP);
        return SendApiResponse.dispatch(APIDataObject.builder().error(false).data(Map.of("otp", OTP)).build());
    }

    @Async
    @GetMapping("/validate/registration/OTP/{email}")
    public CompletableFuture<ResponseEntity<?>> regOTPVerification(@PathVariable String email){
        userService.regNotValidOtp(email);
        int OTP=((int)(Math.floor (1000 + Math.random () * 9000)));
        userService.regSaveOTP(email,OTP);
        return SendApiResponse.dispatch(APIDataObject.builder().error(false).data(Map.of("otp", OTP)).build());
    }

    @PostMapping("/login/without-verify/{username}")
    public CompletableFuture<ResponseEntity<?>> customerLoginWithoutVerify(@PathVariable String username) {
        final var token = customerService.loginWithoutVerification(username);
        return SendApiResponse.dispatch(token);
    }

    @Async
    @PostMapping("/registration/sms")
    public void sendSms(@Valid @RequestBody SmsRequest smsRequest){
        twilioSmsSender.sendSms(smsRequest);
    }
}
