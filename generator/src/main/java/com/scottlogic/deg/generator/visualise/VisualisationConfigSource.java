package com.scottlogic.deg.generator.visualise;

import com.scottlogic.deg.generator.ConfigSource;

import java.io.File;
import java.nio.file.Path;

public interface VisualisationConfigSource extends ConfigSource {

    String getTitleOverride();
    boolean shouldHideTitle();
    boolean dontSimplify();
}
