package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.generation.StringGenerator;
import com.scottlogic.deg.generator.generation.IsinStringGenerator;
import com.scottlogic.deg.generator.generation.RegexStringGenerator;
import com.scottlogic.deg.generator.generation.SedolStringGenerator;

import java.util.Objects;

/**
 * https://github.com/ScottLogic/data-engineering-generator/blob/ws/experimental-data-constraint-solver/data-constraint-poc/src/main/java/com/scottlogic/deg/constrainer/util/RegexProcessor.java
 * https://github.com/ScottLogic/data-engineering-generator/blob/ws/experimental-data-constraint-solver/data-constraint-poc/src/main/java/com/scottlogic/deg/constrainer/RegexFieldConstraint.java#L133
 */
public class StringRestrictions {
    private final StringConstraints constraints;
    public StringGenerator stringGenerator;

    public StringRestrictions(StringConstraints constraints) {

        this.constraints = constraints;
    }

    public StringConstraints getConstraints() {
        return constraints;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringRestrictions that = (StringRestrictions) o;
        return Objects.equals(stringGenerator, that.stringGenerator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stringGenerator);
    }
}
