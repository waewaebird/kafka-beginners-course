## Kafka Streams
___
KafkaStreams의 핵심 확장성 모델은 => 수평적 확장 , 파티셔닝 + 인스턴스 분산 + 자동 리벨렁싱 + 선형적 확장성

## Zookeeper
___
주키퍼 Quorum은 (total/2) + 1 개까지 남을 수 있음. 

## Kafka Monitoring
___
request-latency-avg는 프로듀서 / 컨슈머가 브로커에게 요청을 보낸 후 응답받기까지의 평균시간 (ms) , 낮을 수록 좋은거!

## Kafka Streams
___
Join이란 두 개 이상의 스트림/테이블 데이터를 결합하는것. SQL의 join과 비슷함.
Stream은 계속 흐르는데, 매칭할 데이터를 기다려야(매칭 대기) 하니깐 상태저장이 필요 stateful하다.
(the join operation in Kafka is stateful. It requires maintaining state information about the streams or tables being joined to handle the combination of data 
across different time windows or key matches. This statefulness allows Kafka to manage complex joins that integrate data arriving at different times, 
ensuring accurate and timely results in stream processing applications)

## Kafka Producers
___
Kafka에서 시리얼라이제이션은 효율적인 네트워크 전송을 위해 메시지를 바이트 배열로 변환하는것. String, JSON, AVRO, Protobuf등의 형식

## Kafka Core Concepts
___
Kafka는 오프셋 인덱스를 사용하여 읽기 성능을 향상시킴. 각 로그 세그먼트마다 인덱스 파일(.index)을 생성하여 메시지 오프셋과 해당 메시지의 물리적 파일위치를 매핑합니다.
= 각로그 세그먼트에 대한 인덱스를 유지하여 메시지 오프셋을 파일 위치에 매핑함으로써 전체 세그먼트를 스캔할 필요 없이 메시지를 빠르게 찾고 접근.

## Kafka Producers
___
Kafka Producers batch.size 동일한 파티션에 대한 레코드 배치에 포함될 수 있는 최대 바이트 수. 
이 크기를 초과하는 메시지는 배치 되지 않고, 여러 파티션으로 데이터를 전송하는 경우 단일 요청에 여러 배치가 포함될 수 있다.
네트워크 오버헤드를 줄이고 처리량 효율성을 향상시킴.

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

## Kafka Producers
___
Kafka Producer가 낮은 지연시간을 달성하기 위해.
linger.ms를 낮추고, batch.size를 작은 단위로 줄이면 됨
Low Latency : 지연시간을 낮추기 위해선 -> 작은 linger.ms 작은 batch.size
High Throughput : 처리량을 높이기 위해선 -> 큰 linger.ms 큰 batch.size

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

## Kafka Producers
Idempotent Producer는 프로듀서와 브로커 간의 문제. 즉 프로듀서가 토픽/파티션에 메시지를 쓸 때 중복을 방지하는 메커니즘.
멱등성은 프로듀서가 재시도로 동일한 메시지를 여러 번 전송하더라고 메시지가 파티션에 정확히 한 번만 전달되도록 보장하여 중복을 방지.

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

## Kafka Producers
Kafka Producers에서 Callback은 프로듀서가 메시지를 전송한 후, 
그 결과(실패/성공) 비동기적으로 받아서 처리할 수 있게 해주는 메커니즘.

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

## Kafka Brokers
Kafka Brokers는 데이터를 저장하고, client의 요청을 다루는 일을 한다. 카프카 클러스터 구성을 통해서 데이터를 보장한다.

## Kafka KSQL
KSQL PORT : 8088 (KSQL CLI, REST API 상호작용을 포함.) 
9092 : kafka broker
2181 : zookeeper
8083 : kafka connect
8081 : Schema Registry