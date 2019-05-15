package com.scottlogic.deg.generator.outputs.targets;

public class OutputTargetValidationException extends RuntimeException {
    OutputTargetValidationException(String msg) { super(msg); }
    OutputTargetValidationException(String msg, Throwable t) { super(msg, t); }
}
