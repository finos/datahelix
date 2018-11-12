package com.scottlogic.deg.generator.walker.builder;

import com.scottlogic.deg.generator.walker.routes.RowSpecRoute;

import java.util.Iterator;
import java.util.List;

public interface IDecisionIterator extends Iterator<List<RowSpecRoute>> {
    @Override
    boolean hasNext();

    @Override
    List<RowSpecRoute> next();

    void reset();
}
