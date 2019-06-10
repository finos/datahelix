package com.scottlogic.deg.output.outputtarget;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.scottlogic.deg.output.FileUtils;
import com.scottlogic.deg.output.FileUtilsImpl;
import com.scottlogic.deg.output.OutputPath;
import com.scottlogic.deg.output.writer.trace.TraceOutputWriterFactory;

import java.nio.file.Path;

public class TraceFileOutputTarget extends FileOutputTarget {
    @Inject
    public TraceFileOutputTarget(
        @Named("config:outputPath") Path filePath,
        TraceOutputWriterFactory outputWriterFactory,
        @Named("config:canOverwriteOutputFiles") boolean canOverwriteOutputFiles,
        FileUtils fileUtils)
    {
        super(
            new OutputPath(fileUtils.getTraceFilePath(filePath)),
            outputWriterFactory,
            canOverwriteOutputFiles,
            fileUtils
        );
    }
}
