# TIL

# 2021-03-25
 - DTO
    - Data Transfer Object의 약자이며,
      사용자가 데이터를 입력하면, 객체에 데이터를 담은 후,
      객체를 DAO(Data Access Object)로 전달한다.
      그리고 전달 받은 DAO는 객체를 꺼내서, JDBC Driver에 넣고
      JDBC Driver는 DBMS와 SQL문으로 연결을 해서 결과를 주고 받는다.
      
 - DAO
    - Data Access Object의 약자이며,
      데이터 처리를 위한 JDBC 프로그래밍 코드를
      별도의 클래스로 캡슐화한다.
      캡슐화하는 이유는 복잡한 코드들을 메소드로 묶어서
      감춰서 코드를 재사용하기 용이하게 하려는 목적에 있다.

 - VO / Domain / DTO
    - VO(Value Object)
      - 값을 저장하는 용도로 만든 클래스

    - Domain Object
      - 값 중에서 업무의 개념이나, 사물에 해당하는 값을
        저장할 목적으로 만든 클래스

    - Data Transfer Object
      - 값 중에서 객체와 객체 사이에 메소드를 호출할 때
        여러 값을 간단히 전달할 목적으로 만든 클래스
    
    - VO / Domain / DTO 를 정의하는 순서
      - 1. 요구사항 분석
        - 시스템을 통해, 수행할 업무가 무엇인지 식별
          개념, 사물, 업무 데이터를 저장

      - 2. 시스템 설계
        - 아키텍처 선정
        - application 설계
        - 클래스 식별, 메소드 식별
          - VO(Domain Object를 보충 + Data Transfer Object를 식별)