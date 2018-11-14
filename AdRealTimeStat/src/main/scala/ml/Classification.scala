package ml
import java.util.Date

import org.apache.spark.SparkContext
import org.apache.spark.mllib.tree.DecisionTree
import org.apache.spark.mllib.tree.model.DecisionTreeModel
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.sql.SparkSession

object Classification {


  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("BasicStatics").master("local[2]").getOrCreate()
    val sc = spark.sparkContext
    val start = new Date().getTime
    desisionTree(sc)
    println("It took "+(new Date().getTime -start) + "ms")
  }

  def desisionTree(sc: SparkContext): Unit = {
    // Load and parse the data file.
    val data = MLUtils.loadLibSVMFile(sc, "sample_libsvm_data.txt")
    val splits = data.randomSplit(Array(0.8, 0.2))
    val (trainingData, testData) = (splits(0), splits(1))
//    testData.foreach(i=>println(i))
    val numClasses = 2
    val categoricalFeaturesInfo = Map[Int, Int]()
    val impurity = "gini"
    val maxDepth = 5
    val maxBins = 32

    val model = DecisionTree.trainClassifier(trainingData, numClasses, categoricalFeaturesInfo,
      impurity, maxDepth, maxBins)
    // Evaluate model on test instances and compute test error
    val labelAndPreds = testData.map { point =>
      val prediction = model.predict(point.features)
      (point.label, prediction)
    }
    val testErr = labelAndPreds.filter(r => r._1 != r._2).count().toDouble / testData.count()
    println("Test Error = " + testErr)
    println("Learned classification tree model:" + model.toDebugString)

    model.save(sc, "D:\\tmp\\spark_ml")

  }

}
