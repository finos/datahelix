package com.scottlogic.deg.generator.fieldspecs;

import java.util.Collection;
import java.util.Optional;

public class RowSpecMerger {
    private final FieldSpecMerger fieldSpecMerger;

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
