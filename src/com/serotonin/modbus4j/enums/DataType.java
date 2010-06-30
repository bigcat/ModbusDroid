package com.serotonin.modbus4j.enums;

import java.math.BigInteger;

/**
 * @deprecated use com.serotonin.modbus4j.code.DataType instead
 * @author Matthew Lohbihler
 */
public enum DataType {
    BINARY (1, Boolean.class),
    
    TWO_BYTE_INT_UNSIGNED (1, Integer.class),
    TWO_BYTE_INT_SIGNED (1, Short.class),
    
    FOUR_BYTE_INT_UNSIGNED (2, Long.class),
    FOUR_BYTE_INT_SIGNED (2, Integer.class),
    FOUR_BYTE_INT_UNSIGNED_SWAPPED (2, Long.class),
    FOUR_BYTE_INT_SIGNED_SWAPPED (2, Integer.class),
    FOUR_BYTE_FLOAT (2, Float.class),
    FOUR_BYTE_FLOAT_SWAPPED (2, Float.class),
    
    EIGHT_BYTE_INT_UNSIGNED (4, BigInteger.class),
    EIGHT_BYTE_INT_SIGNED (4, Long.class),
    EIGHT_BYTE_INT_UNSIGNED_SWAPPED (4, BigInteger.class),
    EIGHT_BYTE_INT_SIGNED_SWAPPED (4, Long.class),
    EIGHT_BYTE_FLOAT (4, Double.class),
    EIGHT_BYTE_FLOAT_SWAPPED (4, Double.class);
    
    private int registerCount;
    private Class<?> javaType;
    
    DataType(int registerCount, Class<?> javaType) {
        this.registerCount = registerCount;
        this.javaType = javaType;
    }

    public int getRegisterCount() {
        return registerCount;
    }

    public Class<?> getJavaType() {
        return javaType;
    }
}
