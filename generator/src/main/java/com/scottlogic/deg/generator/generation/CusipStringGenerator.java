package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.utils.*;

public class CusipStringGenerator extends ChecksummedCodeStringGenerator {
    public final static int CUSIP_LENGTH = 9;
    public final static String STANDARD_REGEX_REPRESENTATION = "[0-9]{3}[0-9A-Z]{5}[0-9]";

    public CusipStringGenerator() {
        super(STANDARD_REGEX_REPRESENTATION, CUSIP_LENGTH, 0);
    }

    public CusipStringGenerator(String prefix, String suffix, RegexStringGenerator additionalRestrictions) {
        super(
            prefix + STANDARD_REGEX_REPRESENTATION + suffix,
            additionalRestrictions,
            CUSIP_LENGTH,
            prefix.length()
        );
    }

    private CusipStringGenerator(RegexStringGenerator generator) {
        super(generator, false, CUSIP_LENGTH, 0);
    }

    private CusipStringGenerator(RegexStringGenerator cusipGenerator, boolean negate) {
        super(cusipGenerator, negate, CUSIP_LENGTH, 0);
    }

    @Override
    public char calculateCheckDigit(String str) {
        return IsinUtils.calculateCusipCheckDigit(
            str.substring(prefixLength, CUSIP_LENGTH + prefixLength - 1)
        );
    }

    @Override
    public String fixCheckDigit(String str) {
        char checkDigit = IsinUtils.calculateCusipCheckDigit(
            str.substring(prefixLength, CUSIP_LENGTH + prefixLength - 1)
        );
        if (str.length() > prefixLength + CUSIP_LENGTH) {
            return str.substring(0, prefixLength + CUSIP_LENGTH - 1) +
                checkDigit + str.substring(prefixLength + CUSIP_LENGTH);
        }
        return str.substring(0, str.length() - 1) + checkDigit;
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
