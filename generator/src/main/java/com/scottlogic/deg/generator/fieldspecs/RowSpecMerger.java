package com.scottlogic.deg.generator.fieldspecs;

import com.google.inject.Inject;

import java.util.Collection;
import java.util.Optional;

public class RowSpecMerger {
    private final FieldSpecMerger fieldSpecMerger;

    @Inject
    public RowSpecMerger(FieldSpecMerger fieldSpecMerger) {
        this.fieldSpecMerger = fieldSpecMerger;
    }

    public Optional<RowSpec> merge(Collection<RowSpec> rowSpecs) {
        return RowSpec.merge(
                fieldSpecMerger,
                rowSpecs
        );
    }
}
