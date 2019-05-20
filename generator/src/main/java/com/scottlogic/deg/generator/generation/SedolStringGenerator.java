package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.utils.*;

public class SedolStringGenerator extends ChecksummedCodeStringGenerator {
    public final static int SEDOL_LENGTH = 7;
    private final static String SEDOL_SANS_CHECK_DIGIT_REGEX = "[B-DF-HJ-NP-TV-Z0-9]{6}";

    public SedolStringGenerator() {
        super(SEDOL_SANS_CHECK_DIGIT_REGEX);
    }

    public SedolStringGenerator(String prefix) {
        super(prefix + SEDOL_SANS_CHECK_DIGIT_REGEX);
    }

    private SedolStringGenerator(RegexStringGenerator sedolSansCheckDigitGenerator, boolean negate) {
        super(sedolSansCheckDigitGenerator, negate);
    }

    @Override
    public char calculateCheckDigit(String str) {
        return IsinUtils.calculateSedolCheckDigit(str.substring(str.length() - (SEDOL_LENGTH - 1)));
    }

    @Override
    public StringGenerator complement() {
        return new SedolStringGenerator(sansCheckDigitGenerator, !negate);
    }

    @Override
    public boolean match(String subject) {
        boolean matches = IsinUtils.isValidSedolNsin(subject);
        return matches != negate;
    }
}
