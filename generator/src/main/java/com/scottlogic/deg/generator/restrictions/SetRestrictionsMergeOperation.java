package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.constraints.IsOfTypeConstraint;

import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SetRestrictionsMergeOperation implements RestrictionMergeOperation {
    private static final SetRestrictionsMerger setRestrictionsMerger = new SetRestrictionsMerger();

    @Override
    public Optional<FieldSpec> applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merged) {
        MergeResult<SetRestrictions> mergeResult =
            setRestrictionsMerger.merge(left.getSetRestrictions(), right.getSetRestrictions());

        if (!mergeResult.successful){
            return Optional.empty();
        }

        SetRestrictions setRestrictions = mergeResult.restrictions;

        // Filter the set to match any new restrictions
        if (setRestrictions != null &&
            setRestrictions.getWhitelist() != null &&
            !setRestrictions.getWhitelist().isEmpty()) {

            Stream<?> filterStream = setRestrictions.getWhitelist().stream();
            TypeRestrictions typeRestrictions = merged.getTypeRestrictions();

            if (!typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.NUMERIC)) {
                filterStream = filterStream.filter(x -> !NumericRestrictions.isNumeric(x));
            }

            if (!typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.STRING)) {
                filterStream = filterStream.filter(x -> !StringRestrictions.isString(x));
            }

            if (!typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.TEMPORAL)) {
                filterStream = filterStream.filter(x -> !DateTimeRestrictions.isDateTime(x));
            }

            StringRestrictions stringRestrictions = merged.getStringRestrictions();
            if(stringRestrictions != null){
                filterStream = filterStream.filter(x -> stringRestrictions.match(x));
            }

            NumericRestrictions numberRestrictions = merged.getNumericRestrictions();
            if(numberRestrictions != null){
                filterStream = filterStream.filter(x -> numberRestrictions.match(x));
            }

            DateTimeRestrictions dateTimeRestrictions = merged.getDateTimeRestrictions();
            if(dateTimeRestrictions != null){
                filterStream = filterStream.filter(x -> dateTimeRestrictions.match(x));
            }

            setRestrictions = new SetRestrictions(filterStream.collect(Collectors.toCollection(HashSet::new)),
                setRestrictions.getBlacklist());
        }

        return Optional.of(merged.withSetRestrictions(
            setRestrictions,
            FieldSpecSource.fromFieldSpecs(left, right)));
    }
}

