package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.restrictions.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.scottlogic.deg.generator.restrictions.DateTimeRestrictions.isDateTime;
import static com.scottlogic.deg.generator.restrictions.NumericRestrictions.isNumeric;
import static com.scottlogic.deg.generator.restrictions.StringRestrictions.isString;

public class SetRestrictionsMergeOperation implements RestrictionMergeOperation {
    private static final SetRestrictionsMerger setRestrictionsMerger = new SetRestrictionsMerger();

    @Override
    public Optional<FieldSpec> applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merging) {
        MergeResult<SetRestrictions> mergeResult =
            setRestrictionsMerger.merge(left.getSetRestrictions(), right.getSetRestrictions());

        if (!mergeResult.successful){
            return Optional.of(merging.withSetRestrictions(
                SetRestrictions.fromWhitelist(Collections.emptySet()),
                FieldSpecSource.fromFieldSpecs(left, right)));
        }

        SetRestrictions setRestrictions = mergeResult.restrictions;

        // Filter the set to match any new restrictions
        if (setRestrictions != null &&
            setRestrictions.getWhitelist() != null &&
            !setRestrictions.getWhitelist().isEmpty()) {

            Stream<?> filterStream = setRestrictions.getWhitelist().stream();
            TypeRestrictions typeRestrictions = merging.getTypeRestrictions();

            if (!typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.NUMERIC)) {
                filterStream = filterStream.filter(x -> !isNumeric(x));
            }

            if (!typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.STRING)) {
                filterStream = filterStream.filter(x -> !isString(x));
            }

            if (!typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.DATETIME)) {
                filterStream = filterStream.filter(x -> !isDateTime(x));
            }

            if(merging.getStringRestrictions() != null){
                filterStream = filterStream.filter(x -> !isString(x) || merging.getStringRestrictions().match(x));
            }

            if(merging.getNumericRestrictions() != null){
                filterStream = filterStream.filter(x -> !isNumeric(x) || merging.getNumericRestrictions().match(x));
            }

            if(merging.getDateTimeRestrictions() != null){
                filterStream = filterStream.filter(x -> !isDateTime(x) || merging.getDateTimeRestrictions().match(x));
            }

            GranularityRestrictions granularityRestrictions = merging.getGranularityRestrictions();
            if (granularityRestrictions != null) {
                filterStream = filterStream.filter(x -> !isNumeric(x) || GranularityRestrictions.isCorrectScale((Number)x, granularityRestrictions.getNumericScale()));
            }

            Set<Object> whitelist = filterStream.collect(Collectors.toCollection(HashSet::new));
            SetRestrictions newSetRestrictions = new SetRestrictions(whitelist,
                setRestrictions.getBlacklist());

            setRestrictions = newSetRestrictions;
        }

        return Optional.of(merging.withSetRestrictions(
            setRestrictions,
            FieldSpecSource.fromFieldSpecs(left, right)));
    }
}

