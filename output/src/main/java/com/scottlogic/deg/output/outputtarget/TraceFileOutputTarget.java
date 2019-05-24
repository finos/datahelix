package com.scottlogic.deg.output.outputtarget;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.scottlogic.deg.output.FileUtils;
import com.scottlogic.deg.output.OutputPath;
import com.scottlogic.deg.output.writer.trace.TraceOutputWriterFactory;

import java.nio.file.Path;

public class TraceFileOutputTarget extends FileOutputTarget {

    @Inject
    public TraceFileOutputTarget(
        OutputPath filePath,
        TraceOutputWriterFactory outputWriterFactory,
        @Named("config:canOverwriteOutputFiles") boolean canOverwriteOutputFiles,
        FileUtils fileUtils)
    {
        super(getTraceFilePath(filePath.get()), outputWriterFactory, canOverwriteOutputFiles, fileUtils);
    }

    private static OutputPath getTraceFilePath(Path filePath) {
        if (filePath == null){
            return OutputPath.of(null);
        }
        return OutputPath.of(FileUtils.addFilenameSuffix(FileUtils.replaceExtension(filePath, "json"), "-trace"));
    }
}
