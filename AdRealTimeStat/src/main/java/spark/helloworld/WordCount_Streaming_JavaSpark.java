package spark.helloworld;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import scala.Tuple2;

import java.util.*;

public class WordCount_Streaming_JavaSpark {
    public static void main(String[] args) {
        kafkaStreaming();
    }

    public static void wordCount(){
        System.out.println("Spark in Java is procceeding...");
        SparkConf conf = new SparkConf().setMaster("local").setAppName("wordcount");
//        SparkConf conf = new SparkConf().setMaster("spark://node1:7077").setAppName("wordcount");
        JavaSparkContext sc = new JavaSparkContext(conf);
        List<String> list= new ArrayList<String>();
        list.add("a b c ");
        list.add("lx lance");
        JavaRDD<String> data = sc.parallelize(list);
        JavaRDD<String> words = data.flatMap( line->( Arrays.asList(line.split(" ")).iterator()));
        JavaPairRDD<String,Integer> counts = words.mapToPair(word ->(new Tuple2<>(word,1)))
                .reduceByKey((Integer x, Integer y)->(x+y));
        counts.foreach(wordcount-> {
            System.out.println(wordcount._1+"---" + wordcount._2+" times");
        });
    }


    public static void kafkaStreaming(){
        SparkConf conf = new SparkConf().setAppName("kafkaWordCount").setMaster("local[2]").set("spark.driver.host", "localhost");
//                .set("spark.yarn.maxAppAttempts","4").set("spark.yarn.am.attemptFailuresValidityInterval","1h")
//                .set("spark.yarn.max.executor.failures","8").set("spark.yarn.executor.failuresValidityInterval","1h")
//                .set("spark.task.maxFailures","8").set("spark.hadoop.fs.hdfs.impl.disable.cache","true");
        JavaStreamingContext javaStreamingContext = new JavaStreamingContext(conf, Durations.seconds(5));
        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("bootstrap.servers", "localhost:9092");
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        kafkaParams.put("value.deserializer", StringDeserializer.class);
        kafkaParams.put("group.id", "lance_test_java");
        kafkaParams.put("auto.offset.reset", "latest");
        kafkaParams.put("enable.auto.commit", false);
        Collection<String> topics = Arrays.asList("AdRealTimeLog");
        JavaInputDStream<ConsumerRecord<String,String>> stream = KafkaUtils.createDirectStream(
                javaStreamingContext,
                LocationStrategies.PreferConsistent(),
                ConsumerStrategies.<String, String>Subscribe(topics, kafkaParams)
        );
        JavaDStream msgDstream=stream.map(record -> record.value());
        msgDstream.print();
        javaStreamingContext.start();
        try {
            javaStreamingContext.awaitTermination();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
