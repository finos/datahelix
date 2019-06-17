package com.scottlogic.deg.profile.reader.file.inputstream;

import java.io.InputStream;

public class ClasspathMapper implements FilepathToInputStream {

    @Override
    public InputStream createStreamFromPath(String path) {
        return getClass().getClassLoader().getResourceAsStream(path);
    }
}
