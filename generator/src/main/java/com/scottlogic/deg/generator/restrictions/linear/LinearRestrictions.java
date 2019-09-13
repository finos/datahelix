package com.scottlogic.deg.generator.restrictions.linear;

import com.scottlogic.deg.generator.restrictions.TypedRestrictions;

import java.math.BigDecimal;

public class LinearRestrictions<T> implements TypedRestrictions {

    private final Limit<T> min;
    private final Limit<T> max;
    private final Granularity<T> granularity;
    private final Converter<T> converter;

    public LinearRestrictions(Limit<T> min, Limit<T> max, Granularity<T> granularity, Converter<T> converter) {
        this.min = min;
        this.max = max;
        this.granularity = granularity;
        this.converter = converter;
    }

    @Override
    public boolean match(Object o){

        if (!isInstanceOf(o)) {
            return false;
        }

        T t = converter.convert(o);

        if (min != null) {
            if (!min.isBefore(t)) {
                return false;
            }
        }

        if (max != null) {
            if (!max.isAfter(t)) {
                return false;
            }
        }

        return granularity.isCorrectScale(t);
    }

    @Override
    public boolean isInstanceOf(Object o){
        return converter.isCorrectType(o);
    }

    public Limit<T> getMax() {
        return max;
    }

    public Limit<T> getMin() {
        return min;
    }

    public Converter<T> getConverter(){
        return converter;
    }

    public Granularity<T> getGranularity(){
        return granularity;
    }
}
