package io.conduktor.demos.kafka.wikimedia;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;
import okhttp3.Headers;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.net.URI;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class WikimediaChangesProducer {
    public static void main(String[] args) throws InterruptedException {
        String bootstrapSevers = "172.21.132.209:9092";

        // create Producer Properties
        Properties properties = new Properties();

        // connect to Localhost
        properties.setProperty("bootstrap.servers", bootstrapSevers);

        // set producer properties
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());

        //set safe producer configs(Kafka <= 2.8)
        properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all"); // same as setting -1
        properties.setProperty(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE)); // same as setting MAX_VALUE

        // set high throughput producer config
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "0");
        properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32 * 1024));
        properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");

        // create the Producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        String topics = "wikimedia.recentchange";
        String UA = "WikimediaChangeStreamer/1.0 (contact: you@example.com)";

        EventHandler eventHandler = new WikimediaChangeHandler(producer, topics);
        String url = "https://stream.wikimedia.org/v2/stream/recentchange";
        EventSource.Builder builder = new EventSource.Builder(eventHandler, URI.create(url)).headers(Headers.of(
                "User-Agent", UA, "API-User-Agent", UA));
        EventSource eventSource = builder.build();

        //start the producer in another thread
        eventSource.start();

        // we produce for 10 minutes and block the program until then / 메인스레드 종료 방지
        TimeUnit.MINUTES.sleep(10);

    }
}