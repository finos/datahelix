package com.scottlogic.deg.generator.generation.string;

public final class StringUtils {

    private StringUtils() {
        throw new UnsupportedOperationException("No instantiation of static class");
    }

    /**
     * <p>
     * check to see if the character generated is a valid utf-8 single word value.
     * </p>
     * <p>
     * from chapter 3.9, page 126 of `the Unicode Standard v11.0`
     * (https://www.unicode.org/versions/Unicode11.0.0/ch02.pdf):
     * </p>
     * <code>Because surrogate code points are not Unicode scalar values, any UTF-8 byte
     * sequence that would otherwise map to code points U+D800..U+DFFF is illformed.
     * </code>
     *
     * @return true if the string parameter contains valid plane 0 unicode characters.
     * false if it contains any surrogate characters.
     */
    public static boolean isStringValidUtf8(String str) {
        for (char c : str.toCharArray()) {
            if (!isCharValidUtf8(c)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isCharValidUtf8(char c) {
        return !Character.isSurrogate(c);
    }

}
