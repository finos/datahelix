package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.utils.*;

public class SedolStringGenerator extends ChecksummedCodeStringGenerator {
    public final static int SEDOL_LENGTH = 7;
    private final static String SEDOL_REGEX = "[B-DF-HJ-NP-TV-Z0-9]{6}[0-9]";

    public SedolStringGenerator() {
        super(SEDOL_REGEX);
    }

    public SedolStringGenerator(String prefix) {
        super(prefix + SEDOL_REGEX);
    }

    private SedolStringGenerator(RegexStringGenerator sedolGenerator) {
        super(sedolGenerator, false);
    }

    private SedolStringGenerator(RegexStringGenerator sedolGenerator, boolean negate) {
        super(sedolGenerator, negate);
    }

    @Override
    public char calculateCheckDigit(String withoutCheckDigit) {
        return IsinUtils.calculateSedolCheckDigit(
            withoutCheckDigit.substring(withoutCheckDigit.length() - (SEDOL_LENGTH - 1))
        );
    }

    @Override
    public StringGenerator complement() {
        return new SedolStringGenerator(regexGenerator, !negate);
    }

    @Override
    public boolean match(String subject) {
        boolean matches = IsinUtils.isValidSedolNsin(subject);
        return matches != negate;
    }

    @Override
    ChecksummedCodeStringGenerator instantiate(RegexStringGenerator generator) {
        return new SedolStringGenerator(generator);
    }
}
