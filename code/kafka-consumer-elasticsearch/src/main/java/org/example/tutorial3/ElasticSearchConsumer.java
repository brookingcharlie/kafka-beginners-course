package org.example.tutorial3;

import com.google.gson.JsonParser;
import org.apache.http.HttpHost;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ElasticSearchConsumer {
    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchConsumer.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        RestHighLevelClient elasticSearchClient = createElasticSearchClient();
        KafkaConsumer<String, String> consumer = createConsumer("twitter_tweets");
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            logger.info("Received {} records ", records.count());
            BulkRequest bulkRequest = new BulkRequest();
            for (ConsumerRecord<String, String> record : records) {
                String tweetJsonString = record.value();
                String tweetId = extractIdFromTweet(tweetJsonString);
                IndexRequest indexRequest = new IndexRequest("twitter")
                        .id(tweetId)
                        .source(tweetJsonString, XContentType.JSON);
                bulkRequest.add(indexRequest);
            }
            if (bulkRequest.numberOfActions() > 0) {
                elasticSearchClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            }
            logger.info("Committing offsets...");
            consumer.commitSync();
            logger.info("Offsets have be committed");
            Thread.sleep(1000);
        }
    }

    private static RestHighLevelClient createElasticSearchClient() {
        RestClientBuilder builder = RestClient.builder(new HttpHost("localhost", 9200, "http"));
        return new RestHighLevelClient(builder);
    }

    private static KafkaConsumer<String, String> createConsumer(String topic) {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "kafka-demo-elasticsearch");
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "10");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Arrays.asList(topic));
        return consumer;
    }

    private static String extractIdFromTweet(String tweetJson) {
        return JsonParser.parseString(tweetJson).getAsJsonObject().get("id_str").getAsString();
    }
}
