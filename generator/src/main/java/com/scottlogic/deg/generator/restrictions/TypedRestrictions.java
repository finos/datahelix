package com.scottlogic.deg.generator.restrictions;

public interface TypedRestrictions extends Restrictions {

    boolean match(Object o);

    boolean isInstanceOf(Object o);
}
