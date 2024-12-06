# 📝 대규모 트래픽 / 대용량 데이터 처리 경험하기
실제 서비스를 진행할 때 일어날 수 있는 시나리오 구성해서 해결하는 프로젝트

# ⚙ 기술 스택
<div>
    <img src="https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white">
    <img src="https://img.shields.io/badge/Apache%20Kafka-000?style=for-the-badge&logo=apachekafka&logoColor=white">
    <img src ="https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white">
    <img src ="https://img.shields.io/badge/MongoDB-%234ea94b.svg?style=for-the-badge&logo=mongodb&logoColor=white">
    <img src ="https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white">
</div>
<br>
<hr>
<br>

# Scenario 1. 데이터 3000만건 INSERT
1. 이전 프로젝트까지 최대 1000건의 데이터 다뤄봄.
2. 300만개 데이터를 Jpa Method의 saveAll() 사용 시 7분 소요되는 경험 -> JDBC Bulk Insert 사용 시 6초 단축<br>(JPA Bulk Insert를 사용하지 않은 이유 : ID 생성 전략 AUTO-INCREMET를 유지하기 위함)
3. 1000만개 데이터 JDBC Bulk Insert 사용시 Out Of Memory Exception 발생 -> 메모리에 모든 데이터를 저장하기 때문 -> Batch Size 조절로 해결
4. 결과적으로 약 3000만건의 데이터 안정정 INSERT 성공 ~!
<br>
<hr>
<br>

# Scenario 2. 선착순 쿠폰 발급 아키텍처
> <b>시나리오 조건</b> <br> 1. 서버의 임계치 이상의 TPS가 유입된다.<br> 2. 선착순 인원 안에 포함되었는지 검증이 필요하다.<br> 3. 선착순에 포함된 인원 안에서 발급 순서는 중요하지 않다.
## 🛠️ 프로젝트 아키텍쳐
![_coupon waiting queue sequence diagram](https://github.com/user-attachments/assets/8d0cf66b-784f-4571-b962-64811a0f9779)

## 🤔 해결 과정
- 서버 처리량에 무리가 가지 않도록 처리하기
    - 특성상 빠른 연산이 가능하므로 Redis의 Sorted-Set을 사용.
    - 일정량의 유저를 대기열에서 가져와 쿠폰 선착순 검증 처리하기 -> 스케줄러를 트리거로 사용
- 선착순 검증 시 동시성 문제 해결하기
    - Redis의 decr 연산 Atomic 특성 사용하여 해결
- 장애 발생 시 사용자가 장애를 인식하지 못하고 쿠폰 발급 신청하기
    - 선착순 검증, 쿠포 발급 담당 서버 분리하여 Kafka로 연결. -> Kafka의 데이터 저장 성질 이용하여 발급 서버 장애 발생 시, 복구된 후 이벤트 재소비 가능  

## ⁉️ 추가 개선 가능 부분
- 대기열 시스템 분리
    - Spring WebFlux의 Non-Blocking 특징 활용하여 대기열 입장 서비스 분리 시 더 많은 Traffic 감당 가능
- Redis 클러스터링
    - 대기열 크기가 감당할 수 없게 커질 경우 Redis Scale-out 적용
<br>
<hr>
<br>

# Scenario 3. 주문 도메인 대규모 트래픽
> <b>시나리오 조건</b> <br> 1. 서버의 임계치 이상의 TPS가 유입된다.<br> 2. 주문 발행, 주문 조회가 활발하게 일어난다.<br> 3. 많은 Join 연산으로 인해 조회 성능이 저하된다.
## 🛠️ 프로젝트 아키텍쳐
Before
![모놀로식](https://github.com/user-attachments/assets/7539a9e9-eab5-4d69-a21d-4481e8598071)
<div align = center>↓</div>

After
![cqrs](https://github.com/user-attachments/assets/708f3887-debf-432e-8f4e-ad2166590cc5)

## 🤔 해결 과정

- 조회 성능 향상 시키기
    - Before 모델에서 Index 적용하여 조회 성능 약 70% 향상 -> 주문 발행도 많은 트래픽이 있으므로 Index로 인한 쓰기 성능 저하 가능성 존재
- 주무 발행, 주문 정보 조회 서비스 강한 결합
    - 조회 기능 문제 시 주문 발행 자체가 불가능 할 수 있음 -> CQRS 모델 적용하여 분리하기
    - Query Model의 데이터베이스 NoSQL인 MongoDB 선택 -> 빠른 읽기 성능(기존 모델 대비 약 72% 성능 향상), 데이터 증가 시 Auto-Sharding 지원 
- 데이터 동기화
    - Kafka를 사용해 데이터 동기화 가능 -> 최종 일관성 보장

## ⁉️ 추가 개선 가능 부분
- NoSQL 사용 시 비용 문제
    - 기존에 MongoDB를 사용하지 않았다면 러닝 커브와, 비용적 문제 발생 가능 -> RDBMS의 역정규화로 조회 기능 향상 가능

