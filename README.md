# Notes on Kafka for Beginners course

## Running the Kafka server using Docker

    docker-compose up zookeeper kafka

## Running the Kafka CLI using Docker

    docker-compose exec kafka kafka-topics --zookeeper zookeeper:2181 --list

    docker run --rm --net host confluentinc/cp-kafka:5.4.1 kafka-topics --zookeeper 127.0.0.1:2181 --list

    $ docker run --rm --net host -i -t confluentinc/cp-kafka:5.4.1 bash
    root@docker-desktop:/# kafka-topics --zookeeper 127.0.0.1:2181 --list
    __confluent.support.metrics
    __consumer_offsets
    first_topic
    root@docker-desktop:/# exit

## Running ElasticSearch using Docker

    docker-compose up elasticsearch

## Using the ElasticSearch Web API

    $ curl 'http://localhost:9200/_cat/nodes?v'
    ip         heap.percent ram.percent cpu load_1m load_5m load_15m node.role master name
    172.18.0.2           16          35   1    0.27    0.21     0.13 dilm      *      71ecc2feb242

    $ curl -X PUT 'http://localhost:9200/twitter'
    {"acknowledged":true,"shards_acknowledged":true,"index":"twitter"}

## References

* https://github.com/simplesteph/kafka-stack-docker-compose/blob/master/zk-single-kafka-single.yml
* https://github.com/confluentinc/cp-demo/blob/master/docker-compose.yml
* https://docs.confluent.io/current/quickstart/cos-docker-quickstart.html
