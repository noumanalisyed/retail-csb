package com.retail.csb.service;

import java.sql.Timestamp;

import com.retail.csb.common.api.APIDataObject;
import com.retail.csb.model.User;
import com.retail.csb.model.enumeration.UserStatus;
import com.retail.csb.model.vm.JWTToken;
import com.retail.csb.model.vm.UserLogin;
import com.retail.csb.repository.UserRepository;
import com.retail.csb.security.BCryptPasswordUtils;
import com.retail.csb.security.JWTUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JWTAuthService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordUtils bCryptPasswordUtils;

    public APIDataObject singUp(User user) {
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
    }

    public APIDataObject login(final UserLogin user) {
        log.info("{}", user);
        try {
            final var userDetails = userRepository.findByUsername(user.getUsername());
            final var isUserValid = BCryptPasswordUtils.verifyPassword(user.getPassword(), userDetails.getPassword());
            // * Check if the password is valid and user is active
            // * if not valid or not active then throw exception
            if (!isUserValid || !userDetails.getStatus().equals(UserStatus.active)) {
                throw new Exception();
            }
            return APIDataObject.builder().error(false)
                .data(JWTToken.builder().token(JWTUtils.createJWT(userDetails)).build()).build();
        } catch (final Exception e) {
            log.error("{}", e);
            return APIDataObject.builder().error(true).data("Invalid user details.").build();
        }
    }
    private void setTimeStampAndDates(final User user) {
        final var timestamp = new Timestamp(System.currentTimeMillis());
        user.setCreatedAt(timestamp);
        user.setUpdatedAt(timestamp);
    }
}
