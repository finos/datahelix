package com.scottlogic.deg.common.profile.constraints.atomic;

import java.util.Arrays;

public enum NameConstraintTypes {
    FIRST("firstname", "names/firstname.csv"),
    LAST("lastname", "names/surname.csv"),
    FULL("fullname", null);

    private final String profileText;

    private final String filePath;

    NameConstraintTypes(final String profileText, final String filePath) {
        this.profileText = profileText;
        this.filePath = filePath;
    }

    public String getProfileText() {
        return profileText;
    }

    public String getFilePath() {
        return filePath;
    }

    public static NameConstraintTypes lookupProfileText(final String profileText) {
        return Arrays.stream(values())
            .filter(name -> name.getProfileText().equals(profileText))
            .findFirst()
            .orElseThrow(
                () -> new UnsupportedOperationException("Couldn't find name constraint matching " + profileText));
    }
}
