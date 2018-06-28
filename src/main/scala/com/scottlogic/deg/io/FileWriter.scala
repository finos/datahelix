package com.scottlogic.deg.io

import java.io
import java.io.{BufferedWriter, File}
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import play.api.libs.json._

class FileWriter(spark: SparkSession) {
    import scala.language.implicitConversions
    def writeJson(name: String, df: DataFrame): Boolean = {
        df.write.mode(SaveMode.Overwrite).json("output/" + name)
        return true
    }

    def writeProfile(name: String, dfs: Array[(String, DataFrame)]): Unit = {
        val json = toProfile(toColumnDistribution(dfs))
        val file = new File("test-output.json")
        val bw = new BufferedWriter(new io.FileWriter(file))
        bw.write(Json.stringify(json))
        bw.close()
    }

    def toProfile(columnDistribution: JsValue): JsValue = {
        Json.obj(
            "profileName" -> "testProfile",
            "columnDistributions" -> columnDistribution
        )
    }
    def toColumnDistribution(dfs: Array[(String, DataFrame)]) = {
        dfs.foldLeft(JsArray()) {
            distributionJsCombinator
        }
    }

    def distributionJsCombinator(array: JsArray, next: (String, DataFrame)) = {
        val row = next._2.first()
        val values: Map[String, JsValue] = row.getValuesMap(row.schema.fieldNames)
            .map(pair => (pair._1, jsValue(pair._2)))
        array.append(JsObject(
            Seq(next._1 -> JsObject(values))
        ))
    }

    def jsValue(value: Any): JsValue = value match {
        case s: String => JsString(s)
        case d: Double => JsNumber(d)
        case i: Int => JsNumber(i)
        case _ => JsNull
    }
}
