package com.scottlogic.deg.profile.reader;

import com.scottlogic.deg.common.ValidationException;

public class InvalidProfileException extends ValidationException
{
    public InvalidProfileException(String message) {
        super(message);
    }
}
