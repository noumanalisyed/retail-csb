package com.retail.csb.service;

import com.retail.csb.model.product.RegistrationOtp;
import com.retail.csb.common.api.APIDataObject;
import com.retail.csb.model.User;
import com.retail.csb.model.customer.Contact;
import com.retail.csb.model.product.Otp;
import com.retail.csb.repository.*;
import com.retail.csb.security.BCryptPasswordUtils;
import com.retail.csb.security.JWTUtils;

import com.retail.csb.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    OtpRepository otpRepository;

    @Autowired
    RegistrationOtpRepository registrationOtpRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BCryptPasswordUtils bCryptPasswordUtils;

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public APIDataObject getAUser(final String userToken) {
        try {
            var userDetail = JWTUtils.getUserFromToken(userToken);
            return APIDataObject.builder().error(false)
                    .data(userRepository.findById(userDetail.getUserId()).orElseThrow(Exception::new)).build();
        } catch (final Exception e) {
            log.error("{}", e.getMessage());
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

    public Contact fetchContact(String email){
        System.out.println(email);
        return contactRepository.findByEmail(email);
    }

    public APIDataObject fetchContact2(String id){
        try {
            return APIDataObject.builder().error(false)
                .data(contactRepository.findById(Integer.parseInt(id))).build();

        }catch (final Exception e){
            log.error("{}", e.getMessage());
            return APIDataObject.builder().error(true).data("No account registered against provided contactId").build();
        }
    }

    public void saveUpdates(Contact user){
         contactRepository.save(user);
    }

    public APIDataObject usernameRegistered(String username){
        try {
            return APIDataObject.builder().error(false)
                .data(customerRepository.findByUsername(username)).build();

        }catch (final Exception e){
            log.error("{}", e.getMessage());
            return APIDataObject.builder().error(true).data("No account registered against provided username").build();
        }

    }

    public APIDataObject findUsername(Integer contactId){
        try {
            return APIDataObject.builder().error(false)
                .data(customerRepository.findByContactId(contactId)).build();

        }catch (final Exception e){
            log.error("{}", e.getMessage());
            return APIDataObject.builder().error(true).data("No account registered against provided contactId").build();
        }

    }

    public void NotValidOtp(String Username){

        int id=Integer.parseInt(Username);
        List<Otp> list=new ArrayList<>();

        list=otpRepository.findAll();

        System.out.println("List of otp "+list);
        for (int i = 0; i < list.size(); i++) {
            System.out.println("Vlaid Value"+list.get(i).getValid());
            if(list.get(i).getValid() && list.get(i).getContacts().getId()==id)
            {
                boolean valid =false;
                list.get(i).setValid(valid);
            }
            otpRepository.save(list.get(i));
            System.out.println("List after change"+list.get(i).getValid());
        }
        /*for (int i = 0; i < list.size(); i++) {
            otpRepository.save(list.get(i));
        }*/


    }

    public void regNotValidOtp(String Username){

        String email= Username;
        List<RegistrationOtp> list=new ArrayList<>();

        list=registrationOtpRepository.findAll();

        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getValid() && Objects.equals(list.get(i).getEmail(), email))
            {
                boolean valid =false;
                list.get(i).setValid(valid);
            }
            registrationOtpRepository.save(list.get(i));
        }
        /*for (int i = 0; i < list.size(); i++) {
            otpRepository.save(list.get(i));
        }*/


    }

    public void saveOTP(String Username,int OTP1){

        Contact contact=contactRepository.findById(Integer.valueOf(Username)).get();
        Otp otp=new Otp();
        otp.setContacts(contact);
        otp.setCode(String.valueOf(OTP1));
        otp.setValid(true);
        otpRepository.save(otp);
    }

    public void regSaveOTP(String email,int OTP1){

        RegistrationOtp otp=new RegistrationOtp();
        otp.setEmail(email);
        otp.setCode(String.valueOf(OTP1));
        otp.setValid(true);
        registrationOtpRepository.save(otp);
    }
    /*public APIDataObject singUp(User user) {
        try {
            // * Set the timestamp properties
            setTimeStampAndDates(user);

            // * Hash the password and overwrite the property
            final var hashedPassword = bCryptPasswordUtils.encoder().encode(user.getPassword());
            user.setPassword(hashedPassword);
            return APIDataObject.builder().error(false).data(userRepository.save(user)).build();
        } catch (final Exception e) {
            return APIDataObject.builder().error(true).data(e.getLocalizedMessage()).build();
        }
    }*/


    public void changePassword(String Password, String contactId){

        Integer id=Integer.parseInt(contactId);
        final var hashedPassword= bCryptPasswordUtils.encoder().encode(Password);
        final var customer=customerRepository.findByContactId(id);
        customer.setPassword(hashedPassword);
        customerRepository.save(customer);

    }

    public boolean comparePassword(String Password, String contactId){
        boolean flag;
        Integer id=Integer.parseInt(contactId);
        final var customer=customerRepository.findByContactId(id);
        flag = BCryptPasswordUtils.verifyPassword(Password,customer.getPassword());
        return flag;
    }

    public void changeUsername(String username, String contactId){

        Integer id=Integer.parseInt(contactId);
        final var customer=customerRepository.findByContactId(id);
        customer.setUsername(username);
        customerRepository.save(customer);
    }

    /*public APIDataObject UpdatePassword(String password){
        try{
            return APIDataObject.builder().error(false).data(contactRepository.save())
        }
    }*/
}
