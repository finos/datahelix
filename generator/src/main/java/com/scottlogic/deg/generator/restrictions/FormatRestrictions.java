package com.scottlogic.deg.generator.restrictions;

import java.util.Objects;

public class FormatRestrictions implements Restrictions {
    public String formatString;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FormatRestrictions that = (FormatRestrictions) o;
        return Objects.equals(formatString, that.formatString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(formatString);
    }
}
