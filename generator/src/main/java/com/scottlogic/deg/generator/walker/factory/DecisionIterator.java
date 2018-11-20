package com.scottlogic.deg.generator.walker.factory;

import com.scottlogic.deg.generator.walker.routes.RowSpecRoute;

import java.util.Iterator;
import java.util.List;

public interface DecisionIterator extends Iterator<List<RowSpecRoute>> {
    void reset();
}
