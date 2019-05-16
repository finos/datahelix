package com.scottlogic.deg.generator.utils;

import java.util.*;
import java.util.stream.IntStream;

public class IsinUtils {
    public static final List<String> VALID_COUNTRY_CODES = Arrays.asList("GB", "US");

    public static boolean isValidIsin(String isin) {
        if (isin.length() != 12) {
            return false;
        }
        final String countryCode = isin.substring(0, 2);
        final String nsin = isin.substring(2, 11);
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

    // Assumes generic NSIN checks have already been run, i.e. `nsin` is 9 alphanumeric characters
    public static boolean isValidSedolNsin(String nsin) {
        // A SEDOL has length 7, but is prefixed by zeroes when used as a nine-digit NSIN
        int sedolStartOffset = nsin.length() - 7;
        if (nsin.length() > 7 && !nsin.substring(0, sedolStartOffset).matches("^0*$")) {
            return false;
        }
        // SEDOL is alphanumeric but cannot contain vowels
        if (nsin.matches(".*[AEIOU@*#].*")) {
            return false;
        }
        int checkDigitPosition = sedolStartOffset + 6;
        String sedolPreCheckDigit = nsin.substring(sedolStartOffset, checkDigitPosition);
        char checkDigit = calculateSedolCheckDigit(sedolPreCheckDigit);
        return nsin.charAt(checkDigitPosition) == checkDigit;
    }

    // Assumes generic NSIN checks have already been run, i.e. `nsin` is 9 alphanumeric characters
    public static boolean isValidCusipNsin(String nsin) {
        String cusipPreCheckDigit = nsin.substring(0, 8);
        char checkDigit = calculateCusipCheckDigit(cusipPreCheckDigit);
        return nsin.charAt(8) == checkDigit;
    }

    public static char calculateSedolCheckDigit(String sedol) {
        final Iterator<Integer> reverseSedolWeightIterator = new LinkedList<>(SEDOL_WEIGHTS).descendingIterator();
        reverseSedolWeightIterator.next();
        return luhnsCheckDigit(sedol, false, false, reverseSedolWeightIterator, Collections.emptyList());
    }

    private static List<Character> CUSIP_SPECIAL_CHARACTERS = Arrays.asList('*', '@', '#');

    public static char calculateCusipCheckDigit(String cusip) {
        assert (cusip.length() == 8);
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
                .flatMap(splitStartingDigits ? IsinUtils::splitDigits : IntStream::of)
                .collect(ArrayList::new, List::add, ArrayList::addAll);

        final int weightedDigitSum = IntStream.range(0, convertedDigits.size())
                .map(reverseIndex -> convertedDigits.get(convertedDigits.size() - reverseIndex - 1) * weights.next())
                .flatMap(splitWeightedDigits ? IsinUtils::splitDigits : IntStream::of)
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
