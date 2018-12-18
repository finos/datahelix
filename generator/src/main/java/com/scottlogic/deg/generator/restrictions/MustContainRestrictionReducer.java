package com.scottlogic.deg.generator.restrictions;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class MustContainRestrictionReducer {
    private final FieldSpecMerger merger = new FieldSpecMerger();

    public Set<FieldSpec> getReducedMustContainRestriction(FieldSpec parentFieldSpec) {
        final MustContainRestriction mustContainRestriction = parentFieldSpec.getMustContainRestriction();

        Set<FieldSpec> finalFieldSpecs = new HashSet<>();
        for (FieldSpec spec : mustContainRestriction.getRequiredObjects()) {
            Set<FieldSpec> requiredObjectsExcludingCurrentSpec = new HashSet<>(mustContainRestriction.getRequiredObjects());
            requiredObjectsExcludingCurrentSpec.remove(spec);

            // If two field specs contain the same type of restriction then attempt to merge them into one so that related
            // restrictions will make sense (e.g a range of numbers)
            for (FieldSpec specFromRemainingRequiredObjects : requiredObjectsExcludingCurrentSpec) {
                if (spec.getNumericRestrictions() != null && specFromRemainingRequiredObjects.getNumericRestrictions() != null ||
                    spec.getStringRestrictions() != null && specFromRemainingRequiredObjects.getStringRestrictions() != null ||
                    spec.getNullRestrictions() != null && specFromRemainingRequiredObjects.getNullRestrictions() != null ||
                    spec.getTypeRestrictions() != null && specFromRemainingRequiredObjects.getTypeRestrictions() != null ||
                    spec.getDateTimeRestrictions() != null && specFromRemainingRequiredObjects.getDateTimeRestrictions() != null ||
                    spec.getFormatRestrictions() != null && specFromRemainingRequiredObjects.getFormatRestrictions() != null ||
                    spec.getGranularityRestrictions() != null && specFromRemainingRequiredObjects.getGranularityRestrictions() != null) {

                    final Optional<FieldSpec> mergeResult = merger.merge(spec, specFromRemainingRequiredObjects);

                    if (mergeResult.isPresent()) {
                        FieldSpec mergedFieldSpec = mergeResult.get();

                        // Ensure that we are not adding an existing field spec
                        boolean isInFinalFieldSpec = false;
                        for (FieldSpec finalFieldSpec : finalFieldSpecs) {
                            isInFinalFieldSpec = finalFieldSpec.equals(mergedFieldSpec);
                            if (isInFinalFieldSpec) {
                                break;
                            }
                        }

                        if (!isInFinalFieldSpec) {
                            finalFieldSpecs.add(mergedFieldSpec);
                        }
                    }
                }
                else {
                    finalFieldSpecs.add(specFromRemainingRequiredObjects);
                }
            }
        }

        return finalFieldSpecs;
    }
}
