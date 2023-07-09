package com.retail.csb.repository;

import com.retail.csb.model.product.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OtpRepository extends JpaRepository<Otp, Integer>, JpaSpecificationExecutor<Otp> {

}
