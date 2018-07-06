package com.scottlogic.deg

import java.io.File

import com.scottlogic.deg.io.FileReader
import javax.inject.Inject
import org.apache.spark.sql.SparkSession
import org.hamcrest.CoreMatchers.{equalTo, is}
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api._


@Test
@RequiresInjection
class FileReaderInt extends {

    @Inject
    var spark : SparkSession = _

    @Inject
    var fileReader : FileReader = _

    @Test
    def readCsv_returns_DF_with_expectedColumns() {
        val path = getClass.getClassLoader.getResource("gfx_cleaned.csv").getPath
        val df = fileReader.readCSV(new File(path))
        assertThat(df.columns, is(equalTo(Array("Video Card", "Series", "Chipset", "Memory", "Core Clock", "Price"))))
    }

}
