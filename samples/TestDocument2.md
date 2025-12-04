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






















