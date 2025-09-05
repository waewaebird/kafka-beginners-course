소스시스템 -> 프로듀서 ->카프카에서 클러러스터 > 브로커 > 토픽 > 파티션, 오프셋 > 레플리카, 파티션 리더

라운드 로빈 방식으로
컨슈머 그룹



- wsl2 및 Ubuntu 설치

- Java JDK 11 설치

- Kafka 바이너리 설치

- 





PATH="$PATH:~/home/jzangeva/kafka_2.13-3.1.0/bin"

kafka-server-start.sh config/server.properties

kafka-server-start.sh config/server.properties


1. zookeeper-server-start.sh config/zookeeper.properties
   zookeeper-server-start.sh -daemon config/zookeeper.properties
- 주키퍼 먼저 실행, 경로 알맞게 가야 함

2. kafka-server-start.sh config/server.properties
   kafka-server-start.sh -daemon config/server.properties
- 카프카 서버 실행 (브로커)

  - zookeeper-server-stop.sh
  - kafka-server-stop.sh

3. kafka-topics.sh --bootstrap-server localhost:9092 --topic first_topic --create
- bootstrap-server 브로커서버에서 first_topic 생성


kafka-topics.sh --bootstrap-server localhost:9092 --topic first_topic --describe

Topic: first_topic      TopicId: VrD9Nb5tRU66N-HEphLHLQ PartitionCount: 1       ReplicationFactor: 1    Configs: segment.bytes=1073741824
Topic: first_topic      Partition: 0    Leader: 0       Replicas: 0     Isr: 0
-파티션 0개          리더, 레플리카, Isr 브로커 ID:0


OpenSearch 명령어
GET _cat/indices : 현재 저장된 모든 인덱스(테이블 같은 개념)를 확인하는 방법 