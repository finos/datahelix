package com.scottlogic.deg.generator.fieldspecs.whitelist;

import com.scottlogic.deg.generator.utils.RandomNumberGenerator;

import java.util.Set;
import java.util.stream.Stream;

public interface Whitelist<T> {

    Set<T> set();

    Set<ElementFrequency<T>> distributedSet();

    Stream<T> generate(RandomNumberGenerator source);
}
