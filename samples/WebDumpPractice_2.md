## Question 1
___
What is true about partitions? (select two)
- [ ] A. A broker can have a partition and its replica on its disk
- [ ] B. You cannot have more partitions than the number of brokers in your cluster
- [ ] C. A broker can have different partitions numbers for the same topic on its disk
- [ ] D. Only out of sync replicas are replicas, the remaining partitions that are in sync are also leader
- [ ] E. A partition has one replica that is a leader, while the other replicas are followers

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
C, E => Only one of the replicas is elected as partition leader. And a broker can definitely hold many partitions from the same topic on its disk, try creating a topic with 12 partitions on one broker!
</details>

<br>
<br>

## Question 2
___
The producer code below features a Callback class with a method called onCompletion(). When will the onCompletion() method be invoked?
- [ ] A. When a consumer sends an acknowledgement to the producer
- [ ] B. When the producer puts the message into its socket buffer
- [ ] C. When the producer batches the message
- [ ] D. When the producer receives the acknowledgment from the broker

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
D => The onCompletion() method of Kafka'sCallback interfaceis executedonce the broker acknowledgesthe message. This includes success or failure, and it isinvoked asynchronouslyby the producer.
FromKafka Java Client API Documentation:
"The onCompletion method will be called when the record sent to the server has been acknowledged, or when an error occurs." This ensures thatthe record was sent and acknowledged, not just added to a batch or local buffer.
Reference:Kafka Java Client API > org.apache.kafka.clients.producer.Callback
</details>

<br>
<br>

## Question 3
___
Kafka producers can batch messages going to the same partition. Which statement is correct about producer batching?
- [ ] A. Producers can only batch messages of the same size.
- [ ] B. Two or more broker failures will automatically disable batching on the producer.
- [ ] C. Producers have a separate background thread for each batch.
- [ ] D. Producers can include multiple batches in a single request to a broker.

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
D => Kafka producers can group messages destined fordifferent partitionsintoseparate batches, and send multiple batches in one requestto optimize network usage.
FromKafka Producer Internals:
"The producer maintains buffers (batches) for each partition and can send multiple of these batches together in a single request to the broker."
* Message size is not a constraint for batching (A is false).
* Broker failures don't disable batching (B is false).
* There isno separate thread per batch(C is false).
Reference:Kafka Producer Architecture and Internals
</details>

<br>
<br>

## Question 4
___
What will happen if a producer tries to send messages to a topic that does not exist in the Kafka cluster?
- [ ] A. Messages will only be sent if auto topic creation is enabled.
- [ ] B. The messages will be sent to a topic called unnamed_topic_1.
- [ ] C. The Producer "send()" call will block indefinitely.
- [ ] D. The brokers will hold the messages until a user creates a topic for the messages.

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
A => No words needed
</details>

<br>
<br>

## Question 5
___
An application is consuming messages from Kafka. You observe partitions being frequently reassigned to the consumer group from the application logs.  
Which factors may be contributing to this? (Choose 2.)
- [ ] A. The number of partitions is not matching the number of application instances.
- [ ] B. There is a slow consumer processing application.
- [ ] C. There is a storage issue on the broker.
- [ ] D. An instance of the application is crashing and getting restarted

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
B, D => No words needed
</details>

<br>
<br>

## Question 6
___
Which configuration determines how many bytes of data are collected before sending messages to the Kafka broker?
- [ ] A. max. request.size
- [ ] B. batch .size
- [ ] C. buffer, memory
- [ ] D. send. buffer, bytes

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
B => No words needed
</details>

<br>
<br>

## Question 7
___
What isn't an internal Kafka Connect topic?
- [ ] A. connect-status
- [ ] B. connect-offsets
- [ ] C. connect-configs
- [ ] D. connect-jars

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
D => connect-configs stores configurations, connect-status helps to elect leaders for connect, and connect-offsets store source offsets for source connectors
</details>

<br>
<br>

## Question 8
___
Your Kafka cluster has five brokers. The topic t1 on the cluster has:
* Two partitions
* Replication factor = 4
* min.insync.replicas = 3
You need strong durability guarantees for messages written to topic t1.You configure a producer acks=all and all the replicas for t1 are in-sync.How many brokers need to acknowledge a message before it is considered committed?
- [ ] A. 2
- [ ] B. 3
- [ ] C. 4
- [ ] D. 5

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
B => With acks=all, the leader waits formin.insync.replicasto acknowledge the message. Since min.insync.
replicas=3, Kafka will only commit the messageonce 3 brokers (leader + 2 followers)confirm they have the message.
From Kafka Documentation > Acks and Durability:
"If acks=all is specified, the producer will wait until the full set of in-sync replicas has acknowledged the record. The minimum number of in-sync replicas is controlled by min.insync.replicas." Even though the replication factor is 4, only3 acknowledgments are needed, as defined by min.insync.
replicas.
Reference:Apache Kafka Producer Configs > acks, min.insync.replicas
</details>

<br>
<br>

## Question 9
___
What are two examples of performance metrics? (Select two.)
- [ ] A. fetch-rate
- [ ] B. Number of active users
- [ ] C. total-login-attempts
- [ ] D. incoming-byte-rate
- [ ] E. Number of active user sessions
- [ ] F. Time of last failed login

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
A , D => * fetch-rate: Measures how frequently consumers fetch data - critical for monitoring throughput.
* incoming-byte-rate: Shows how much data is being written to the broker per second - a key producer-side metric.
FromKafka JMX Metrics Guide:
"fetch-rate, incoming-byte-rate, request-latency are key performance metrics for producers and consumers."
* B, C, E, and F areapplication-layer business metrics, not Kafka performance metrics.
Reference:Kafka Monitoring > JMX Metrics
</details>

<br>
<br>

## Question 10
___
You are working on a Kafka cluster with three nodes. You create a topic named orders with:
* replication.factor = 3
* min.insync.replicas = 2
* acks = all
What exception will be generated if two brokers are down due to network delay?
- [ ] A. NotEnoughReplicasException
- [ ] B. NetworkException
- [ ] C. NotCoordinatorException
- [ ] D. NotLeaderForPartitionException


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
A  => With acks=all and min.insync.replicas=2, Kafka requires at least two in-sync replicas to acknowledge a write.
If only one broker is alive, the condition fails, and NotEnoughReplicasExceptionis thrown by the producer.
From Kafka Producer Exception Docs:
"NotEnoughReplicasException is thrown when the number of in-sync replicas is insufficient to satisfy acks=all with min.insync.replicas."
* NetworkException is generic and not raised here.
* NotCoordinatorException is related to consumer group coordination.
* NotLeaderForPartitionException is unrelated unless accessing an unassigned leader.
Reference:Kafka Producer Error Handling
</details>

<br>
<br>

## Question 11
___
Which KSQL queries write to Kafka?
- [ ] A. COUNT and JOIN
- [ ] B. SHOW STREAMS and EXPLAIN <query> statements
- [ ] C. CREATE STREAM WITH <topic> and CREATE TABLE WITH <topic>
- [ ] D. CREATE STREAM AS SELECT and CREATE TABLE AS SELECT


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
C , D  => SHOW STREAMS and EXPLAIN <query> statements run against the KSQL server that the KSQL client is connected to. They don't communicate directly with Kafka. CREATE STREAM WITH <topic> and CREATE TABLE WITH <topic> write metadata to the KSQL command topic. Persistent queries based on CREATE STREAM AS SELECT and CREATE TABLE AS SELECT read and write to Kafka topics.
Non-persistent queries based on SELECT that are stateless only read from Kafka topics, for example:
SELECT A FROM foo WHERE A;
Non-persistent queries that are stateful read and write to Kafka, for example, COUNT and JOIN. The data in Kafka is deleted automatically when you terminate the query with CTRL-C.
</details>

<br>
<br>

## Question 12
___
In Kafka, every broker... (select three)
- [ ] A. contains all the topics and all the partitions
- [ ] B. knows all the metadata for all topics and partitions
- [ ] C. is a controller
- [ ] D. knows the metadata for the topics and partitions it has on its disk
- [ ] E. is a bootstrap broker
- [ ] F. contains only a subset of the topics and the partitions


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
B , E , F => Kafka topics are divided into partitions and spread across brokers. Each brokers knows about all the metadata and each broker is a bootstrap broker, but only one of them is elected controller
</details>

<br>
<br>

## Question 13
___
If a topic has a replication factor of 3...
- [ ] A. 3 replicas of the same data will live on 1 broker
- [ ] B. Each partition will live on 4 different brokers
- [ ] C. Each partition will live on 2 different brokers
- [ ] D. D. Each partition will live on 3 different brokers


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
D => Replicas are spread across available brokers, and each replica = one broker. RF 3 = 3 brokers 
</details>

<br>
<br>

## Question 14
___
Where are the dynamic configurations for a topic stored?
- [ ] A. In Zookeeper
- [ ] B. In an internal Kafka topic __topic_configuratins
- [ ] C. In server.properties
- [ ] D. On the Kafka broker file system


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
A => Dynamic topic configurations are maintained in Zookeeper.
</details>

<br>
<br>

## Question 15
___
A bank uses a Kafka cluster for credit card payments. What should be the value of the property unclean.leader.election.enable?
- [ ] A. FALSE
- [ ] B. TRUE


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
A => Setting unclean.leader.election.enable to true means we allow out-of-sync replicas to become leaders, 
we will lose messages when this occurs, effectively losing credit card payments and making our customers very angry.
</details>

<br>
<br>

## Question 16
___
How can you gracefully make a Kafka consumer to stop immediately polling data from Kafka and gracefully shut down a consumer application?
- [ ] A. Call consumer.wakeUp() and catch a WakeUpException
- [ ] B. Call consumer.poll() in another thread
- [ ] C. Kill the consumer thread


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
A => No words needed
</details>

<br>
<br>

## Question 18
___
You want to send a message of size 3 MB to a topic with default message size configuration. How does KafkaProducer handle large messages?
- [ ] A. KafkaProducer divides messages into sizes of max.request.size and sends them in order
- [ ] B. KafkaProducer divides messages into sizes of message.max.bytes and sends them in order
- [ ] C. MessageSizeTooLarge exception will be thrown, KafkaProducer will not retry and return exception immediately
- [ ] D. MessageSizeTooLarge exception will be thrown, KafkaProducer retries until the number of retries are exhausted


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
C => MessageSizeTooLarge is not a retryable exception.
</details>

<br>
<br>

## Question 19
___
If I supply the setting compression.type=snappy to my producer, what will happen? (select two)
- [ ] A. The Kafka brokers have to de-compress the data
- [ ] B. The Kafka brokers have to compress the data
- [ ] C. The Consumers have to de-compress the data
- [ ] D. The Consumers have to compress the data
- [ ] E. The Producers have to compress the data


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
C , E => Kafka transfers data with zero copy and no transformation. Any transformation (including compression) is the responsibility of clients.
</details>

<br>
<br>

## Question 20
___
You are using JDBC source connector to copy data from a table to Kafka topic. There is one connector created with max.tasks equal to 2 deployed on a cluster of 3 workers. How many tasks are launched?
- [ ] A. 3
- [ ] B. 2
- [ ] C. 1
- [ ] D. 6


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
C => JDBC connector allows one task per table.
</details>

<br>
<br>

## Question 21
___
Which producer exceptions are examples of the class RetriableException? (Choose 2.)
- [ ] A. NotEnoughReplicasException
- [ ] B. LeaderNotAvailableException
- [ ] C. AuthorizationException
- [ ] D. RecordTooLargeException


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
A, B => 일시적으로 레플리카 부족한 상황 시간이 지나면 Replica 살아남, 파티션의 리더가 없으면 바로 새로운리더가 선출됨.
</details>

<br>
<br>

## Question 22
___
A consumer wants to read messages from a specific partition of a topic. How can this be achieved?
- [ ] A. Call subscribe(String topic, int partition) passing the topic and partition number as the arguments
- [ ] B. Call assign() passing a Collection of TopicPartitions as the argument
- [ ] C. Call subscribe() passing TopicPartition as the argument



<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
B => assign() can be used for manual assignment of a partition to a consumer, in which case subscribe() must not be used. Assign() takes a collection of TopicPartition object as an argument https://kafka.apache.org/23
/javadoc/org/apache/kafka/clients/consumer/KafkaConsumer.html#assign-java.util.Collection-
</details>

<br>
<br>

## Question 23
___
To import data from external databases, I should use
- [ ] A. Confluent REST Proxy
- [ ] B. Kafka Connect Sink
- [ ] C. Kafka Streams
- [ ] D. Kafka Connect Source



<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
D => Kafka Connect Sink is used to export data from Kafka to external databases and Kafka Connect Source is used to import from external databases into Kafka.
</details>

<br>
<br>

## Question 24
___
What is encoding and decoding of a message also known as?
- [ ] A. Scalability and fault tolerance
- [ ] B. Serialization and deserialization
- [ ] C. Publish and subscribe
- [ ] D. Aggregation and deaggregation



<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
B => 인코딩 , 디코딩 == 직렬화 , 역질렬화
</details>

<br>
<br>

## Question 25
___
What are the requirements for a Kafka broker to connect to a Zookeeper ensemble? (select two)
- [ ] A. Unique value for each broker's zookeeper.connect parameter
- [ ] B. Unique values for each broker's broker.id parameter
- [ ] C. All the brokers must share the same broker.id
- [ ] D. All the brokers must share the same zookeeper.connect parameter



<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
B , D=> Each broker must have a unique broker id and connect to the same zk ensemble and root zNode
</details>

<br>
<br>

## Question 26
___
When auto.create.topics.enable is set to true in Kafka configuration, what are the circumstances under which a Kafka broker automatically creates a topic? (select three)
- [ ] A. Client requests metadata for a topic
- [ ] B. Consumer reads message from a topic
- [ ] C. Client alters number of partitions of a topic
- [ ] D. Producer sends message to a topic



<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
A , B , D=> A kafka broker automatically creates a topic under the following circumstances- When a producer starts writing messages to the topic - When a consumer starts reading messages from the topic - When any client requests metadata for the topic
</details>

<br>
<br>

## Question 27
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
A => JSON, AVRO, PROTOBUF, DELIMITED, KAFKA 지원 
</details>

<br>
<br>

## Question 28
___
Select all the way for one consumer to subscribe simultaneously to the following topics - topic.history, topic.  sports, topic.politics? (select two)
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
A , D => Multiple topics can be passed as a list or regex pattern
</details>

<br>
<br>

## Question 29
___
This schema excerpt is an example of which schema format?
package com.mycorp.mynamespace;
message SampleRecord {
int32 Stock = 1;
double Price = 2;
string Product_Name = 3;
}
- [ ] A. Avro
- [ ] B. Protobuf
- [ ] C. JSON Schema
- [ ] D. YAML



<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
B => This syntax is a clear match toProtocol Buffers (Protobuf). It defines a schema with fields, types, and tags, which is a format supported by Kafka when using Protobuf-based schema registry serialization.
FromConfluent Schema Registry Docs:
"Kafka supports Protobuf serialization, where schemas are written in .proto files and include fields with tags."
* int32, double, and string are standard Protobuf types.
* Avro uses JSON-style schema.
* JSON Schema uses JSON object structure, not .proto.
* YAML is unrelated.
Reference:Confluent Schema Registry for Protobuf
</details>

<br>
<br>

## Question 30
___
Which of the following is not an Avro primitive type?
- [ ] A. string
- [ ] B. long
- [ ] C. int
- [ ] D. date
- [ ] E. null



<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
D => date is a logical type
</details>

<br>
<br>

## Question 31
___
What client protocol is supported for the schema registry? (select two)
- [ ] A. HTTP
- [ ] B. HTTPS
- [ ] C. JDBC
- [ ] D. Websocket
- [ ] E. SASL



<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
A , B => clients can interact with the schema registry using the HTTP or HTTPS interface
</details>

<br>
<br>

## Question 32
___
Clients that connect to a Kafka cluster are required to specify one or more brokers in the bootstrap.
servers parameter. What is the primary advantage of specifying more than one broker?
- [ ] A. It provides redundancy in making the initial connection to the Kafka cluster.
- [ ] B. It forces clients to enumerate every single broker in the cluster.
- [ ] C. It is the mechanism to distribute a topic's partitions across multiple brokers.
- [ ] D. It provides the ability to wake up dormant brokers.



<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
A => The bootstrap.servers setting is used by clients (producers and consumers) to initially connect to the Kafka cluster. 
It does not need to list all brokers-just a few that are up and reachable. The client will then use the cluster metadata to discover the full broker list.
From Kafka Java Client Configuration Docs:
"This is a list of host/port pairs to use for establishing the initial connection to the Kafka cluster. 
The client will make use of all servers irrespective of which servers are specified here for bootstrapping." 
Hence,listing multiple brokers provides redundancy and fault tolerance in the event one is down during client startup.
Reference:Apache Kafka Java Client Configuration > bootstrap.servers
</details>

<br>
<br>

## Question 33
___
Your company has three Kafka clusters: Development, Testing, and Production.
The Production cluster is running out of storage, so you add a new node.
Which two statements about the new node are true?
(Select two.)
- [ ] A. A node ID will be assigned to the new node automatically.
- [ ] B. A newly added node will have KRaft controller role by default.
- [ ] C. A new node will not have any partitions assigned to it unless a new topic is created or reassignment occurs.
- [ ] D. A new node can be added without stopping existing cluster nodes.



<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
C , D => * C is true: When a new broker is added,no partitions are assigned to itunless you create new topics or reassign existing onesusing kafka-reassign-partitions.sh.
* D is true: Kafka brokers arehot-pluggable; no need to stop the cluster when scaling.
FromKafka Operations Guide:
"A newly added broker won't be assigned partitions until reassignments or new topic creation."
"Kafka allows dynamic scaling by adding brokers without downtime."
* A is false: Broker IDs must bemanually setunless using dynamic broker registration in KRaft mode.
* B is false unless the cluster usesKRaft modeand the broker isspecifically assigneda controller role.
Reference:Kafka Operations > Adding Brokers
</details>

<br>
<br>

## Question 34
___
A consumer has auto.offset.reset=latest, and the topic partition currently has data for offsets going from 45 to
2311. The consumer group never committed offsets for the topic before. Where will the consumer read from?
- [ ] A. offset 2311
- [ ] B. offset 0
- [ ] C. offset 45
- [ ] D. it will crash



<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
A => Latest means that data retrievals will start from where the offsets currently end
</details>

<br>
<br>

## Question 35
___
You are sending messages with keys to a topic. To increase throughput, you decide to increase the number of partitions of the topic. Select all that apply.
- [ ] A. All the existing records will get rebalanced among the partitions to balance load
- [ ] B. New records with the same key will get written to the partition where old records with that key were written
- [ ] C. New records may get written to a different partition
- [ ] D. Old records will stay in their partitions



<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
C , D => Increasing the number of partition causes new messages keys to get hashed differently, and breaks the guarantee "same keys goes to the same partition". 
Kafka logs are immutable and the previous messages are not re-shuffled
</details>

<br>
<br>

## Question 36
___
Once sent to a topic, a message can be modified
- [ ] A. No
- [ ] B. Yes



<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
A => 카프카 데이터는 수정할 수 없어! immutable
</details>

<br>
<br>

## Question 37
___
A consumer starts and has auto.offset.reset=latest, and the topic partition currently has data for offsets going from 45 to 2311. 
The consumer group has committed the offset 643 for the topic before. Where will the consumer read from?
- [ ] A. it will crash
- [ ] B. offset 2311
- [ ] C. offset 643
- [ ] D. offset 45


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
C => The offsets are already committed for this consumer group and topic partition, so the property auto.offset.reset is ignored
</details>

<br>
<br>

## Question 38
___
Compaction is enabled for a topic in Kafka by setting log.cleanup.policy=compact. What is true about log compaction?
- [ ] A. After cleanup, only one message per key is retained with the first value
- [ ] B. Each message stored in the topic is compressed
- [ ] C. Kafka automatically de-duplicates incoming messages based on key hashes
- [ ] D. After cleanup, only one message per key is retained with the latest value Compaction changes the offset of messages


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
D => Log compaction retains at least the last known value for each record key for a single topic partition. 
All compacted log offsets remain valid, even if record at offset has been compacted away as a consumer will get the next highest offset.
</details>

<br>
<br>

## Question 39
___
You need to configure a sink connector to write records that fail into a dead letter queue topic.
Requirements:
* Topic name: DLQ-Topic
* Headers containing error context must be added to the messagesWhich three configuration parameters are necessary?(Select three.)
- [ ] A. errors.tolerance=all
- [ ] B. errors.deadletterqueue.topic.name=DLQ-Topic
- [ ] C. errors.deadletterqueue.context.headers.enable=true
- [ ] D. errors.tolerance=none
- [ ] E. errors.log.enable=true
- [ ] F. errors.log.include.messages=true


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
A , B , C => To send failed records to adead letter queue (DLQ), you must configure:
* errors.tolerance=all: Tells the connector tonot failon errors but handle them (e.g., send to DLQ).
* errors.deadletterqueue.topic.name=DLQ-Topic: Specifies the DLQ topic.
* errors.deadletterqueue.context.headers.enable=true: Includes error context in message headers.
FromKafka Connect Error Handling Docs:
"Kafka Connect supports directing problematic records to a separate topic (DLQ) using errors.* configs.
Headers can include failure metadata."
Options D, E, F are related tologging, not DLQ behavior.
Reference:Kafka Connect Configurations > Error Handling
</details>

<br>
<br>

## Question 40
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
B => When using Kafka Connect with sink connectors (like S3 Sink),each task is assigned one or more topic partitions. Thetotal number of partitions (6)isevenly distributedacross the available tasks (2). Thus, each task will handle3 partitions.
FromKafka Connect Documentation:
"Kafka Connect divides the topic partitions among available tasks. For example, a topic with six partitions and two tasks results in each task handling three partitions." Reference:Kafka Connect Concepts > Tasks and Partitions
</details>

<br>
<br>

## Question 41
___
You are using JDBC source connector to copy data from 3 tables to three Kafka topics. 
There is one connector created with max.tasks equal to 2 deployed on a cluster of 3 workers. How many tasks are launched?
- [ ] A. 2
- [ ] B. 1
- [ ] C. 3
- [ ] D. 6


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
A => here, we have three tables, but the max.tasks is 2, so that's the maximum number of tasks that will be created
</details>

<br>
<br>

## Question 42
___
To continuously export data from Kafka into a target database, I should use
- [ ] A. Kafka Producer
- [ ] B. Kafka Streams
- [ ] C. Kafka Connect Sink
- [ ] D. Kafka Connect Source


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
C => Kafka Connect Sink is used to export data from Kafka to external databases and Kafka Connect Source is used to import from external databases into Kafka.
</details>

<br>
<br>

## Question 43
___
You have a Kafka cluster and all the topics have a replication factor of 3. 
One intern at your company stopped a broker, and accidentally deleted all the data of that broker on the disk. What will happen if the broker is restarted?
- [ ] A. The broker will start, and other topics will also be deleted as the broker data on the disk got deleted
- [ ] B. The broker will start, and won't be online until all the data it needs to have is replicated from other leaders
- [ ] C. The broker will crash
- [ ] D. The broker will start, and won't have any data. If the broker comes leader, we have a data loss


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
B => Kafka replication mechanism makes it resilient to the scenarios where the broker lose data on disk, 
but can recover from replicating from other brokers. This makes Kafka amazing!
</details>

<br>
<br>

## Question 44
___
What is the function of Kafka Connect Converters?
- [ ] A. Serialize data written to and deserialize data read from Kafka topics.
- [ ] B. Make simple updates to data as it is written to and read from Kafka topics.
- [ ] C. Convert data from the structure used by the source system to the standard Connect structure.
- [ ] D. Isolate the producer and consumer work from the source and sink connectors.


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
A => written to: Connect → Kafka 토픽으로 데이터 쓸 때 serialize / read from: Kafka 토픽에서 → Connect로 데이터 읽을 때 deserialize
</details>

<br>
<br>

## Question 45
___
Which of the following statements are true regarding the number of partitions of a topic?
- [ ] A. The number of partitions in a topic cannot be altered
- [ ] B. We can add partitions in a topic by adding a broker to the cluster
- [ ] C. We can add partitions in a topic using the kafka-topics.sh command
- [ ] D. We can remove partitions in a topic by removing a broker
- [ ] E. We can remove partitions in a topic using the kafka-topics.sh command


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
C => We can only add partitions to an existing topic, and it must be done using the kafka-topics.sh command. 우리는 파티션 수를 줄일수 있는 방법은 없다!
</details>

<br>
<br>

## Question 46
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

## Question 47
___
Refer to the producer code below. It features a 'Callback' class with a method called 'onCompletion()'.
In the 'on Completion*.)' method, what does the 'metadata.offset()' value represent? producer.send(record, new MyCallback(record));
- [ ] A. The id of the partition that the message was committed to
- [ ] B. Its position in the producer's batch of messages
- [ ] C. The number of bytes that overflowed beyond a producer batch of messages
- [ ] D. The sequential id of the message is committed into a partition


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
D => 프로듀서 메시지 전송, 브로커가 파티션에 저장, 저장된 위치 순차 번호 callback
</details>

<br>
<br>

## Question 48
___
You are managing the schema of data in a Kafka Topic using Schema Registry. 
You need to add new fields to the message schema. 
You need to select a compatibility type that allows you to add required fields, delete optional fields, 
and allows consumers to read all previous versions of the schema.
Which compatibility type is correct?
- [ ] A. FULL_TRANSITIVE
- [ ] B. FORWARD
- [ ] C. BACKWARD
- [ ] D. FORWARD_TRANSITIVE


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
A => 스키마 레지스트리 호환성 정책. 
</details>

<br>
<br>

## Question 49
___
I am producing Avro data on my Kafka cluster that is integrated with the Confluent Schema Registry. After a schema change that is incompatible, 
I know my data will be rejected. Which component will reject the data?
- [ ] A. The Confluent Schema Registry
- [ ] B. The Kafka Broker
- [ ] C. The Kafka Producer itself
- [ ] D. Zookeeper


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
A => The Confluent Schema Registry is your safeguard against incompatible schema changes and will be the component that ensures no breaking schema evolution will be possible. 
Kafka Brokers do not look at your payload and your payload schema, and therefore will not reject data
</details>

<br>
<br>

## Question 50
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
B => To increase consumer throughput, one effective strategy is to increase the amount of data fetched in each poll 
by raising fetch.max.bytes or max.partition.fetch.bytes. This allows each poll to retrieve more records per request, improving processing efficiency.
FromKafka Consumer Config Docs:
"Increasing fetch size allows consumers to retrieve larger batches of messages, improving throughput and reducing request overhead."
* Removing consumers (A) may reduce parallelism.
* Manual commit (C) adds complexity, not throughput.
* Decreasing session timeout (D) risks unnecessary rebalances.
Reference:Kafka Consumer Configuration > fetch.max.bytes
</details>

<br>
<br>

## Question 51
___
You need to correctly join data from two Kafka topics. What will allow for the co-partitioning? (Choose 2.)
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
C , D => Co-partitioning -> 두 토픽에서 같은 키를 갖은 데이터가 같은 파티션 번호에 위치하도록 하는것? kafkaStreams에서 조인 연산을 위해 필요...
                            같은 파티션 번호, 파티션 번호만 서로 공유 하니깐 파치션 수가 같고, 키가 같고, 키의 해싱전략이 같아야 함.
</details>

<br>
<br>

## Question 52
___
You are running a Kafka Streams application in a Docker container managed by Kubernetes, and upon application restart, 
it takes a long time for the docker container to replicate the state and get back to processing the data. How can you improve dramatically the application restart?
- [ ] A. Mount a persistent volume for your RocksDB
- [ ] B. Increase the number of partitions in your inputs topic
- [ ] C. Reduce the Streams caching property
- [ ] D. Increase the number of Streams threads


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
A => Although any Kafka Streams application is stateless as the state is stored in Kafka, it can take a while and lots of resources to recover the state from Kafka. 
In order to speed up recovery, it is advised to store the Kafka Streams state on a persistent volume, so that only the missing part of the state needs to be recovered.
</details>

<br>
<br>

## Question 53
___
In Avro, adding a field to a record without default is a __ schema evolution
- [ ] A. forward
- [ ] B. backward
- [ ] C. full
- [ ] D. breaking


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
A => Clients with old schema will be able to read records saved with new schema.
</details>

<br>
<br>

## Question 54
___
The exactly once guarantee in the Kafka Streams is for which flow of data?
- [ ] A. Kafka => Kafka
- [ ] B. Kafka => External
- [ ] C. External => Kafka


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
A => Kafka Streams can only guarantee exactly once processing if you have a Kafka to Kafka topology.
</details>

<br>
<br>

## Question 55
___
A customer has many consumer applications that process messages from a Kafka topic. 
Each consumer application can only process 50 MB/s. Your customer wants to achieve a target throughput of 1 GB/s. 
What is the minimum number of partitions will you suggest to the customer for that particular topic?
- [ ] A. 10
- [ ] B. 20
- [ ] C. 1
- [ ] D. 50


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
B => each consumer can process only 50 MB/s, so we need at least 20 consumers consuming one partition so that 50 * 20 = 1000 MB target is achieved.
</details>

<br>
<br>

## Question 56
___
By default, which replica will be elected as a partition leader? (select two)
- [ ] A. Preferred leader broker if it is in-sync and auto.leader.rebalance.enable=true
- [ ] B. Any of the replicas
- [ ] C. Preferred leader broker if it is in-sync and auto.leader.rebalance.enable=false
- [ ] D. An in-sync replica


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
D ??? B , D => Preferred leader is a broker that was leader when topic was created. 
It is preferred because when partitions are first created, the leaders are balanced between brokers. Otherwise, any of the in-sync replicas (ISR) will be elected leader, as long as unclean.leader.election=false (by default)
</details>

<br>
<br>

## Question 57
___
You are building a system for a retail store selling products to customers.
Which three datasets should you model as a GlobalKTable?
(Select three.)
- [ ] A. Inventory of products at a warehouse
- [ ] B. All purchases at a retail store occurring in real time
- [ ] C. Customer profile information
- [ ] D. Log of payment transactions
- [ ] E. Catalog of products


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
A , C , E => AGlobalKTable is a replicated, read-only table available in full on all instances. It's best for reference or lookup datasets such as:
* Product catalog
* Customer profiles
* Warehouse inventory
FromKafka Streams Developer Guide:
"Use GlobalKTable when you need to perform joins using non-partition-aligned reference data that's small enough to replicate."
* Purchases and transactions are high-throughput, time-sensitive streams, not static reference data.
Reference:Kafka Streams Concepts > GlobalKTable
</details>

<br>
<br>

## Question 58
___
In Kafka Streams, by what value are internal topics prefixed by?
- [ ] A. tasks-<number>
- [ ] B. application.id
- [ ] C. group.id
- [ ] D. kafka-streams-


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
B => In Kafka Streams, the application.id is also the underlying group.id for your consumers, and the prefix for all internal topics (repartition and state)
</details>

<br>
<br>

## Question 59
___
In Java, Avro SpecificRecords classes are
- [ ] A. automatically generated from an Avro Schema
- [ ] B. written manually by the programmer
- [ ] C. automatically generated from an Avro Schema + a Maven / Gradle Plugin


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
C => SpecificRecord is created from generated record classes
</details>

<br>
<br>

## Question 60
___
How do you read a table or stream from the beginning of a topic?
- [ ] A. automatically generated from an Avro Schema
- [ ] B. written manually by the programmer
- [ ] C. automatically generated from an Avro Schema + a Maven / Gradle Plugin
- [ ] D. automatically generated from an Avro Schema + a Maven / Gradle Plugin
- [ ] E. automatically generated from an Avro Schema + a Maven / Gradle Plugin
- [ ] F. automatically generated from an Avro Schema + a Maven / Gradle Plugin
- [ ] G. automatically generated from an Avro Schema + a Maven / Gradle Plugin
- [ ] H. automatically generated from an Avro Schema + a Maven / Gradle Plugin


<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
G => 
</details>

<br>
<br>
