package com.scottlogic.deg.profile.reader.file.inputstream;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

public class PathMapper implements FilepathToInputStream {

    @Override
    public InputStream createStreamFromPath(String path) {
        try {
            return new FileInputStream(path);
        } catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
