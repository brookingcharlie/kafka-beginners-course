package org.example.tutorial2;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TwitterProducer {
    private static final Logger logger = LoggerFactory.getLogger(TwitterProducer.class);

    public TwitterProducer() {

    }

    public void run() {
        logger.info("Setup");

        // create a twitter client
        BlockingQueue<String> queue = new LinkedBlockingQueue<>(1000);
        Client twitterClient = createTwitterClient(queue);
        twitterClient.connect();

        // create a kafka producer

        // loop to send tweets to kafka
        while (!twitterClient.isDone()) {
            String msg = null;
            try {
                msg = queue.poll(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                twitterClient.stop();
            }
            if (msg != null) {
                logger.info(msg);
            }
        }

        logger.info("End of application");
    }

    private Client createTwitterClient(BlockingQueue<String> queue) {
        Hosts hosts = new HttpHosts(Constants.STREAM_HOST);

        Authentication authentication = new OAuth1(
                System.getProperty("twitter.consumerKey"),
                System.getProperty("twitter.consumerSecret"),
                System.getProperty("twitter.token"),
                System.getProperty("twitter.secret")
        );

        StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
        List<String> terms = Lists.newArrayList("kafka");
        endpoint.trackTerms(terms);

        ClientBuilder builder = new ClientBuilder()
                .name("Hosebird-Client-01")
                .hosts(hosts)
                .authentication(authentication)
                .endpoint(endpoint)
                .processor(new StringDelimitedProcessor(queue));
        return builder.build();
    }

    public static void main(String[] args) {
        new TwitterProducer().run();
    }
}
