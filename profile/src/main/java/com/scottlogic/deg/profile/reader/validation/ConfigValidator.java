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

package com.scottlogic.deg.profile.reader.validation;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.scottlogic.deg.common.ValidationException;

import java.io.File;

/**
 * Class used to determine whether the command line options are valid for generation.
 */
public class ConfigValidator {

    private final File profileFile;

    @Inject
    public ConfigValidator(@Named("config:profileFile") File profileFile) {
        this.profileFile = profileFile;
    }

    public void checkProfileInputFile() {
        if (containsInvalidChars(profileFile)) {
            throw new ValidationException("Profile file path " + profileFile +
                " contains one or more invalid characters ? : %% \" | > < "
            );
        }
        else if (!profileFile.exists()) {
            throw new ValidationException("Profile file " + profileFile + " does not exist");
        }
        else if (profileFile.isDirectory()) {
            throw new ValidationException("Profile file path " + profileFile +
                " provided is to a directory");
        }
        else if (isFileEmpty(profileFile)) {
            throw new ValidationException("Profile file " + profileFile + " has no content");
        }
    }

    boolean containsInvalidChars(File file) {
        return file.getPath().matches(".*[?%*|><\"].*|^(?:[^:]*+:){2,}[^:]*+$");
    }


    boolean isFileEmpty(File file) {
        return file.length() == 0;
    }

}
