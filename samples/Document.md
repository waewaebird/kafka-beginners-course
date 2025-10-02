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










































