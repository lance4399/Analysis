package ml

import org.apache.spark.mllib.linalg._
import org.apache.spark.mllib.stat.{MultivariateStatisticalSummary, Statistics}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession
import scala.runtime.Statics
object BasicStatics {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("BasicStatics").master("local[2]").getOrCreate()
    val sc = spark.sparkContext
//    sc.setLogLevel("WARN")
//    showSummaryStatistics(sc)
    showCorrelation(sc)
  }
  def showSummaryStatistics(sc: SparkContext):Unit={
    val observationRDD = sc.parallelize(
      Seq(
        Vectors.dense(1.0,10,100.0),
        Vectors.dense(2.0,20,200.0),
        Vectors.dense(3.0,30,300.0)
      )
    )
    val summary:MultivariateStatisticalSummary = Statistics.colStats(observationRDD)
    println("[mean]="+summary.mean)
    println("[variance]="+summary.variance)
    println("[numNonzeros]="+summary.numNonzeros)
    println("[count]="+summary.count)
  }

  def showCorrelation(sc: SparkContext):Unit={
    val seriesX:RDD[Double] = sc.parallelize(Array(1,2,3,4,5))
    val seriesY:RDD[Double] =  sc.parallelize(Array(11,22,33,44,55))
    val correlation:Double = Statistics.corr(seriesX,seriesY,"pearson")
    println(s"Correlation is:$correlation")

    val data: RDD[Vector] = sc.parallelize(
      Seq(
        Vectors.dense(1.0, 10.0, 100.0),
        Vectors.dense(2.0, 20.0, 200.0),
        Vectors.dense(5.0, 33.0, 366.0))
    )  // note that each Vector is a row and not a column

    val corrMatrix:Matrix = Statistics.corr(data,"spearman")
    println("[corrMatrix]="+corrMatrix.toString())
  }




}
