package com.scottlogic.deg.generator.restrictions;

public interface Restrictions {

    boolean isInstanceOf(Object o);

    boolean match(Object o);

}
