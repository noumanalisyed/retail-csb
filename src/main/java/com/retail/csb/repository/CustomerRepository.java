package com.retail.csb.repository;

import com.retail.csb.model.customer.CustomerAuth;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CustomerRepository extends PagingAndSortingRepository<CustomerAuth, Integer> {

    // todo add where clause for business specific users
    @Query(value = "SELECT contacts.*, customer_auth.username FROM contacts c INNER JOIN customer_auth ca ON ca.contact_id=c.id", nativeQuery = true)
    public Iterable<CustomerAuth> getAllCustomers();

    public CustomerAuth findByUsername(final String username);

    public CustomerAuth findByContactId(final Integer contactId);

}
