package com.scottlogic.deg.generator.generation.string;

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
        return FinancialCodeUtils.calculateCusipCheckDigit(
            str.substring(prefixLength, CUSIP_LENGTH + prefixLength - 1)
        );
    }

    @Override
    public int getLength() {
        return CUSIP_LENGTH;
    }

    @Override
    public StringGenerator complement() {
        return new CusipStringGenerator(regexGenerator, !negate);
    }

    @Override
    public boolean match(String subject) {
        boolean matches = FinancialCodeUtils.isValidCusipNsin(subject);
        return matches != negate;
    }

    @Override
    ChecksummedCodeStringGenerator instantiate(RegexStringGenerator generator) {
        return new CusipStringGenerator(generator);
    }
}
