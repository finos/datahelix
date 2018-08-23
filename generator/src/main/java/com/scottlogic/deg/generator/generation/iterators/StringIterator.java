package com.scottlogic.deg.generator.generation.iterators;

import com.scottlogic.deg.generator.utils.IStringGenerator;
import com.scottlogic.deg.generator.utils.StringGenerator;

import java.util.*;

public class StringIterator implements IFieldSpecIterator {

    IStringGenerator stringGenerator;
    Iterator<String> iterator;

    public StringIterator(IStringGenerator stringGenerator, Set<Object> blacklist) {

        if (blacklist != null && blacklist.size() > 0) {
            StringGenerator blacklistGenerator = StringGenerator.createFromBlacklist(blacklist);

            this.stringGenerator = stringGenerator.intersect(blacklistGenerator);
        } else {
            this.stringGenerator = stringGenerator;
        }

        this.iterator = this.stringGenerator.generateAllValues();
    }

    @Override
    public boolean hasNext() {
        return this.iterator.hasNext();
    }

    @Override
    public Object next() {
        return this.iterator.next();
    }

    @Override
    public boolean isInfinite() {
        return !stringGenerator.IsFinite();
    }


}
