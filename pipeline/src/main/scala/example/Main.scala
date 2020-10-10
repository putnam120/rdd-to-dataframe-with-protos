package example

import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.SparkSession
import scalapb.spark.ProtoSQL
import scalapb.spark.Implicits._

object Main {
  def main(args: Array[String]): Unit = {
    val pipelineName: String = "Example"

    // Set up Spark session
    val spark: SparkSession =
      SparkSession
        .builder()
        .appName(pipelineName)
        .getOrCreate()

    val sparkContext = spark.sparkContext

    val recordElementRdd = sparkContext
      .binaryFiles("ExampleRecordElement")
      .map(x => x._2.toArray)
      .map(RecordElement.parseFrom(_))

    val recordElementDataFrame =
      ProtoSQL.protoToDataFrame(spark, recordElementRdd)
  }
}
