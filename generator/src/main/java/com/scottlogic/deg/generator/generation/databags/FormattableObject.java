package com.scottlogic.deg.generator.generation.databags;

public class FormattableObject {

    private Object value;
    private String formatString;

    public FormattableObject() {}

    public FormattableObject(Object value){
        this.setValue(value);
    }

    public FormattableObject(Object value, String format){
        this.setValue(value);
        this.setFormatString(format);
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getFormatString() {
        return formatString;
    }

    public void setFormatString(String formatString) {
        this.formatString = formatString;
    }
}
