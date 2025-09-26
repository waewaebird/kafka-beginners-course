
Consumer Group
Rebalance - Kafka Consumer partition.assignment.strategy
 eager : stop to world / RangeAssignor , RoundRobin , StickyAssignor
 cooperative : incremental / CooperativeStickyAssignor

Consumer Offset
 enable.auto.commit = true & auto.commit.interval.ms = 5000
.poll() -> 컨슈머도 auto.commit.interval.ms 시간마다 계속 커밋한다.