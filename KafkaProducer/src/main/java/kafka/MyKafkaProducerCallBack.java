package kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: xiliang
 * @Create: 2018/8/10 20:38
 */
public class MyKafkaProducerCallBack implements Callback {
    private final long startTime;
    private final String message;

    public MyKafkaProducerCallBack(long startTime,String message) {
        this.startTime = startTime;
        this.message = message;
    }

    public void onCompletion(RecordMetadata metadata, Exception exception) {
        long elapsedTime = System.currentTimeMillis() - startTime;
        if (metadata != null) {
            System.out.println(
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(new Date().getTime())
                            + message + " sent to partition(" + metadata.partition() +
                            "),offset(" + metadata.offset() + ") in " + elapsedTime + " ms");
        } else {
            exception.printStackTrace();
        }
    }
}
