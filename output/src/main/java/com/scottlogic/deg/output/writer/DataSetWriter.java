package com.scottlogic.deg.output.writer;

import com.scottlogic.deg.common.output.GeneratedObject;

import java.io.Closeable;
import java.io.IOException;

/**
 * An object that can persist generated data to some destination (whether that be memory, a filesystem, a service, etc).
 *
 * DataSetWriters should take ownership of any Closeables they use, and close them into their own .close() implementation
 **/
public interface DataSetWriter extends Closeable {
    void writeRow(GeneratedObject row) throws IOException;
}
