package com.scottlogic.deg.common;

import java.util.Collections;
import java.util.List;

public class ValidationException extends RuntimeException {
    public final List<String> errorMessages;

    public ValidationException(String msg) {
        super(msg);
        errorMessages = Collections.singletonList(msg);
    }

    public ValidationException(List<String> errorMessages){
        super();
        this.errorMessages = errorMessages;
    }
}
