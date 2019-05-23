package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.utils.*;

public class CusipStringGenerator extends ChecksummedCodeStringGenerator {
    public final static int CUSIP_LENGTH = 9;
    private final static String CUSIP_SANS_CHECK_DIGIT_REGEX = "[0-9]{3}[0-9A-Z]{5}";

    public CusipStringGenerator() {
        super(CUSIP_SANS_CHECK_DIGIT_REGEX);
    }

    public CusipStringGenerator(String prefix) {
        super(prefix + CUSIP_SANS_CHECK_DIGIT_REGEX);
    }

    private CusipStringGenerator(RegexStringGenerator cusipSansCheckDigitGenerator, boolean negate) {
        super(cusipSansCheckDigitGenerator, negate);
    }

    @Override
    public char calculateCheckDigit(String str) {
        return IsinUtils.calculateCusipCheckDigit(str.substring(str.length() - (CUSIP_LENGTH - 1)));
    }

    @Override
    public StringGenerator complement() {
        return new CusipStringGenerator(sansCheckDigitGenerator, !negate);
    }

    @Override
    public boolean match(String subject) {
        boolean matches = IsinUtils.isValidCusipNsin(subject);
        return matches != negate;
    }
}
