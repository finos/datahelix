package com.scottlogic.deg.generator.walker.factory;

import com.scottlogic.deg.generator.walker.routes.RowSpecRoute;

import java.util.Collection;
import java.util.Iterator;

public interface DecisionIterator extends Iterator<Collection<RowSpecRoute>> {
    void reset();
}
