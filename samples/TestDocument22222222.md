






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




## Kafka Configuration
- JVM 힙 크기를 증가시키면 가비 컬렉션이 발생하기 전에 메모리에 더 많은 객체를 유지할수 있음
- G1GC JVM에 내장되 Garbage Collector 로서 java9 부터의 기본 GC, 일시 중지 시간을 최소화하여 대형 힙과 고처리량 시스템을 더 잘 지원



## Confluent Schema Registry
스키마 호환성파괴(breaking compatibility)는 스키마에 가한 변경으로 인해 이전 버전과 최신 버전이 서로의 데이터를 읽을 수 없게 되는 상황을 의미.
이러한 변경 유형은 시스템 구성 요소 간 데이터 엑세스 문제를 피하기 위해 신중한 관리가 필요.




## Kafka Streams
Kafka Streams는 늦게 도착하는 데이터를 처리하기 위해 구성 가능한 유예 기간을 제공하여, 해당 기간 동안 도착한 늦은 데이터도 윈도우 기반 집계에
포함 될 수 있음. 이는 더 정확하고 포괄적인 데이터 처리를 보장.

## Kafka Monitoring
outgoing-byte-rate : Broker to Consumer Kafka에서 나가는 데이터의 초당 바이트 수. 브로커별로 측정되며 네트워크 처리량에 대한 명환한 가시성을 제공하며, 네트워크 용량 계획과 성능 평가에 필수적





## Kafka Monitoring
Java Management Extensions(JMX)는 자바 애플리케이션 전반의 표준 모니터링 기술. JVM에서 실행 중인 프로그램의 내부 상태(메모리, 스레드, 성능 메트릭 등)을 확인하고 제어할 수 있음.
JMX MBean(메트릭 객체)를 통해 자원을 모니터링 함. JMX에서 활용할 수 있도록 카프카에서 ConsumerLag, ProducerThroughput 같은 핵심 모니터링 지표를 MBean으로 제공함. 
JVM Health - GC시간, 힙 메모리 사용량, 스레드 수등 JVM 상태를 추적해 메모리 누수나_ GC병복 체크
ConsumerLag - consumer가 처리하지 못한 메시지 수로 소비 지연 확인
Producer throughput - 생산자 요청 속도, 바이트 전송량등으로 클러스터 부하와 성능을 확인.









## Kafka Configuration
카프카 주키퍼 연결 설정은 server.properties 파일에 zookeeper.connect항목 수정을 통해서 할 수 있음.

## Confluent REST Proxy
Confluent REST Proxy에서 Request요청을 보낼때 Content-Type은 항상 v2로 하는것이 권장됨. v2로 하는것이 최신 규격에 맞는 올바른 설정



## Kafka KSQL
KEY에 INT BIGINT VARCHAR 다 가능
BOOLEAN , INT , BIGINT , DOUBLE , DECIMAL , VARCHAR , BYTES , ARRAY, MAP, STRUCT , TIME, DATE, TIMESTAMP





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





## Kafka Streams
props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
Key는 String으로 직렬화/역직렬화 Value도 String으로 직렬화/역직렬화 별도로 지정하지 않으면 이 설정이 자동 적용

## Kafka Streams
Kafka Streams에서의 에러처리 전략은
Fail-fast : 기본동작. 에러 발생시 애플리케이션 즉시 중단
Log-and-continue : 에러를 로그로 기록하고 계속 진행 props.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, LogAndContinueExceptionHandler.class);
Custom exception handling : 커스텀 예외 핸들러 구성


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





## Kafka Configuration
zookeeper.properties파일은 Kafka의 클러스터 조정에 필수적인 주키퍼에 대한 구성 세부 정보를 포함하고 있으므로 매우 중요!

## Kafka Connect
DLQ : Dead Letter Queue 처리 실패한 메시지를 별도의 토픽에 저장하여 메시지를 안전하게 보관하고 나중에 재처리
Exponential Backoff : 지수백오프란 1초 후 재시도, 2초후 재시도 ,4 초후 재시도 .....60초후 재시도 이런식으로 시간제한을 두면서 재시도 하여 시스템 회복 시간 및 부하를 줄이는 시도 방법





max.poll.records : poll() 메서드 한번 호출시 반환되는 최대 레코드 수. 브로커로 부터 이미 많은 양의 데이터를 전달 받은 상황에서 내부 버퍼에 저장
애플리케이션에는 max.poll.records 갯수만큼만 전달함.
props.put("max.poll.records", "100");     // 100개씩만 주기



## Kafka Connect
Single Message Transforms는 kafka Connect에서 데이터를 필터링하고 변환하는데 사용되는 핵심 기능. SMT는 커넥터 구성에서 설정할 수 있으며
소스 커넥터가 데이터를 생성한 후 kafka에 쓰기 전에, 또는 Sink 커넥터가 데이터를 대상 시스템에 쓰기전에 메시지를 변환함
Mask Field 또는 특정 조건 레코드 필터링 하거나, 민감한 정보가 포함된 필드는 제거 하거나 이름을 변경함.


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




## Kafka Connect
Source connector : 외부 시스템에서 데이터를 가져와 Kafka Topic으로 쓰는 역할 , 외부시스템 -> Kafka
Sink connector : Kafka Topic에서 데이터를 읽어서 외부 시스템으로 전달.

## Kafka Streams
peek()는 비변환 연산(non-transforming operation)**으로, 스트림을 수정하지 않고 데이터를 검사할 수 있게 해줍니다.
peek()을 통해 람다함수를 실행할 수 있는 기회를 제공.




## Kafka Configuration
Broker-level settings : 클러스터 전체 또는 해당 브로커의 모든 토픽에 대한 기본값으로 작동. server.properties파일에 정의하여 전역 기본값을 제공하여 일관된 클러스터 동작을 보장
Topic-level settings : 특정 토픽에만 적용되는 개별 맞춤 설정으로 토픽 생성 시 또는 kafka-configs.sh로 동적설정. 브로커 기본값을 덮어쓰고 토픽별 요구사항 충족함.
우선순위는 토픽레벨 설정이 더 높아서 브로커레벨 설정을 덮어씀.

