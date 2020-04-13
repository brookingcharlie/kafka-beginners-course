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
