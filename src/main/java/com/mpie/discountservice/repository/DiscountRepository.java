package com.mpie.discountservice.repository;

import com.mpie.discountservice.model.entity.DiscountPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscountRepository extends JpaRepository<DiscountPolicy, Long> {

    List<DiscountPolicy> findByProductId(String productId);

}