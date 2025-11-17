- Kafka는 TCP 네트워크 프로토콜을 통해 이벤트를 통신하는, 서버와 클라이언트로 구성된 분산 시스템.

## Zookeeper
## Kraft
___
1. kafka Cluster의 메타데이터 관리와 조정역할을 함.
2. Zookeeper는 Apache Zookeper라는 외부 별도 시스템, Kraft는 자체 내장 메커니즘
3. ZooKeeper Node = ZooKeeper 서버 프로세스 (인스턴스) 그러나 보통 Node = 서버로 통용됨.
4. ZooKeeper 앙상블에도 Zookeeper Node 즉 서버를 여러대 놓을 수 있음.
5. ZooKeeper Quorum은 (total/2) + 1 개까지 남을 수 있음. 즉 정족수를 충족해야함. 충족하지 못한다면 Kafka 생태계가 점진적으로 마비됨.


## Kafka Brokers
___
1. 이벤트를 저장하는 Kafka 스토리지 계층의 서버.
2. Kafka Brokers는 데이터를 저장하고, client(producer, consumer)의 요청을 다루는 일을 한다.
3. Kafka Cluster에서 카프카 브로커에 대한 다양한 구성을 관리한다.
4. Controller라는 것이 있음. Cluster가 시작되면 Broker들 중 Zookeeper(Kraft)가 합의 알고리즘을 통해 특정 Broker를 ControllerBroker로 지정.
5. ControllerBroker가 Topic 파티션 어느 브로커에 어떤 파티션을 배치할지 계획 수립
6. ControllerBroker가 파티션의 복제본을 관리 
7. ControllerBroker가 파티션의 리더를 선출하고 ISR 변경사항 메타데이터에 기록
8. 파티션의 리더 Broker가 In Sync Replicas, follower 모니터링


## Kafka Producers
___
1. 프로듀서는 Kafka에 event를 입력하는 CLIENT.
2. 프로듀서는 작성할 토픽을 지정하고, 토픽 내 파티션에 이벤트가 할당되는 방식을 제어.
3. 프로듀서에서 효율적인 네트워크 전송을 위해 Serialization을 통해 메시지를 바이트 배열로 변환함. String, JSON, AVRO, Protobuf가 대표적인 직렬화 규칙(방식)
4. batch.size : 동일한 파티션에 대한 레코드 배치의 최대 바이트 수. 배치가 이 크기에 도달하면 전송됨. 파티션마다 별도의 배치를 유지함. default:16kb 네트워크 오버헤드를 줄이고 처리량 효율성을 향상시킴. (전송 최적화 관련)
5. linger.ms : 전송하기전 대기하는 시간. default:0ms이고 그 시간이 지나면 전송함. (전송 최적화 관련)
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
16. 멱등성은 프로듀서가 재시도로 동일한 메시지를 여러 번 전송하더라고 메시지가 파티션에 정확히 한 번만 전달되도록 보장하여 중복을 방지.
17. 프로듀서 측에서의 데이터 안정성 보장은 properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true")해 달성 가능.
18. properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true")를 하면 acks=all retries > 0  max.in.flight.requests.per.connection ≤ 5 자동으로 설정된다.
19. 설정간 충돌이 발생하면 ConfigException이 발생
20. request.timeout.ms : acks=1 , acks=all 일경우 브로커로부터 응답을 받기 위해 기다리는 시간. 시간내에 못받으면 재시도(다시 메시지 전송)함.


## Kafka Consumers
___
1. 컨슈머는 Kafka에서 event를 읽는 CLIENT.
2. __consumer_offsets는 컨슈머가 직접 관리하는게 아님.
3. Kafka Cluster 내부에 __consumer_offsets 내부 토픽이 있고 거기서 컨슈머 그룹의 offset을 전체 관리함.
4. ConsumerGroup이라는 개념이 있음. 여러 컨슈머들의 논리적 그룹이고, group.id를 지정하여 집합으로 묶을 수 있음.
5. 한 ConsumerGroup내에서 토픽의 각 파치션은 오직 한 멤버에 의해 소비됨.
6. 컨슈머 그룹에 새로운 멤버가 참여하거나, 컨슈머가 장애로 HeartBeat 전송에 실패하거나, Consuming하고 있는 파티션 갯수가 증가하면 Kafka Consumer Rebalance(컨슈머그룹의 멤버가 소비하는 토픽의 리밸런식)가 발생한다. 
7. 각 컨슈머 그룹마다 Group Coordinator가 할당되어(해쉬 로직으로 선정) 해당 그룹의 offset을 관리함. Group별 관리를 통해 효율성과 신뢰성을 높임.
8. Consumer Rebalance에는 Eager와 Cooperative가 있음
9. Eager는 Stop the world를 발생시킴. 모든 파티션을 회수한 후 전체 재할당. 다운타임 발생.
10. Cooperative 영향받는 파티션만 재할당. 점진적 리밸런싱으로 다운타임 최소화 
11. auto.offset.reset : 초기 오프셋이 없거나, 현재 오프셋이 유효하지 않을 때 컨슈머가 어떻게 동작할지를 정의. (__consumer_offsets 내부 토픽에서 이 컨슈머 그룹의 오프셋 조회)
12. auto.offset.reset : earliest - 파티션의 가장 처음부터, latest - 컨슈머가 시작되는 시점의 토픽/파티션에서 가장 최신 오프셋(LEO) 위치부터 읽기 시작, none - 예외를 발생시킴 (수동 처리)
13. consumer.commitSync() : poll()메시지 처리 완료 후, 명시적으로 offset 수동커밋, 기본은 자동 커밋(enable.auto.commit=true) , Group Coordinator로부터 응답을 받기 전까지 다음 코드로 진행하지 않습니다. (blocking)
14. subscribe() : Consumer Group에 참여. 파티션 자동할당. 리밸런싱 발생 consumer.subscribe(Arrays.asList("topic1", "topic2")); (props1.put("group.id", "group-A") 그룹 아이디 설정은 subscribe전에 해야함)
15. assign() : 파티션 수동 할당. 그룹 참여 하지 않고 리밸런싱 발생 안함 consumer.assign(Arrays.asList(new TopicPartition("topic", 0)));
16. seek() : consumer가 읽을 위치(offset)를 수동으로 지정. consumer.seek(topicPartition, offset); 실패한 메시지 재처리, 특정 메시지 반복 분석, 동일 데이터에 대한 비교 분석 위해 씀.
17. poll전에 초기에 입력하거나 consumer.seek(partition0, 100);  , 에러 발생시 재처리 catch 블록에 적거나, 아무튼 컨슈머 로직의 poll 전에 넣어야 함. (여기서 부터 다시 poll 해라라는 의미)


## Kafka Core Concepts
___
0. 아파치 카프카는 real-time 데이터를 처리하고, 스트리밍 기능이 가능하도록 설계되어 시스템과 애플리케이션 간에 안정적인 데이터 전송을 제공.
0. 실시간 대용량 데이터를 안정적으로 수집 저장 처리할 수 있는 파이프라인을 제공한다.(DataPipeLine : 데이터 전달 통로. 데이터 소스,수집,가공/변환,저장,분석, 활용 등 데이터가 여러 시스템을 거쳐 흐르도록 한 시스템)
1. Offset은 보통 파티션 내 데이터의 고유한 순번을 말함. 메시지가 토픽에 생설될 때 브로커가 자동 할당함.(consumer_offset은 컨슈머가 어디까지 읽었는지 추적하는 값)
2. Kafka는 .index 파일을 사용해 메시지를 빠르게 찾음. 오프셋-> 파일 위치 매핑 정보를 저장하여 전체 파일을 스캔하지 않고 바로 원하는 위치로 점프 가능 
3. 토픽의 실제 메시지 데이터는 .log파일에 들어가 있고 그와 매핑되는 .index 파일도 있음(00000000000000001000.log , 00000000000000001000.index)
4. 특정 offset의 파일을 찾을때는 파일명으로 먼저 세그먼트를 선택하고, .index파일에서 물리적 위치를 찾아 .log에 있는 메시지 데이터를 찾을 수 있음. 
5. 주키퍼 의존성을 지우기 위해 Kraft의 Quorum Controller가 있음. 클러스터의 메타데이터, 파티션 리더쉽, 멤버쉽 변화를 관리.
6. Kraft모드에서는 Controller가 QuorumController Cluster로 여러대 구성돼 있음. ControllerBroker가 QuorumController로 진화함.
7. 역할은 동일 파티션 배치, 리더 선출, ISR 관리 등.
8. 단일 Controller가 아닌 Quorum(정족수) 기반, 그 중 실제 일하는 Active Leader가 있고, 장애 시 하나가 리더로 승격
9. 장애 복구시 Zookeeper에서는 재선출이 필요해서 느리지만, Kraft에서는 빠르게 증시 승격
10. Producer에서 메시지 압축을 통해 네트워크 효율과, Broker 저장소 사용에 있어서 향상을 줌. 하지만 Producer의 압축과정, Consumer의 해제과정에서 CPU자원의 추가 소모가 들어감.
11. Partition수를 증가시켜 컨슈머그룹에서 컨슈머를 할당하면 더 많은 병렬처리가 가능하지만 감소는 불가능 함. 
12. 파티션 수 증가 시 Key의 파티션 매핑이 변경되어 동일 Key의 메시지가 다른 파티션으로 분산되며, 이는 순서 보장을 깨뜨림.
13. 일반적인 Exactly-Once 는 Producer to Broker Exactly-Once , 이것은 Producer가 Broker에게 정확히 한번 쓰기 보장.
14. props.put("enable.idempotence", "true");
15. props.put("acks", "all");
16. props.put("retries", Integer.MAX_VALUE);
17. props.put("max.in.flight.requests.per.connection", 5); 
18. Kafka Stream에서 주로 사용하는 Exactly-Once는 읽기 -> 처리 -> 쓰기 전체를 원자적으로 처리, Consumer offset commit까지 Transaction으로 포함.
19. props.put("enable.idempotence", "true");
20. props.put("transactional.id", "order-service-producer"); // Producer 인스턴스 하나당 하나의 고유한 값.
21. 읽기 처리 쓰기를 하나의 Transaction으로 묶음. producer.beginTransaction(); producer.send(msg); producer.commitTransaction(); //여기서 트랜잭션 끝
22. log.cleanup.policy : compact로 설정하면 LogCompaction 활성화 되고, Producer에서 보낸 같은 Key에 대한 최신값만 유지하고 이전 값들은 삭제.
23. log.cleaner.min.cleanable.ratio=0.2 0.5 이런식으로 조정. 낮을 수록 더 자주 트리거 됨
24. log.cleaner.backoff.ms=15000 Cleaner 스레드 체크 간격 (기본 15초).
25. log.cleanup.policy : delete 시간/크기 기반 삭제(기본값) , 기간은 7일 bytes는 기본은 무제한.
26. log.retention.check.interval.ms : 설정 시간마다 백그라운드 스레드가 실행되고 로그 세그먼트 확인(compact, delete, compact+delete)
27. log.retention.hours : 로그 보관 기간 (기본 168시간 = 7일).  이 시간을 초과한 세그먼트 삭제. 
28. log.retention.bytes : 파티션당 최대 로그 크기 (기본 -1 = 무제한). 이 크기를 초과하면 가장 오래된 세그먼트부터 삭제.


## Kafka Configuration
___
1. Kafka의 보존 설정은 log.retention.hours , log.retention.bytes , log.retention.minutes가 있다.
2. .log .index .timeindex 3개 파일을 하나의 '세그먼트'라 부르고 설정에 따라 통째로 세그먼트를 삭제한다.
3. JVM Options 중 가비지 컬렉션(G1GC 권장)과 힙 메모리 크기 설정은 Kafka 성능 향상에 중요함. 특히 GC pause 시간을 줄여 처리량과 지연시간을 개선할 수 있음.
4. kafka-configs --bootstrap-server localhost:9092 --entity-type brokers --entity-name 0 --alter --add-config log.cleaner.threads=2 를 통해 동적으로 변경가능 한 설정이 있음. 
5. Kafka는 일부 설정(Broker/Topic 레벨)을 재시작 없이 동적으로 변경 가능함. kafka-configs 명령어 사용
6. Kafka Quota란게 특정 client(Producer, Consumer)가 Kafka를 독점하지 못하도록 제한하는 기능 
7. ex ) Producer A의 과도한 전송(1GB/s)이 Producer B에 영향 → Quota로 공평하게 제한 (각 100MB/s)
8. kafka-configs.sh 명령어로 관리
9. Producer Quota (쓰기 제한) , Consumer Quota (읽기 제한) , Request Quota (요청 수 제한)


## Kafka Setup
___
1. MirrorMaker는 재해 복구, 지역별 서비스, 데이터 통합/분리를 위해 KafkaCluster의 데이터를 다른 KafkaCluster로 복사하는 것.
2. 최소 2개의 cluster 필요
3. MirrorMaker는 Kafka 설치 시 함께 제공되므로 별도 설치가 필요 없고, 설정 파일 작성 후 스크립트를 실행하여 작동시키는 방식.
4. 별도의 MirrorMaker를 두고 bin/connect-mirror-maker.sh mm2.properties 명령어를 통해서 실행
5. Zookeeper, Kraft 또는 별도의 설정을 복사하는건 아니고, 토픽의 메시지 데이터(세그먼트)만 복사한다.
4. MirrorMaker 2.0 가장 중요한 업데이트 내용은
5. Kafka Connect 기반 아키텍처로 전환 (확장성/안정성 향상)
6. Consumer Group 오프셋 자동 동기화 (재해 복구 지원)
7. 정규식 기반 토픽 자동 복제
8. 양방향 복제 지원 (Active-Active)
9. kafka의 재해 복구 계획에는 장애 발생시에도 데이터를 사용할 수 있도록 여러 위치에 데이터를 복제하는 전략이 포함되어야 합니다. 
10. 여러 데이터 센터나 가용 영역에 복제 설정
11. 중요한 메타데이터 백업
12. 장애 조치와 복구 절차를 정기적으로 테스트 하여 신뢰성 확보 


## Kafka Monitoring
___
1. request-latency-avg 모니터링 지표는 프로듀서 or 컨슈머가 브로커에게 요청을 보낸 후 응답받기까지의 평균시간 (ms) , 낮을 수록 좋은거!
2. Kafka I/O작업에서 비정상적인 패턴 발견되면, 디스크 I/O 메트릭 (사용률, IOPS, 지연시간) , 브로커 로그 (I/O 관련 에러) , 관련 시스템 메트릭 (CPU, 메모리) 순서


## Kafka Streams
___
1. kafka안에서 데이터를 실시간으로 변환/처리하는 라이브러리.
2. kafka안에서 돌아가고 kafka를 사용하는 애플리케이션.
3. 여러 토픽에서 데이터를 읽어 변환/집계하고, 결과를 다른 토픽으로 출력한다.
4. Kafka Streams의 설정 키 상수에는 _CONFIG 접미사를 사용하는게 권장된다고 함. 더 나아가 Kafka Java API 네이밍 컨벤션.
5. Kafka Streams 인스턴스를 하나 더 추가(수평적 확장) 하면 자동 리벌런싱이 발생하여 파티션이 인스턴스들에가 분산되고, 결과적으로 선형적 확장성을 얻는다. 
6. Kafka Streams Instance 안에 Task 있음. 내부 처리단위 이고 Partition당 하나의 Task가 생성됨.
7. Task : Partition , 1:1. 입력 토픽의 파티션 갯수만큼 Task 자동생성.
8. 효과적인 Kafka 데이터 파이프 라인은 파티션 수와 kafka Streams의 인스턴스 수를 함께 조정.
9. 파티션 증가 -> 병렬 처리 단위 증가 + 인스턴스 증가 -> 분산 처리 능력 향상 => 처리량 개선 및 처리 시간 단축
10. 데이터를 바라보는 관점에서 KStream, KTable이 있음.
11. KStream은 모든 레코드를 독립적인 이벤트로 인식. 이벤트 스트림. ex) 은행 거래 내역
12. KTable은 현재 상태만 보존하는것. 상태 테이블. ex) 계좌 잔액
13. Kafka Topic(Data Source)에서 KStream 또는 KTable로 읽어와서 처리/변환 후 다른 Topic으로 보내버림.
10. Kafka Streams를 처리하는 두 가지 방식에는 DSL과 ProcessorAPI가 있음
11. DSL은 간단한 코드로 데이터를 처리할 수 있음. 예를들어 filter(), map(), join() 등
12. join : KStream/KTable을 결합하는 연산. SQL의 join과 비슷함. Stateful 연산으로 Task 내부 State Store에 매칭에 필요한 데이터를 저장함.
13. KStream-KStream : 양쪽 데이터를 State Store에 저장하고 매칭 대기(Join Window설정 필수)
14. KStream-KTable : KTable은 이미 저장되어 있어 즉시 조회 가능 
15. KTable-KTable : 양쪽 모두 State Store에 저장되어 있음
16. groupByKey : 레코드를 키별로 그룹화 (Stateful)
17. reduce : 여러 개의 데이터들을 하나로 집계/축약 (Stateful)
18. windowed by : 시간 기반으로 데이터를 윈도우 단위로 그룹화(Stateful)
19. mapValues : Key는 유지한채 Value만 원하는 형태로(toUpperCase, parsJson, ...) (Stateless)





## Kafka Streams
Kafka Streams는 여러가지 집계 연산 기능을 제공한다.
집계 연산이란? 스트림으로 들어오는 다양한 데이터를 실시간으로 집계할 수 있는 연산기능 내장. 데이터베이스 집계 기능이랑 비슷하다고 생각되지만, Kafka Streams의 집계연산은 즉시 자동으로 집계 되고, 들어오면 자동으로 처리됨
Summing - Key별 합계
Min/Max - Key별 최댓갑 최소값 계산
Count - Key별 이벤트 횟수 세기











## Kafka Streams
___
Kafka Streams의 데이터는 계속해서 흘러들어오고 나가는것. 그래서 특정 기준 범위로 끊는 Window 타입이란게 있음.
Tumbling Windows : 고정시간 분 , 시간 , 일일 통계등 겹치지 안흔 고정 시간. 시간기준
Hopping Windows : 중첩시간 윈도우 10분 짜리 길이의 윈도우를 5분마다 이동. 겹치는 시간이 존재. 시간기준
Sliding Windows : 최소 두 이벤트. 그 이벤트들이 발생할때의 시간차. 이벤트 기준
Event at 00:01 → [23:51-00:01] 윈도우 생성/업데이트
Event at 00:03 → [23:53-00:03] 윈도우 생성/업데이트
Event at 00:07 → [23:57-00:07] 윈도우 생성/업데이트
이벤트마다 지속적으로 업데이트 됨.
Session Windows : Gap이라는 비활동 시간으로 구분해서 사용자 활동 패턴 파악. 사용자 세션 기준이라 크기가 동적임. 비활동 기준





## Kafka Streams
- flatMapValues 연산 기능 : 각 입력 레코를 여러 출력 레코드로 변환
  입력 Record : key="order123", value={items: [item1, item2, item3]}
  출력: flatMapValues 연산을 통해
  key="order123", value=item1
  key="order123", value=item2
  key="order123", value=item3

- branch 연산 기능 : 조건에 따라 레코드를 여러 스트림으로 필터링
  if else나 case when 처럼 분기를 태워서
  하나의 스트림에 있는 Record를  -> Branch [조건 체크]를 통해 -> 여러 스트림으로 보냄.

## Kafka Streams
Window는 Kafka Streams의 시간 범위별 집계 도구.
KStream 객체를 groupByKey()로 그룹화한 후,
.windowedBy()와 집계 메소드(aggregate, count, reduce)를 통해
KTable로 결과가 나옴.
필요에 따라 다른 Stream 또는 토픽으로 보냄.
Sliding Window : 윈도우가 연속적으로 겹침 , 시간 범위 내 모든 레코드 포함
Hopping Window : 일정 간겨으로 점프
Tumbling Window : 겹침 없는 고정 윈도우

## Kafka Streams
"TextLinesTopic" 이라는 토픽에서 읽어 들어와서 split하고 word로 groupBy해서 숫자세고, "WordsWithCountsTopic"이라는 것으로 카운팅 데이터를 넘겨줌**







. ProcessorAPI는 프로그래머가 직접 로직을 구현하는것. Processor Interface 구현해서 로직 작성 해야함(필수 구현).




## Kafka Streams
___
Interactive Queries는 Kafka Streams 애플리케이션 로컬 상태 저장소를 실시간으로 조회할 수 있게 해주는 기능.
Kafka Streams의 내부를 확인하기 위해. 개발자가 직접 REST API를 만들고 그 안에서 Interactive Queries 사용하여 내부 상태를 조회할 수 있음.



## Kafka Streams
카프카 스트림에서 모니터링 해야하는 핵심 Key는 process-rate, process-latency, commit-rate, commit-latency가 있다.
데이터 처리와 커밋 작업의 효율성과 속도에 대한 인사이트를 제공함. 최적의 성능으로 실행되고 있음을 보장하는데 도움이 됨.






















## Confluent REST Proxy
___
REST Proxy는 HTTP/JSON 기반이라서 전송을 위해 base64필요
프로듀서가 바이너리 데이터를 base64로 인코딩하여 HTTP/JSON으로 전송, REST API는 텍스트 기반이므로 바이너리 데이터를 base64로 인코딩해야함.
REST Proxy가 base64 문자열을 디코딩하여 원본 바이트로 변환, Kafka토픽에 저장
Kafka컨슈머는 바이너리 데이터 수신. 추가 디코딩 필요 없음.







## Avro
___
Avro Logical Type이란 기본 타입을 확장하여 소수점, 날짜와 같은 데이터를 정확하게 표현하기 위한것
Time-millis , Decimal , Timestamp-millis , Date 등이 있다.



## Kafka KSQL
KSQL의 배포 옵션에는 Headless Mode, Interactive Mode가 있음.
Headless : SQL 파일 , 미리 작성하고 서버 시작 시 자동 실행
Interactive : CLI 또는 REST API , 통해 대화형으로 실시간 쿼리 실행

## Kafka Testing
테스트를 위해  kafka-console-producer.sh와 kafka-console-consumer.sh 사용된다. 







## Kafka KSQL
KSQL PORT : 8088 (KSQL CLI, REST API 상호작용을 포함.) 
9092 : kafka broker
2181 : zookeeper
8083 : kafka connect
8081 : Schema Registry

## Kafka Connect
Kafka Connect Schema는 , Connector(Source, Sink)를 직접 개발할 때 사용함.
Source Connector : 나중에 Sink가 쓸 수 있도록 타입 정보를 Schema로 만들어서 데이터와 함께 전달. Source가 보내지 않으면 타입 안정성이 없어져서 런타입 에러가 날 수 있음.
Sink Connector : 받은 Schema를 보고 DB 타입에 맞게 저장
스키마 정의서는 user Entity에 대한 구조와 데이터 타입, 데이터 통합과 지속성에 중대한, 업무 통압하는 동한 시스템에서 중요한



## Avro
timestamp_millis (Under Bar)

## Kafka Connect
Kafka Connect는 다른 시스템 간에 데이터를 스트리밍하는 도구이다.
플러그인 형 커넥터와 작업으로 구성된 런타입을 띄우고, 커넥터 설정만으로 손쉽게 데이터 이동을 수행.



## Confluent Schema Registry 
Full Compatibility(전체 호환성): Backward Compatibility(후방) + Forward Compatibility(전방)
Full Compatibility에서는 기본값(default)이 있는 필드만 추가하거나 제거할 수 있다.





## Kafka KSQL
KSQL CLI를 통해, 쿼리를 작성하고 KSQL Stream을 만드는것.(SQL 기반)




## Kafka Security
Kafka 보안관련 모니터링은
failed_authentication_total (실패한 인증 시도 모니터링)
Access Control List (Access Control List 관리)
SASL (Simple Authentication and Security Layer 카프카 인증 매커니즘 작동 여부 확인)
ssl_handshake_rate (암호화딘 연결 설정 상태 모니터링)













