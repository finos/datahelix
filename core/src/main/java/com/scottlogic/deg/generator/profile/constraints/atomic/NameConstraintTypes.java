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

package com.scottlogic.deg.generator.profile.constraints.atomic;

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
