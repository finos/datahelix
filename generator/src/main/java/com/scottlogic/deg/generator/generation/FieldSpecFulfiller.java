package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.DataBagValue;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.IsOfTypeConstraint;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.generation.databags.IDataBagSource;
import com.scottlogic.deg.generator.generation.field_value_sources.*;
import com.scottlogic.deg.generator.restrictions.*;
import com.scottlogic.deg.generator.utils.JavaUtilRandomNumberGenerator;
import com.scottlogic.deg.generator.utils.ProjectingIterable;

import java.util.*;

public class FieldSpecFulfiller implements IDataBagSource {
    private final Field field;
    private final FieldSpec spec;

    public FieldSpecFulfiller(Field field, FieldSpec spec) {
        this.field = field;
        this.spec = spec;
    }

    @Override
    public Iterable<DataBag> generate(GenerationConfig generationConfig) {
        List<IFieldValueSource> fieldValueSources = getAllApplicableValueSources();

        IFieldValueSource combinedFieldValueSource = new CombiningFieldValueSource(fieldValueSources);

        return new ProjectingIterable<>(
                getDataValues(combinedFieldValueSource, generationConfig.getDataGenerationType()),
                value ->
                {
                    DataBagValue dataBagValue = new DataBagValue(
                            value,
                            this.spec.getFormatRestrictions() != null
                                    ? this.spec.getFormatRestrictions().formatString
                                    : null);

                    return DataBag.startBuilding()
                            .set(
                                    this.field,
                                    dataBagValue)
                            .build();
                });
    }

    private List<IFieldValueSource> getAllApplicableValueSources() {
        List<IFieldValueSource> validSources = new ArrayList<>();

        // check nullability...
        if (determineNullabilityAndDecideWhetherToHalt(validSources))
            return validSources;

        // if there's a whitelist, we can just output that
        if (spec.getSetRestrictions() != null) {
            Set<?> whitelist = spec.getSetRestrictions().getWhitelist();
            if (whitelist != null) {
                return Collections.singletonList(
                    new CannedValuesFieldValueSource(
                        new ArrayList<>(whitelist)));
            }
        }

        TypeRestrictions typeRestrictions = spec.getTypeRestrictions() != null
                ? spec.getTypeRestrictions()
                : TypeRestrictions.all;

        if (typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.Numeric)) {
            NumericRestrictions restrictions = spec.getNumericRestrictions() == null
                ? new NumericRestrictions()
                : spec.getNumericRestrictions();

            int numericScale = spec.getGranularityRestrictions() != null
                ? spec.getGranularityRestrictions().getNumericScale()
                : 0;

            if (numericScale == 0) {
                validSources.add(
                    new IntegerFieldValueSource(
                        restrictions,
                        getBlacklist()));
            } else {
                validSources.add(
                    new RealNumberFieldValueSource(
                        restrictions,
                        getBlacklist(),
                        numericScale));
            }
        }

        if (typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.String)) {

            StringRestrictions stringRestrictions = spec.getStringRestrictions();
            if (stringRestrictions != null && (stringRestrictions.stringGenerator != null)) {
                Set<Object> blacklist = getBlacklist();

                final IStringGenerator generator;
                if (blacklist.size() > 0) {
                    RegexStringGenerator blacklistGenerator = RegexStringGenerator.createFromBlacklist(blacklist);

                    generator = stringRestrictions.stringGenerator.intersect(blacklistGenerator);
                } else {
                    generator = stringRestrictions.stringGenerator;
                }

                validSources.add(generator.asFieldValueSource());

            } else {
                // todo: move default interesting values into the string field value source
                validSources.add(CannedValuesFieldValueSource.of("Lorem Ipsum"));
            }
        }

        if (typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.Temporal)) {

            DateTimeRestrictions restrictions = spec.getDateTimeRestrictions();
            validSources.add(new TemporalFieldValueSource(
                    restrictions != null ? restrictions : new DateTimeRestrictions(),
                    getBlacklist()));
        }

        return validSources;
    }

    private Iterable<Object> getDataValues(IFieldValueSource source, GenerationConfig.DataGenerationType dataType) {
        switch (dataType) {
            case FullSequential:
            default:
                return source.generateAllValues();
            case Interesting:
                return source.generateInterestingValues();
            case Random:
                return source.generateRandomValues(new JavaUtilRandomNumberGenerator(0));
        }
    }

    private boolean determineNullabilityAndDecideWhetherToHalt(List<IFieldValueSource> fieldValueSources) {
        IFieldValueSource nullOnlySource = new CannedValuesFieldValueSource(Collections.singletonList(null));

        if (spec.getNullRestrictions() != null) {
            if (spec.getNullRestrictions().nullness == NullRestrictions.Nullness.MustBeNull) {
                // if *always* null, add a null-only source and signal that no other sources are needed
                fieldValueSources.add(nullOnlySource);
                return true;
            } else if (spec.getNullRestrictions().nullness == NullRestrictions.Nullness.MustNotBeNull) {
                // if *never* null, add nothing and signal that source generation should continue
                return false;
            }
        }

        // if none of the above, the field is nullable
        fieldValueSources.add(nullOnlySource);
        return false;
    }

    private Set<Object> getBlacklist() {
        if (spec.getSetRestrictions() == null)
            return Collections.emptySet();

        return new HashSet<>(spec.getSetRestrictions().getBlacklist());
    }
}
