FROM confluentinc/cp-kafka:5.4.1

RUN wget -P /tmp "https://github.com/jcustenborder/kafka-connect-twitter/releases/download/0.2.26/kafka-connect-twitter-0.2.26.tar.gz"
RUN mkdir /tmp/kafka-connect-twitter
RUN tar -C /tmp/kafka-connect-twitter/ -x -f /tmp/kafka-connect-twitter-0.2.26.tar.gz
RUN mkdir -p /usr/share/java/kafka-connect-twitter
RUN cp $(find /tmp/kafka-connect-twitter/ -name '*.jar') /usr/share/java/kafka-connect-twitter
