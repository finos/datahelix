package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.walker.routes.RowSpecRoute;

import java.util.Iterator;

public interface IConstraintIterator extends Iterator<RowSpecRoute> {
    @Override
    boolean hasNext();

    @Override
    RowSpecRoute next();

    void reset();
}
