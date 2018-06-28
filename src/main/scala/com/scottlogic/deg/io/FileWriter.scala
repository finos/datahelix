package com.scottlogic.deg.io

import java.io
import java.io.{BufferedWriter, File}

import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import play.api.libs.json.{Json, JsValue, JsObject, JsArray, JsString}

class FileWriter(spark: SparkSession) {
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

    def toColumnDistribution(dfs: Array[(String, DataFrame)]) = {
        dfs.foldLeft(JsArray()) {
            distributionJsCombinator
        }
    }

    def distributionJsCombinator(array: JsArray, next: (String, DataFrame)) = {
        val row = next._2.first()
        val values: Map[String, JsValue] = row.getValuesMap(row.schema.fieldNames)
            .map(pair => (pair._1, JsString(pair._2)))
            .asInstanceOf[Map[String, JsValue]]
        array.append(JsObject(
            Seq(next._1 -> JsObject(values))
        ))
    }

    def toProfile(columnDistribution: JsValue): JsValue = {
        Json.obj(
            "profileName" -> "testProfile",
            "columnDistributions" -> columnDistribution
        )
    }
}
