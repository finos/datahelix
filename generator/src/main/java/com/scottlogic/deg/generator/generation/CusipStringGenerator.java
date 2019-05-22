package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.utils.*;

public class CusipStringGenerator extends ChecksummedCodeStringGenerator {
    public final static int CUSIP_LENGTH = 9;
    private final static String CUSIP_REGEX = "[0-9]{3}[0-9A-Z]{5}[0-9]";

    public CusipStringGenerator() {
        super(CUSIP_REGEX);
    }

    public CusipStringGenerator(String prefix) {
        super(prefix + CUSIP_REGEX);
    }

    private CusipStringGenerator(RegexStringGenerator generator) {
        super(generator, false);
    }

    private CusipStringGenerator(RegexStringGenerator cusipSansCheckDigitGenerator, boolean negate) {
        super(cusipSansCheckDigitGenerator, negate);
    }

    @Override
    public char calculateCheckDigit(String withoutCheckDigit) {
        return IsinUtils.calculateCusipCheckDigit(
            withoutCheckDigit.substring(withoutCheckDigit.length() - (CUSIP_LENGTH - 1))
        );
    }

    @Override
    public String getRegexRepresentation() {
        return CUSIP_REGEX;
    }

    @Override
    public StringGenerator complement() {
        return new CusipStringGenerator(regexGenerator, !negate);
    }

    @Override
    public boolean match(String subject) {
        boolean matches = IsinUtils.isValidCusipNsin(subject);
        return matches != negate;
    }

    @Override
    ChecksummedCodeStringGenerator instantiate(RegexStringGenerator generator) {
        return new CusipStringGenerator(generator);
    }
}
