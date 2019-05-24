package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.utils.*;

public class CusipStringGenerator extends ChecksummedCodeStringGenerator {
    public final static int CUSIP_LENGTH = 9;
    public final static String STANDARD_REGEX_REPRESENTATION = "[0-9]{3}[0-9A-Z]{5}[0-9]";

    public CusipStringGenerator() {
        super(STANDARD_REGEX_REPRESENTATION);
    }

    public CusipStringGenerator(String prefix) {
        super(prefix + STANDARD_REGEX_REPRESENTATION);
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
