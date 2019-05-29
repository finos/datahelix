package com.scottlogic.deg.output.manifest;

import com.scottlogic.deg.common.profile.ViolatedProfile;

import java.io.IOException;
import java.util.List;

public interface ManifestWriter {
    void writeManifest(List<ViolatedProfile> result) throws IOException;
}
