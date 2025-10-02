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

## Question 21
___
You are working on a Kafka cluster with three nodes. You create a topic named orders with:
* replication.factor = 3
* min.insync.replicas = 2
* acks = allWhat exception will be generated if two brokers are down due to network delay?
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
A => With acks=all and min.insync.replicas=2, Kafka requiresat least two in-sync replicasto acknowledge a write.
If onlyone broker is alive, the condition fails, andNotEnoughReplicasExceptionis thrown by the producer.
FromKafka Producer Exception Docs:
"NotEnoughReplicasException is thrown when the number of in-sync replicas is insufficient to satisfy acks=all with min.insync.replicas."
* NetworkException is generic and not raised here.
* NotCoordinatorException is related to consumer group coordination.
* NotLeaderForPartitionException is unrelated unless accessing an unassigned leader.
Reference:Kafka Producer Error Handling
</details>

<br>
<br>

## Question 22
___
An ecommerce wesbite sells some custom made goods. What's the natural way of modeling this data in Kafka streams?
- [ ] A. Purchase as stream, Product as stream, Customer as stream
- [ ] B. Purchase as stream, Product as table, Customer as table
- [ ] C. Purchase as table, Product as table, Customer as table
- [ ] D. Purchase as stream, Product as table, Customer as stream

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
B => Mostly-static data is modeled as a table whereas business transactions should be modeled as a stream.
</details>

<br>
<br>

## Question 23
___
When using plain JSON data with Connect, you see the following error messageorg.apache.kafka.connect.
errors.DataExceptionJsonDeserializer with schemas.enable requires "schema" and "payload" fields and may not contain additional fields. How will you fix the error?
- [ ] A. Set key.converter, value.converter to JsonConverter and the schema registry url
- [ ] B. Use Single Message Transforms to add schema and payload fields in the message
- [ ] C. Set key.converter.schemas.enable and value.converter.schemas.enable to false
- [ ] D. Set key.converter, value.converter to AvroConverter and the schema registry url

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
C => You will need to set the schemas.enable parameters for the converter to false for plain text with no schema.
</details>

<br>
<br>

## Question 24
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
* A is false: Broker IDs must be manually set unless using dynamic broker registration in KRaft mode.
* B is false unless the cluster usesKRaft modeand the broker isspecifically assigneda controller role.
Reference:Kafka Operations > Adding Brokers
</details>

<br>
<br>

## Question 25
___
You are building a system for a retail store to sell products to customers Which dataset should be modeled as a GlobatKTable? (Choose 3.)
(Select two.)
- [ ] A. Log of payment transactions
- [ ] B. Catalog of products
- [ ] C. Inventory of products at a warehouse e.g 100 items left
- [ ] D. All purchases at a retail store occurring in real time
- [ ] E. Customer profile information

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
B , C , E => GlobalKTable참조 테이터에 적합. 변경이 적고, 조회가 많은 데이터
            kafka Streams API에는 KStream(이벤트 처리 : 주문 클릭 결제같은 이벤트), 
                                 KTable(현재 상태를 저장 : 파티션별로 분산, 큰 상태 데이터) - 사용자 계정 정보, 집계 결과 - 큰 데이터, 같은 키 조인, 메모리 효율 (자주 바뀌는 것들 포인트, 장바구니 등)
                                 GlobalKTable(현재 상태를 저장 : 참조 데이터) 상품 카탈로그, 고객 프로필, 환율 정도, 지역코드 등 - 작은 참조 데이터, 외래 키 조인, 편의성
</details>

<br>
<br>

## Question 26
___
The producer code below features a Callback class with a method called onCompletion().
When will the onCompletion() method be invoked?
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

## Question 27
___
An application is writing AVRO messages using Schema Registry to topic t1. During this process, the Schema Registry becomes unavailable for a few seconds.
What is the expected impact to the application?
- [ ] A. Since messages are cached by the producer, the application will only get an error if the producer is sending the batch at that time.
- [ ] B. The application may not have any impact, unless it is writing messages with a new Schema Definition.
- [ ] C. All messages within that time will receive an error.
- [ ] D. Since the broker will eventually replicate the message Schema, there will not be an error.

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
B => 스키마 레지스트리는 독립된 서버로 돌고있는데, 그게 멈췄을떄 프로듀서는 기존에 보내던 양식을 알고 있어서, 이전 양식 그대로 브로커에 전송함.
</details>

<br>
<br>

## Question 28
___
Which of the following is true regarding thread safety in the Java Kafka Clients?
- [ ] A. One Producer can be safely used in multiple threads
- [ ] B. One Consumer can be safely used in multiple threads
- [ ] C. One Consumer needs to run in one thread
- [ ] D. One Producer needs to be run in one thread

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
A , C => KafkaConsumer is not thread-safe(인스턴스는 반드시 하나의 스레드에서 사용해야함.), KafkaProducer is thread safe.
</details>

<br>
<br>

## Question 29
___
What information isn't stored inside of Zookeeper? (select two)
- [ ] A. Schema Registry schemas
- [ ] B. Consumer offset
- [ ] C. ACL inforomation
- [ ] D. Controller registration
- [ ] E. Broker registration info

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
A , B => Consumer offsets are stored in a Kafka topic __consumer_offsets, and the Schema Registry stored schemas in the _schemas topic.
</details>

<br>
<br>

## Question 30
___
How do you create a topic named test with 3 partitions and 3 replicas using the Kafka CLI?
- [ ] A. bin/kafka-topics.sh --create --broker-list localhost:9092 --replication-factor 3 --partitions 3 --topic test
- [ ] B. bin/kafka-topics-create.sh --zookeeper localhost:9092 --replication-factor 3 --partitions 3 --topic test
- [ ] C. bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 3 --partitions 3 --topic test
- [ ] D. bin/kafka-topics.sh --create --bootstrap-server localhost:2181 --replication-factor 3 --partitions 3 --topic test

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
C => As of Kafka 2.3, the kafka-topics.sh command can take --bootstrap-server localhost:9092 as an argument.
You could also use the (now deprecated) option of --zookeeper localhost:2181.
</details>

<br>
<br>

## Question 31
___
What is the protocol used by Kafka clients to securely connect to the Confluent REST Proxy?
- [ ] A. Kerberos
- [ ] B. SASL
- [ ] C. HTTPS (SSL/TLS)
- [ ] D. HTTP

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
C => Confluent REST Proxy는 HTTP/HTTPS를 통해 카프카 클라이언트가 Kafka브로커에 접근할 수 있게 해주는 RESTful API 서비스
</details>

<br>
<br>

## Question 32
___
How would you describe a connector in ksqlDB?
- [ ] A. DESCRIBE ip_sum EXTENDED;
- [ ] B. DROP CONNECTOR [IF EXISTS] connector_name;
- [ ] C. DESCRIBE CONNECTOR connector_name;
- [ ] D. DESCRIBE connector_name CONNECTOR;

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
C => 커넥터의 정보를 조회하는 구문 ex)DESCRIBE CONNECTOR jdbc_source_connector;
</details>

<br>
<br>

## Question 33
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
A , C , E => AGlobalKTableis a replicated, read-only table available in full on all instances. It's best forreference or lookup datasetssuch as:
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

## Question 34
___
There are two consumers C1 and C2 belonging to the same group G subscribed to topics T1 and T2. Each of the topics has 3 partitions. How will the partitions be assigned to consumers with Partition Assigner being Round Robin Assigner?
- [ ] A. C1 will be assigned partitions 0 and 2 from T1 and partition 1 from T2. C2 will have partition 1 from T1 and partitions 0 and 2 from T2.
- [ ] B. Two consumers cannot read from two topics at the same time
- [ ] C. C1 will be assigned partitions 0 and 1 from T1 and T2, C2 will be assigned partition 2 from T1 and T2.
- [ ] D. All consumers will read from all partitions

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
A => The correct option is the only one where the two consumers share an equal number of partitions amongst the two topics of three partitions. 
An interesting article to read is https://medium.com/@anyili0928/what-i-have- learned-from-kafka-partition-assignment-strategy-799fdf15d3ab
</details>

<br>
<br>

## Question 35
___
The Controller is a broker that is... (select two)
- [ ] A. elected by Zookeeper ensemble
- [ ] B. is responsible for partition leader election
- [ ] C. elected by broker majority
- [ ] D. is responsible for consumer group rebalances

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
A , B => Controller is a broker that in addition to usual broker functions is responsible for partition leader election. 
The election of that broker happens thanks to Zookeeper and at any time only one broker can be a controller
브로커 중 하나로, 일반 브로커 약할 수행 + 파티션 리더 선출, 브로커 추가/제거 감지, isr관리, 메타데이터 변경사항 다른 브로커에게 전달 등등..
zookeeper는 실제 선추ㄹ과정에서 조정자 역할 을 수행, 그러나 Kraft에서는 노드들이 kafka상호 합의 알고리즘을 통해 controller를 선출함
</details>

<br>
<br>

## Question 36
___
We want the average of all events in every five-minute window updated every minute. What kind of Kafka Streams window will be required on the stream?
- [ ] A. Session window
- [ ] B. Tumbling window
- [ ] C. Sliding window
- [ ] D. Hopping window

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
D => A hopping window is defined by two properties the window's size and its advance interval (aka "hop"), e.g., 
a hopping window with a size 5 minutes and an advance interval of 1 minute.
윈도우는 특정 기간 동안의 데이터를 하나의 상태로 묶어 서 집계하는 방식.
Tubling : 고정 크기, 겹치지 않음 , Hopping : 고정 크기, 겹침 5분윈도우를 1분마다 이동하며 집계 , sliding : 고정 크기, 레코드 타임스탬프에 따라 동적으로 겹침 , session : 동적 크기, 비활성시간 기준으로 구분
</details>

<br>
<br>

## Question 37
___
A consumer receives a Kafka message that is serialized using an Avro schema. The consumer does not have cache locally mapping between the schema id and the schema.
What does the consumer do?
- [ ] A. The consumer throws an exception because it does not have the required schema.
- [ ] B. The consumer consumes the message without the schema.
- [ ] C. The consumer retrieves the schema from the schema registry.
- [ ] D. The consumer drops do not consume the message because the mapping is not in its cache.

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
C => 컨슈머는 메시지에서 처음보는 스키마 ID를 읽고, Schema Registry에서 ID의 스키마를 조회하고, 조회한 스키마로 메시지 역직렬화 
    알고 있다면 로컬캐시를 확인해서 있으면 바로 역직렬화
</details>

<br>
<br>

## Question 38
___
You are experiencing low throughput from a Java producer.
Metrics show low I/O thread ratio and low I/O thread wait ratio.
What is the most likely cause of the slow producer performance?
- [ ] A. Compression is enabled.
- [ ] B. The producer is sending large batches of messages.
- [ ] C. There is a bad data link layer (layer 2) connection from the producer to the cluster.
- [ ] D. The producer code has an expensive callback function.

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
D => Low I/O thread activity with blocked throughput often indicates that producer callbacks are consuming too much time, 
causing the sender thread to block while waiting for onCompletion() to finish.
From Kafka Producer Performance Guide:
"Expensive logic in callbacks (e.g., I/O or complex computation) can block the sender thread, reducing throughput."
* Compression (A) may slightly impact CPU but not I/O thread usage.
* Large batches (B) improve throughput if managed correctly.
* A Layer 2 network issue (C) would lead to packet loss, not specifically low callback metrics.
Reference:Kafka Producer Metrics and Performance Tuning
</details>

<br>
<br>

## Question 39
___
Which of the following setting increases the chance of batching for a Kafka Producer?
- [ ] A. Increase batch.size
- [ ] B. Increase message.max.bytes
- [ ] C. Increase the number of producer thread
- [ ] D. Increase linger.ms

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
D => linger.ms forces the producer to wait to send messages, hence increasing the chance of creating batches
batch.size , linger.ms 모두 Producer 설정이며 배치크기, 배치에 합류할 시간 기준임.. linger.ms가 0이면 바로바로 전송인데.
batch.size는 배치가 가득 찬 조건만 제공하여, 메시지 유입이 느리면 효과가 없고, linger.ms 설정이 더 직접적인 batching chance를 늘리는데 더 효과적. 비치 처리 확률.
</details>

<br>
<br>

## Question 40
___
A kafka topic has a replication factor of 3 and min.insync.replicas setting of 2. How many brokers can go down before a producer with acks=1 can't produce?
- [ ] A. 0
- [ ] B. 3
- [ ] C. 1
- [ ] D. 2

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
D => min.insync.replicas does not impact producers when acks=1 (only when acks=all)
</details>

<br>
<br>