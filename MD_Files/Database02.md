# TIL

# 2021-03-18
 - DB
    - SQL
      - DDL(Data Definition Language)
        - DB자원의 생성/변경/삭제
        - 데이터베이스, 테이블, 뷰 등등.
      
      - DML(Data Manipulation Language)
        - Data 생성/변경/삭제
        - insert, update, delete
      
      - DQL(Data Query Language)
        - Data 조회
        - select
      
      - DML과 DQL는 통틀어서 DML이라고 부르기도 한다.

    - Key의 종류
      - key의 정의 : 데이터를 식별할 때 사용할 수 있는 컬럼들

      - key의 종류
        - Candidate Key (최소 키 = 후보 키)
          - 테이블에서 각 컬럼을 유일하게 식별할 수 있는 최소한의 속성들의 집합
        
        - Primary Key(메인 키)
          - 후보키들 중에서 하나로 선택한 키로, 최소성과 유일성을 만족하는 속성
          - 테이블에서 기본키는 오직 1개만 지정할 수 있다.
          - 기본키는 NULL값을 절대 가질 수 없고, 중복된 값 또한 가질 수 없다.
    
        - Alternate Key(대안 키)
          - 후보키가 두 개 이상일 경우, 그 중에서 어느 하나를 기본키로 지정하고
            남은 후보키들을 대안키 라 한다.

    - SQL 작성법
      - create, insert, select, alter, show, drop 등등.
      - 아래는 연습용 SQL문

      ```
      mysql -u 유저명 -p
      패스워드 입력

      create user 'user'@'localhost' identified by '비밀번호';
      create user 'user'@'%' identified by '비밀번호'; <-원격 접속용 
      create database userDB character set utf8 collate utf8_general_ci;
      위 문에서 default를 사용하면, utf8로 인식되지 않기 때문에, 캐릭터셋을 설정한다.
      
      grant all on userDB.* to 'user'@'localhost';
      user에게 userDB의 모든 권한을 부여한다.
      
      drop user 'user'@'localhost';
      
      user로 로그인 후,
      show databases;
      show tables;
      use userDB;
      none에서 userDB로 활성화
      
      create table test(
        num int not null,
        name varchar(20),
        grade char(20)
      );
      not null은 삽입 시, null값을 줄 수 없다는 뜻이고,
      varchar(수) 와 char(수)의 차이는
      insert into test(grade)) values("A+");
      를 적용했을 때, 문자열 길이가 총 20byte인데, test는 2byte임에도 불구하고,
      나머지 18byte도 사용한다.
      따라서, 메모리 절약 측면에서 좋은 방법은 아니다.
      
      반면에, varchar(20)은 문자열 길이만큼 byte를 사용한다.
      
      그리고 설정한 바이트 길이를 넘을 수 없다.
      Ex) insert into test(name) values("123456789012345678901");
      위 코드는 오류가 발생한다.
      
      create or replace table test(
        num int not null,
        name varchar(30) default 'Zero',
        height float default 0.0
      );
      replace는 현재 테이블이 존재할 때,
      기존에 있던 테이블을 지우고, 새로 만들어 주는 용도이다.
      
      default값을 설정하고,
      insert into test(num) values(1);
      위 코드를 적용하면,
      자세히 보기를 했을 때, default행에는 적용한 값이 출력되고,
      select로 값을 보면, num(1), name(zero), height(0) 이 출력된다.

      create table test(
      no int,
      name varchar(20),
      age int,
      kor int,
      eng int,
      math int,
      constraint test1_pk primary key(no),
      constraint test1_uk unique (name, age)
      );
      위 코드 처럼, 하나의 문에 다 적용할 수도 있고,
      
      create table test(
      no int,
      name varchar(20),
      age int,
      kor int,
      eng int,
      math int,
      );
      
      alter table test
        add constraint test1_pk primary key (no),
        add constraint test1_uk unique (name, age);
      
      이렇게 적용할 수도 있다.
      여기서 주의할 점은 alter를 사용하면, 테이블명 뒤에 ()가 오지 않는다.
      따라서, 마지막 문장에 세미콜론(;)을 추가하고 적용하면 된다.
      ```