## Kafka Streams
KafkaStreams 는 Kafka 생태계내어서 데이터 처리와 변환을 지원하는 강력한 라이브러리.
데이터 변환, 데이터 보강, 복합 이벤트 처리의 기능을 제공하며, Exactly-once 기능을 지원하기 때문에 실시간 애플리케이션에 매우 적합하다.

## Avro
Avro는 데이터를 다루는 두 가지 주요 방식을 제공함.
Specific Record : 스키마로부터 자동 생성된 Java 클래스 사용
Generic Record : 런타임에 스키마를 동적으로 로드하여 사용

두 방식 모두 인스턴스를 생성하여 Avro 메시지를 다루는것

Specific Record는 컴파일 타임 타입 안정성과 성능 최적화를 위해 사용
GenericRecord의 핵심 목적은 런타임에 동적으로 스키마를 사용하여 데이터를 유연하게 처리하는것,

## Kafka Connect
Kafka Connect의 Config Definitions는 커넥터가 어떤 설정을 받아들일 수 있는지 메타데이터로 정의하는 것.
필수 및 선택적 파라미터를 포함하고 기본값을 제공하여 커넥터 설정을 간소화.

## Kafka Configuration
MirrorMaker 2.0을 이용하여 지리적으로 먼 클러스터로 데이터를 복제하는 것은 데이터 가용성을 보장

## Kafka Brokers
브로커의 퍼포먼스를 유지하기 위해서는
처리량과 응답성, 데이터 전송 능력, 리소스 활용도가 중요함.

## Kafka Consumers
높은 데이터 흐름에서 예외가 발생하면, try catch 블록으로 예외 타입빌 다른 전략 처리하고, 오프셋 관리를 정확히 하여 중복/손실 방지

## Kafka Configuration
- JVM 힙 크기를 증가시키면 가비 컬렉션이 발생하기 전에 메모리에 더 많은 객체를 유지할수 있음
- G1GC JVM에 내장되 Garbage Collector 로서 java9 부터의 기본 GC, 일시 중지 시간을 최소화하여 대형 힙과 고처리량 시스템을 더 잘 지원

## Kafka Core Concepts
카프카는 근본적으로 commit log 분산의 원칙을 기반으로 만들어졌음. 이것은 높은 처리량, 장애 내성 저장소를 제공함
Commit Log = Kafka가 사용하는 "append-only, ordered, immutable" 데이터 저장 방식을 부르는 이름(개념적 자료구조)

## Confluent Schema Registry
스키마 호환성파괴(breaking compatibility)는 스키마에 가한 변경으로 인해 이전 버전과 최신 버전이 서로의 데이터를 읽을 수 없게 되는 상황을 의미.
이러한 변경 유형은 시스템 구성 요소 간 데이터 엑세스 문제를 피하기 위해 신중한 관리가 필요.

## Kafka Core Concepts
카프카는 모든 메시지를 디스크에 저장(Disk-based Retention), 설정 가능한 기간동안보관.
카프카가 대용량 데이터를 설정 가능한 기간 동안 저장할 수 있는 이유는 디스크 기반 저장 덕분. 메모리가 아닌 디시크를 사용하기 떄문에 비용 효율적으로 데이터를 본관 가능

## Kafka Streams
Kafka Streams는 늦게 도착하는 데이터를 처리하기 위해 구성 가능한 유예 기간을 제공하여, 해당 기간 동안 도착한 늦은 데이터도 윈도우 기반 집계에
포함 될 수 있음. 이는 더 정확하고 포괄적인 데이터 처리를 보장.

## Kafka Monitoring
outgoing-byte-rate : Broker to Consumer Kafka에서 나가는 데이터의 초당 바이트 수. 브로커별로 측정되며 네트워크 처리량에 대한 명환한 가시성을 제공하며, 네트워크 용량 계획과 성능 평가에 필수적

## Kafka Core Concepts
Kafka 토픽의 파티션 수를 늘리면 메시지 키에 대한 해싱 메커니즘이 변경되어 동일한 키가 항상 같은 파티션으로 가는 기존 보장이 깨질 수 있습니다. Kafka 
로그는 불변(immutable) 특성을 가지므로 이전에 저장된 메시지는 원래 파티션에 그대로 남아 재분배되거나 재정렬되지 않습니다. 이는 단일 파티션 내에서 키 기반 정렬에 의존하는 컨슈머 로직에 영향을 줄 수 있습니다.

## ZooKeeper
주키퍼는 3개의 주요 포트를 사용함
2181 : 클라이언트가 주키퍼서버와 연결할때 사용하는 포트
2888 : 주키퍼들간의 통신(Leader & Follower)
3888 : 주키퍼들간의 Leader 선출용 Port 

## Kafka Core Concepts
Kafka = Sequential I/O를 Segment "강제"하는 구조
✅ Append-only (끝에만 추가)
✅ 수정/삭제 없음
✅ 순서대로만 읽기
→ 자연스럽게 Sequential I/O  → 빠른 성능

## Kafka Consumers
max.poll.interval.ms : poll() 이 호출 이후 메시지 처리하고 다시 다음poll() 을 호출하는 전체 주가. default 5분이고 이 시간을 넘는동안 poll이 끝나지 않으면
해당 컨슈머가 문제있다고 판단하여 리밸런싱을 일으킴.

## Kafka Monitoring
Java Management Extensions(JMX)는 자바 애플리케이션 전반의 표준 모니터링 기술. JVM에서 실행 중인 프로그램의 내부 상태(메모리, 스레드, 성능 메트릭 등)을 확인하고 제어할 수 있음.
JMX MBean(메트릭 객체)를 통해 자원을 모니터링 함. JMX에서 활용할 수 있도록 카프카에서 ConsumerLag, ProducerThroughput 같은 핵심 모니터링 지표를 MBean으로 제공함. 
JVM Health - GC시간, 힙 메모리 사용량, 스레드 수등 JVM 상태를 추적해 메모리 누수나_ GC병복 체크
ConsumerLag - consumer가 처리하지 못한 메시지 수로 소비 지연 확인
Producer throughput - 생산자 요청 속도, 바이트 전송량등으로 클러스터 부하와 성능을 확인.

## Kafka Core Concepts
Log Compaction은 동일 키의 오래된 레코드를 제거하고 각 키의 마지막 값만 보존하는 카프카 기능

## Kafka Core Concepts
트랜잭셔널 메시지란건 프로듀서가 여러 토픽/파티션에 메시지를 원자적으로 쓰는 기능을 의미함.
프로듀서가 2개의 토픽에 메시지를 적고 커밋을 안하고 있다가 모든 토픽에서 메시지르 받았으면 그때 producer.commitTransaction();를 해서 메시지 커밋을 끝냄
만약 하나의 토픽에서 못받으면 producer.commitTransaction을 암함
여러 토픽/파티션에 걸친 원자적 메시지 쓰기

## Kafka Configuration
카프카 주키퍼 연결 설정은 server.properties 파일에 zookeeper.connect항목 수정을 통해서 할 수 있음.

## Confluent REST Proxy
Confluent REST Proxy에서 Request요청을 보낼때 Content-Type은 항상 v2로 하는것이 권장됨. v2로 하는것이 최신 규격에 맞는 올바른 설정

## Kafka Producers
기본 파티셔너는 메시지의 키 해시값에 따라 파티션을 지정하지만, 특정 비즈니스 규칙이나 사용자 정의 기준에 따라 메시지를 특정 파티션에 보내야 할 때
커스텀 파티셔너를 사용할 수 있음. ex : 특정 고객 ID, 날짜별 지역별 분배
Partitioner 인터페이스를 구현하여 그안에 파티션 로직 직접 작성

## ZooKeeper
주키퍼는 브로커, 토픽, 파티션에 대한 정보를 유지 관리하는 것을 포함하여 Kafka 클러스터의 상태를 관리.

## Kafka Producers
bootstrap.servers는 Kafka 브로커 리스트(주소:포트)를 지정하는 필수 설정으로, 이 값 없이는 프로듀서가 어느 Kafka 클러스터에 연결할지 모르기 때문에 반드시 필요합니다.
acks, client.id는 권장 설정이나 기본값이 있어 없어도 실행되지만, bootstrap.servers는 필수입니다.

## Kafka KSQL
KEY에 INT BIGINT VARCHAR 다 가능
BOOLEAN , INT , BIGINT , DOUBLE , DECIMAL , VARCHAR , BYTES , ARRAY, MAP, STRUCT , TIME, DATE, TIMESTAMP

## Kafka Consumers
Cooperative Rebalance는 전략적으로 소수의 파티션만 재할당 합니다. 이를 통해 영향을 받지 않는 Consumer는 중단 없이 데이터 처리를 계속 할 수 있습니다.
다운타임 최소화 및 처리 효율성 향상.
Consumer Group 전체에서 안정적인 파티션 할당이 이루어질 때까지 부분 재조정을 여러번 반복하는 과정이 있을 수 있음.

## Kafka Producers
Kafka Properties는 키-값 모두 String 타입으로 저장

## Kafka Consumers
카프카는 메시지를 바이트 배열로(사람이 직접 읽을 수 없는 이진 데이터) 저장하기 때문에, Consumer가 사용가능한 형식으로 Deserializer가 필요
바이트 배욜로 저장하는 이유는? 어떤 개발언어든 사용가능, 네트워크 전송에 최적화 돼있는 효율성, 문자열 숫자 JSON 객체등 모든 데이터 타입도 가능한 유연성

## Kafka Producers
압축 타입을 고려할 때는 CPU 자원 사용과 네트워크 대역폭 절감 간의 균형을 잘 고려해서 선택해야 한다.
Producer가 compression.type을 설정하면, 메시지는 브로커로 전송되기 전에 Producer 단에서 압축됩니다
Kafka Producer에서 사용하는 압축 타입은 gzip, snappy, lz4, zstd가 있다. Default none.

## Kafka Streams
KafkaStreams는 별도 클러스터 불필요 : 일반 Java 애플리케이션 라이브러리로 실행
Exactly-once 지원 트랜잭션 기능으로 정확히 한번 처리 보장
읽기 : KStream<String, String> input = builder.stream("input-topic");
처리 : DSL 사용KStream<String, String> processed = input
    .filter((key, value) -> value.length() > 5)
    .mapValues(value -> value.toUpperCase());
쓰기 : processed.to("output-topic"); 다른 토픽으로 결과 보내기
Offset commit: input-topic의 offset 커밋 Kafka Streams도 내부적으로는 Consumer이기 때문에 Consumer offset을 사용합니다.

## Kafka Configuration
server.properties내 log.dirs 설정은 카프카 로그가 어느 디렉토리에 저장될지 지정할 수 있다.

## Kafka Core Concepts
카프카는 분산 아키텍처를 갖추고 있어 확장성이 매우 뛰어나며, 토픽을 분할하여 여러 브로커에 복제할 수 있습니다.

## Kafka Consumers
group.id는 컨슈머 그룹을 고유하게 식별 하여, 같은 group.id를 가진 컨슈머들이 하나의 그룹으로 묶임. 그룹내에서 파티션을 자동으로 분배하여 병럴 처리
파티션 할당 조정, 오프셋 관리, 리밸런싱

## Kafka Producers
linger.ms 설정을 사용하면 Producer가 추가 메시지가 일괄 처리될 때까지 기다릴 수 있어서 처리량은 늘어나지만 대기 시간 또한 늘어남

## Kafka Core Concepts
gzip zstd lz4 snapp 등의 압축 타입을 카프카에서 지원한다.
각 압축 유형은 압축 속도와 효율성 측면에서 서로 다른 균형을 제공함

## Kafka Streams
props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
Key는 String으로 직렬화/역직렬화 Value도 String으로 직렬화/역직렬화 별도로 지정하지 않으면 이 설정이 자동 적용

## Kafka Streams
Kafka Streams에서의 에러처리 전략은
Fail-fast : 기본동작. 에러 발생시 애플리케이션 즉시 중단
Log-and-continue : 에러를 로그로 기록하고 계속 진행 props.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, LogAndContinueExceptionHandler.class);
Custom exception handling : 커스텀 예외 핸들러 구성

## Kafka Core Concepts
log.segment.bytes설정은 log 세그먼트 마일의 최대 크기를 지정, 이 크기에 도달하면 새로운 세그먼트 파일로 전환

## Kafka Streams
Re-partitioning : 데이터를 새로운 Key 기준으로 파티션에 재배치 , Key가 변경되면 발생
selectKey : 명시적으로 key 변경
groupBy : 집계를 위해 Key 변경

## Kafka Configuration
Read-only 설정이란 브로커 시작 시에만 읽히는 설정, 실행중에는 변경 불가능하녀 반드시 재시작 필요
- broker.id (브로커 ID)
- log.dirs (데이터 경로)
- listeners (네트워크 리스너)
- zookeeper.connect (ZK 연결)
- process.roles (KRaft 역할)

Kafka 문서의 "Dynamic Update Mode" 컬럼을 확인하면 각 설정의 업데이트 모드를 알 수 있음

## Kafka Core Concepts
Consuemer 메시지 처리 , Offset Commit, __consumer_offsets 토픽에 저장, 브로커가 관리
Offset은 컨슈머 그룹별로 독립적으로 저장

## Kafka Consumers
max.poll.interval.ms() 시간안에 poll()과 관련된 작업을 완료하지 못한다면, Consumer에 문제가 있다고 판단하여 리밸런싱
그러니깐 기본적으로 처리 시간이 오래걸리는 경우에는 max.poll.interval.ms() 충분히 증가시켜 처리시간을 확보해야함

## Kafka Configuration
zookeeper.properties파일은 Kafka의 클러스터 조정에 필수적인 주키퍼에 대한 구성 세부 정보를 포함하고 있으므로 매우 중요!

## Kafka Connect
DLQ : Dead Letter Queue 처리 실패한 메시지를 별도의 토픽에 저장하여 메시지를 안전하게 보관하고 나중에 재처리
Exponential Backoff : 지수백오프란 1초 후 재시도, 2초후 재시도 ,4 초후 재시도 .....60초후 재시도 이런식으로 시간제한을 두면서 재시도 하여 시스템 회복 시간 및 부하를 줄이는 시도 방법

## Kafka Core Concepts
Apache Kafka는 Java와 Scala언어로 개발 된 시스템

## Kafka Producers
Asynchronous Send의 핵심 장점은 브로커의 응답을 기다리지 않고 계속 전송하여 Throughput 상승

## Kafka Consumers
fetch-min-bytes : Consumer가 더 큰 데이터 블록을 기다리게 되어, fetch 요청 횟수를 줄여 처리량을 향상시킬 수 있음.
하지만 충분한 데이터가 누적될때까지 기다려야 하므로 추가적인 지연시간이 발생.네트워크 레벨
props.put("fetch.min.bytes", "1048576");  // 1MB씩 가져오기

max.poll.records : poll() 메서드 한번 호출시 반환되는 최대 레코드 수. 브로커로 부터 이미 많은 양의 데이터를 전달 받은 상황에서 내부 버퍼에 저장
애플리케이션에는 max.poll.records 갯수만큼만 전달함.
props.put("max.poll.records", "100");     // 100개씩만 주기

## Kafka Producers
Kafka Consumer는 메시지에 포함된 특후 헤더를 인식하여 압축을 해제. 그래서 컨슈머 측에서 압축 타입과 관련된 설정을 별도로 하지 않아도 처리할 수 있음
Kafka Producer 레벨에서 compression.type 속성을 통해 압축 구성. 압축은 토픽 레벨에서도 설정할 수 있지만 일반적이지는 않음.

## Kafka Connect
Single Message Transforms는 kafka Connect에서 데이터를 필터링하고 변환하는데 사용되는 핵심 기능. SMT는 커넥터 구성에서 설정할 수 있으며
소스 커넥터가 데이터를 생성한 후 kafka에 쓰기 전에, 또는 Sink 커넥터가 데이터를 대상 시스템에 쓰기전에 메시지를 변환함
Mask Field 또는 특정 조건 레코드 필터링 하거나, 민감한 정보가 포함된 필드는 제거 하거나 이름을 변경함.

## Kafka Consumers
Kafka Consumer에서 Shutdown Hook을 통해 close() 시키면 컨슈머가 즉시 그룹에서 탈퇴하고 리벨러스가 발생함. 다른 컨슈머들이 쁘라게 파티션을 재할당받아 메시지 처리 재게
close()가 호출되지 않고 종료되면 해당 컨슈머가 여전히 살아있다고 판단하여 session.timeout.ms 기간동안 대기하게됨
그 동안 파티션의 메시지가 처리되지 않고 타임아웃 후에야 레빌런스스 시작.
그러므로 close()가 제대로 호출되야 불필요한 메시지 처리 지연이나, 중복메시지 소비 가능성을 줄일 수 있음

## Kafka Streams
State stores : Kafka Streams의 stateful연산을 지원하는 핵심 구성요소.  기본적으로 RocksDB로 구현된 영구 state store를 사용하지만, 인메모리 스토어도 사용할 수 있습니다. 
각 처리 태스크는 로컬 state store에서 상태를 유지하며, 키별로 데이터를 구성하여 효율성을 제공합니다.

Changelog topics : Changelog topics는 state stores를 백업하여 Kafka Streams의 상태를 내결함성(fault-tolerant)으로 만듭니다. 
모든 state store는 별도의 Kafka 토픽으로 백업되며, 재시작이나 재할당 시 changelog에서 상태를 복원합니다. 이를 통해 장애 발생 시에도 상태 데이터가 손실되지 않습니다

## Kafka KSQL
KStream-to-KStream 조인만이 windowed join을 지원합니다

## Kafka Streams
Sliding Window는 각 이벤트 주변의 특정 기간 내 이벤트를 분석하기 위한 것
해당 이벤트의 타임스탬프를 기준으로 새 윈도우를 생성, 정의된 시간 차이내에 있는 이전 레코드들을 자동으로 포함. 결과적으로 각 이벤트를 중심으로 특정 기간의 데이터를 연속적으로 분석함.

## Kafka Producers
retries 설정의 핵심 역할은 실패한 메시지를 자동으로 재전송하는 것입니다. 프로듀서가 메시지를 브로커에 전송할 때 일시적인 오류가 발생하면, 재시도 메커니즘이 작동하여 메시지를 다시 보내려고 시도합니다
retries : Integer.MAX_VALUE 사실상 무제한으로 재시도함.횟수
delivery.timeout.ms : 기본값 2분으로 이 시간동안만 재시도를 함. 메시지 전송의 전체 허용 시간.

## Kafka Producers
파티션 선택 매커니즘
키가 있을 경우 MurmurHash함수를 키에 적용하여, 해시 결과를 파티션수로 나눈 나머지로 파티션 결정. 동일한 키는 항상 동일한 파티션으로 매핑함
키가 없는경우 2.4 이전에는 Round Robin 방식으로 순차적으로 배분, 
2.4이후에는 Sticky Partitioning 전략 도입. batch.size , linger.ms 조건을 만족하면 다음파티션으로 전환 배치 효율성 향상 및 지연시간 감소. 


## Kafka Connect
Source connector : 외부 시스템에서 데이터를 가져와 Kafka Topic으로 쓰는 역할 , 외부시스템 -> Kafka
Sink connector : Kafka Topic에서 데이터를 읽어서 외부 시스템으로 전달.

## Kafka Streams
peek()는 비변환 연산(non-transforming operation)**으로, 스트림을 수정하지 않고 데이터를 검사할 수 있게 해줍니다.
peek()을 통해 람다함수를 실행할 수 있는 기회를 제공.

## Kafka Core Concepts
.log 파일 자체는 하나의 타임스탬프만 저장 CreateTime or LogAppendTime
Topic Level에선 message.timestamp.type = CreateTime , message.timestamp.type = LogAppendTime 이런식으로 설정
Broker Level에선 server.properties 파일의 log.message.timestamp.type

## Kafka Configuration
Broker-level settings : 클러스터 전체 또는 해당 브로커의 모든 토픽에 대한 기본값으로 작동. server.properties파일에 정의하여 전역 기본값을 제공하여 일관된 클러스터 동작을 보장
Topic-level settings : 특정 토픽에만 적용되는 개별 맞춤 설정으로 토픽 생성 시 또는 kafka-configs.sh로 동적설정. 브로커 기본값을 덮어쓰고 토픽별 요구사항 충족함.
우선순위는 토픽레벨 설정이 더 높아서 브로커레벨 설정을 덮어씀.

## Kafka Consumers
fetch.min.bytes : 브로커가 fetch요청에서 반환할 최소 데이터 양을 지정. 큰 값으로 설정하면 처리량이 증가함
fetch.max.wait.ms : fetch.min.bytes에 도달하기 전까지 브로커가 대기할 최대시간.