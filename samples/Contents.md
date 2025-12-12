 [핵심]
    ├─ Kafka Broker 메시지 저장소 (9092 Port)
    ├─ Zookeeper / KRaft 메타데이터 관리 (2181 Port)
    ├─ Topic, Partition 데이터 구조
    └─ Producer, Consumer(Consumer Group) 클라이언트
 
 [데이터 수집/전송]
    ├─ Producer Client API
    │   └─ Idempotent Producer, Transactions
    ├─ Consumer Client API
    │   └─ Rebalancing, Offset Management
    └─ Kafka Connect DB, 파일, API 연동 (8083 Port)
        ├─ Source Connector
        └─ Sink Connector

 [스트림 처리]
    ├─ Kafka Streams Java 라이브러리
    │    ├─ Stateless 연산 (filter, map)
    │    ├─ Stateful 연산 (aggregate, join)
    │    └─ Interactive Queries (state 조회)
    └─ KsqlDB SQL로 Stream 처리 (8088 Port)
        ├─ Stream 이벤트
        ├─ Table 상태
        └─ Pull/Push Query

 [스키마/데이터 관리]
    ├─ Schema Registry 스키마 버전 관리 (8081 Port)
    ├─ Avro, Protobuf, JSON Schema
    └─ Compatibility Forward, Backward, Full

 [접근성/편의성]
    ├─ REST Proxy HTTP로 Kafka 접근 (8082 Port)
    ├─ Kafka UI 도구들 
    └─ CLI 도구들 kafka-topics, kafka-console 등.

 [성능 최적화]
    ├─ Compression (gzip, snappy, lz4, zstd)
    ├─ Batching & Buffering
    ├─ Partition 설계
    └─ Replica 배치 전략

 [모니터링/운영]
    ├─ Metrics (JMX)
    ├─ Kafka Manager, AKHQ 등
    └─ Prometheus, Grafana 연동

- 여러 서버에 이벤트를 저장하고, 전달하는 분산 메시지 시스템.
- Kafka Protocol, TCP를 통해 이벤트를 통신하는. 서버(Broker, Zookeeper/Kraft) , 클라이언트(Producer, Consumer, Etc) 구조.


## Zookeeper
## Kraft
___
1. kafka Cluster의 상태, 메타데이터 관리와 조정역할을 함. 브로커, 토픽, 파티션에 대한 정보를 유지 관리.
2. Apache Zookeeper라는 이름의 외부 별도 시스템, Kraft는 자체 내장 메커니즘
3. ZooKeeper Node = ZooKeeper 서버 프로세스 (인스턴스) 그러나 보통 Node = 서버로 통용됨.
4. ZooKeeper 앙상블에도 Zookeeper Node 즉 서버를 여러대 놓을 수 있음.
5. ZooKeeper Quorum은 (total/2) + 1 개까지 남을 수 있음. 즉 정족수를 충족해야함. 충족하지 못한다면 Kafka 생태계가 점진적으로 마비됨.
6. ZooKeeper Port에는 2181(클라이언트 연결), 2888(Leader & Follower 통신), 3888(Leader 선출용) 이 있음.

## Kafka Brokers
___
1. 이벤트를 저장하는 Kafka 스토리지 계층의 서버.
2. Kafka Brokers는 데이터를 저장하고, client(producer, consumer)의 요청을 다루는 일을 한다.
3. Kafka Cluster에서 카프카 브로커에 대한 다양한 구성을 관리한다.
4. Controller라는 것이 있음. Cluster가 시작되면 Broker들 중 Zookeeper(Kraft)가 합의 알고리즘을 통해 특정 Broker를 ControllerBroker로 지정.
5. ControllerBroker가 어느 브로커에 어떤 파티션을 배치할지 계획 수립
6. ControllerBroker가 파티션의 복제본을 관리 
7. ControllerBroker가 파티션의 리더를 선출하고 ISR 변경사항 메타데이터에 기록
8. ZooKeeper 의존성을 지우기 위해 Kraft의 Quorum Controller가 있음. 클러스터의 메타데이터, 파티션 리더쉽, 멤버쉽 변화를 관리.
9. Kraft모드에서는 Controller가 QuorumController Cluster로 여러대 구성돼 있음. ControllerBroker가 QuorumController로 진화함.
10. 역할은 동일 파티션 배치, 리더 선출, ISR 관리 등.
11. 단일 Controller가 아닌 Quorum(정족수) 기반, 그 중 실제 일하는 Active Leader가 있고, 장애 시 다른 하나가 리더로 승격 (가장 최신의 로그를 가진 노드가 리더)
12. 장애 복구시 Zookeeper에서는 재선출이 필요해서 느리지만, Kraft에서는 빠르게 증시 승격
13. 파티션의 리더 Broker가 In Sync Replicas, follower 모니터링
14. Broker의 성능을 유지하기 위해선 Throughput, Latency/Responsiveness, Data Transfer Capability, Resource Utilization의 균형있는 관리가 중요.


## Kafka Producers
___
1. 프로듀서는 Kafka에 event를 입력하는 send Client.
2. 프로듀서는 작성할 토픽을 지정하고, 토픽 내 파티션에 이벤트가 할당되는 방식을 제어.
3. 프로듀서에서 효율적인 네트워크 전송을 위해 Serialization을 통해 메시지를 바이트 배열로 변환함. (String, JSON, AVRO, Protobuf to Byte Array)
4. batch.size : 동일한 파티션에 대한 레코드 배치의 최대 바이트 수. 배치가 이 크기에 도달하면 전송됨. default:16kb 네트워크 오버헤드를 줄이고 처리량 효율성을 향상시킴. (파티션마다 별도의 배치, 전송 최적화 관련)
5. linger.ms : 전송하기전 대기하는 시간. default:0ms이고 그 시간이 지나면 전송함. (파티션마다 별도의 배치, 전송 최적화 관련)
6. batch.size , linger.ms 먼저 만족하는 조건에 따라 전송
7. Low Latency를 위해선 linger.ms , batch.size 모두 작게 , High Throughput을 위해선 linger.ms , batch.size 모두 크게
8. buffer.memory : 메시지를 즉시 브로커로 보내기 전 내부 메모리에 잠깐 보관(배치로 모아서 보내거나, 네트워크 대기를 위해). 모든 파티션이 공유하는 전체 메모리. default : 32mb
9. max.block.ms : buffer.memory가 꽉 찼을때 (메시지 생성속도 > 전송속도) max.block.ms동안  buffer.memory에 공간이 생기기를 기다림.
10. acks=0 브로커 응답을 기다리지 않고 buffer.memory를 해제함(전송 순간 해제)
11. acks=1 리더 파티션에 저장되면 buffer.memory 해제
12. acks=all(-1) 리더와 팔로워에 대한 응답을 받으면 버퍼를 해제함.
13. producer의 callback은 프로듀서가 메시지를 전송 한 후 그 결과를 비동기적으로 받아서 처리할 수 있게 해주는 매커니즘.
14. callback또한 acks설정 값에 따라 호출되는 시점이 달라진다.
15. Idempotent Producer는 프로듀서와 브로커 간의 문제. 프로듀서가 토픽/파티션에 메시지를 쓸 때 중복을 방지하는 메커니즘. 
16. 멱등성은 프로듀서가 재시도로 동일한 메시지를 여러 번 전송하더라고 메시지가 파티션에 정확히 한 번만 전달되도록 보장하여 중복을 방지. 순서 보장.
17. 프로듀서 측에서의 데이터 안정성 보장은 properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true")해 달성 가능.
18. properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true")를 하면 acks=all, retries Integer.MAX_VALUE, max.in.flight.requests.per.connection ≤ 5(응답을 안기다리고 연속으로 보냄) 자동으로 설정된다.
19. 설정간 충돌이 발생하면 ConfigException이 발생
20. request.timeout.ms : acks=1 , acks=all 일경우 브로커로부터 응답을 받기 위해 기다리는 시간. 시간내에 못받으면 재시도(다시 메시지 전송)함.
21. retries : 실패한 메시지를 자동으로 재전송하는 횟수 설정. default는 Integer.MAX_VALUE 사실상 무제한으로 시도함
22. delivery.timeout.ms : 메시지 재시도를 하는 시간 default 2분으로, 이 시간동안만 재시도를 함.
23. 기본 Partitioning은 메시지 Key를 입력하면 MurmurHash2함수를 키에 적용하여 해시 결과를 파티션수로 나눈 나머지로 파티션 결정.
24. Key가 없을 경우 kafka 2.4이전 버전에서는 RoundRobin 방식으로, 이후에는 Sticky 방식으로 배분
25. Sticky 방식은 batch.size, linger.ms 조건을 만족하면 다음 파티션으로 전환 배치하는 방식으로 효율성 향상 및 지연시간 감소.
26. 또는 Partitioner 인터페이스를 구현하여, 특정 비즈니스 규칙이나 사용자 정의 기준에 따라 파티션 배분을 하는 커스텀 파티셔너를 사용할 수 있음.
27. properties 설정에서 bootstrap.servers는 필수 설정
28. properties 설정시 Key-Value 모두 String 타입으로 지정하는 것이 표준
29. Producer에서 send() 할 때 동기, 비동기 방식을 선택할 수 있음. 
30. Asynchronous => producer.send(record); 대용량 데이터, 성능 중요할때 사용. Broker의 응답을 기다리지 않고 계속 전송하여 Throughput 상승
31. Synchronous = > producer.send(record).get(); 중요한 데이터, 순서 중요할때 사용
32. producer에서 압축 타입을 고려할 때는 CPU 자원 사용과 대욕폭 절감 간의 균현을 고려하여 선택해야함.
33. compression.type을 설정하면 Producer가 압축하여 Broker에게 전송. 
34. 토픽 레벨에서도 설정가능하지만 일반적이는 않음.
35. consumer는 메시지에 포함된 특수 헤더를 인식하여 합축을 해제. 컨슈머 측에서 압축 타입과 관련된 설정을 하지 않아도 처리 가능함.
36. gzip, snappy, lz4, zstd 압축 타입이 있음. default는 none. 


## Kafka Consumers
___
1. 컨슈머는 Kafka에서 event를 읽는 poll Client.
2. 컨슈머의 데이터 처리중 발생할 수 있는 예외를 타입별로 다르게 처리하고, commit 타이밍을 정확히 제어해야 데이터 손실이나 중복 처리를 방지할 수 있음.
3. kafka는 메시지를 바이트배열로 저장하기 때문에, Consumer가 사용가능한 형식으로 Deserializer가 필요
4. 바이트 배율로 저장하는 이유는? 어떤 개발언어든 사용가능, 네트워크 전송에 최적화 돼있는 효율성, 문자열 숫자 JSON 객체 등 모든 데이터 타입도 가능한 유연성이라는 장점들이 있음.
5. __consumer_offsets는 컨슈머가 직접 관리하는게 아님.
6. Kafka Cluster 내부에 __consumer_offsets 내부 토픽이 있고 거기서 컨슈머 그룹의 offset을 전체 관리함.
7. ConsumerGroup이라는 개념이 있음. 여러 컨슈머들의 논리적 그룹이고, group.id를 지정하여 집합으로 묶을 수 있음.
8. group.id로 묶인 그룹 내에서 파티션을 자동으로 분배하여 병렬 처리, 파티션 할당 조정, 오프셋 관리, 리밸런싱을 할 수 있음
9. 한 ConsumerGroup내에서 토픽의 각 파티션은 오직 한 멤버에 의해 소비됨.
10. 컨슈머 그룹에 새로운 멤버가 참여하거나, 컨슈머가 장애로 HeartBeat 전송에 실패하거나, Consuming하고 있는 파티션 갯수가 증가하면 Kafka Consumer Rebalance(컨슈머그룹의 멤버가 소비하는 토픽의 리밸런싱)가 발생한다. 
11. 각 컨슈머 그룹마다 Group Coordinator가 할당되어(해쉬 로직으로 선정) 해당 그룹의 offset을 관리함. Group별 관리를 통해 효율성과 신뢰성을 높임.
12. Consumer Group Coordinator는 cluster의 Broker 중 한대가 관리 역할을 맡는거임.
13. 그룹 멤버 관리, Consumer Leader 선정, 파티션 할당 정보 다른 consumer 들에게 전달, consumer 상태 모니터링, 오프셋 저장관리, 리밸런스 관장
14. Consumer Leader는 consumer 인스턴 중 하나로, 파티션 할당 전략(예: RangeAssignor, RoundRobinAssignor 등) 에 따라 consumer에게 파티션을 할당하고 Group Coordinator에가 내용 전달.
15. Consumer Rebalance에는 Eager와 Cooperative가 있음
16. Eager는 Stop the world를 발생시킴. 모든 파티션을 회수한 후 전체 재할당. 다운타임 발생.
17. Cooperative 영향받는 소수의 파티션만 재할당. 점진적 리밸런싱으로 다운타임 최소화. Consumer Group 전체에서 안정적인 파티션 할당이 이루어질 때까지 부분 재조정을 여러번 반복할 수 있음 
18. auto.offset.reset : 초기 오프셋이 없거나, 현재 오프셋이 유효하지 않을 때 컨슈머가 어떻게 동작할지를 정의. (__consumer_offsets 내부 토픽에서 이 컨슈머 그룹의 오프셋 조회)
19. auto.offset.reset : earliest - 파티션의 가장 처음부터, latest - 컨슈머가 시작되는 시점의 토픽/파티션에서 가장 최신 오프셋(LEO) 위치부터 읽기 시작, none - 예외를 발생시킴 (수동 처리)
20. consumer.commitSync() : poll()메시지 처리 완료 후, 명시적으로 offset 수동커밋, 기본은 자동 커밋(enable.auto.commit=true) , Group Coordinator로부터 응답을 받기 전까지 다음 코드로 진행하지 않습니다. (blocking)
21. subscribe() : Consumer Group에 참여. 파티션 자동할당. 리밸런싱 발생 consumer.subscribe(Arrays.asList("topic1", "topic2")); (props1.put("group.id", "group-A") 그룹 아이디 설정은 subscribe전에 해야함)
22. assign() : 파티션 수동 할당. 그룹 참여 하지 않고 리밸런싱 발생 안함 consumer.assign(Arrays.asList(new TopicPartition("topic", 0)));
23. seek() : consumer가 읽을 위치(offset)를 수동으로 지정. consumer.seek(topicPartition, offset); 실패한 메시지 재처리, 특정 메시지 반복 분석, 동일 데이터에 대한 비교 분석 위해 씀.
24. poll전에 초기에 입력하거나 consumer.seek(partition0, 100);  에러 발생시 재처리 catch 블록에 적거나, 아무튼 컨슈머 로직의 poll 전에 넣어야 함. (여기서 부터 다시 poll 해라라는 의미)
25. Shutdown Hook을 통해 close() 시키면 그 consumer가 즉시 그룹에서 탈퇴하고 리밸러스 발생. 다른 consumer들이 빠르게 파티션을 재할당받아 메시지 처리 재게
26. session.timeout.ms : consumer의 정상여부 체크 시간. close()가 호출되지 않고 종료되면, Consumer Group Coordinator는 그 consumer가 여전히 살아있다고 판단하여 이 기간동안 대기
27. 파티션의 메시지가 처리되지 않고 타임아웃 후에야 리밸런싱 시작. 그러므로 close()가 제대로 호출되어야 불필요한 메시지 처리 지연을 줄일 수 있음. 또한 close()는 마지막 오프셋을 커밋하여 리밸런싱 후 중복 처리 가능성도 감소시킴.
28. max.poll.interval.ms : poll() 호출 간 최대 허용 시간. poll() 이후 메시지 처리하고 다음 poll()을 호출하는 주기. default는 5분
29. 위 시간 내에 다음 poll()이 호출되지 않으면 해당 컨슈머가 문제있다고 판단하여 리밸런싱
30. 만약 처리 시간이 오래걸리는 복잡한 메시지 처리 로직이 있다면 max.poll.interval.ms를 증가시켜 처리시간을 충분히 확보해야함.
31. fetch.min.bytes : 네트워크 레벨에서 consumer가 broker로부터 받을 최소 데이터 양을 지정. 큰 값으로 설정하면 처리량이 증가함. props.put("fetch.min.bytes", "1048576");
32. fetch.max.wait.ms : fetch.min.bytes 크기에 도달하기 전까지 broker가 대기하는 최대시간.
33. max.poll.records : 애플리케이션 레벨에서 poll() 메서드 한번 호출시 반환되는 최대 레코드 수. 브로커로부터 데이터를 전달 받은 상황에서 내부 버퍼에 저장 후 갯수만큼만 전달. props.put("max.poll.records", "100"); // 100개씩만 주기


## Kafka Core Concepts
___
1. 아파치 카프카는 real-time 데이터를 처리하고, 스트리밍 기능이 가능하도록 설계되어 시스템과 애플리케이션 간에 안정적인 데이터 전송을 제공.
2. 실시간 대용량 데이터를 안정적으로 수집 저장 처리할 수 있는 파이프라인을 제공한다.(DataPipeLine : 데이터 전달 통로. 데이터 소스,수집,가공/변환,저장,분석, 활용 등 데이터가 여러 시스템을 거쳐 흐르도록 한 시스템)
3. Apache Kafka는 Java와 Scala 언어로 개발 된 시스템
4. 분산 아키텍처를 갖추고 있어 확장성이 매우 뛰어나며, 토픽의 파티션을 여러 브로커에 분산하고 복제할 수 있습니다.
5. Commit Log 분산의 원칙: "append-only, ordered, immutable" 데이터 저장 방식을 기반으로, 토픽을 여러 파티션으로 나누고 이를 여러 브로커에 분산 저장하여 높은 처리량, 장애 내성, 확장성을 얻음.
6. ✅ Append-only (끝에만 추가) ✅ 순서대로만 읽기 ✅ 수정/삭제 없음을 통해 자연스럽게 Sequential I/O 구조를 강제하고 빠른 성능을 얻을 수 있음. 
7. 모든 메세지를 디스크에 저장(Disk-based Retention), 설정한 기간동안 보관.
8. 카프카가 대용량 데이터를 저장할 수 있는것도 디스크 기반 저장 덕분. 메모리가 아닌 디스크를 이용하기 때문에 비용 효율적으로 데이터 보관 가능.
9. Offset은 보통 파티션 내 데이터의 고유한 순번을 말함. 메시지가 토픽에 생설될 때 브로커가 자동 할당함.
10. consumer_offset은 컨슈머가 어디까지 읽었는지 추적하는 값으로 Broker가 관리하는 __consumer_offsets라는 내부 Topic에 저장. 은 컨슈머 그룹별로 독립적으로 저장.
11. Kafka는 .index 파일을 사용해 메시지를 빠르게 찾음. 오프셋-> 파일 위치 매핑 정보를 저장하여 전체 파일을 스캔하지 않고 바로 원하는 위치로 점프 가능 
12. 토픽의 실제 메시지 데이터는 .log파일에 들어가 있고 그와 매핑되는 .index 파일도 있음(00000000000000001000.log , 00000000000000001000.index)
13. 특정 offset의 파일을 찾을때는 파일명으로 먼저 세그먼트를 선택하고, .index파일에서 물리적 위치를 찾아 .log에 있는 메시지 데이터를 찾을 수 있음.
14. .log 파일은 CreateTime or LogAppendTime중 하나의 타임스테프만 저장할 수 있음
15. Topic Level에선 message.timestamp.type=CreateTime , message.timestamp.type=LogAppendTime 이런식으로 설정
16. Broker Level에선 server.properties 파일의 log.message.timestamp.type
17. Producer에서 메시지 압축을 통해 네트워크 효율과, Broker 저장소 사용에 있어서 향상을 줌. 하지만 Producer의 압축과정, Consumer의 해제과정에서 CPU자원의 추가 소모가 들어감.
18. gzip zstd lz4 snapp 등의 압축타입을 카프카에서 지원하며, 각 압축 유형은 압축 속도와 효율성 측면에서 균형을 제공함.
19. Partition수를 증가시켜 컨슈머그룹에서 컨슈머를 할당하면 더 많은 병렬처리가 가능하지만 감소는 불가능 함. 
20. 파티션 수 증가 시 Key의 파티션 매핑이 변경되어 동일 Key의 메시지가 다른 파티션으로 분산되며, 이는 순서 보장을 깨뜨림.
21. 로그는 불변(immutable) 특성을 가지므로 이전에 지정된 메시지는 재분배되거나 재정렬 되비 않음.
22. 일반적인 Exactly-Once 는 Producer to Broker Exactly-Once , 이것은 Producer가 Broker에게 정확히 한번 쓰기 보장.
23. props.put("enable.idempotence", "true");
24. props.put("acks", "all");
25. props.put("retries", Integer.MAX_VALUE); 
26. props.put("max.in.flight.requests.per.connection", 5); 
27. Kafka Stream에서 주로 사용하는 Exactly-Once는 읽기 -> 처리 -> 쓰기 전체를 원자적으로 처리, Consumer offset commit까지 Transaction으로 포함.
28. props.put(StreamsConfig.APPLICATION_ID_CONFIG, "order-service");
29. props.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE_V2);
30. 위 설정을 통해 Kafka Streams에서 Exactly-Once를 보장
31. Exactly-Once Semantics (EOS), Read-Process-Write 패턴의 트랜잭션 이라는것 이 있음.
32. Topic에서 메시지 읽기 → 처리 로직(집계/통계) → 다른 Topic에 쓰기 → Consumer offset 커밋, 이 전체 과정을 하나의 트랜잭션으로 묶음.
33. 중간에 장애 발생 시 전체가 롤백되어 메시지가 정확히 한 번만 처리되고, 중복 결과 생성을 방지함.
34. 위와 같은 EOS를 Kafka Streams에선 EXACTLY_ONCE_V2 설정만으로 간단하게 자동 처리됨.
35. 일반 Producer/Consumer API에서는 트랜잭션 관련 메소드(beginTransaction, sendOffsetsToTransaction, commitTransaction 등)를 개발자가 직접 호출해야 함.
36. log.cleanup.policy : compact로 설정하면 LogCompaction 활성화 되고, Producer에서 보낸 같은 Key에 대한 최신값만 유지하고 이전 값들은 삭제.
37. log.cleaner.min.cleanable.ratio=0.2 0.5 이런식으로 조정. 낮을 수록 더 자주 트리거 됨 (오래된 값의 비율)
38. log.cleaner.backoff.ms=15000 Cleaner 스레드 체크 간격 (기본 15초). 
39. log.cleanup.policy : delete 시간/크기 기반 삭제(기본값) , 기간은 7일 bytes는 기본은 무제한. 
40. log.retention.check.interval.ms : 설정 시간마다 백그라운드 스레드가 실행되고 로그 세그먼트 확인(compact, delete, compact+delete)
41. log.retention.hours : 로그 보관 기간 (기본 168시간 = 7일).  이 시간을 초과한 세그먼트 삭제. 
42. log.retention.bytes : 파티션당 최대 로그 크기 (기본 -1 = 무제한). 이 크기를 초과하면 가장 오래된 세그먼트부터 삭제.
43. log.segment.bytes : log 세그먼크 파일의 최대 크기를 지정, 이 크기에 도달하면 새로운 세그먼트 파일로 전환


## Kafka Configuration
___
1. Kafka의 보존 설정은 log.retention.hours , log.retention.bytes , log.retention.minutes가 있다.
2. .log .index .timeindex 3개 파일을 하나의 '세그먼트'라 부르고 설정에 따라 통째로 세그먼트를 삭제한다.
3. JVM Options 중 가비지 컬렉션(G1GC 권장)과 힙 메모리 크기 설정은 Kafka 성능 향상에 중요함. 특히 GC pause 시간을 줄여 처리량과 지연시간을 개선할 수 있음.
4. JVM의 힙크기를 증가시키면 GC 발생전에 더 많은 객체를 메모리에 유지할 수 있음.
5. G1GC는 JVM에 내장된 Garbage Collector로서 java9 부터 기본 GC. 일시 중지 시간을 최소화하여 대형 힙과 고처리량 시스템을 지원
6. kafka-configs --bootstrap-server localhost:9092 --entity-type brokers --entity-name 0 --alter --add-config log.cleaner.threads=2 를 통해 동적으로 변경가능 한 설정이 있음. 
7. Kafka는 일부 설정(Broker/Topic 레벨)을 재시작 없이 동적으로 변경 가능함. kafka-configs 명령어 사용
8. Kafka Quota란게 특정 client(Producer, Consumer)가 Kafka를 독점하지 못하도록 제한하는 기능 
9. ex ) Producer A의 과도한 전송(1GB/s)이 Producer B에 영향 → Quota로 공평하게 제한 (각 100MB/s)
10. kafka-configs.sh 명령어로 관리
11. Producer Quota (쓰기 제한) , Consumer Quota (읽기 제한) , Request Quota (요청 수 제한)
12. zookeeper.properties파일은 Kafka의 클러스터 조정에 필수적인 주키퍼에 대한 구성 세부 정보를 포함하고 있으므로 매우 중요.
13. server.properties 파일에 zookeeper.connect항목 수정을 통해서 zookeeper 연결 설정을 할 수 있음
14. server.properties 내 log.dirs을 통해 kafka log의 저장 디렉토리를 설정 할 수 있다.
15. Broker-level settings : 클러스터 전체 또는 해당 브로커의 모든 토픽에 대한 기본값으로 작동. server.properties파일에 정의하여 전역 기본값을 제공하여 일관된 클러스터 동작을 보장.
16. Topic-level settings : 특정 토픽에만 적용되는 개별 맞춤 설정. 토픽 생성 시 또는 kafka-configs.sh로 동적설정. 브로커 기본값을 덮어쓰고 토픽별 요구사항 충족함.
17. 우선순위는 토픽레벨 설정이 더 높아서 브로커레벨 설정을 덮어씀.
18. Read-only 설정이란 브로커 시작 시에만 읽히는 설정. 실행중에는 변경 불가능하며 반드시 재시작 필요.
19. broker.id (브로커 ID)
20. log.dirs (데이터 경로)
21. listeners (네트워크 리스너)
22. zookeeper.connect (ZK 연결)
23. process.roles (KRaft 역할)
24. 반면 동적 update가능한 설정들은 Kafka 문서의 "Dynamic Update Mode" 컬럼을 확인하면 알 수 있음
25. MirrorMaker 2.0을 이용하여 지리적으로 먼 클러스터로 데이터를 복제하는 것은 데이터 가용성을 보장


## Kafka Setup
___
1. MirrorMaker는 재해 복구, 지역별 서비스, 데이터 통합/분리를 위해 KafkaCluster의 데이터를 다른 KafkaCluster로 복사하는 것.
2. 최소 2개의 cluster 필요
3. MirrorMaker는 Kafka 설치 시 함께 제공되므로 별도 설치가 필요 없고, 설정 파일 작성 후 스크립트를 실행하여 작동시키는 방식.
4. 별도의 MirrorMaker를 두고 bin/connect-mirror-maker.sh mm2.properties 명령어를 통해서 실행
5. Zookeeper, Kraft 또는 별도의 설정을 복사하는건 아니고, 토픽의 메시지 데이터(세그먼트)만 복사한다.
6. MirrorMaker 2.0 가장 중요한 업데이트 내용은
7. Kafka Connect 기반 아키텍처로 전환 (확장성/안정성 향상)
8. Consumer Group 오프셋 자동 동기화 (재해 복구 지원)
9. 정규식 기반 토픽 자동 복제
10. 양방향 복제 지원 (Active-Active)
11. kafka의 재해 복구 계획에는 장애 발생시에도 데이터를 사용할 수 있도록 여러 위치에 데이터를 복제하는 전략이 포함되어야 합니다. 
12. 여러 데이터 센터나 가용 영역에 복제 설정
13. 중요한 메타데이터 백업 
14. 장애 조치와 복구 절차를 정기적으로 테스트 하여 신뢰성 확보 


## Kafka Monitoring
___
1. request-latency-avg 모니터링 지표는 프로듀서 or 컨슈머가 브로커에게 요청을 보낸 후 응답받기까지의 평균시간 (ms) , 낮을 수록 좋은거!
2. Kafka I/O작업에서 비정상적인 패턴 발견되면, 디스크 I/O 메트릭 (사용률, IOPS, 지연시간) , 브로커 로그 (I/O 관련 에러) , 관련 시스템 메트릭 (CPU, 메모리) 순서
3. outgoing-byte-rate : Broker to Consumer Kafka에서 나가는 데이터의 초당 바이트 수. 브로커별로 측정되며 네트워크 처리량에 대한 명환한 가시성을 제공하며, 네트워크 용량 계획과 성능 평가에 필수적
4. Java Management Extensions(JMX)는 자바 애플리케이션 전반의 표준 모니터링 기술. JVM에서 런타임 내부 상태(메모리, 스레드, 성능 메트릭 등)을 확인하고 제어할 수 있음.
5. JMX MBean(메트릭 객체)를 통해 자원을 모니터링 함. JMX에서 활용할 수 있도록 카프카에서 ConsumerLag, ProducerThroughput 같은 핵심 모니터링 지표를 MBean으로 제공함.
6. JVM Health - GC시간, 힙 메모리 사용량, 스레드 수등 JVM 상태를 추적해 메모리 누수나 GC병목 체크
7. ConsumerLag -  프로듀서가 생산한 메시지와 컨슈머가 처리한 메시지 간의 차이입니다.
8. Producer throughput - 생산자 요청 속도, 바이트 전송량등으로 클러스터 부하와 성능을 확인. 


## Kafka Streams
___
1. kafka 생태계 내에서 데이터를 실시간으로 변환/처리를 지원하는 라이브러리.
2. 데이터 변환, 데이터 보강, 복합 이벤트 처리의 기능을 제공하며, Exactly-once 기능을 지원하기 때문에 실시간 애플리케이션에 적합하다.
2. kafka안에서 돌아가고 kafka를 사용하는 애플리케이션.
3. 여러 토픽에서 데이터를 읽어 변환/집계하고, 결과를 다른 토픽으로 출력한다.
4. Kafka Streams의 설정 키 상수에는 _CONFIG 접미사를 사용하는게 권장된다고 함. 더 나아가 Kafka Java API 네이밍 컨벤션.
5. Kafka Streams 인스턴스를 하나 더 추가(수평적 확장)하면 자동 리밸런싱이 발생하여 Task들이 인스턴스들에 재분배되고, 결과적으로 선형적 확장성을 얻는다.
6. Kafka Streams Instance 안에 Task가 있음. Task는 내부 처리 단위이고, Partition당 하나의 Task가 생성됨. 하나의 Instance는 여러 Task를 처리할 수 있음.
7. Task : Partition , 1:1. 입력 토픽의 파티션 갯수만큼 Task 자동생성. 여러 토픽의 파티션을 담당할 수 있으나 권장은 50~200개 파티션이라 한다.
8. 효과적인 Kafka 데이터 파이프 라인은 파티션 수와 kafka Streams의 인스턴스 수를 함께 조정.
9. 파티션 증가 -> 병렬 처리 단위 증가 + 인스턴스 증가 -> 분산 처리 능력 향상 => 처리량 개선 및 처리 시간 단축
10. 데이터를 바라보는 관점에서 KStream, KTable이 있음.
11. KStream은 모든 레코드를 독립적인 이벤트로 인식. 이벤트 스트림. ex) 은행 거래 내역
12. KTable은 현재 상태만 보존하는것. 상태 테이블. ex) 계좌 잔액
13. Kafka Topic(Data Source)에서 KStream 또는 KTable로 읽어와서 처리/변환 후 다른 Topic으로 보내버림.
14. State Stores : Kafka Streams의 stateful연산을 지원하는 핵심 구성요소. 기본적으로 RocksDB(Persistent)로 구현된 디스크 기반 state store를 사용, 인메모리 스토어도 사용할 수 있음.
15. Changelog topics : Persistent state stores를 자동으로 백업하여 Kafka Streams의 상태를 내결함성으로 만듭니다. 모든 state store는 changelog 토픽으로 백업되며, 상태를 복원할 수 있다.
10. Kafka Streams를 처리하는 두 가지 방식에는 DSL과 ProcessorAPI가 있음
11. DSL은 간단한 코드로 데이터를 처리할 수 있음. 예를들어 filter(), map(), join() 등
12. join : KStream/KTable을 결합하는 연산. SQL의 join과 비슷함. Stateful 연산으로 Task 내부 State Store에 매칭에 필요한 데이터를 저장함.
13. KStream-KStream : 양쪽 데이터를 State Store에 저장하고 매칭 대기(Join Window설정 필수)
14. KStream-KTable : KTable은 이미 저장되어 있어 즉시 조회 가능 
15. KTable-KTable : 양쪽 모두 State Store에 저장되어 있음
16. 여러가지 집계 / 연산 기능을 제공. 스트림으로 들어오는 다양한 데이터를 실시간으로 집계 / 연산하는 기능. Kafka Streams의 집계연산은 즉시 자동으로 집계 되고, 들어오면 자동으로 처리됨
17. Re-partitioning : Key 변경 시 데이터를 새 파티션에 재배치하는 과정
18. selectKey : Key 변경 + 재분할 "필요" 표시 실제 재분할은 아직 안함. (Stateless) 
17. groupByKey :  실제 재분할 실행. 레코드를 키별로 그룹화하여 내부 토픽 생성하여 데이터 재배치 (Stateful)
18. mapValues : Key는 유지한채 Value만 원하는 형태로 변환 (toUpperCase, parsJson, ...) (Stateless)
19. reduce : 여러 개의 데이터들을 하나로 집계/축약, 같은 타입으로만 리턴(Integer to Integer) (Stateful)
20. aggregate + custom aggregator : key별 합계, 평균 등 커스텀 집계 로직을 정의하여 사용할 수 있음(sum, min, max), 다른 타입으로 리턴 가능(Integer to Object) (Stateful)
21. count : key별 이벤트 횟수 세기 (Stateful)
22. windowed by : 시간 기반으로 데이터를 윈도우 단위로 그룹화(Stateful). 데이터는 계속해서 흘러들오기 때문에 특정 시간 범위로 끊을때 사용함.
23. 집계 : groupByKey() -> windowedBy() -> aggregate / count / reduce => 결과는 무조건 KTable로 나옴. (현재까지의 집계 결과, 집계는 누적된 상태를 유지)
24. join : KStream - KStream join시 JoinWindow로 시간 범위 지정. Join 된 새로운 KStream 이벤트가 생성
25. Tumbling Window : 고정시간 (분,시간,일 등) 단위로 시간겹침 없이 집계.(1-3, 4-6, 7-9, ...)
26. Hopping Window : 고정크기 윈도우를 일정 간격으로 점프. 중첩시간 있음. (1-3, 3-5, 5-7, ...)
27. Sliding Window :  이벤트를 중심으로 +-N 시간 범위 내의 다른 스트림 이벤트를 찾아 Join. (주문 03시 발생 시 01~05시 사이 결제를 자동 매칭) 
28. Session Window : 비활동 시간(Gap) 으로 구분해서 사용자 활동 패턴 추적. 활동 있으면 세션 유지, Gap시간 동안 비활동 시 세션 종료. 사용자 기준 세션이라 크기가 동적임.
29. Tumbling, Hopping, Sliding Windows 등 시간 기반 윈도우는 도착 유예 기간을 제공하여 늦은 데이터도 윈도우 집계에 포함한다. 이를 통해 더 정확하고 포괄적인 데이터 처리 보장을 한다. 
29. flatMapValues : 하나의 key에 대해 value를 여러개로 쪼개는 무상태 변환 오퍼레이터 (Stateless).
30. key="order123", value={items: [item1, item2, item3]} /  key="order123", value=item1 key="order123", value=item2 key="order123", value=item3 로 분리.
31. branch : 하나의 스트림에 있는 Record를  -> Branch [조건 체크]를 통해 -> 여러 스트림으로 보냄. (Stateless)
32. "TextLinesTopic" 이라는 토픽에서 읽어 들어와서 split하고 word로 groupBy해서 숫자세고, "WordsWithCountsTopic"이라는 것으로 카운팅 데이터를 넘겨줌.
33. ProcessorAPI는 프로그래머가 직접 로직을 구현하는것. Processor Interface 구현해서 로직 작성 해야함(필수 구현) DSL을 대체할 수 있음.
34. Interactive Queries는 stateful 오퍼레이터를 통해 저장된 state store 내부를 조회하는 기능. REST API 방식으로 내부 상태를 조회할 수 있음.
35. KafkaStreams는 to("Topic")을 통해 다른 토픽으로 결과를 전송하여 다운스트림 파이프라인을 구축하기도 하고, Interactive Queries를 통해 state store를 직접 조회하여 실시간 응답을 제공하기도 함.
36. 토픽 방식이 더 표준적이고 안전함. Interactive Queries는 특수케이스용(디버깅, 임시 데이터, 초저지연 조회)
37. Kafka Streams 모니터링 4가지 메트릭 데이터 처리와 커밋 작업의 효율성과 속도에 대한 인사이트를 제공함. 최적의 성능으로 실행되고 있음을 보장하는데 도움이 됨.
38. Metrics 객체가 있음 streams.metrics(), metric 인스턴스의 이름을 체크하여 출력.
38. process-rate : 초당 처리 레코드 수
39. process-latency : 레코드 하나 처리 시간
40. commit-rate : 초당 커밋 횟수
41. commit-latency : 커밋 작업에 걸리는 시간
42. props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
43. props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
44. Key는 String으로 직렬화/역직렬화 Value도 String으로 직렬화/역직렬화 별도로 지정하지 않으면 이 설정이 자동 적용
45. Kafka Streams에서의 에러처리 전략
46. Fail-fast : 기본동작. 에러 발생시 애플리케이션 즉시 중단. deserialization 에러, processing 에러 등에서 발생할 수 있음
47. Log-and-continue : 로그로 기록하고 계속 진행.  deserialization 에러에만 적용. props.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, LogAndContinueExceptionHandler.class);
48. Custom exception handling : 에러 유형별로 다른 exception handler를 설정할 수 있음
49. peek() 메소드는 비변환 연산으로 스트림을 수정하지 않고 데이터를 검사할 수 있는 기회를 제공. 디버깅 로깅 모니터링에서 주로 쓰임


## Confluent Schema Registry
___
1. Schema Registry는 Kafka 생태계 전반에서 메시지 스키마(데이터 구조)를 중앙 관리하고 버전 관리를 제공하는 시스템. 데이터 타입 안전성과, 호환성을 높혀 준다.
2. 독립 실행형 서버로 별도 실행하고, 클라이언트는 라이브러리를 통해 스키마를 자동으로 등록/조회. 메시지에는 스키마 ID만 포함되어 용량 절약
3. schema-registry-start /etc/schema-registry/schema-registry.properties로 실행
4. props.put("schema.registry.url", "http://localhost:8081"); 로 등록해줘야함. 
5. Producer/Consumer, Kafka Connect, Kafka Streams, ksqlDB 등 Kafka와 데이터를 주고받는 모든 컴포넌트에서 사용 가능
6. 스키마 중앙 저장 및 버전 관리, 스키마 등록 및 검증, 조회 및 역직렬화, 호환선 검증 등.
7. 호환성 체크를 켜두면 Schema Registry에서 호환성모드(Forward, Backward, Full, None)에 따라서 막아줌.
8. Forward Compatibility(전방) : 이전 스키마로 새 데이터 읽기 가능. Producer 먼저 업그레이드 해야함.
9. 새 데이터에 phone필드가 있으면 스키마는 phone을 모름. 그렇기 때문에 스키마가 그냥 phone을 무시하고 데이터를 읽어야 함.
10. phone 필드에 optional/default가 있어야 무시 가능. 새 필드 추가할 때 Default값이 있어야 함 
11. Backward Compatibility(후방) : 새 스키마로 이전 데이터 읽기 가능.  Consumer 먼저 업그레이드 해야함
12. 이전 데이터에 phone필드가 없는데, 새 스키마는 phone을 기대함
13. 없는 필드를 default로 채워서 읽을 수 있어야 함. 새필드에 default를 채움
14. 둘의 차이는 필드 삭제할 때 나옴.
15. Forward: 필드 삭제 가능 (이전 스키마로 새 데이터(삭제된 필드 없음)를 읽을 때 그냥 default로 채우면 됨)
16. Backward: 필드 삭제 불가 (새 스키마로 이전 데이터(삭제된 필드 있음)를 읽을 수 없으니)
17. Full Compatibility(전체 호환성) : 둘다 만족. 양방향 호환성 보장을 위해 기본값(default)이 있는 필드만 추가하거나 제거할 수 있음.
   

## Confluent REST Proxy
___
1. HTTP/REST API로 kafka에 접근할 수 있는 Restful Interface.
2. Kafka Client 없이 HTTP 기반으로 메시지를 토픽에 전송하거나, 토픽에서 읽을 수 있음.
3. Http Client : Producer측, Consumer측 역할로 사용할 수 있음. request를 날릴 수 있는 postMan, Web Application, Curl등이 될 수 있음.
4. Rest Proxy는 독립 실행형 서버 애플리케이션 (confluent-rest-proxy-start /etc/kafka-rest/kafka-rest.properties 로 실행 할 수 있음) 8082
5. Producer Http Client : Rest Proxy서버에 바이너리 데이터를 base64로 인코딩하여 전송
6. Rest Proxy : base64문자열을 디코딩 하여 바이트로 변환. Kafka Topic에 데이터 저장
7. Rest Proxy : Kafka Topic에서 바이너리 데이터를 읽어서 base64인코딩 및 전송
7. Consumer Http Client : base64데이터를 받아 디코딩하여 원본 바이너리로 복원
8. HTTP Client → base64 인코딩 → REST Proxy → base64 디코딩 → Kafka Topic(바이너리) → REST Proxy → base64 인코딩 → HTTP Client
9. Kafka client는 더 빠른 바이너리 프로토콜을 사용하고 배치 처리 최적화가 되어 있음.
10. 멱등성 확보, 트랜젝션 사용, 다양한 기능 사용에 있어서 더 편리하기 때문에 저빈도 또는 아주 간략한 프로젝트 아닌이상 client 쓰는게 훨씬 이점이 많음.


## Kafka Connect
___
1. Kafka Connect는 Kafka와 다른 시스템(DB, 파일, API 등) 간에 데이터를 스트리밍하는 도구
2. 플러그인형 커넥터와 작업(Task)으로 구성된 런타임을 띄우고, 커넥터 설정만으로 손쉽게 데이터 이동을 수행 8083
3. connect-standalone connect-standalone.properties connector1.properties 실행
4. Kafka Connect Schema : Connector 개발 시 사용하기도 하지만, 기존 Connector 사용시에도 활용. 데이터의 타입 안전성을 보장하는 메타데이터.
5. Source Connector : 데이터를 읽어서 Kafka로 전송할 때, 나중에 Sink가 올바르게 쓸 수 있도록 타입 정보를 Schema로 만들어 데이터와 함께 전달. Schema 없이 보내면 타입 안정성이 없어져 Sink에서 런타임 에러 발생 가능.
6. Sink Connector : 받은 Schema를 보고 대상 시스템(DB 등)의 타입에 맞게 데이터를 변환하여 저장
7. 스키마는 데이터 구조와 타입을 정의하며, 시스템 간 데이터 통합과 일관성 유지에 중요함


## Kafka KSQL
___
1. KSQL은 Kafka Streams를 SQL로 쉽게 다룰 수 있게 해주는 스트리밍 데이터 베이스
2. Kafka Topic을 테이블 처럼 SQL로 쿼리로 조회/집계/조인.
3. KSQL CLI를 통해, 쿼리를 작성하고 KSQL Stream을 만드는것.(SQL 기반)
4. KSQL PORT : 8088 (KSQL CLI, REST API 상호작용을 포함.)
5. KSQL의 배포 모드에는 Headless Mode, Interactive Mode가 있음.
6. Headless : SQL 파일을 미리 작성하고 서버 시작 시 자동 실행 
7. ex : CREATE STREAM filtered_users AS SELECT * FROM users_stream WHERE age > 20;
8. ksql 서버가 계속 살아있는동안 백그라운드에서 쿼리가 실행되면서 동작함.
9. Interactive : CLI 또는 REST API, 통해 대화형으로 실시간 쿼리 실행
10. Kafka Streams 라이브러리 데이터 집계는 개발자 한정, 컴파일,빌드,배포 코드 수정 등 작업이 필요. 반면 KSQL을 통해서 간단하게 데이터 연산/집계.


## Avro
___
1. Avro는 바이너리 데이터 직렬화 형식임. (protobuf, json ...)
2. 스키마 기반, 바이너리 포맷, kafka 생태계에서 널리 사용.
3. "type" : "record" 라는 key-value가 필수임. "fields" 배열안에 여러 필드가 있음.
3. Avro Logical Type이란 기본 타입을(int, long, string 등) 확장하여 소수점, 날짜와 같은 데이터를 정확하게 표현하기 위한것.
4. decimal(소수점), date(날짜), timestamp(시간) 등
5. Apache Avro 공식 스펙에 따르면 논리 타입은 하이픈(-)으로 구분 (timestamp-millis, timestamp-micros, time-millis, local-timestamp-millis)


## Kafka Testing
___
1. 테스트를 위해 kafka-console-producer.sh와 kafka-console-consumer.sh 사용된다. 


## Kafka Security
___
1. Kafka 보안관련 모니터링 지표
2. failed_authentication_total (실패한 인증 시도를 모니터링하는 지표로, 보안 이벤트 감지에 중요)
3. Access Control List (Access Control List 관리)
4. SASL (Simple Authentication and Security Layer 카프카 인증 매커니즘 작동 여부 확인)
5. ssl_handshake_rate (암호화된 연결 설정 상태 모니터링)
