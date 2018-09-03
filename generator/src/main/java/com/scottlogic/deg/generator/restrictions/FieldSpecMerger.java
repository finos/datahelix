package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.constraints.IsOfTypeConstraint;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Returns a FieldSpec that permits only data permitted by all of its inputs
 */
public class FieldSpecMerger {
    private final SetRestrictionsMerger setRestrictionsMerger = new SetRestrictionsMerger();
    private final NumericRestrictionsMerger numericRestrictionsMerger = new NumericRestrictionsMerger();
    private final StringRestrictionsMerger stringRestrictionsMerger = new StringRestrictionsMerger();
    private final NullRestrictionsMerger nullRestrictionsMerger = new NullRestrictionsMerger();
    private final TypeRestrictionsMerger typeRestrictionsMerger = new TypeRestrictionsMerger();
    private final DateTimeRestrictionsMerger dateTimeRestrictionsMerger = new DateTimeRestrictionsMerger();
    private final FormatRestrictionsMerger formatRestrictionMerger = new FormatRestrictionsMerger();

    /**
     * Null parameters are permitted, and are synonymous with an empty FieldSpec
     * <p>
     * Returning an empty Optional conveys that the fields were unmergeable.
     */
    public Optional<FieldSpec> merge(FieldSpec left, FieldSpec right) {
        if (left == null && right == null) {
            return Optional.of(new FieldSpec());
        }
        if (left == null) {
            return Optional.of(right);
        }
        if (right == null) {
            return Optional.of(left);
        }
        final FieldSpec merged = new FieldSpec();
        try {

            TypeRestrictions typeRestrictions = typeRestrictionsMerger.merge(
                    left.getTypeRestrictions(), right.getTypeRestrictions());

            if (typeRestrictions == null) {
                typeRestrictions = TypeRestrictions.createAllowAll();
            }

            SetRestrictions setRestrictions =
                    setRestrictionsMerger.merge(left.getSetRestrictions(), right.getSetRestrictions());

            // Strings
            StringRestrictions stringRestrictions = stringRestrictionsMerger.merge(
                    left.getStringRestrictions(), right.getStringRestrictions());
            merged.setStringRestrictions(stringRestrictions);

            if (stringRestrictions != null) {

                if (typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.String)) {
                    typeRestrictions.allowedTypes.retainAll(Collections.singleton(IsOfTypeConstraint.Types.String));
                } else {
                    throw new UnmergeableRestrictionException();
                }

                if(setRestrictions != null) {
                    setRestrictions.whitelist = filter(setRestrictions.whitelist, x -> stringRestrictions.match(x));
                }
            }

            // Numeric
            NumericRestrictions numberRestrictions = numericRestrictionsMerger.merge(
                    left.getNumericRestrictions(), right.getNumericRestrictions());
            merged.setNumericRestrictions(numberRestrictions);

            if (numberRestrictions != null) {

                if (typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.Numeric)) {
                    typeRestrictions.allowedTypes.retainAll(Collections.singleton(IsOfTypeConstraint.Types.Numeric));
                } else {
                    throw new UnmergeableRestrictionException();
                }

                if(setRestrictions != null) {
                    setRestrictions.whitelist = filter(setRestrictions.whitelist, x -> numberRestrictions.match(x));
                }
            }

            // Temporal (Dates and times)
            DateTimeRestrictions dateTimeRestrictions = dateTimeRestrictionsMerger.merge(
                    left.getDateTimeRestrictions(), right.getDateTimeRestrictions());
            merged.setDateTimeRestrictions(dateTimeRestrictions);

            if (dateTimeRestrictions != null) {

                if (typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.Temporal)) {
                    typeRestrictions.allowedTypes.retainAll(Collections.singleton(IsOfTypeConstraint.Types.Temporal));
                } else {
                    throw new UnmergeableRestrictionException();
                }

//                if(setRestrictions != null) {
//                    setRestrictions.whitelist = filter(setRestrictions.whitelist, x -> !dateTimeRestrictions.matches(x));
//                }
            }

            // Filter the set to match any new restrictions
            if (setRestrictions != null && setRestrictions.whitelist != null && !setRestrictions.whitelist.isEmpty()) {
                Stream<?> filterStream = setRestrictions.whitelist.stream();

                if (!typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.Numeric)) {
                    filterStream = filterStream.filter(x -> !NumericRestrictions.defaultMatcher(x));
                }

                if (!typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.String)) {
                    filterStream = filterStream.filter(x -> !StringRestrictions.defaultMatcher(x));
                }

                // todo: temporal

                setRestrictions.whitelist = filterStream.collect(Collectors.toCollection(HashSet::new));
            }

            merged.setNullRestrictions(
                    nullRestrictionsMerger.merge(left.getNullRestrictions(), right.getNullRestrictions()));

            merged.setSetRestrictions(setRestrictions);

            merged.setFormatRestrictions(
                    formatRestrictionMerger.merge(left.getFormatRestrictions(), right.getFormatRestrictions()));

            // If one or more restrictions have provided a type (e.g. less than) add it as a restriction

            merged.setTypeRestrictions(typeRestrictions);

        } catch (UnmergeableRestrictionException e) {
            return Optional.empty();
        }
        return Optional.of(merged);
    }


    private <T> Set<T> filter(Set<T> source, Predicate<? super T> predicate) {
        return source
                .stream()
                .filter(predicate)
                .collect(Collectors.toCollection(HashSet::new));
    }


}
