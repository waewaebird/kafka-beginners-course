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































