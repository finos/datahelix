/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.deg.generator.utils;

import java.util.*;
import java.util.stream.IntStream;

public class FinancialCodeUtils {
    public static final List<String> VALID_COUNTRY_CODES = Arrays.asList("GB", "US");
    public static final int ISIN_LENGTH = 12;
    public static final int CUSIP_LENGTH = 9;
    public static final int SEDOL_LENGTH = 7;

    public static boolean isValidIsin(String isin) {
        if (isin.length() != ISIN_LENGTH) {
            return false;
        }
        final String countryCode = isin.substring(0, 2);
        final String nsin = isin.substring(2, ISIN_LENGTH - 1);
        if (!isValidCountryCode(countryCode)) {
            return false;
        }
        if (!isValidNsin(countryCode, nsin)) {
            return false;
        }
        return isinHasValidCheckDigit(isin);
    }

    private static boolean isValidCountryCode(String countryCode) {
        return VALID_COUNTRY_CODES.contains(countryCode);
    }

    private static boolean isValidNsin(String countryCode, String nsin) {
        if (!nsin.matches("[A-Z0-9@*#]{9}")) {
            return false;
        }
        if (countryCode.equals("GB")) {
            return isValidSedolNsin(nsin);
        }
        if (countryCode.equals("US")) {
            return isValidCusipNsin(nsin);
        }
        return true;
    }

    private static List<Integer> SEDOL_WEIGHTS = Arrays.asList(1, 3, 1, 7, 3, 9, 1);

    public static boolean isValidSedolNsin(String nsin) {
        // A SEDOL has length 7, but is prefixed by zeroes when used as a nine-digit NSIN
        return isValidSedolNsin(nsin, nsin.length() - SEDOL_LENGTH);
    }

    public static boolean isValidSedolNsin(String nsin, int startOffset) {
        return isValidSuffixedSedolNsin(nsin, startOffset) &&
            nsin.length() == startOffset + SEDOL_LENGTH;
    }

    public static boolean isValidSuffixedSedolNsin(String nsin, int startOffset) {
        if (startOffset < 0) {
            return false;
        }
        if (nsin.length() < startOffset + SEDOL_LENGTH ) {
            return false;
        }
        if (nsin.length() > SEDOL_LENGTH && !nsin.substring(0, startOffset).matches("^0*$")) {
            return false;
        }

        // SEDOL is alphanumeric but cannot contain vowels
        if (nsin.substring(0, startOffset + SEDOL_LENGTH).matches(".*[AEIOU@*#].*")) {
            return false;
        }
        int checkDigitPosition = startOffset + SEDOL_LENGTH - 1;
        String sedolPreCheckDigit = nsin.substring(startOffset, checkDigitPosition);
        char checkDigit = calculateSedolCheckDigit(sedolPreCheckDigit);
        return nsin.charAt(checkDigitPosition) == checkDigit;
    }

    public static boolean isValidCusipNsin(String nsin) {
        return isValidSuffixedCusipNsin(nsin) && nsin.length() == CUSIP_LENGTH;
    }

    public static boolean isValidSuffixedCusipNsin(String nsin) {
        if (nsin.length() < CUSIP_LENGTH) { return false; }
        // CUSIPs can only contain digits in the first three positions
        if (nsin.substring(0, 3).matches(".*[^0-9].*")) { return false; }
        String cusipPreCheckDigit = nsin.substring(0, CUSIP_LENGTH - 1);
        char checkDigit = calculateCusipCheckDigit(cusipPreCheckDigit);
        return nsin.charAt(CUSIP_LENGTH - 1) == checkDigit;
    }

    public static char calculateSedolCheckDigit(String sedol) {
        final Iterator<Integer> reverseSedolWeightIterator = new LinkedList<>(SEDOL_WEIGHTS).descendingIterator();
        reverseSedolWeightIterator.next();
        return luhnsCheckDigit(sedol, false, false, reverseSedolWeightIterator, Collections.emptyList());
    }

    private static List<Character> CUSIP_SPECIAL_CHARACTERS = Arrays.asList('*', '@', '#');

    public static char calculateCusipCheckDigit(String cusip) {
        return luhnsCheckDigit(cusip, false, true, new CyclicIterable<>(Arrays.asList(2, 1)).iterator(), CUSIP_SPECIAL_CHARACTERS);
    }

    // Validates the check digit at the end of `isin`, which is assumed to be a valid 12-character ISIN
    private static boolean isinHasValidCheckDigit(String isin) {
        final char calculatedCheckDigit = calculateIsinCheckDigit(isin.substring(0, 11));
        return isin.charAt(11) == calculatedCheckDigit;
    }

    // Generates  the check digit that should be appended to `isin`, which should be the first 11-characters of a valid ISIN
    public static char calculateIsinCheckDigit(String isin) {
        return luhnsCheckDigit(isin, true, true, new CyclicIterable<>(Arrays.asList(2, 1)).iterator(), Collections.emptyList());
    }

    private static char luhnsCheckDigit(String source, boolean splitStartingDigits, boolean splitWeightedDigits,
                                        Iterator<Integer> weights, List<Character> specialCharacters) {
        final List<Integer> convertedDigits = source.chars()
                .map(character -> specialCharacters.contains((char) character)
                        ? 36 + specialCharacters.indexOf((char) character)
                        : Character.digit(character, 36))
                .flatMap(splitStartingDigits ? FinancialCodeUtils::splitDigits : IntStream::of)
                .collect(ArrayList::new, List::add, ArrayList::addAll);

        final int weightedDigitSum = IntStream.range(0, convertedDigits.size())
                .map(reverseIndex -> convertedDigits.get(convertedDigits.size() - reverseIndex - 1) * weights.next())
                .flatMap(splitWeightedDigits ? FinancialCodeUtils::splitDigits : IntStream::of)
                .reduce(0, Integer::sum);

        int checkDigit = (10 - (weightedDigitSum % 10)) % 10;
        return Character.forDigit(checkDigit, 10);
    }

    private static IntStream splitDigits(int num) {
        return num >= 10
                ? IntStream.of(num / 10, num % 10)
                : IntStream.of(num);
    }
}
