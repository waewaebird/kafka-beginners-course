- Kafka는 TCP 네트워크 프로토콜을 통해 이벤트를 통신하는, 서버와 클라이언트로 구성된 분산 시스템.

## Zookeeper
## Kraft
1. kafka Cluster의 메타데이터 관리와 조정역할을 함.
2. Zookeeper는 Apache Zookeper라는 외부 별도 시스템, Kraft는 자체 내장 메커니즘
3. ZooKeeper Node = ZooKeeper 서버 프로세스 (인스턴스) 그러나 보통 Node = 서버로 통용됨.
4. ZooKeeper 앙상블에도 Zookeeper Node 즉 서버를 여러대 놓을 수 있음.
5. ZooKeeper Quorum은 (total/2) + 1 개까지 남을 수 있음. 즉 정족수를 충족해야함. 충족하지 못한다면 Kafka 생태계가 점진적으로 마비됨.

## Kafka Brokers
1. 이벤트를 저장하는 Kafka 스토리지 계층의 서버.
2. Kafka Brokers는 데이터를 저장하고, client(producer, consumer)의 요청을 다루는 일을 한다.
3. Kafka Cluster에서 카프카 브로커에 대한 다양한 구성을 관리한다.
4. Controller라는 것이 있음. Cluster가 시작되면 Broker들 중 Zookeeper(Kraft)가 가장 먼저 도착한 Broker를 ControllerBroker로 지정.
5. ControllerBroker가 Topic 파티션 어느 브로커에 어떤 파티션을 배치할지 계획 수립
6. ControllerBroker가 파티션의 복제본을 관리 
7. ControllerBroker가 파티션의 리더를 선출하고 ISR 변경사항 메타데이터에 기록
8. 파티션의 리더 Broker가 In Sync Replicas 상태관리



## Kafka Producers
___
Kafka에서 시리얼라이제이션은 효율적인 네트워크 전송을 위해 메시지를 바이트 배열로 변환하는것. String, JSON, AVRO, Protobuf등의 형식

## Kafka Producers
___
Kafka Producers batch.size 동일한 파티션에 대한 레코드 배치에 포함될 수 있는 최대 바이트 수.
이 크기를 초과하는 메시지는 배치 되지 않고, 여러 파티션으로 데이터를 전송하는 경우 단일 요청에 여러 배치가 포함될 수 있다.
네트워크 오버헤드를 줄이고 처리량 효율성을 향상시킴.

## Kafka Producers
___
Kafka Producer가 낮은 지연시간을 달성하기 위해.
linger.ms를 낮추고, batch.size를 작은 단위로 줄이면 됨
Low Latency : 지연시간을 낮추기 위해선 -> 작은 linger.ms 작은 batch.size
High Throughput : 처리량을 높이기 위해선 -> 큰 linger.ms 큰 batch.size

## Kafka Producers
Idempotent Producer는 프로듀서와 브로커 간의 문제. 즉 프로듀서가 토픽/파티션에 메시지를 쓸 때 중복을 방지하는 메커니즘.
멱등성은 프로듀서가 재시도로 동일한 메시지를 여러 번 전송하더라고 메시지가 파티션에 정확히 한 번만 전달되도록 보장하여 중복을 방지.

## Kafka Producers
Kafka Producers에서 Callback은 프로듀서가 메시지를 전송한 후,
그 결과(실패/성공) 비동기적으로 받아서 처리할 수 있게 해주는 메커니즘.

## Kafka Producers
데이터 생성량이 많은 기간 동안
buffer.memory : 더 많은 데이터를 일시벅으로 보관함
max.block.ms : send() => 토픽에 보내기전 Producer 내부 버퍼에 넣는것!. 버퍼를 꽉 채울 수 있도록 시간을 늘림.

## Kafka Producers
최대 데이터 안전성을 위해선 acks= 'all' 모든 in-sync replicas로 부터 성공 메시지 받는것을 보장하고, 멱등성을 "정확히 한번"을 통해 중복 데이터를 막아 데이터 손실을 막을 수 있다.

## Kafka Producers
producer의 request.timeout.ms는 Kafka 프로듀서가 브로커로부터 응답을 받기 위해 기다리는 최대 시간.

## Kafka Streams
___
KafkaStreams의 핵심 확장성 모델은 => 수평적 확장 , 파티셔닝 + 인스턴스 분산 + 자동 리벨렁싱 + 선형적 확장성

## Kafka Streams
___
Join이란 두 개 이상의 스트림/테이블 데이터를 결합하는것. SQL의 join과 비슷함.
Stream은 계속 흐르는데, 매칭할 데이터를 기다려야(매칭 대기) 하니깐 상태저장이 필요 stateful하다.
(the join operation in Kafka is stateful. It requires maintaining state information about the streams or tables being joined to handle the combination of data
across different time windows or key matches. This statefulness allows Kafka to manage complex joins that integrate data arriving at different times,
ensuring accurate and timely results in stream processing applications)


 

## Kafka Monitoring
___
request-latency-avg는 프로듀서 / 컨슈머가 브로커에게 요청을 보낸 후 응답받기까지의 평균시간 (ms) , 낮을 수록 좋은거!





## Kafka Core Concepts
___
Kafka는 오프셋 인덱스를 사용하여 읽기 성능을 향상시킴. 각 로그 세그먼트마다 인덱스 파일(.index)을 생성하여 메시지 오프셋과 해당 메시지의 물리적 파일위치를 매핑합니다.
= 각로그 세그먼트에 대한 인덱스를 유지하여 메시지 오프셋을 파일 위치에 매핑함으로써 전체 세그먼트를 스캔할 필요 없이 메시지를 빠르게 찾고 접근.



## Kafka Streams
___
효과적인 Kafka 데이터 파이프 라인은
1. Kafka Streams : 분산 스트림 처리를 가능하게 하여, 병렬 처리를 통해 대용량 데이터를 처리하는 능력을 향상
2. 파티션 수 증가 : 병렬성을 향상시켜 더 많은 컨슈머가 동시에 데이터를 처리할 수 있도록 하며, 이를 통해 처리량을 개선하고 처리 시간을 단축.

## Kafka Consumers
___
컨뮤서가 그룹에 참여가하거나 떠나거나, 허트비트를 보내는데 실패하거나, 컨슈밍하고있는 파티션의 변화가 있으면 카프카 컨슈머 리밸런싱이 발생한다.

## Confluent REST Proxy
___
REST Proxy는 HTTP/JSON 기반이라서 전송을 위해 base64필요
프로듀서가 바이너리 데이터를 base64로 인코딩하여 HTTP/JSON으로 전송, REST API는 텍스트 기반이므로 바이너리 데이터를 base64로 인코딩해야함.
REST Proxy가 base64 문자열을 디코딩하여 원본 바이트로 변환, Kafka토픽에 저장
Kafka컨슈머는 바이너리 데이터 수신. 추가 디코딩 필요 없음.

## Kafka Core Concepts
___
Quorum(정족수) Controller는 주키퍼 의존성을 지우기 위해 만들어짐. 클러스터 메타데이터, 파티션 리더쉽 멤버쉽 변화을 운영하는데 있어서의 권한을 다룬다.
Kraft의 핵심구성요소.

## Kafka Streams
___
Kafka Streams API의 설정 키 상수에는 _CONFIG 접미사를 사용하는게 권장된다고 함. 더 나아가 Kafka Java API 네이밍 컨벤션.

## Kafka Streams
___
Interactive Queries는 Kafka Streams 애플리케이션 로컬 상태 저장소를 실시간으로 조회할 수 있게 해주는 기능.
Kafka Streams의 내부를 확인하기 위해. 개발자가 직접 REST API를 만들고 그 안에서 Interactive Queries 사용하여 내부 상태를 조회할 수 있음.

## Kafka Streams
___
Kafka Streams의 병렬 처리 단위는 Partition. Partition당 하나의 Task가 생성됨. 
Source Topic 파티션 수 = Stream Task 수 (일대일)

## Kafka Consumers
___
__consumer_offsets은 컨슈머가 직접 관리하는게 아님.
Kafka Cluster 내부에 __consumer_offsets 토픽이라는 내부 토픽이 있고, 거기서 컨슈머 그룹의 offset을 전체 관리함.
특히, 그안에서 Group Coordinator로 선정(해쉬 로직으로..)된 브로커가 관리함.
이런식으로 중앙집권화 관리하고, 효율성과 신뢰성을 높임.



## Kafka Core Concepts
___
Offset이란 파티션 내 데이터의 순번

## Kafka Configuration
___
Kafka 로그 보존 설정은 log.retention.hours, log.retention.bytes, log.retention.minutes 이 있다.

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

## Avro
___
Avro Logical Type이란 기본 타입을 확장하여 소수점, 날짜와 같은 데이터를 정확하게 표현하기 위한것
Time-millis , Decimal , Timestamp-millis , Date 등이 있다.

## Kafka Core Concepts
___
exactly-once를 활성화 하기 위해선
enable.idempotence=true 설정 + transactional.id= 를 Properties 객체에서 설정

## Kafka Monitoring
Kafka I/O작업에서 비정상적인 패턴 발견되면, 디스크 I/O 메트릭과 로그를 확인하여 급격한 변화나 오류를 찾기가 먼저다.

## Kafka Consumers
consumer.commitSync(); / 수동커밋 : 동기적으로 컨슈머 커밋

## Kafka KSQL
KSQL의 배포 옵션에는 Headless Mode, Interactive Mode가 있음.
Headless : SQL 파일 , 미리 작성하고 서버 시작 시 자동 실행
Interactive : CLI 또는 REST API , 통해 대화형으로 실시간 쿼리 실행

## Kafka Testing
테스트를 위해  kafka-console-producer.sh와 kafka-console-consumer.sh 사용된다. 

## Kafka Consumers
seek Method는 파티션의 오프셋을 특정위치로 수동으로 설정하기 위해 쓰임.
1. 실패한 메시지 재처리
2. 특정 메시지를 반복적으로 분석해야하는 경우
3. 동일한 데이터에 대한 A/B 비교 분석 위해.


## Kafka Streams
Kafka Streams는 여러가지 집계 연산 기능을 제공한다.
집계 연산이란? 스트림으로 들어오는 다양한 데이터를 실시간으로 집계할 수 있는 연산기능 내장. 데이터베이스 집계 기능이랑 비슷하다고 생각되지만, Kafka Streams의 집계연산은 즉시 자동으로 집계 되고, 들어오면 자동으로 처리됨
Summing - Key별 합계
Min/Max - Key별 최댓갑 최소값 계산
Count - Key별 이벤트 횟수 세기

## Kafka Consumers
auto.offset.reset의 역할은 
1. 초기 오프셋이 없을때
2. 현재 오프셋이 더 이상 유효하지 않을 때 
컨슈머가 어떻게 동작할지를 정의. __consumer_offsets 내부 토픽에서 이 컨슈머 그룹의 커밋 오프셋 조회.
earliest : 파티션의 가장 처음부터
latest : 컨슈머가 시작되는 시점의 토픽/파티션에서 가장 최신 오프셋(LEO) 위치부터 읽기 시작
none : 예외를 발생시킴 (수동 처리)



## Kafka Streams
ex ) 판매 데이터로부터 누적 합계를 계산해야하는 경우 , Operation(연산자), Method라고도 부름. 이 메소드들은 자바에서 KTable 혹은 KStream 객체로 받을 수 있음.
group by : 렠고드를 키별로 그룹화
reduce : 여러 개의 데이터들을 하나의 집계로 줄임(축약)
windowed by, mapValues 또한 집계 operation이지만 필수는 아님

## Kafka Core Concepts
log.cleanup.policy=delete는 기본설정임. 기간은 7일 bytes는 기본은 무제한.
log.cleanup.policy=compact는 Producer에서 같은 key로 보낸데이터중 최신 것만 남기는것. 키값의 최신 상태만 남김.
log.cleaner.min.cleanable.ratio=0.2 0.5 이런식으로 조정 , log.cleaner.backoff.ms=15000 시간마다 컴팩션 실행

## Kafka Configuration
Kafka Quota란게 client(Producer, Consumer) 별로 클러스터에 대한 사용량 제한을 두는것,kafka-configs.sh 명령어로 관리



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

## Kafka Consumers
subscribe() => 메서드는 컨슈머가 동적으로 파티션을 할당(group id 필요)받고 컨슈머 그룹에 참여,  리밸런싱 발생
assign() => 컨슈머 그룹 코디네이션 없이 수동으로 파티션을 지정,  리밸런싱 발생 안함 

## Kafka Streams
카프카 스트림에서 모니터링 해야하는 핵심 Key는 process-rate, process-latency, commit-rate, commit-latency가 있다.
데이터 처리와 커밋 작업의 효율성과 속도에 대한 인사이트를 제공함. 최적의 성능으로 실행되고 있음을 보장하는데 도움이 됨.

## Kafka KSQL
KSQL CLI를 통해, 쿼리를 작성하고 KSQL Stream을 만드는것.(SQL 기반)

## Kafka Consumers
Consumer Rebalance : 컨슈머 그룹 내에서 파티션 소유권을 재분배하는 과정. Eager와 Cooperative가 있음
Eager는 Stop the world를 발생킴 모든컨슈머에게 다운타임을 발생하고, 모든파티션 재할당 함
Cooperative 영향받는 파티션만 일부 컨슈머, 일부 파티션에서 발생함

## Kafka Core Concepts
아파치 카프카는 real-time데이터를 처리하고, 스티리밍 기능이 가능하도록 설계되어 시스템과 애플리케여신 간에 안정적인 데이터 전송을 제공합니다.
즉 대용량의 실시간 데이터를 안정적으로 수집 저장 처리할 수 있는 파이프라인을 제공한다.
데이터 파이프 라인 : 데이터 전달 통로. 데이터 소스 , 수집 , 가공/변환 , 저장 ,분석 활용등. 데이터가 여러 시스템을 거쳐 흐르도록 한 시스템 전체

## Kafka Core Concepts
Kafka의 로그 보존은 log.retention.hours 시간, log.retention.bytes 크기 둘을 기반으로한 정책을 통해 관리된다.

## Kafka Security
Kafka 보안관련 모니터링은
failed_authentication_total (실패한 인증 시도 모니터링)
Access Control List (Access Control List 관리)
SASL (Simple Authentication and Security Layer 카프카 인증 매커니즘 작동 여부 확인)
ssl_handshake_rate (암호화딘 연결 설정 상태 모니터링)

## Kafka Core Concepts
Kafka 로그 세그먼트는 log.retention.check.interval.ms 설정에 따라 주기적으로 백그라운드 스레드를 통해 로그 세그먼트 제거.
log.retention.check.interval.ms 설정 시간마다 백그라운드 스레드가 실행되고
→ log.retention.hours(시간), log.retention.bytes(크기)의
로그 보존 정책을 위반한 로그 세그먼트들을 확인
→ 위반된 세그먼트는 삭제함

## Kafka Setup
kafka의 재해 복구 계획에는 장애 발생시에도 데이터를 사용할 수 있도록 여러 위치에 데이터를 복제하는 전략이 포함되어야 합니다.
- 여러 데이터 센터나 가용 영역에 복제 설정
- 중요한 메타데이터 백업
- 장애 조치와 복구 절차를 정기적으로 테스트 하여 신뢰성 확보

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

## Kafka Setup
MirrorMaker는 Kafka클러스터의 데이터를 다른 Kafka클러스터로 복사하는 도구(백업용도, 지역별 서비스)
MirrorMaker는 Kafka 설치 시 함께 제공되므로 별도 설치가 필요 없으며, 설정 파일 작성 후 스크립트를 실행하여 작동시키는 방식
MirrorMaker 2.0 가장 중요한 업데이트 내용은
1. 오프셋 동기화
2. 향상된 처리량
3. 자동화된 동기화

## Kafka Configuration
JVM Options 중 카비지 컬렉션이나, 힙메모리 사이즈 설정은 카프카 퍼포먼스를 강화할 수 있다. 특히 메모리 사용량이나 멈춤시간을 줄이는데 기여 가능함.

## Kafka Core Concepts
메시지 압축은 네트워크와 저장소 사용에 있어서 효과적인 향상을 줄수 있지만, 압축과 해제 과정에서 CPU자원의 추가 소모가 필요하다.

## Kafka Core Concepts
Partition 수를 증가시켜 컨슈머를 할당하여 더 많은 병렬 처리를 할 수 있지만, 감소는 불가능하다.
또한 증가시 메시지 KEY가 변경되어 해시값이 달리져, 기존 KEY가 다른 파티션으로 할당될 수 있음. 이는 메시지 순서 보장을 꺠드릴 수 있음.

## Kafka Configuration
ex ) kafka-configs --bootstrap-server localhost:9092 --entity-type brokers --entity-name 0 --alter --add-config log.cleaner.threads=2
를 통해 동적으로 변경가능 한 설정이 있음.

## Kafka Core Concepts
log.cleanup.policy를 Log Compaction은 토픽 레벨에서 적용되며, 같은 키에 대해 최신 값만 유지하고 이전 값들을 삭제



## Kafka Streams
"TextLinesTopic" 이라는 토픽에서 읽어 들어와서 split하고 word로 groupBy해서 숫자세고, "WordsWithCountsTopic"이라는 것으로 카운팅 데이터를 넘겨줌