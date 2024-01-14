package com.mpie.discountservice.model;

public enum DiscountType {
    PERCENTAGE, COUNT;

    public static class Values {
        public static final String PERCENTAGE = "PERCENTAGE";
        public static final String COUNT = "COUNT";
    }
}
