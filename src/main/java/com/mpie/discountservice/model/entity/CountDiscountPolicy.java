package com.mpie.discountservice.model.entity;

import com.mpie.discountservice.model.DiscountType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue(DiscountType.Values.COUNT)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CountDiscountPolicy extends DiscountPolicy {
    @Column(name = "MIN_NUMBER_ORDERED")
    private Integer minNumberOrdered;

}
