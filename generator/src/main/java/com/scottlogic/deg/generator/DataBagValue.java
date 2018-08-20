package com.scottlogic.deg.generator;

public class DataBagValue {
    public final Object value;
    public final String format;

    public DataBagValue(Object value, String format){
        this.value = value;
        this.format = format;
    }

    public DataBagValue(Object value){
        this.value = value;
        this.format = null;
    }
}
