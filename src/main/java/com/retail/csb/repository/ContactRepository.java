package com.retail.csb.repository;

import com.retail.csb.model.customer.Contact;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ContactRepository extends PagingAndSortingRepository<Contact, Integer> {

    public Contact findByEmail(final String email);
    public Optional<Contact> findById(Integer id);

}
