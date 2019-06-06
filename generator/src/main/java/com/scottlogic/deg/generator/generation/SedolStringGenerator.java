package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.utils.*;

public class SedolStringGenerator extends ChecksummedCodeStringGenerator {
    public final static int SEDOL_LENGTH = 7;
    public final static String STANDARD_REGEX_REPRESENTATION = "[B-DF-HJ-NP-TV-Z0-9]{6}[0-9]";

    public SedolStringGenerator() {
        super(STANDARD_REGEX_REPRESENTATION, SEDOL_LENGTH, 0);
    }

    public SedolStringGenerator(String prefix) { this(prefix, ""); }

    public SedolStringGenerator(String prefix, String suffix) {
        super(prefix + STANDARD_REGEX_REPRESENTATION + suffix, SEDOL_LENGTH, 0);
    }

    public SedolStringGenerator(String prefix, String suffix, RegexStringGenerator additionalRestrictions) {
        super(prefix + STANDARD_REGEX_REPRESENTATION + suffix, additionalRestrictions, SEDOL_LENGTH, prefix.length());
    }

    private SedolStringGenerator(RegexStringGenerator sedolGenerator) {
        super(sedolGenerator, false, SEDOL_LENGTH, 0);
    }

    private SedolStringGenerator(RegexStringGenerator sedolGenerator, boolean negate) {
        super(sedolGenerator, negate, SEDOL_LENGTH, 0);
    }

    @Override
    public char calculateCheckDigit(String str) {
        return IsinUtils.calculateSedolCheckDigit(
            str.substring(prefixLength, SEDOL_LENGTH + prefixLength - 1)
        );
    }

    @Override
    public String fixCheckDigit(String str) {
        char checkDigit = IsinUtils.calculateSedolCheckDigit(
            str.substring(prefixLength, SEDOL_LENGTH + prefixLength - 1)
        );
        if (str.length() > prefixLength + SEDOL_LENGTH) {
            return str.substring(0, prefixLength + SEDOL_LENGTH - 1) +
                checkDigit + str.substring(prefixLength + SEDOL_LENGTH);
        }
        return str.substring(0, str.length() - 1) + checkDigit;
    }

    @Override
    public StringGenerator complement() {
        return new SedolStringGenerator(regexGenerator, !negate);
    }

    @Override
    public boolean match(String subject) {
        boolean matches = IsinUtils.isValidSedolNsin(subject, prefixLength);
        return matches != negate;
    }

    @Override
    ChecksummedCodeStringGenerator instantiate(RegexStringGenerator generator) {
        return new SedolStringGenerator(generator);
    }
}
