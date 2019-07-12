package com.scottlogic.deg.generator.fieldspecs.whitelist;

import java.util.Set;

public interface Whitelist<T> {

    Set<T> set();

    Set<ElementFrequency<T>> distributedSet();
}
