package com.scottlogic.deg.generator.inputs;

import com.scottlogic.deg.generator.Profile;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Defines an interface for a class to take in a filepath, read the specified profile and produce a Profile object.
 */
public interface ProfileReader {
    Profile read(Path filePath) throws IOException, InvalidProfileException;
}
