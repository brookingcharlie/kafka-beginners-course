# Notes on Kafka for Beginners course

## Running the Kafka server using Docker

    docker-compose up zookeeper kafka

## Running the Kafka CLI using Docker

    docker-compose exec kafka kafka-topics --zookeeper zookeeper:2181 --list

## Running ElasticSearch using Docker

    docker-compose up elasticsearch

## Running Kafka Connect using Docker

To run the Kafka Connect included in the Kafka distribution:

    docker-compose exec kafka connect-standalone \
    /etc/kafka/connect-standalone.properties \
    /mnt/config/connect-twitter.properties

The command above takes advantage of the fact we've mounted our host directory as `/mnt`.
For the Twitter examples in the course, create `./config/connect-twitter.properties` as follows:

    name=TwitterSourceDemo
    tasks.max=1
    connector.class=com.github.jcustenborder.kafka.connect.twitter.TwitterSourceConnector
    
    process.deletes=false
    filter.keywords=bitcoin
    kafka.status.topic=twitter_status_connect
    kafka.delete.topic=twitter_deletes_connect
    twitter.oauth.consumerKey=***YOUR_TWITTER_OAUTH_CONSUMER_KEY***
    twitter.oauth.consumerSecret=***YOUR_TWITTER_OAUTH_CONSUMER_SECRET***
    twitter.oauth.accessToken=***YOUR_TWITTER_OAUTH_ACCESS_TOKEN***
    twitter.oauth.accessTokenSecret=***YOUR_TWITTER_OAUTH_ACCESS_TOKEN_SECRET***

## References

* https://github.com/simplesteph/kafka-stack-docker-compose/blob/master/zk-single-kafka-single.yml
* https://github.com/confluentinc/cp-demo/blob/master/docker-compose.yml
* https://docs.confluent.io/current/quickstart/cos-docker-quickstart.html
