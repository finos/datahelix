package com.scottlogic.deg

import com.scottlogic.deg.spark.reader.FileReader
import org.apache.spark.sql.SparkSession
import org.junit.Assert.assertTrue
import org.junit.{Before, Test}

@Test
class FileReaderInt extends {

    var spark : SparkSession = _ // var to get spark session before each
    var fileReader : FileReader = _
    @Before
    def before(): Unit = {
        spark = SparkSession.builder
            .appName("Data Engineering Generator")
            .config("spark.master", "local")
            .getOrCreate()

        fileReader = new FileReader(spark)
    }

    @Test
    def readCsv(): Unit = {
        val path = getClass.getClassLoader().getResource("gfx_cleaned.csv").getPath
        val df = fileReader.readCSV(path)
        val expectedColumns = Array("Video Card", "Series", "Chipset", "Memory ()", "Core Clock ()", "Price (£)")
        assertTrue(df.columns.length == 6)
    }
}
