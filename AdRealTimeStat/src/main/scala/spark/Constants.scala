package spark

import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}

object Constants {
  //亦庄kafka集群
  val yz_kafka_cluster = Map[String, Object](
    "bootstrap.servers" -> "kfk013212.heracles.sohuno.com:6667,kfk013214.heracles.sohuno.com:6667,kfk013217.heracles.sohuno.com:6667,kfk013215.heracles.sohuno.com:6667,kfk013213.heracles.sohuno.com:6667,kfk013220.heracles.sohuno.com:6667,kfk013218.heracles.sohuno.com:6667,kfk013216.heracles.sohuno.com:6667,kfk013219.heracles.sohuno.com:6667",
    "key.deserializer" -> classOf[StringDeserializer],
    "value.deserializer" -> classOf[StringDeserializer],
    "group.id" -> "Dwd_Flow7",
    "auto.offset.reset" -> "latest",
    "enable.auto.commit" -> (false: java.lang.Boolean),
//    "enable.auto.commit" -> (true: java.lang.Boolean),
//    "sasl.jaas.config" -> "com.sun.security.auth.module.Krb5LoginModule required useTicketCache=true;",
    "security.protocol" -> "SASL_PLAINTEXT",
    "sasl.mechanism" -> "GSSAPI",
    "sasl.kerberos.service.name" -> "kafka",
    "key.serializer" -> classOf[StringSerializer],
    "value.serializer" -> classOf[StringSerializer] ,
    "offsets.storage" -> "kafka",
//    "session.timeout.ms" -> "40000",
//    "heartbeat.interval.ms" -> "13000",
//    "request.timeout.ms" -> "40000"

  "session.timeout.ms" -> "30000",
  "heartbeat.interval.ms" -> "10000"
//  "request.timeout.ms" -> "31000"
  )

  //亦庄 CDH_Kafka集群
  val yz_cdh_kafka_cluster = Map[String, Object](
//    "bootstrap.servers" -> "kfk013218.heracles.sohuno.com:9092,pdn096224.heracles.sohuno.com:9092,kfk013217.heracles.sohuno.com:9092,pdn096223.heracles.sohuno.com:9092,pdn096229.heracles.sohuno.com:9092,pdn096228.heracles.sohuno.com:9092,pdn096226.heracles.sohuno.com:9092,pdn096230.heracles.sohuno.com:9092,pdn096225.heracles.sohuno.com:9092,kfk013219.heracles.sohuno.com:9092,pdn096222.heracles.sohuno.com:9092,pdn096221.heracles.sohuno.com:9092,kfk013220.heracles.sohuno.com:9092",
    "bootstrap.servers" ->   """
                               kfk013218.heracles.sohuno.com:9092,
                               pdn096224.heracles.sohuno.com:9092,
                               kfk013217.heracles.sohuno.com:9092,
                               pdn096223.heracles.sohuno.com:9092,
                               pdn096229.heracles.sohuno.com:9092,
                               pdn096228.heracles.sohuno.com:9092,
                               pdn096226.heracles.sohuno.com:9092,
                               pdn096230.heracles.sohuno.com:9092,
                               pdn096225.heracles.sohuno.com:9092,
                               kfk013219.heracles.sohuno.com:9092,
                               pdn096222.heracles.sohuno.com:9092,
                               pdn096221.heracles.sohuno.com:9092,
                               kfk013220.heracles.sohuno.com:9092
                             """,
    "key.serializer" -> classOf[StringSerializer],
    "value.serializer" -> classOf[StringSerializer] ,
    "key.deserializer" -> classOf[StringDeserializer],
    "value.deserializer" -> classOf[StringDeserializer],
    "auto.offset.reset" -> "latest",
    "enable.auto.commit" -> (true: java.lang.Boolean),
    "group.id" -> "spm_refresh2"
//    "offsets.storage" -> "kafka",
//    "session.timeout.ms" -> "30000",
//    "heartbeat.interval.ms" -> "10000"
    //    "request.timeout.ms" -> "30000",
  )

  //土城kafka集群
  val tc_kafka_cluster = Map[String, Object](
    "bootstrap.servers" -> "10.11.159.85:6667,10.11.159.86:6667,10.11.159.87:6667,10.11.159.88:6667,10.11.159.89:6667,10.11.159.90:6667,10.11.159.91:6667,10.11.159.92:6667,10.11.159.93:6667,10.11.159.94:6667",
    "key.serializer" -> classOf[StringSerializer],
    "value.serializer" -> classOf[StringSerializer],
    "key.deserializer" -> classOf[StringDeserializer],
    "value.deserializer" -> classOf[StringDeserializer],
    "auto.offset.reset" -> "latest",
    "enable.auto.commit" -> (true: java.lang.Boolean),
    "group.id" -> "spm_refresh2"

  )
  // rawlog分隔符
  val ORIGINAL_SEPARATOR = '\u0003';

  val pv_test_hive_table = "dwd_pv_di_test"
  val pv_test_hive_table2 = "dwd_pv_di_testtt"
  val pv_test_hive_table3 = "dwd_pv_di_test_ttt"

  val ev_test_hive_table = "dwd_ev_di_testtt"

  val ev_test_hive_table2 = "dwd_ev_di_testt"

  val action_test_hive_table = "dwd_action_di_testtt"

  val hdfs_path = "hdfs://dc1/user/mediaai/hive/warehouse/mediaai/tables/"

  val hdfs_tmp_path = "hdfs://dc1/user/mediaai/tmp/liangxi/"

  /**  test topic to write **/
  val test_topic = "sohudc_app_click_cleaned_test"

  /** topics to fetch data **/
  val topic_spm_pv = "mediaai_logcollect_wap_pv"
  val topic_spm_ev = "mediaai_logcollect_wap_ev"
  val topic_spm_action = "mediaai_logcollect_wap_click"

  /** new topics to write **/
  val topic_pv_dwd = "mediaai_logcollect_wap_pv_dwd"
  val topic_ev_dwd = "mediaai_logcollect_wap_se_dwd"
  val topic_action_dwd = "mediaai_logcollect_wap_action_dwd"

//  def main(args: Array[String]): Unit = {
//    val business="wap"
//    val dtStr = new SimpleDateFormat("yyyyMMdd").format(new Date().getTime)
//    val sql2 = "insert into table "+Constants.ev_test_hive_table2 +" partition(dt='" +dtStr+"',business='"+ business +"') select * from dwd_web_pv"
//    println(sql2)
//  }
}
