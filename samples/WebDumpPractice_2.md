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