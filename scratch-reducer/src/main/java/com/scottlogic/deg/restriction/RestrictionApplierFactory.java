package com.scottlogic.deg.restriction;

public class RestrictionApplierFactory {
    final FieldRestrictionClassifier fieldRestrictionClassifier = new FieldRestrictionClassifier();

    public IRestrictionApplier getRestrictionApplier(IFieldRestriction fieldRestriction) {
        final var restrictionType = fieldRestrictionClassifier.classify(fieldRestriction);
        switch (restrictionType) {
            case Numeric:
                return new NumericRestrictionApplier();
            case String:
                return new StringRestrictionApplier();
            default:
                throw new IllegalStateException();
        }
    }
}
