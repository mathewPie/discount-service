package com.mpie.discountservice.model.entity;

import com.mpie.discountservice.model.DiscountType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DISCOUNT_TYPE", discriminatorType = DiscriminatorType.STRING)
public class DiscountPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "PRODUCT_ID")
    private String productId;
    @Enumerated(EnumType.STRING)
    @Column(name = "DISCOUNT_TYPE", insertable = false, updatable = false)
    private DiscountType discountType;
    @Column(name = "DISCOUNT_VALUE")
    private Double discountValue;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscountPolicy that = (DiscountPolicy) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash();
    }

}
