package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.fieldspecs.AtomicConstraintConstructTuple;
import com.scottlogic.deg.generator.generation.RegexStringGenerator;
import com.scottlogic.deg.generator.generation.StringGenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class StringGeneratorFactory
{
    private static Map<AtomicConstraintConstructTuple, StringGenerator> constraintToStringGeneratorMap = new HashMap<>();

    public StringGenerator create(AtomicConstraint atomicConstraint, boolean matchFullString, boolean negate, Pattern pattern){
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
}
