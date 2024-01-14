package com.mpie.discountservice.model.entity;

import com.mpie.discountservice.model.DiscountType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue(DiscountType.Values.PERCENTAGE)
@NoArgsConstructor
public class PercentageDiscountPolicy extends DiscountPolicy {

}
