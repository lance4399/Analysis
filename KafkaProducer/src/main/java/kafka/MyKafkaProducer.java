package kafka;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
/**
 * @Author: xiliang
 * @Create: 2018/8/10 20:38
 *
 * zkServer.sh start
 * cd ~/Software/kafka_2.11-0.10.2.1/bin/
 * ./kafka-server-start.sh ../config/server.properties
 */
public class MyKafkaProducer {
    private static final Random random = new Random();
    private static final String[] provinces = new String[]{"Jiangsu", "Hubei", "Sichuan", "Guangdong", "Guangxi"};
    private static final Map<String, String[]> provinceCityMap = new HashMap<String, String[]>();
    private static KafkaProducer<String, String> producer;

    static {
        provinceCityMap.put("Jiangsu", new String[] {"Nanjing", "Suzhou"});
        provinceCityMap.put("Hubei", new String[] {"Wuhan", "Jingzhou"});
        provinceCityMap.put("Sichuan", new String[] {"Chengdu", "Panzhihua"});
        provinceCityMap.put("Guangdong", new String[] {"Shenzhen", "Guangzhou"});
        provinceCityMap.put("Guangxi", new String[] {"Hezhou", "Guilin"});
        Map<String,Object> config=new HashMap<String, Object>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"127.0.0.1:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
//		config.put(ProducerConfig.BATCH_SIZE_CONFIG, 1024*1024*5);
        //往kafka服务器提交消息间隔时间，0则立即提交不等待
        config.put(ProducerConfig.LINGER_MS_CONFIG,0);
        producer=new KafkaProducer<>(config);
    }



    public static void main(String[] args) throws Exception {
        String topic = "AdRealTimeLog";
        while (true) {
            //log format: timestamp|province|city|userid|adid
            String province = provinces[random.nextInt(5)];
            String city = provinceCityMap.get(province)[random.nextInt(2)];
            String log = new Date().getTime() + "|" + province + "|" + city + "|" + (random.nextInt(1000)+1) + "|" +  (random.nextInt(200)+1);
            long startTime = System.currentTimeMillis();
            producer.send(new ProducerRecord<String, String>(topic, log), new MyKafkaProducerCallBack(startTime,log));
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
