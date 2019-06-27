package com.scottlogic.deg.profile.reader;

import com.scottlogic.deg.common.profile.Profile;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Defines an interface for a class to take in a filepath, read the specified profile and produce a Profile object.
 */
public interface ProfileReader {
    Profile read(Path filePath) throws IOException;
}
