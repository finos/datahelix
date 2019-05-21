package com.scottlogic.deg.generator.outputs.targets;

import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.outputs.formats.DataSetWriter;

import java.io.IOException;

public interface SingleDatasetOutputTarget {
    DataSetWriter openWriter(ProfileFields fields) throws IOException;
}
