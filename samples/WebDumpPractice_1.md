## Question 1
___
 If a topic has a replication factor of 3...
- [ ] A. 3 replicas of the same data will live on 1 broker
- [ ] B. Each partition will live on 4 different brokers
- [ ] C. Each partition will live on 2 different brokers
- [ ] D. Each partition will live on 3 different brokers

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

## Question 2
___
A Zookeeper ensemble contains 5 servers. What is the maximum number of servers that can go missing and the ensemble still run?
- [ ] A. 3
- [ ] B. 4
- [ ] C. 2
- [ ] D. 1

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
C => majority consists of 3 zk nodes for 5 nodes zk cluster, so 2 can fail
</details>

<br>
<br>

## Question 3
___
Select all the way for one consumer to subscribe simultaneously to the following topics - topic.history, topic.sports, topic.politics? (select two)
- [ ] A. consumer.subscribe(Pattern.compile('topic\..*'));
- [ ] B. consumer.subscribe('topic.history'); consumer.subscribe('topic.sports'); consumer.subscribe('topic.politics');
- [ ] C. consumer.subscribePrefix('topic.');
- [ ] D. consumer.subscribe(Arrays.asList('topic.history', 'topic.sports', 'topic.politics'));

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
A, D => Multiple topics can be passed as a list or regex pattern.
</details>

<br>
<br>

## Question 4
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
A => Kafka logs are append-only and the data is immutable
</details>

<br>
<br>

## Question 5
___
Your topic is log compacted and you are sending a message with the key K and value null. What will happen?
- [ ] A. The broker will delete all messages with the key K upon cleanup
- [ ] B. The producer will throw a Runtime exception
- [ ] C. The broker will delete the message with the key K and null value only upon cleanup
- [ ] D. The message will get ignored by the Kafka broker

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
A => Sending a message with the null value is called a tombstone in Kafka and will ensure the log compacted topic does not contain any messages with the key K upon compaction
</details>

<br>
<br>

## Question 6
___
To produce data to a topic, a producer must provide the Kafka client with...
- [ ] A. the list of brokers that have the data, the topic name and the partitions list
- [ ] B. any broker from the cluster and the topic name and the partitions list
- [ ] C. all the brokers from the cluster and the topic name
- [ ] D. any broker from the cluster and the topic name

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
D => All brokers can respond to a Metadata request, so a client can connect to any broker in the cluster and then figure out on its own which brokers to send data to.
</details>

<br>
<br>

## Question 7
___
StreamsBuilder builder = new StreamsBuilder();
KStream textLines = builder.stream("word-count-input");
KTable wordCounts = textLines
.mapValues(textLine -> textLine.toLowerCase())
.flatMapValues(textLine -> Arrays.asList(textLine.split("\W+")))
.selectKey((key, word) -> word)
.groupByKey()
.count(Materialized.as("Counts"));
wordCounts.toStream().to("word-count-output", Produced.with(Serdes.String(), Serdes.Long()));
builder.build();
- [ ] A. max.message.bytes=10000000
- [ ] B. cleanup.policy=delete
- [ ] C. compression.type=lz4
- [ ] D. cleanup.policy=compact

<details>
<summary>
<strong>
🎯 Answer :
</strong>
</summary>
D => Result is aggregated into a table with key as the unique word and value its frequency. We have to enable log compaction for this topic to align the topic's cleanup policy with KTable semantics.
</details>