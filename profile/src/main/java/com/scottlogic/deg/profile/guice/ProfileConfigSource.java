package com.scottlogic.deg.profile.guice;

import java.io.File;

public interface ProfileConfigSource {
    File getProfileFile();
    boolean isSchemaValidationEnabled();
    String fromFilePath();
}
