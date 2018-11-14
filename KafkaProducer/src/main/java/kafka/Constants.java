package kafka;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Author: xiliang
 * @Date: 2018/8/22 16:15
 **/

public class Constants {


    public static final String topic_pv_dwd = "mediaai_logcollect_wap_pv_dwd";
    public static final String topic_ev_dwd = "mediaai_logcollect_wap_se_dwd";
    public static final String topic_action_dwd = "mediaai_logcollect_wap_action_dwd";
    public static Properties kafkaParams = new Properties();

    static {
        kafkaParams.put("bootstrap.servers",  "kfk013218.heracles.sohuno.com:9092,pdn096224.heracles.sohuno.com:9092,kfk013217.heracles.sohuno.com:9092,pdn096223.heracles.sohuno.com:9092,pdn096229.heracles.sohuno.com:9092,pdn096228.heracles.sohuno.com:9092,pdn096226.heracles.sohuno.com:9092,pdn096230.heracles.sohuno.com:9092,pdn096225.heracles.sohuno.com:9092,kfk013219.heracles.sohuno.com:9092,pdn096222.heracles.sohuno.com:9092,pdn096221.heracles.sohuno.com:9092,kfk013220.heracles.sohuno.com:9092");
        kafkaParams.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        kafkaParams.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        kafkaParams.put("group.id", "test-consumer");
        kafkaParams.put("enable.auto.commit", "true");
        kafkaParams.put("auto.commit.interval.ms", "1000");
        kafkaParams.put("session.timeout.ms", "30000");
        kafkaParams.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaParams.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    }
}
