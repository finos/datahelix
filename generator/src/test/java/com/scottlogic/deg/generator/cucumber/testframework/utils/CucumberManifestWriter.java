package com.scottlogic.deg.generator.cucumber.testframework.utils;

import com.scottlogic.deg.generator.outputs.manifest.ManifestWriter;
import com.scottlogic.deg.generator.violations.ViolatedProfile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class CucumberManifestWriter implements ManifestWriter {
    @Override
    public void writeManifest(List<ViolatedProfile> result, Path directoryPath) throws IOException {

    }
}
