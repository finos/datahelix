package com.scottlogic.deg.output.outputtarget;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.output.writer.DataSetWriter;
import com.scottlogic.deg.output.writer.OutputWriterFactory;

import java.io.IOException;

public class StdoutOutputTarget implements SingleDatasetOutputTarget{
    private final OutputWriterFactory formattingWriterFactory;

    @Inject
    public StdoutOutputTarget(OutputWriterFactory formattingWriterFactory) {
        this.formattingWriterFactory = formattingWriterFactory;
    }

    @Override
    public DataSetWriter openWriter(ProfileFields fields) throws IOException {
        return formattingWriterFactory.createWriter(System.out, fields);
    }
}
