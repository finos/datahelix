package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.constraints.atomic.MatchesStandardConstraint;
import com.scottlogic.deg.generator.constraints.atomic.StandardConstraintTypes;
import com.scottlogic.deg.generator.fieldspecs.AtomicConstraintConstructTuple;
import com.scottlogic.deg.generator.generation.IsinStringGenerator;
import com.scottlogic.deg.generator.generation.RegexStringGenerator;
import com.scottlogic.deg.generator.generation.SedolStringGenerator;
import com.scottlogic.deg.generator.generation.StringGenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class StringGeneratorFactory
{
    private static final Map<StandardConstraintTypes, StringGenerator>
        standardNameToStringGenerator = new HashMap<StandardConstraintTypes, StringGenerator>() {{
        put(StandardConstraintTypes.ISIN, new IsinStringGenerator());
        put(StandardConstraintTypes.SEDOL, new SedolStringGenerator());
    }};

    private static final Map<AtomicConstraintConstructTuple, StringGenerator> constraintToStringGeneratorMap = new HashMap<>();

    public StringGenerator create(AtomicConstraint atomicConstraint, boolean matchFullString, boolean negate, Pattern pattern){
        if (atomicConstraint instanceof MatchesStandardConstraint){
            return createMatchesStandardGenerator((MatchesStandardConstraint) atomicConstraint, negate);
        }

        AtomicConstraintConstructTuple key = new AtomicConstraintConstructTuple(atomicConstraint, matchFullString, negate);

        if (constraintToStringGeneratorMap.containsKey(key)){
            return constraintToStringGeneratorMap.get(key);
        }

        StringGenerator generator = new RegexStringGenerator(pattern.toString(), matchFullString);
        if (negate) {
            generator = generator.complement();
        }

        constraintToStringGeneratorMap.put(key, generator);
        return generator;
    }

    private StringGenerator createMatchesStandardGenerator(MatchesStandardConstraint constraint, boolean negate) {
        StringGenerator generator = standardNameToStringGenerator.get(constraint.standard);

        return negate
            ? generator.complement()
            : generator;
    }
}
