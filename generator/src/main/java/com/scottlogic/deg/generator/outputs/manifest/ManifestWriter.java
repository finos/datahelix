package com.scottlogic.deg.generator.outputs.manifest;

import com.scottlogic.deg.generator.violations.ViolatedProfile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface ManifestWriter {

    void writeManifest(List<ViolatedProfile> result, Path directoryPath) throws IOException;
}
