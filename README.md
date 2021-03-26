# TIL

# 2021-03-15
 - Thread 이해
    - Thread는 하나의 실행 환경이고, Stack을 포함한다.
      그래서 여러 Thread를 생성해서 동시에 사용할 수 있다.
      Heap과 Method Area는 한 프로세스에 하나 뿐이다.
    
    - 각각의 Thread는 Heap과 Method Area를 공유하며 사용할 수 있다.
    
    - JVM과 Thread 실행 순서
     1. 명령어를 실행시키면, JVM이 실행된다.
     2. 실행된 JVM은 Thread를 생성하고 run()을 실행시키고, main()을 호출한다.
     3. class파일은 Method Area를 로딩한다. (클래스 로딩)
     
    - 프로세스 복제 방식의 멀티프로세싱이 있지만,
      메모리도 똑같이 복제해서 사용하므로, 메모리 낭비가 심해진다.
      따라서, 프로그램이 무거워진다.
      반면에, 멀티쓰레드는 특정 코드를 복제해서 사용한다.

    - 쓰레드는 Heap과 Code 영역을 공유한다.
      그래서 메모리 사용량이 적다.

    - 부모 쓰레드와 자식 쓰레드는 우선 순위가 같다.
      주의할 점은 모든 쓰레드가 종료될 때까지 JVM은 종료되지 않는다.

    - JVM의 쓰레드들
        - Reference Handler = 인스턴스의 레퍼런스 카운트 관리
        - Finalizer = Garbage 관리

    - Thread와 Runnable의 차이
        - 쓰레드를 구현하는 방법은 2가지가 있다.
     
        - 첫 번째로, Thread 클래스를 상속 받아서 사용하는 것인데,
        Thread 클래스를 상속 받으면, 타 클래스를 상속 받을 수 없다는 단점이 있다.
        ```
        static class A extends Thread {
           @Override
           public void run() {
               내용
           }
        }
       A a = new A();
       a.start();
        ```
        - 두 번째는 Runnable 인터페이스를 구현하는 방식이다.
          Runnable 인터페이스 구현은 코드 재사용성이 높고,
          일관성을 유지할 수 있다는 장점이 있다.
          그래서 주로, Runnable 방식을 사용한다.
        ```
       static class B implements Runnable {
           @Override
           public void run() {
               내용
           }
       }
       Thread b = new Thread(new B());
       b.start();
        ```
    - 쓰레드 생명주기
        - NEW - 객체를 생성하고, start() 메소드를 호출하기 전.
        - Runnable - 쓰레드가 실행되기 위한 준비 단계.
                      start() 메소드를 호출하면, run() 메소드에 설정된 쓰레드가
                      Runnable 상태로 진입한다.
        - Running - CPU를 점유하여 실행중인 상태. 
        - Terminated - 실행 종료 상태
                       Running 상태에서 쓰레드가 모두 실행되고 난 후, 완료 상태가 된다.
                       종료된 쓰레드는 다시 시작할 수 없다.
        - Blocked - 일시 정지 상태
                    wait() 메소드에 의해 Blocked 상태가 된 쓰레드는 notify() 메소드를 호출하면, Runnable 상태로 돌아간다.
                    sleep(시간) 메소드에 의해 Blocked 상태가 된 쓰레드는 지정된 시간이 지나면,
                    Runnable 상태로 돌아간다.

 - 멀티태스킹 동작 원리
    - 동시에 여러 작업을 수행한다.
      실제로는 여러 작업을 번갈아가면서 실행하지만,
      CPU가 매우 빠르기 때문에, 동시에 실행되는 것처럼 보일 뿐이다.

    - CPU Scheduling(CPU 분배 방식)
        - Round Robin : 시간을 일정하게 쪼개서 프로세스에 순차적으로 분배
            - Windows의 실행 방식
        - Priority : 우선 순위가 높은 프로세스에게더 많이 분배
            - Unix 계열 : Linux, MacOS 등
            - 이 방식에 큰 문제(우선 순위가 낮은 프로그램이 엄청 나중에 실행되는 문제)를
              해결하기 위해, aging 기법이 탄생했다.
              aging : 우선 순위가 낮은 프로세스가 실행 순서에서
                      우선 순위가 높은 다른 프로세스에 밀릴 때마다,
                      우선 순위를 증가시켜서 결국에는 실행되게 만든다.

    - Context Switching
        - 프로세스(하나의 프로그램)의 실행 정보를 저장하고 읽어오는(로딩) 작업
     
        - 항상, 멀티태스킹에는 context switching 시간이 필요하다.
          따라서, 실행시간을 결정할 때, context switching 시간도 고려해야 한다.
          이 작업이 필요한 이유는 정보를 저장하지 않으면, 위치가 꼬여버리기 때문에,
          확실하게 저장과 로딩을 해야 순서가 뒤바뀌지 않는다.

        - 프로세스나 Thread 모두 동등한 조건으로 CPU를 배분한다.
          멀티태스킹을 이용하여, 반복문을 돌린다고 가정해보자.
          비록 실행 결과를 보면, 순서가 섞인 채로 출력되지만,
          긴 시간을 본다면, 결국엔 동등하게 시간이 배분된 채로
          실행된 결과를 볼 수 있다.
     
        - 하나의 명령은 n개의 작업을 수행할 수 있고,
          하나의 작업은 하나의 클럭을 실행한다.
          하나의 클럭이 실행될 때, 전기공급이 필요하며,
          공급해주는 기계를 수정 발진기 라고 한다.

        - 하나의 작업은 메모리 값 저장, 로딩, AND, OR, NOR 연산 등 하나만 수행한다.
          예를 들면, 더하기 연산을 할 때, 로딩을 해야 하며, 연산을 하고, 결과를 저장해야 한다.
          따라서, 더하기 연산을 수행하려면 n개의 작업이 필요하다.
    
        - 1초 내에 일어나는 진동의 수를 Hz(헤르츠) 라고 한다.
          CPU에 전기적인 공급을 하는 신호를 클럭 이라고 한다.
          클럭이 1초에 처리할 수 있는 횟수 즉, Hz가 1이면 
          클럭이 1초에 처리할 수 있는 단위는 1번이다.
          그래서 기가헤르츠(GHz)는 초당 10억번 이상의 정보를 처리할 수 있다.
          2GHz = 클럭 당 20억

 - 동기 / 비동기
    - synchronous(동기) 와 asynchronous(비동기)로 나뉜다.
    
    - 비동기 방식의 문제점
        - 동시에 여러 쓰레드가 같은 메모리의 값을 변경할 때 문제가 생긴다.
          예를 들면, 동시에 계좌에서 돈을 빼려고 한다면, 먼저 인출하는 쪽을
          인식하는 순간, 다른 곳에서 접근을 막아야 하는데, 비동기 방식은 그렇지 않다.

        - 해결책 : 데이터 변경과 관련된 코드를 실행할 때는 한 번에 한 쓰레드만
                   진입할 수 있게 제한한다.
        
        - 뮤텍스(Mutex)와 세마포어(Semaphore)에서 뮤텍스가 비동기 방식의 문제점을
          해결하는 방법이고, 세마포어는 n개의 쓰레드까지만 진입할 수 있게 하는 방법이다.
          

# 2021-03-16
 - Thread 이해
    - Critical Section 과 Thread Safe
      - Critical Section 이란, 임계영역이란 뜻으로
        여러 쓰레드가 동시에 실행할 때, 문제가 될 수 있는 코드를 말한다.
        그래서 쓰레드가 안전하지 않다고 라고 표현할 수도 있다.
      
      - Thread Safe 란, 여러 쓰레드가 동시에 실행해도 문제가 없는 코드를 말한다.
        보통, 데이터를 조회하는 코드로 사용된다.

 - Semaphore 와 Mutex
    - Semaphore(n)는 Critical Section에 n개의 쓰레드를 접근 허용하게 하는 방식
    
    - Mutex는 Critical Section에 오직, 1개의 쓰레드만 접근 허용하게 하는 방식.
      다른 표현으로, Semephore(1) 이라고 할 수 있다.


 - 비동기 프로그래밍의 문제점
    - 여러 쓰레드가 동시에 특정 메소드를 호출하면, 중간 코드를 건너 뛰는 상황이 발생할 수 있다.
      이처럼, 중간 코드를 건너 뛰는 상황을 문제가 발생되는 코드라고 하고
      그것을 Critical Section 이라고 부른다.

 - wait() / notify()
    - Thread.sleep(시간) 과 다르게, wait()는 notify()가 호출되지 않는 이상,
      절대 일어나지 않는다.
    
    - 실행 순서는 다음과 같다.
      - 생성한 쓰레드에서 wait()를 실행해서 객체에 넘기고,
        main 쓰레드에서 값을 설정하고, notify()를 실행해서 객체에 담는다.
        그리고 마지막으로 기존 쓰레드에서 사용을 한다.
        Ex) 홀서빙이 주문을 받고, 객체에 넘긴 다음, 요리사는 객체를 확인하고
            조리를 시작한 다음, 조리가 완료되면 객체에 담는다.
            그리고 홀서빙은 그 객체를 서빙으로 마무리한다.

 - Pooling 기법
    - 하나의 Thread Pool 안에 여러 쓰레드가 존재하며,
      작업이 실행될 때마다, 쓰레드가 빠져 나가서, 작업을 수행하고,
      작업이 완료되면 다시 Thread Pool 안으로 돌아간다.
      
      - 순서 : 1. get -> 2. 작업 수행 -> 3. return
      - 웹사이트의 대기열과 같은 원리다.
        한꺼번에 많은 접속자가 몰릴 때, 대기열이 생기는데
        여기서 한 명 당 하나의 쓰레드가 사용되는 셈이다.
        그래서 작업을 마무리한 사용자가 웹사이트에서 빠져 나가면,
        사용자가 점유했던 쓰레드는 return하여 Thread Pool로 돌아간다.
        그래서 대기하고 있던 다른 사용자가 사용을 할 수 있게 된다.

      - 사용 목적은 객체를 생성하고, 다 사용 후 반납함으로써,
        재사용할 수 있어서, 메모리 절약에 도움된다.

      - Pooling 기법을 사용하는 주 목적은 객체의 재사용에 있다.

 - Web Server와 Thread Pool
    - Web Server 안에 Thread Pool 이 존재하고,
      Web Browser에서 Web Server 에 접속을 하면,
      Thread Pool의 한 개의 Thread가 빠져 나와서 
      Web Browser의 요청을 받고, Thread는 응답을 한다.
      이 작업이 끝나면, Thread는 다시 Thread Pool로 돌아간다.


 - 동기화
    - 장점 : 여러 쓰레드를 동시에 접근하는 것을 막고, 순차적으로 접근하게 할 수 있다.
    
    - 단점 : 동시 실행의 이점을 포기하는 것이기 때문에, 실행 시간이 더 걸린다.
             CPU의 성능을 극대화해서 사용하는 것이 아니다.

    - 동기화 처리 문법
      - synchronized 접근제어자 리턴타입 메소드명
      ```
      synchronized public void Test()
      ```
      ```
      synchronized (객체)
      ```


# 2021-03-17
 - DBMS
    - DBMS와 SQL 역할
      - DBMS는 데이터 저장/조회 역할을 하며,
        데이터 관리를 총괄한다.
        사용자 접근 제어 (Authentication)
        사용자 권한 제어 (Authorization)
        여러 클라이언트 접속 제어 등등

      - DBMS에 상관없이, 명령어를 작성할 수 있도록
        명령 작성 문법을 통일시키고 이 문법을 SQL이라 부른다.
    
    - APP -> DBMS 흐름
      - App은 JDBC Driver를 호출하고,
        호출된 Driver는 DBMS와 프로토콜로 요청과 응답을 하며,
        데이터를 주고 받는다.
        그리고 DBMS는 JDBC 프로그래밍과 SQL 문법으로 파일을 관리한다.
    
    - SQL 개념
      - DBMS를 위한 표준 문법 SQL이 있지만,
        모든 DBMS에 100% 적용하지 않고, 각 사마다 독자적으로 지원하는 문법이 있다.
        따라서, 표준 SQL의 일부 문법을 지원하지 못 하는 경우도 있다.

      - DBMS를 교체하면, SQL 일부를 변경 해야 한다.    

    - DBMS와 API
      - DBMS와 DBMS API는 제조사에서 제작하며,
        여기서 API는 VenDor API라 불리며, API 파일 안에는 C/C++ fuction/class가 있다.
        그리고, DBMS API는 각 제조사마다 함수 규격이 다르기 때문에,
        특정 DBMS에 종속된다.
    
    - ODBC
      - 위와 같은 문제점을 해결하기 위해 ODBC(Open Database Connectivity)가 나왔다.
      
      - ODBC Driver는 특정 DBMS에 종속되지 않는 표준 C/C++ API로 제정하며,
        개발자는 DBMS에 상관없이 일관된 방식으로 프로그래밍이 가능하다.

      - ODBC 드라이버는 API를 모두 구현하는 것은 아니다.
        Driver마다 구현률이 다를 수 있기 때문에,
        Oracle ODBC Driver를 사용할 때, 호출할 수 있었던 함수가
        타 ODBC Driver를 사용할 땐, 호출할 수 없는 경우가 생기기도 한다.
        그래서 개발 시에 고려해서 프로그래밍 해야 한다.

      - ODBC 드라이버 동작 원리
        - ODBC Driver이 Vendor API를 호출하고, 호출된 Vendor API는
          DBMS와 요청과 응답을 주고 받는다.

    - Vendon API(Native API)
      - DBMS API는 C/C++ 로 제작되며, Native API 또는 Vendor API 라고 불린다.
        Native API는 *.dll 나 *.lib를 포함하고 있다.
        
        dll은 dynamic link library 약자로, 프로그램 실행 시, 필요에 따라
        외부 DLL파일에서 함수를 사용한다.
        
        lib는 static link library의 의미로 필요한 함수를 프로그램 코드에 붙여
        프로그램 자체에서 사용한다.

        lib는 중복 로딩이 될 수 있고, dll은 한 번만 로딩한다.


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


# 2021-03-19
 - DB
    - auto-increment
      - 지정한 번호의 다음 값을 삽입한다.
        혹시 지정하지 않는다면, 최대 수에서 증가한 값을 삽입한다.
        Ex) 1,2,3 순차적으로 삽입하다가 중간에 100을 삽입하더라도,
            그 다음은 4가 아닌 101이 삽입된다.
    
    - unique 설정
      - unique를 설정하려면 우선, 현재 컬럼의 레코드에 값이 null이면 안 된다.
        따라서, null 값을 유효한 값을 넣은 다음 설정해야 한다.
        ```
        update test set name='aaa' where name is null;
        ```
        조건문에서 null은 다른 케이스와 다르게, 컬럼 = null 이 아닌,
        컬럼 is null 이여야 한다.
        반대는 컬럼 is not null이다.
    
    - autocommit
      - 통상적으로 설치 후, 사용할 때 autocommit=true 로 설정되어 있다.
        그래서 실수를 방지하고자, set autocommit=false 로 설정을 바꾼 다음
        사용할 수도 있다.
        여기서 주의할 점은 오롯이 insert, update, delete문에만 해당이 된다.
        그리고 현재 클라이언트에서 값을 변경하고 확인해보면, 바로 변경된 결과를
        볼 수 있지만, 적용이 된 건 아니다.
        따라서 적용까지 하려면, commit; 까지 입력해야 한다.
        추가로, 타 클라이언트에서는 변경된 결과가 아닌 그 전의 결과를 보여준다.
        
        반대로, 변경을 했지만 되돌리고 싶다면, rollback; 명령어를 입력하면 된다.

    - concat
      - concat(값1, 값2, 값3, ...);
      - 여러 개의 값을 하나의 문자열로 만든다.
      ```
      select num, concat(name,'(',class,')') from test;
      ```
      그리고 그룹명을 만들 수도 있다.
      ```
      select num Number <- num은 컬럼명, Number는 그룹명 
      ```

    - Projection 과 Selection
      ```
      select num, name from test where name is null;
      ```
      위 문에서 num, name이 Projection을 의미하며,
      where name is null 이 Selection을 의미한다.
      쉽게 말하자면, 레코드(Row)가 Selection이고, 컬럼이 Projection 이다.

    - Primary Key는 모든 테이블에 반드시 존재해야 하며, 지정하기 애매한 경우에는
      인공키를 생성해서 사용한다.

    - delete
      - delete from 테이블명; <- 이 명령어는 모든 테이블을 지우므로 주의해야 한다.
        ```
        delete from test1 where num=1;
        delete from test2 where name='김';
        ```
    
    - select
      - select 컬럼 from 테이블명; <- 조건 없이, 컬럼에 맞게 출력.
      - select 컬럼 from 테이블명 where 조건식; <- 조건에 따라 컬럼 출력.
      - select 컬럼 from 테이블명 where 컬럼 like '%문자';
      - %가 앞에 있으면 맨 뒤에 있는 문자의 값과 비교하여 같으면 출력
      - %가 뒤에 있으면 맨 앞에 있는 문자의 값과 비교하여 같으면 출력
      ```
      select * from test;
      select num, name from test;
      select * from test where num=1;
      select * from test where (num % 2) = 0;
      select * from test where name like 'a%';
      select * from test where name like '%a';
      ```
      
    - 날짜 관련 연산자와 함수
      - select now(); <- 현재 날짜 와 시간 출력
        Ex) 2021-03-19 18:23:23

      - select curdate(); <- 현재 날짜 출력
        Ex) 2021-03-19

      - select curtime(); <- 현재 시간 출력
        Ex) 18:24:07

      - date_format
      ```
      select date, date_format(date, '%m/%e/%Y') from test;
      출력 : 03/19/2021
      select date, date_format(date, '%M/%d/%y') from test;
      출력 : March/19/21
      select date, date_format(date, '%W %w %a') from test;
      출력 : Friday 5 Fri
      select date, date_format(date, '%M %b') from test;
      출력 : March Mar
      select date, date_format(now(), '%p %h %H %l') from test;
      출력 : PM 06(시) 18(시) 6(시 한 자리)
      select date, date_format(now(), '%i %s') from test;
      출력 : 34(분) 08(초)
      ```

      - 날짜 값을 저장할 때 기본 형식은 yyyy-MM-dd이다.
        - 문자열을 날짜 값으로 저장하려면, str_to_date() 함수 사용해야 한다.
          Ex) insert into test(title, date) 
              values('aaaa', str_to_date('03/19/2021', '%m/%d/%Y'));

      - FK (Foreign Key)
        - 다른 테이블의 Primary Key를 참조하는 컬럼.
        - 사용 이유 : 데이터 중복을 방지하기 위해 사용.
      
      - 테이블 정규화
        - 데이터 중복을 제거하는 테이블 설계 기법.
        
        - 데이터 중복은 곧, 데이터 결함을 발생시키는 원인이다.
        
        - 제 1 정규화 ~ 제 6 정규화 까지 있지만,
          실무에서는 제 1 ~ 3 정규화까지 주로 사용한다.

        - Relational Database Management System.
          RDBMS 라고 부른며,
          ER-Diagram 이라고 부르기도 한다.
          Entity Relationship. 
        
        - 사용 방법은 중복되는 컬럼이 존재하면, 별도의 테이블에 분리한다.

        - 별도 테이블을 자식 테이블로 만들고,
          기존 테이블을 부모 테이블로 만든다.

        - 자식 테이블에는 부모 테이블과 대칭되는 컬럼을 하나 준비한다.
          그리고 인공 키를 만들어 Primary Key로 적용한다.
          대칭되는 컬럼을 부모 테이블의 Primary Key로부터 상속을 받는다.
        
        ```
        alter table test2
          add constraint test2_bno_fk foreign key (bno) references test1(no);
        ```
        - test2 테이블을 변경 작업을 실행하고,
          제약 조건을 건 다음, 제약조건이름(생랴 가능) 외부키를 입력하고 어떤 칼럼을 넣을 지
          정한다. 그리고 부모로 사용할 테이블을 참조하며, 마지막으로 부모테이블명 괄호 안에
          primary key를 넣으면 된다.

        - 삭제를 할 때, 부모 테이블에서 바로 삭제가 되지 않는다.
          그래서 자식 테이블에서 삭제 후, 부모 테이블에서 삭제한다.

        ```
        delete from test2 where bno=3; <- 자식 테이블에서 외부키 삭제.
        delete from test1 where no=3; <- 부모 테이블에서 메인키 삭제.
        ```
        

# 2021-03-22
 - SQL
    - 트랜젝션의 의미
      - 여러 개의 데이터 변경 작업을 하나로 묶는 것
      - commit 과 rollback에 연관이 있다.

    - source 사용
      - SQL파일을 불러올 때 사용한다.
      ```
      source C:/Users/user/..../Code.sql;
      ```
      - Windows에서는 파일 경로에서 \가 아닌 /를 사용해야 한다.
    
    - order by
      - 정렬할 때 사용한다.
      ```
      select num, name from test order by num;
      ```
      위 코드에서 num를 오름차순 정렬을 하여 출력하겠다는 의미다.
      ※ default값이 asc 즉, 오름차순이며,
      내림차순은 desc를 명시해야 한다.
      ```
      select num, name from test order by num desc;
      ```
      추가로, 정렬을 여러 번 할 수도 있다.
      예를 들면, 숫자로 우선 정렬을 한 뒤, 문자를 정렬하고 싶다면
      ```
      select num, name from test order by num desc, name desc;
      ```
      이렇게 적용하면 된다.
      
    - count
      - 행의 개수를 파악할 때 쓰인다.
      - 한 테이블에 데이터가 6개 있다고 가정해보자.
      ```
      +-----+------+--------+----------+
      | num | work | acc_no | bank     |
      +-----+------+--------+----------+
      | 100 | N    | 1000   | 신한은행 |
      | 101 | Y    | 1001   | 국민은행 |
      | 102 | N    | 1002   | 국민은행 |
      | 103 | Y    | 1003   | 우리은행 |
      | 104 | N    | 1004   | 하나은행 |
      | 105 | N    | 1005   | 신한은행 |
      +-----+------+--------+----------+
      ```

      ```
      select count(컬럼명 or *) from 테이블명 [where 조건];
      select count(*) from test;
      ```
      
      ```
      +----------+
      | count(*) |
      +----------+
      |        6 |
      +----------+
      ```
      개수를 출력해준다.

    - union
      - 중복 값 자동 제거에 쓰인다.
      ```
      select distinct num from test
      union
      select distinct num from test2;
      ```
      test와 test2의 num에서 중복된 값은 제거된 채로 출력한다.

    - union all
      - 중복 값 제거를 하지 않는다.
      ```
      select distinct num from test
      union all
      select distinct num from test2;
      ```
      test와 test2의 num을 중복 포함해서 출력한다.

    - IF EXISTS
      - 의미 그대로, 존재한다면 이라는 의미로
        테이블이 존재하면 삭제를 하고, 아니면 무시한다.
      ```
      DROP TABLE IF EXISTS test RESTRICT;
      ```
      위 코드에서 RESTRICT는 default값으로 붙이지 않아도 된다.
      쓰임새는 만일, test에 의존하는 객체(자식 테이블)이 있으면,
      table 삭제를 거부한다는 뜻이다.
      즉, 실수로 삭제하는 걸 방지하기 위해서 사용된다.

      반면에, 의존 객체를 무시하고 삭제하는 방법은
      CASCADE를 사용하면 된다.
      ```
      DROP TABLE IF EXISTS test CASCADE;
      ```
      test 테이블을 참조하는 테이블이 있어도 삭제가 진행된다.

    - cross join
      - cross join은 두 테이블의 데이터를 1:1로 연결해서 사용한다.
      ```
      select test1.num, test1.name, test2.num, test2.name
      from test1 cross join test2;
      ```
      여기서 주의할 점은 같은 컬럼명이 있다면, 어떤 테이블의 것인지
      명시를 해야된다.

      추가로, 테이블명이 길다면, 별명을 사용하여 컬럼을 지정하면 된다.
      ```
      select t1.num, t1.name, t2.num, t2.name
      from test1 t1 cross join test2 t2;
      ```

    - natural join
      - 같은 이름을 가진 컬럼 값을 기준으로 레코드를 연결한다.
        단, natural join은 문제점이 여러 가지 있다.

        우선 첫 번째로는 두 테이블의 join 기준이 되는 컬럼 이름이 다르면,
        연결할 수 없다.
        두 번째로는 상관없는 컬럼과 이름이 같을 때이며,
        세 번째는 같은 이름의 컬럼이 여러 개 존재할 때다.
        
        물론, 해결법은 존재한다.

      - 세 번째 문제점의 해결법은 using을 사용하는 것이다.
      ```
      select t1.num, t1.name, t2.num, t2.name
      from test1 t1 join test2 t2 using(num)
      ```
      using은 기준이 될 컬럼을 지정하는 용도로 쓰인다.

      - 만약 두 테이블에 같은 이름을 가진 컬럼이 없으면,
        natural join을 수행하지 못 하며, join using으로도 해결을 못 한다.
        따라서, join ~ on 문법을 사용해야 한다.
        ```
        select t1.num, t1.name
        from test1 t1 inner join test2 t2 on t1.num=t2.number;
        ```

      - join ~ on 의 문제점
        - 반드시, on 에서 지정한 컬럼의 값이 같을 경우에만
          두 테이블의 데이터가 연결된다.
          따라서, 같은 값을 갖는 데이터가 없다면, 연결이 되지 않고
          결과로 출력되지 않는다.

      - outer
        - join ~ on 의 문제점을 해결한다.
        - Left 테이블을 기준으로 Right 테이블의 데이터를 연결한다.
        - 만약, Left 테이블과 일차하는 데이터가 Right 테이블에 없더라도
          Left 테이블의 데이터를 출력한다.
          

        ※ 아래 결과는 예시

        outer 사용 전
        ```
        +-----+------+------+----------------+
        | num | room | loc  | titl           |
        +-----+------+------+----------------+
        |   1 | 501  | 강남 | Solaris        |
        |   4 | 301  | 종로 | Linux          |
        +-----+------+------+----------------+
        ```

        outer 사용 후
        ```
         +-----+------+------+----------------+
        | num | room | loc  | titl           |
        +-----+------+------+----------------+
        |   1 | 501  | 강남 | Solaris        |
        |   4 | 301  | 종로 | Linux          |
        |   5 | NULL | NULL | System         |
        +-----+------+------+----------------+
        ```
        
        select 컬럼명
        from Left테이블명
        left outer join Right테이블명 on 조건문;

        ```
        select t1.num, t1.name
        from test1 t1
        left outer join test2 t2 on t1.num=t2.num;
        ```
        
        left outer가 아닌 right outer를 사용한다면,
        기준이 오른쪽이 된다.
        따라서, test2를 기준으로 test1의 데이터를 연결한다.


# 2021-03-23
 - JDBC Programming
    - JDBC API와 Driver
      - JDBC API
        - 드라이버 제어와 관련된 클래스를 제공한다.
        - 드라이버 호출 규칙을 인터페이스로 정의
        - 관련 패키지 java.sql.*, javax.sql.*

      - Driver
        - API 규칙에 따라 클래스를 구현한다.
        - 모든 JDBC Driver는 사용법이 같다.
          사용법이 같다는 의미는 수행하는 클래스 및 메소드가 같다는 뜻이다.
      
  - API 의 개념
    - 애플리케이션을 제작할 때 사용하는 도구이다.
      여기서 제작은 Programming을 의미하고,
      도구는 클래스, 인터페이스, Enum 등을 의미한다.
    
    - 직접 사용하는 경우는
      DBMS의 Vendor API 사용,
      네트워크 API(java.net.*) 사용,
      File I/O API(java.io.*) 사용,
      Collection API(java.util.*) 사용 등이 있다.
      즉, JDK에 내장된 API를 사용하는 의미이다. 

    - 외부 API를 추가하여 사용하는 경우도 있다.
      정의된 규칙에 따라 클래스나 인터페이스를 구현한다.
      JDBC API(java.sql.*, javax.sql.*),
      XML API(java.xml.*) 등이 있다.
      외부 JDBC 파일을 다운 받아서 적용한 다음, 사용할 수 있다.

  - JDBC Driver 의 주요 클래스
    - DriverManager
      - JDBC Driver 목록 관리
      - Driver 역할자를 실행

    - Driver 구현체
      - JDBC Driver 정보를 다룸
      - Connection 역할자 생성

    - Connection 구현체
      - DBMS 연결 정보를 다룸
      - Statement 역할자 생성
      - Commit/Rollback 수행
    
    - Statement 구현체
      - SQL을 DBMS에 전송
      - ResultSet 역할자 생성

    - ResultSet 구현체
      - 서버에서 실행한 Select 결과를 한 개씩 가져온다.

    - 실행 순서
      1. App이 Driver 구현체를 생성한다.
      2. Driver 구현체가 DriverManager를 등록한다.
      3. App이 DriverManager에게 DBMS 연결을 요청한다.
      4. DriverManager는 Driver 구현체에게 connect() 요청한다.
      5. Driver 구현체는 Connection 구현체를 생성한다.
      6. App의 SQL 실행자 요청을 Connection 구현체에게 보낸다.
      7. Connection 구현체는 받은 요청을 토대로, 
         Statement 구현체를 생성한다.
      8. App의 SQL 실행 요청을 Statement 구현체에게 보낸다.
      9. Statement 구현체는 ResultSet을 생성한다.
      10. App은 ResultSet 구현체에게 Select 결과 조회를 요청한다.

  - DriverManager 와 Dirver 객체
    - App은 getConnection("JDBC URL")에서 
      UDBC URL 정보를 보고, 실행할 Driver 객체를 선택해서 연결한다.

      만일, 등록된 드라이버가 없다면, 연결 에러가 발생한다.

  - Driver 구현체 등록 방법
    1. Driver 구현체 생성 후, 직접 등록
      - App에서 Driver 구현체를 생성하고,
        DriverManager로 Driver를 등록한다.
    
    ```
    java.sql.Driver mariaDB_Driver = new org.mariadb.jdbc.Driver();
    DriverManager.registerDriver(mariaDB_Driver);
    Driver 인스턴스 확인
    java.sql.Driver driver = DriverManager.getDriver("jdbc:mariadb:");
    System.out.println(driver);
    ```

    2. Driver 구현체 생성 후, 자동 등록
      - App에서 Driver 구현체를 생성하고,
        바로 DriverManager로 등록한다.
    
    ```
    new org.mariadb.jdbc.Driver();
    Driver 인스턴스 확인
    java.sql.Driver mariaDriver = DriverManager.getDriver("jdbc:mariadb:");
    System.out.println(mariaDriver);
    ```

    3. Driver 클래스 로딩 후, 자동 등록
      - APP에서 Driver 구현체를 Class.forName()으로 로딩하고,
        DriverManager로 등록한다.
    
    ```
    Class.forName("org.mariadb.jdbc.driver");
    Driver 인스턴스 확인
    java.sql.Driver driver = DriverManager.getDriver("jdbc:mariadb:");
    System.out.println(driver);
    ```
    
    4. JVM Property에 "jdbc.drivers" 라는 이름으로
       Driver 구현체를 지정해두면, DriverManager가 지정된
       클래스를 자동 로딩한다.

      - DriverManager가 JVM Property 클래스를 로딩한다.

    ```
    System.setProperty("jdbc.drivers", "org.mariadb.jdbc.Driver");
    System.out.println(System.getProperty("jdbc.drivers"));
    ```

    5. DriverManager의 getDriver()나 getConnection()을 호출할 때,
       *.jar(Java Archive) 파일에서 META-INF/services/java.sql.Driver
       파일을 찾는다. 만약 있다면, 그 파일에 적어 놓은 클래스를 자동 로딩한다.

      - DriverManager가 *.jar JDBC 드라이버 파일을 찾아서 읽고,
        적어 놓은 클래스를 로딩한다.
    
    ```
    java.sql.Driver driver = DriverManager.getDriver("jdbc:mariadb:");
    Driver 인스턴스확인
    System.out.println(driver);
    ```

    - static 블록을 실행하고, 안에서 new를 실행하기 때문에 바로 생성이 된다.
      클래스 로딩 -> static 블록 실행 -> 인스턴스 생성
    

# 2021-03-24
 - JDBC Programming
    - DBMS에 연결
      - JVM에서 jdbc driver 파일(.jar)을 탐색하며,
        java.sql.Driver를 구현한 클래스를 자동으로 로딩한다.
        따라서, Class.forName("org.mariadb.jdbc.Driver") 를
        명시할 필요가 없다.

      - 연결 방법
        - DriverManager에게 DBMS와의 연결을 요청한다.
          jdbc url : DBMS 서버 정보
          Ex) jdbc:DBMS명://서버주소:포트/데이터베이스명.
          
          username : DBMS 사용자 아이디
          Ex) 데이터베이스명?user=사용자명.

          password : DBMS 사용자 암호
          Ex) 데이터베이스명?user=사용자명&password=암호.

          ```
          java.sql.Connection connect = DriverManager.getConnection(
            "jdbc:mariadb://localhost:3306/userdb?user=vella&password=1234"
          )
          ```
        - 실행 순서
          - DriverManager는 등록된 java.sql.Driver 구현체 중에서
            jdbc url에 지정된 Driver 객체를 찾는다.
          
          - Driver 객체의 connect()를 호출한다.

          - Driver 구현체(Driver 객체)는 DBMS와 연결 후,
            소켓 정보를 갖고 있는 java.sql.Connection 구현체를 리턴한다.
          
          - 자원해제를 위해 close()를 호출해야 하지만,
            try with resources를 활용함으로써 해야 할 필요가 없어진다.
    
    - Statement 구현
      - Statement 객체란, SQL문을 DBMS의 형식에 따라
        인코딩하여 서버에 전달하는 역할을 한다.
      - Statement 객체를 사용하기 위해서는
        아래와 같이 코드를 구현해야 한다.
        ```
        try 
        (java.sql.Connection connect = DriverManager.getConnection(
            "jdbc:mariadb://localhost:3306/userdb?user=vella&password=1234"
          );
        java.sql.Statement stmt = connect.createStatement()) 
        {
           INSERT, UPDATE, DELETE문 수행 가능
           stmt.executeUpdate("insert into test(name) values("하나")); 
           stmt.executeUpdate("update test set name = "하나" where num = 1);
           stmt.executeUpdate("delete from test where name = 1 or name = 2);
        }
        ```
        - 참조된 데이터가 있다면, 자식 테이블의 데이터를 먼저 삭제하면 된다.

        - 데이터 조회로 사용하는 Select문은 ResultSet 객체로 구현한다.
        ```
        java.sql.ResultSet rs = stmt.executeQuery(
          "select * from test order by num desc"
        );
        ```

        여기서 값을 가져 오면 true를 리턴하고,
        아니면 false를 리턴한다.
        
        전체 출력을 위해서는 while(rs.next()) 를 사용한다.
        ```
        System.out.printf("번호 : %d 이름 : %s 등급 : %s 가입일 : %s\n",
          rs.getInt("num"),
          rs.getString("name"),
          rs.getString("grade"),
          rs.getDate("registeredDate")
        );
        ```
        getXxx(컬럼명)은 컬럼의 값을 출력한다는 의미다.
        반대로 컬럼값 설정은 setXxx(컬럼명) 이다.
        
        - 참고로, 인덱스 시작은 1 이다 0이 아니다.
          배열과 다르게 1부터 시작하므로, 주의해야 한다.
        
        - 입력스트림을 이용하여, 사용할 수도 있다.
        ```
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("번호 : ");
        int num = scanner.nextInt();
        
        System.out.print("이름 : ");
        String name = scanner.nextLine();

        System.out.print("등급 : ");
        String grade = scanner.nextLine();

        try(Connect connect = DriverManager.getConnection(
          "jdbc:mariadb://localhost:3306/userdb?user=vella&password=1234
        );
        Statement stmt = connect.createStatement()
        );
        {
          String sql = String.format(
            "insert into test(number, name, grade) values('%d', '%s', '%s')",
            num, name, grade
          );
        }
        ```

    - SQL Injection
      - 사용자가 입력한 값에 SQL실행에 영향을 끼치는
        SQL 코드를 삽입할 경우, 데이터를 의도적으로 변경 가능하다.
      
      - 입력값에 or 1=1를 추가할 경우, 참이 된다.
        따라서, 비밀번호를 모르더라도 로그인이 가능하다.

      - SQL Injection 방지를 위해, Statement가 아닌,
        PreparedStatement를 사용한다.
        
      - PreparedStatement는 기존 Statement와 다르게,
        데이터를 변경하는 위치에 ? 를 넣는다.
        ```
        update test set num = ?, name = ?, grade = ? where num = 1;
        stmt.setInt(1, num);
        stmt.setString(2, name);
        stmt.setString(3, grade);
        ```
        여기서 setXxx() 메소드를 사용하여 값을 설정한다.
        그리고 번호는 왼쪽 ?부터 시작한다.

        ? 는 in-parameter 라고 부른다.

      - Statement 와 PreparedStatement의 차이
        - Statement는 값을 가지고 문자열로 직접 SQL문을 만들기 때문에
          PreparedStatement에 비해, 읽기 힘들다.
        
        - PreparedStatement는 SQL Injection을 불가능하게 만들지만,
          Statement는 아니다.
        
        - PreparedStatement는 미리 SQL문을 작성한 다음,
          DBMS 프로토콜에 맞게 파싱을 한 후, executeUpdate()를 호출한다.
          따라서, executeUpdate()를 호출할 때마다 SQL 문법을 분석하지
          않으므로, 반복해서 실행하는 경우, Statement 보다 실행 속도가 더 빠르다.

    - PreparedStatement
      - setXxx(파라미터 인덱스, 컬럼명)을 사용한다.
      - 파라미터 인덱스는 미리 작성된 SQL문에서 좌측부터 1로 시작한다.

    - 자식 테이블과 부모 테이블
      - 부모 테이블에서 값을 추가 후, 자식 테이블에도 부모 테이블 PK로
        데이터를 추가하려면, PK를 리턴 받아야 한다.

      - Statement.RETURN_GENERATED_KEYS 를 사용하면 된다.


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


# 2021-03-26






















































          