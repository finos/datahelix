package com.scottlogic.deg.generator.fieldspecs.whitelist;

import javax.swing.text.Element;
import java.util.Set;

public interface Whitelist<T> {

    Set<T> set();

    Set<ElementFrequency<T>> distributedSet();
}
