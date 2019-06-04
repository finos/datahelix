package com.scottlogic.deg.output.outputtarget;

import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.output.writer.DataSetWriter;

import java.io.IOException;

public interface SingleDatasetOutputTarget {
    DataSetWriter openWriter(ProfileFields fields) throws IOException;
    default void validate() throws OutputTargetValidationException, IOException {}
}
