package com.jishop.option.domain.embed;

public enum CategoryType {
    FASHION_CLOTHING("패션의류"),
    FASHION_ACCESSORIES("패션잡화"),
    DIGITAL_APPLIANCES("디지털/가전"),
    FURNITURE_INTERIOR("가구/인테리어"),
    FOOD("식품");

    private final String label;

    CategoryType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static CategoryType fromLabel(String label) {
        for (CategoryType type : values()) {
            if (type.label.equals(label)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown category type: " + label);
    }
}