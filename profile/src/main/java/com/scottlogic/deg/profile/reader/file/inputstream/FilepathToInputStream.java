package com.scottlogic.deg.profile.reader.file.inputstream;

import java.io.InputStream;

public interface FilepathToInputStream {

    InputStream createStreamFromPath(String path);

}
