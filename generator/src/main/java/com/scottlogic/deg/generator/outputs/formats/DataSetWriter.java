package com.scottlogic.deg.generator.outputs.formats;

import com.scottlogic.deg.generator.outputs.GeneratedObject;

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
