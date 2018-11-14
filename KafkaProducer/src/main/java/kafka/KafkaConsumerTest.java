package kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

public class KafkaConsumerTest {
    public static void main(String[] args) {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(Constants.kafkaParams);
//        getRealTimePv(consumer);
//        getRealTimeEv(consumer);
        getRealTimeAction(consumer);
    }
    public static void getRealTimePv(  KafkaConsumer<String, String> consumer){
        consumer.subscribe(Arrays.asList(Constants.topic_pv_dwd));
        System.out.println("about to consume messages...");
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(1000);
            for (ConsumerRecord<String, String> record : records)
                System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
        }
    }

    public static void getRealTimeEv(  KafkaConsumer<String, String> consumer){
        consumer.subscribe(Arrays.asList(Constants.topic_ev_dwd));
        System.out.println("about to consume messages...");
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(1000);
            for (ConsumerRecord<String, String> record : records)
                System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
        }
    }
    public static void getRealTimeAction(  KafkaConsumer<String, String> consumer){
        consumer.subscribe(Arrays.asList(Constants.topic_action_dwd));
        System.out.println("about to consume messages...");
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(1000);
            for (ConsumerRecord<String, String> record : records)
                System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
        }
    }
}
