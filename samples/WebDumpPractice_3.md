## Question 1
___
You have a consumer group with default configuration settings reading messages from your Kafka cluster.
You need to optimize throughput so the consumer group processes more messages in the same amount of time.
Which change should you make?
- [ ] A. Remove some consumers from the consumer group.
- [ ] B. Increase the number of bytes the consumers read with each fetch request.
- [ ] C. Disable auto commit and have the consumers manually commit offsets.
- [ ] D. Decrease the session timeout of each consumer.


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
B => To increase consumer throughput, one effective strategy is to increase the amount of data fetched in each poll by 
raising fetch.max.bytes or max.partition.fetch.bytes. 
This allows each poll to retrieve more records per request, improving processing efficiency.
FromKafka Consumer Config Docs:
"Increasing fetch size allows consumers to retrieve larger batches of messages, improving throughput and reducing request overhead."
* Removing consumers (A) may reduce parallelism.
* Manual commit (C) adds complexity, not throughput.
* Decreasing session timeout (D) risks unnecessary rebalances.
Reference:Kafka Consumer Configuration > fetch.max.bytes
</details>

<br>
<br>

## Question 2
___
What happens when broker.rack configuration is provided in broker configuration in Kafka cluster?
- [ ] A. You can use the same broker.id as long as they have different broker.rack configuration
- [ ] B. Replicas for a partition are placed in the same rack
- [ ] C. Replicas for a partition are spread across different racks
- [ ] D. Each rack contains all the topics and partitions, effectively making Kafka highly available


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
C => Partitions for newly created topics are assigned in a rack alternating manner, this is the only change broker. rack does
</details>

<br>
<br>

## Question 3
___
Select all the way for one consumer to subscribe simultaneously to the following topics - topic.history, topic.
sports, topic.politics? (select two)
- [ ] A. consumer.subscribe(Pattern.compile("topic\..*"));
- [ ] B. consumer.subscribe("topic.history"); consumer.subscribe("topic.sports"); consumer.subscribe("topic. politics");
- [ ] C. consumer.subscribePrefix("topic.");
- [ ] D. consumer.subscribe(Arrays.asList("topic.history", "topic.sports", "topic.politics"));


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
A , D => Multiple topics can be passed as a list or regex pattern.
</details>

<br>
<br>

## Question 4
___
You have a topic t1 with six partitions. You use Kafka Connect to send data from topic t1 in your Kafka cluster to Amazon S3. Kafka Connect is configured for two tasks.
How many partitions will each task process?
- [ ] A. 2
- [ ] B. 3
- [ ] C. 6
- [ ] D. 12


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
B => When using Kafka Connect with sink connectors (like S3 Sink),each task is assigned one or more topic partitions. 
The total number of partitions (6)is evenly distributed across the available tasks (2). Thus, each task will handle3 partitions.
From Kafka Connect Documentation:
"Kafka Connect divides the topic partitions among available tasks. For example, a topic with six partitions and two tasks results in each task handling three partitions." Reference:Kafka Connect Concepts > Tasks and Partitions
</details>

<br>
<br>

## Question 5
___
The producer code below features a Callback class with a method called onCompletion().
In the onCompletion() method, when the request is completed successfully, what does the value metadata.offset() represent?
- [ ] A. The sequential ID of the message committed into a partition
- [ ] B. Its position in the producer's batch of messages
- [ ] C. The number of bytes that overflowed beyond a producer batch of messages
- [ ] D. The ID of the partition to which the message was committed


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
A => The offset in the Record Metadata object returned by the producerrepresents the position of the record in the partition- i.e., thesequential IDassigned by Kafka once the message is committed.
FromKafka Producer API Documentation:
"The offset is the position of the record in the partition. This is a unique, sequential number assigned by the broker."
* D refers to metadata.partition(), not offset().
* B and C are unrelated to how Kafka handles committed offsets.
Reference:Kafka Producer Java API > RecordMetadata
</details>

<br>
<br>

## Question 6
___
Producing with a key allows to...
- [ ] A. Ensure per-record level security
- [ ] B. Influence partitioning of the producer messages
- [ ] C. Add more information to my message
- [ ] D. Allow a Kafka Consumer to subscribe to a (topic,key) pair and only receive that data


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
B => Keys are necessary if you require strong ordering or grouping for messages that share the same key. If you require that messages with the same key are always seen in the correct order, attaching a key to messages will ensure messages with the same key always go to the same partition in a topic. Kafka guarantees order within a partition, but not across partitions in a topic, so alternatively not providing a key - which will result in round- robin distribution across partitions - will not maintain such order.
</details>

<br>
<br>

## Question 7
___
Which of the following is NOT a supported serialization format in ksqlDB?
- [ ] A. BSON
- [ ] B. Protobuf
- [ ] C. Delimited
- [ ] D. Avro


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
A => BSON은 몽고DB에서 주로 사용되는 BinaryJSON 포맷이지만 , 따로 지원되지는 않음. 
</details>

<br>
<br>

## Question 8
___
To read data from a topic, the following configuration is needed for the consumers
- [ ] A. all brokers of the cluster, and the topic name
- [ ] B. any broker to connect to, and the topic name
- [ ] C. the list of brokers that have the data, the topic name and the partitions list
- [ ] D. any broker, and the list of topic partitions


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
B => All brokers can respond to Metadata request, so a client can connect to any broker in the cluster.
</details>

<br>
<br>

## Question 9
___
Suppose you have 6 brokers and you decide to create a topic with 10 partitions and a replication factor of 3.
The brokers 0 and 1 are on rack A, the brokers 2 and 3 are on rack B, and the brokers 4 and 5 are on rack C.
If the leader for partition 0 is on broker 4, and the first replica is on broker 2, which broker can host the last replica? (select two)
- [ ] A. 6
- [ ] B. 1
- [ ] C. 2
- [ ] D. 5
- [ ] E. 0
- [ ] F. 3

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
B , E => When you create a new topic, partitions replicas are spreads across racks to maintain availability. Hence, the Rack A, which currently does not hold the topic partition, will be selected for the last replica
</details>

<br>
<br>

## Question 10
___
Which item is not a valid ksqlDB data type?
- [ ] A. BYTES
- [ ] B. LONG
- [ ] C. TIMESTAMP
- [ ] D. VARCHAR

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
B => int, bigint, double, decimal
</details>

<br>
<br>


## Question 11
___
What is a consequence of increasing the number of partitions in an existing Kafka topic?
- [ ] A. Existing data will be redistributed across the new number of partitions temporarily increasing cluster load.
- [ ] B. Records with the same key could be located in different partitions.
- [ ] C. Consumers will need to process data from more partitions which will significantly increase consumer lag.
- [ ] D. The acknowledgment process will increase latency for producers using acks=all.

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
C => Increasing partitions increases parallelism, but also means:
* Consumers in a group may have to handle more partitions, especially if the number of consumers is lower than the number of partitions.
* This can result in increased lag, especially under highload.
From Kafka Topic Management Docs:
"Increasing the number of partitions increases consumer work, and if consumers can't keep up, lag can accumulate."
* A is false:existing data is not redistributed.
* B is false:records with the same key always map to the same partition based on hash.
* D is not directly impacted by the partition count.
Reference:Kafka Topic Management > Adding Partitions


</details>

<br>
<br>


## Question 12
___
What's is true about Kafka brokers and clients from version 0.10.2 onwards?
- [ ] A. Clients and brokers must have the exact same version to be able to communicate
- [ ] B. A newer client can talk to a newer broker, but an older client cannot talk to a newer broker
- [ ] C. A newer client can talk to a newer broker, and an older client can talk to a newer broker
- [ ] D. A newer client can't talk to a newer broker, but an older client can talk to a newer broker

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
C => Kafka's new bidirectional client compatibility introduced in 0.10.2 allows this. Read more here
 https://www.confluent.io/blog/upgrading-apache-kafka-clients-just-got-easier/ 


</details>

<br>
<br>

## Question 13
___
An application is consuming messages from Kafka.
The application logs show that partitions are frequently being reassigned within the consumer group.
Which two factors may be contributing to this?
(Select two.)
- [ ] A. There is a slow consumer processing application.
- [ ] B. The number of partitions does not match the number of application instances.
- [ ] C. There is a storage issue on the broker.
- [ ] D. An instance of the application is crashing and being restarted.

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
A , D => Frequent rebalances in a consumer group occur when:
* Consumers are too slow and miss heartbeats (A), or
* Instances crash or restart, triggering group membership changes (D)
From Kafka Consumer Group Coordination Docs:
"If a consumer fails to send a heartbeat in time (due to slowness or crash), it is removed from the group, causing a rebalance." Option B (partition mismatch) affectsload balancing, not rebalance frequency.
Option C (broker storage) doesn't trigger consumer rebalances.
Reference:Kafka Consumer Group Coordination and Heartbeats
</details>

<br>
<br>

## Question 14
___
What is the default port that the KSQL server listens on?
- [ ] A. 9092
- [ ] B. 8088
- [ ] C. 8083
- [ ] D. 2181

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
B => Default port of KSQL server is 8088
</details>

<br>
<br>

## Question 15
___
There are 3 producers writing to a topic with 5 partitions. There are 5 consumers consuming from the topic. How many Controllers will be present in the cluster?
- [ ] A. 3
- [ ] B. 5
- [ ] C. 2
- [ ] D. 1

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
D => There is only one controller in a cluster at all times.
</details>

<br>
<br>

## Question 16
___
You need to correctly join data from two Kafka topics.
Which two scenarios will allow for co-partitioning?
(Select two.)
- [ ] A. Both topics have the same number of partitions.
- [ ] B. Both topics have the same key and partitioning strategy.
- [ ] C. Both topics have the same value schema.
- [ ] D. Both topics have the same retention time.

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
A , B => co-partitioning 두 토픽 모두 같은 수의 파티션 번호에 위치하게 하는 전략, 같은 키의 데이터 두 토픽에서 불러와 쓸 수 있음
</details>

<br>
<br>

## Question 17
___
Select all that applies (select THREE)
- [ ] A. min.insync.replicas is a producer setting
- [ ] B. acks is a topic setting
- [ ] C. acks is a producer setting
- [ ] D. min.insync.replicas is a topic setting
- [ ] E. min.insync.replicas matters regardless of the values of acks
- [ ] F. min.insync.replicas only matters if acks=all

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
C , D , F => acks is a producer setting min.insync.replicas is a topic or broker setting and is only effective when acks=all
</details>

<br>
<br>

## Question 18
___
You have a Kafka Connect cluster with multiple connectors.
One connector is not working as expected.
How can you find logs related to that specific connector?
- [ ] A. Modify the log4j.properties file to enable connector context.
- [ ] B. Modify the log4j.properties file to add a dedicated log appender for the connector.
- [ ] C. Change the log level to DEBUG to have connector context information in logs.
- [ ] D. Make no change, there is no way to find logs other than by stopping all the other connectors.

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
B => To isolate logs for a specific connector, you can configurea separate logger and appenderin theConnect worker's log4j.propertiesfile, using the connector's name as the logging context.
FromKafka Connect Logging Docs:
"Kafka Connect loggers use hierarchical logger names. You can configure per-connector log levels and output files by extending log4j.properties."
* A and C change verbosity but don't separate logs.
* D is false; targeted logging is possible.
Reference:Kafka Connect > Logging and Debugging
</details>

<br>
<br>

## Question 19
___
What is the default maximum size of a message the Apache Kafka broker can accept?
- [ ] A. 1MB
- [ ] B. 2MB
- [ ] C. 5MB
- [ ] D. 10MB

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
A => The default maximum message size that a Kafka broker accepts is 1MB (1,048,576 bytes), controlled by the config property message.max.bytes.
From Kafka Broker Configuration Docs:
"The default maximum message size is 1MB. To accept larger messages, configure message.max.bytes and the producer's max.request.size."  
Producers also have a matching limit via max.request.size, and consumers via fetch.message.max.bytes.
Reference:Kafka Broker Configuration > message.max.bytes
</details>

<br>
<br>

## Question 20
___
You need to correctly join data from two Kafka topics.
What will allow for the co-partitioning? (Choose 2.)
- [ ] A. Both topics have the same retention time.
- [ ] B. Both topics have the same value schema.
- [ ] C. Both topics have the same key and partitioning strategy.
- [ ] D. Both topics have the same number of partitions.

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
C , D => 위와 같은 co-partitioning 두 토픽 모두 같은 수의 파티션 번호에 위치하게 하는 전략, 같은 키의 데이터 두 토픽에서 불러와 쓸 수 있음
</details>

<br>
<br>