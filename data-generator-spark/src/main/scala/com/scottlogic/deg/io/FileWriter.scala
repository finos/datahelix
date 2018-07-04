package com.scottlogic.deg.io

import java.io
import java.io.{BufferedWriter, File}

class FileWriter() {
    def write(file:File, payload: String): Unit = {
        val bw = new BufferedWriter(new io.FileWriter(file))
        bw.write(payload)
        bw.close()
    }
}
