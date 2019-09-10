package com.scottlogic.deg.generator.restrictions.linear;

import com.scottlogic.deg.generator.restrictions.TypedRestrictions;

public abstract class LinearRestictions<T> implements TypedRestrictions {

    private final Limit<T> min;
    private final Limit<T> max;
    private final Granularity<T> granularity;
    private Class<T> clazz;

    LinearRestictions(Limit<T> min, Limit<T> max, Granularity<T> granularity, Class<T> clazz) {
        this.min = min;
        this.max = max;
        this.granularity = granularity;
        this.clazz = clazz;
    }


    Granularity<T> getGranularity(){
        return granularity;
    }

    @Override
    public boolean match(Object o){

        if (!isInstanceOf(o)) {
            return false;
        }

        T t = clazz.cast(o);

        if (min != null) {
            if (min.isAfter(t)) {
                return false;
            }
        }

        if (max != null) {
            if (max.isBefore(t)) {
                return false;
            }
        }

        return granularity.isCorrectScale(t);
    }

    @Override
    public boolean isInstanceOf(Object o){
        return clazz.isInstance(o);
    }

    public Limit<T> getMax() {
        return max;
    }

    public Limit<T> getMin() {
        return min;
    }
}
