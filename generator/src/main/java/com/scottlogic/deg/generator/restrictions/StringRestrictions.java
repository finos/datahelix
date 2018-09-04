package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.utils.IStringGenerator;

/**
 * https://github.com/ScottLogic/data-engineering-generator/blob/ws/experimental-data-constraint-solver/data-constraint-poc/src/main/java/com/scottlogic/deg/constrainer/util/RegexProcessor.java
 * https://github.com/ScottLogic/data-engineering-generator/blob/ws/experimental-data-constraint-solver/data-constraint-poc/src/main/java/com/scottlogic/deg/constrainer/RegexFieldConstraint.java#L133
 */
public class StringRestrictions {
    public IStringGenerator stringGenerator;

    public static boolean isString(Object o){
        return o instanceof String;
    }

    public boolean match(Object o) {
        if (!StringRestrictions.isString(o)) {
            return false;
        }

        String s = (String) o;
        return stringGenerator.match(s);

    }
}
