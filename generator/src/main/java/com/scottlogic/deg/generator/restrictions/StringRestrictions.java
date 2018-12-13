package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.generation.StringGenerator;
import com.scottlogic.deg.generator.generation.IsinStringGenerator;
import com.scottlogic.deg.generator.generation.RegexStringGenerator;
import com.scottlogic.deg.generator.generation.SedolStringGenerator;

/**
 * https://github.com/ScottLogic/data-engineering-generator/blob/ws/experimental-data-constraint-solver/data-constraint-poc/src/main/java/com/scottlogic/deg/constrainer/util/RegexProcessor.java
 * https://github.com/ScottLogic/data-engineering-generator/blob/ws/experimental-data-constraint-solver/data-constraint-poc/src/main/java/com/scottlogic/deg/constrainer/RegexFieldConstraint.java#L133
 */
public class StringRestrictions {
    public StringGenerator stringGenerator;

    public StringRestrictions() {

    }

    public static boolean isString(Object o) {
        return o instanceof String;
    }

    public boolean match(Object o) {
        if (!StringRestrictions.isString(o)) {
            return false;
        }

        String s = (String) o;
        return stringGenerator.match(s);

    }

    @Override
    public String toString() {
        if (stringGenerator instanceof RegexStringGenerator)
            return String.format("Regex: `%s`", stringGenerator.toString());

        if (stringGenerator instanceof SedolStringGenerator)
            return "Sedol";

        if (stringGenerator instanceof IsinStringGenerator)
            return "Isin";

        return stringGenerator.toString();
    }
}
