package com.scottlogic.deg.generator.visualisation;

import com.scottlogic.deg.generator.ConfigSource;

public interface VisualisationConfigSource extends ConfigSource {

    String getTitleOverride();
    boolean shouldHideTitle();

}
