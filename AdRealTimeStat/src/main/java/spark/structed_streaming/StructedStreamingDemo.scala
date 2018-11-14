package spark.structed_streaming
import org.apache.spark.sql.functions._
import org.apache.spark.sql.SparkSession

object StructedStreamingDemo {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder.appName("StructuredNetworkWordCount").getOrCreate()
    val df = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "kfk013212.heracles.sohuno.com:6667,kfk,67,kfk013214.heracles.sohuno.com:6667,kfk,67,kfk013217.heracles.sohuno.com:6667,kfk,67,kfk013215.heracles.sohuno.com:6667,kfk,67,kfk013213.heracles.sohuno.com:6667,kfk,67,kfk013220.heracles.sohuno.com:6667,kfk,67,kfk013218.heracles.sohuno.com:6667,kfk,67,kfk013216.heracles.sohuno.com:6667,kfk,67,kfk013219.heracles.sohuno.com:6667")
      .option("subscribe", "mediaai_logcollect_wap_click")
      .load()
    df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)")
    df.show(5)
    spark.streams.awaitAnyTermination()
  }
}
