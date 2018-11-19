package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.constraints.IsOfTypeConstraint;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SetRestrictionsMergeOperation implements RestrictionMergeOperation {
    private static final SetRestrictionsMerger setRestrictionsMerger = new SetRestrictionsMerger();

    @Override
    public boolean applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merged) {
        MergeResult<SetRestrictions> mergeResult =
            setRestrictionsMerger.merge(left.getSetRestrictions(), right.getSetRestrictions());

        if (!mergeResult.successful){
            return false;
        }

        SetRestrictions setRestrictions = mergeResult.restrictions;

        // Filter the set to match any new restrictions
        if (setRestrictions != null &&
            setRestrictions.getWhitelist() != null &&
            !setRestrictions.getWhitelist().isEmpty()) {

            Stream<?> filterStream = setRestrictions.getWhitelist().stream();
            TypeRestrictions typeRestrictions = merged.getTypeRestrictions();

            if (!typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.Numeric)) {
                filterStream = filterStream.filter(x -> !NumericRestrictions.isNumeric(x));
            }

            if (!typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.String)) {
                filterStream = filterStream.filter(x -> !StringRestrictions.isString(x));
            }

            if (!typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.Temporal)) {
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

        merged.setSetRestrictions(setRestrictions);
        return true;
    }

    private <T> Set<T> filter(Set<T> source, Predicate<? super T> predicate) {
        return source
            .stream()
            .filter(predicate)
            .collect(Collectors.toCollection(HashSet::new));
    }
}

