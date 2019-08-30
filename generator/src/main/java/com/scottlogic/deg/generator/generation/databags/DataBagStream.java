package com.scottlogic.deg.generator.generation.databags;

import java.util.Iterator;
import java.util.stream.*;

public class DataBagStream {
    private final Stream<DataBag> stream;

    public DataBagStream(Stream<DataBag> stream) {
        this.stream = stream;
    }

    public Iterator<DataBag> toIterator() {
        return stream.iterator();
    }

    public Stream<DataBag> stream () {
        return stream;
    }
}
