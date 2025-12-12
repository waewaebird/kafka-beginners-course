


## Kafka Streams
Exactly-once 지원 트랜잭션 기능으로 정확히 한번 처리 보장
읽기 : KStream<String, String> input = builder.stream("input-topic");
처리 : DSL 사용KStream<String, String> processed = input
.filter((key, value) -> value.length() > 5)
.mapValues(value -> value.toUpperCase());
쓰기 : processed.to("output-topic"); 다른 토픽으로 결과 보내기
Offset commit: input-topic의 offset 커밋 Kafka Streams도 내부적으로는 Consumer이기 때문에 Consumer offset을 사용합니다.

















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




## Confluent Schema Registry
스키마 호환성파괴(breaking compatibility)는 스키마에 가한 변경으로 인해 이전 버전과 최신 버전이 서로의 데이터를 읽을 수 없게 되는 상황을 의미.
이러한 변경 유형은 시스템 구성 요소 간 데이터 엑세스 문제를 피하기 위해 신중한 관리가 필요.











## Confluent REST Proxy
Confluent REST Proxy에서 Request요청을 보낼때 Content-Type은 항상 v2로 하는것이 권장됨. v2로 하는것이 최신 규격에 맞는 올바른 설정



## Kafka KSQL
KEY에 INT BIGINT VARCHAR 다 가능
BOOLEAN , INT , BIGINT , DOUBLE , DECIMAL , VARCHAR , BYTES , ARRAY, MAP, STRUCT , TIME, DATE, TIMESTAMP











## Kafka Connect
DLQ : Dead Letter Queue 처리 실패한 메시지를 별도의 토픽에 저장하여 메시지를 안전하게 보관하고 나중에 재처리
Exponential Backoff : 지수백오프란 1초 후 재시도, 2초후 재시도 ,4 초후 재시도 .....60초후 재시도 이런식으로 시간제한을 두면서 재시도 하여 시스템 회복 시간 및 부하를 줄이는 시도 방법









## Kafka Connect
Single Message Transforms는 kafka Connect에서 데이터를 필터링하고 변환하는데 사용되는 핵심 기능. SMT는 커넥터 구성에서 설정할 수 있으며
소스 커넥터가 데이터를 생성한 후 kafka에 쓰기 전에, 또는 Sink 커넥터가 데이터를 대상 시스템에 쓰기전에 메시지를 변환함
Mask Field 또는 특정 조건 레코드 필터링 하거나, 민감한 정보가 포함된 필드는 제거 하거나 이름을 변경함.




## Kafka KSQL
KStream-to-KStream 조인만이 windowed join을 지원합니다







## Kafka Connect
Source connector : 외부 시스템에서 데이터를 가져와 Kafka Topic으로 쓰는 역할 , 외부시스템 -> Kafka
Sink connector : Kafka Topic에서 데이터를 읽어서 외부 시스템으로 전달.






