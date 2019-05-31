package com.scottlogic.deg.common.profile.constraints.atomic;

import java.util.Arrays;

public enum NameConstraintTypes {
    FIRST("firstname"),
    LAST("lastname");

    private final String profileText;

    NameConstraintTypes(final String profileText) {
        this.profileText = profileText;
    }

    private String getProfileText() {
        return profileText;
    }

    public static NameConstraintTypes lookupProfileText(final String profileText) {
        return Arrays.stream(values())
            .filter(name -> name.getProfileText().equals(profileText))
            .findFirst()
            .orElseThrow(
                () -> new UnsupportedOperationException("Couldn't find name constraint matching " + profileText));
    }
}
