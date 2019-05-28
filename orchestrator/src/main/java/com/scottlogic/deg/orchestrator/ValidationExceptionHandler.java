package com.scottlogic.deg.orchestrator;

import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.generator.validators.ErrorReporter;
import picocli.CommandLine;

import java.io.IOException;

public class ValidationExceptionHandler implements CommandLine.IExecutionExceptionHandler {
    @Override
    public int handleExecutionException(Exception ex, CommandLine commandLine, CommandLine.ParseResult parseResult) throws Exception {
        if (ex instanceof ValidationException)
            new ErrorReporter().displayValidation((ValidationException) ex);
        else if (ex instanceof IOException)
            new ErrorReporter().displayException(ex);
        else throw ex;

        return 0;
    }
}
