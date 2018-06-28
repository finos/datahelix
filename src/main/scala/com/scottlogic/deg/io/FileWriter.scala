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

    def writeProfile(name: String, dfs: Array[(String, DataFrame]): Unit = {
        val columnDistribution = dfs.foldLeft(JsArray()) {
            (array, next: (String, DataFrame)) => {
                val row = next._2.first()
                val values: Map[String, JsValue] = row.getValuesMap(row.schema.fieldNames)
                    .map(pair => (pair._1, JsString(pair._2)))
                    .asInstanceOf[Map[String, JsValue]]
                array.append(JsObject(
                    Seq(next._1 -> JsObject(values))
                ))
            }
        }

        val json: JsValue = Json.obj(
            "profileName" -> "testProfile",
            "columnDistributions" -> columnDistribution
        )

        val file = new File("test-output.json")
        val bw = new BufferedWriter(new io.FileWriter(file))
        bw.write(Json.stringify(json))
        bw.close()


    }
}
