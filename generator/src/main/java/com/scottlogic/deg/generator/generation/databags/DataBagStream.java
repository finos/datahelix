package com.scottlogic.deg.generator.generation.databags;

import java.util.Iterator;
import java.util.stream.*;

public class DataBagStream {
    private final boolean unique;
    private final Stream<DataBag> stream;

    public DataBagStream(Stream<DataBag> stream, boolean unique) {
        this.unique = unique;
        this.stream = stream;
    }

    public boolean isUnique() {
        return unique;
    };

    public Iterator<DataBag> toIterator() {
        return stream.iterator();
    }

    public Stream<DataBag> stream () {
        return stream;
    }
}
