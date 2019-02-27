package com.scottlogic.deg.generator.visualisation;

import com.scottlogic.deg.generator.ConfigSource;

/**
 * Command line options specific to visualisation of a profile.
 * See @{@link com.scottlogic.deg.generator.CommandLine.VisualiseCommandLine VisualiseCommandLine} for descriptions
 */
public interface VisualisationConfigSource extends ConfigSource {

    String getTitleOverride();

    boolean shouldHideTitle();

}
