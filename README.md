# TIL 하루에 한 번씩.

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

    - Vendor API(Native API)
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
        - 만약, Left 테이블과 일치하는 데이터가 Right 테이블에 없더라도
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
 - ER Diagram
    - 식별관계와 비식별관계
      - 식별관계 (identifying relationship)
        - FK = PK
        - 자식 테이블의 FK가 부모 테이블의 PK를 참조하면,
          식별관계가 성립된다.

      - 비식별관계 (non-identifying relationship)
        - FK ≠ PK
        - 자식 테이블의 FK가 부모 테이블의 PK가 아닌 일반키를
          참조하면, 비식별관계가 성립된다.

 - JDBC 리뷰
    - 트랙젝션 commit / rollback
      - MySQL 기준으로 setAutoCommit값을
        false로 바꾸면 DML 적용 시, 바로 저장되지 않고,
        임시테이블을 생성 후, 그 안에 저장했다가
        commit이 이루어지면, 임시테이블에 있던 데이터들이
        저장되고, 기존에 있던 임시테이블은 사라진다.
    
    - unique key
      - 컬럼내에 유일한 값을 가지는 즉, 중복 값을 허용하지 않는
        key를 unique key 라고 한다.
        ```
        alter table test
        add constraint test_UK unique(name);
        ```
    
    - 두 컬럼의 중복값 방지
      - 두 컬럼을 PK로 셋팅하면 된다.
        그러면, 두 컬럼 서로 같은 값은 가질 수 없다.

 - JDBC
    - 백업 테이블 생성
      - 기존 테이블을 부득이하게 삭제하고 재생성해야 해야 할 때
        사용한다.
        ```
        insert into 백업테이블명
        select 컬럼명(복수 가능) from 기존테이블명
        
        insert into test_bak
        select num, name, email, tel from test;
        ```

    - 패스워드 암호화
      - password(?)를 사용하면 된다.
      ```
      insert into test(name, id, password, email)
      values(?,?,password(?),?);
      
      stmt.setString(1, name);
      stmt.setString(2, id);
      stmt.setString(3, password);
      stmt.setString(4, email);
      ```
    
    - Delete join 사용
      - 자식 테이블의 데이터를 선 제거 후, 부모 테이블의 데이터를 삭제한다.
      ```
      delete 자식테이블별명, 부모테이블별명 from 자식테이블명 자식테이블별명
      left join 부모테이블명 부모테이블별명 on 참조하는값=참조되는값
      where 자식테이블의 조건

      delete t2, t1 from test2 t2
      left join test t1 on t1.num=t2.num
      where t2.num = v;
      ```
      - 두 개 이상의 자식 테이블을 삭제할 경우
      ```
      delete 자식테이블명1, 자식테이블명2 from 부모테이블명
      inner join 자식테이블명1 on 참조값=참조값
      inner join 자식테이블명2 on 참조값=참조값
      where 조건

      delete t1, t2 from test t
      inner join test1 t1 on t1.no = t.no
      inner join test2 t2 on t2.no = t.no
      where t.no = 1;
      ```
      

# 2021-03-27
 - SQL문 복습
    - root 사용
      ```
      mysql -u root -p
      
      유저 목록 조회
      select user, host from mysql.user;

      유저 생성
      create user 'user'@'localhost' identified by '1234';
      create user 'user'@'%' identified by '1234';
      % 의 의미는 모든 네트워크에서 접속을 허용한다는 의미다.

      데이터베이스 생성
      create database userdb default character set utf8
      collate utf8_general_ci;
      
      데이터베이스 권한 부여
      grant all on userdb.* to 'user'@'localhost';
      ```

    - user 사용
      ```
      mysql -u user -p
      
      권한을 부여 받은 데이터베이스 사용
      use userdb;
      
      show tables;

      create table test(
        num int not null,
        name varchar(25) not null,
        email varchar(30) not null,
        address varchar(100) default ''
      );

      create table test2(
        num int not null,
        name varchar(25) not null,
        email varchar(30) not null,
        address varchar(100) default ''
      );

      제약조건 추가
      alter table test
      add constraint primary key(num);
      add constraint unique (email);

      번호 자동 증가
      alter table test
      modify column num int auto_increment;

      외부키 참조
      alter table test2
      add constraint foreign key(num) references test(num);

      ``` 


# 2021-03-29
 - 실습 프로젝트 진행
    - 작업을 등록하기 전에 현재 등록된 프로젝트 목록을 로딩하고,
      프로젝트에 적용할 작업을 등록한다.

      ```
      List<Project> projects = new ArrayList<>();

      while (rs.next()) {
        Project p = new Project();
        p.setNo(rs.getInt("no"));
        p.setTitle(rs.getString("title"));
        projects.add(p);
      }

      System.out.println("[프로젝트 목록]");
      if (projects.size() == 0) {
        System.out.println("등록된 프로젝트가 없습니다.");
        return;
      }
      for (Project p : projects) {
        System.out.printf("%d,  %s\n", p.getNo(), p.getTitle());
      }

      int projectNumber = 0;
      loop:
      while(true) {
        String input = Prompt.inputString("프로젝트 번호(취소 : 엔터) : ");
        if(input.length() == 0) {
          System.out.println("작업 등록을 취소합니다.");
          return;
        }
        try {
          projectNumber = Integer.parseInt(input);
        }
        catch (Exception e) {
          System.out.println("숫자를 입력하세요.");
          continue;
        }
        for(Project p : projects) {
          if(p.getNo() == projectNumber) {
            break loop;
          }
        }
        System.out.println("존재하지 않는 프로젝트 번호입니다.");
      }
      ```
      
      - 예외 처리할 것들을 항상 주의해야 한다.
        숫자만 입력해야 하는 상황인데 문자를 입력한다면,
        오류가 나기 마련이다.
        
        그렇기 때문에, 문자로 입력하고 파싱한 다음 정수로
        사용하면 문제가 없다.

      - 순서에 유의하자.
        실행 순서에 따라 예상밖의 결과값을 얻을 수도 있다.
        제어와 반복을 적절하게 잘 사용하여, 자연스러운 흐름을 만들자.

      - SQL문에서 조건을 만들 때, 더 고민을 하자.
        괜히 하나 더 만들 수도 있게 된다.

      - FK 설정과 참조 키 설정 생각할 것.

      - where절에서 and / or 를 생각하자.

      - order by 사용법 추가
        - 첫 번째 컬럼 다음, 두 번째 컬럼이 올 때
          첫 번째 컬럼이 정렬된 후, 나머지 컬럼을 정렬한다.
          예를 들면, 4 2 3 1 -> 1 2 3 4 첫 번째 컬럼 정렬
          3.9 1.1 1.3 4.1 2.2 4.5 3.1 1.2 3.2 2.1 2.3 3.3
          두 번째 컬럼 정렬
          1.1 1.2 1.3 2.1 2.2 2.3 3.1 3.2 3.3 3.9 4.1 4.5
          두 컬럼 전부 오름차순으로 정렬하였기에,
          첫 번째 컬럼은 1 -> 2 -> 3 -> 4 가 되면서
          두 번째 컬럼은 1의 수들을 먼저 정렬하고,
          그 다음 2의 수 -> 3의 수 -> 4의 수 이렇게 정렬이 된다.

      - 자바클래스에서는 쌍방참조를 하면 안 된다.
        

# 2021-03-30
 - 실습 프로젝트
    - 데이터 조회 시, PK를 반드시 조회해야 한다.
    ```
    select p.no, p.title, p.content, p.writer, m.no, m.name
    from project p
    inner join member m on m.no=p.writer
    where p.no = ?
    ```

    - Static 블록을 사용하는 이유
      - 인스턴스 필드로 사용을 하지 못 할 때.
      - 변수 초기화로 해결이 되지 않을 때.

    - Static 블록 순서
      - static 변수가 선언이 되있으면,
        static 블록에서 변수를 초기화하고,
        static 필드에는 있던 값이 사라진다.
        ```
        public class Ex {
          static int a = 10;
          
          static {
            a = 10;
          }
        }

        static int a;
        ```
        
    - try with resources
      - 괄호 안에 들어갈 수 있는 건, AutoCloseable 구현체와
        변수 선언이다.
        ```
        O try(String check = "check";)
        
        String check = null;
        X try(check = "check";)
        ```
        

# 2021-03-31
 - 실습 프로젝트
    - 하나의 테이블에 하나의 DAO만 소유하게 해야 한다.
      DAO끼리 종속 관계를 맺으면, 유지보수가 더 힘들어진다.
      하나의 DAO가 여러 테이블을 조회하는 건 괜찮다.

    - 인터페이스 도입 전.
      - DAO클래스의 SQL문 변경할 시, 
        교체할 DBMS에 맞춰, 새 DAO를 작성하고,
        작성된 DAO에 따라 Handler의 소스도 변경해야 하기 때문에
        기존에 DB를 사용하는 고객의 유지보수가 더 힘들어진다.
    
    - 인터페이스 도입 후.
      - DBMS를 교체할 때, 그에 맞는 DAO 객체를 주입하면 된다.
        주입이란, 현재 사용중인 DAO를 인터페이스화하고,
        DBMS에 맞는 패키지를 생성하고, 그 패키지 안에
        구현체 클래스들을 생성하여, DAO 인터페이스를 구현한다.

    - 인터페이스 구현체 이름 종류
      - 클래스명 Impl
        Ex) ProjectDaoImpl
      
      - DBMS명 클래스명
        Ex) MariaDBProjectDao

    - Connection 객체 주입
      - DAO가 Connection 객체를 만들게 하지 말고,
        외부에서 주입 받게 하면 된다.
        그러면, 공유와 교체가 쉬워진다.
        ```
         Connection con = DriverManager.getConnection(
        "jdbc:mariadb://localhost:3306/taehoondb?user=taehoon&password=1111"
        );

        BuyerMemberDao buyerMemberDao = new BuyerMemberDaoImpl(con);
        BuyerBoardDao buyerBoardDao = new BuyerBoardDaoImpl(con);
        BuyerBoardCommentDao buyerBoardCommentDao = new BuyerBoardCommentDaoImpl(con);
        SellerMemberDao sellerMemberDao = new SellerMemberDaoImpl(con);
        SellerBoardDao sellerBoardDao = new SellerBoardDaoImpl(con);
        SellerBoardCommentDao sellerBoardCommentDao = new SellerBoardCommentDaoImpl(con);
        OrderDao orderDao = new OrderDaoImpl(con);
        MenuDao menuDao = new MenuDaoImpl(con);
        ```
        위 처럼, DAO에서 Connection 객체를 생성할 필요가 사라진다.

    - Coonection 객체 공유와 rollback()
      - Connection을 공유해서 사용할 때는 commit에 성공하지 못해
        임시 보관소에 남아있는 데이터 변경 결과를 확실히 삭제해야 한다.
        
        그렇지 않으면, 같은 Connection으로 작업하는 다른 쪽에 영향을 줄 수 있다.

      - 이 점을 해결하기 위해, rollback을 명시적으로 호출해야 한다.

      - commit/rollback 을 하기 전에는 데이터 변경 결과를 임시DB에 보관한다.
        commit을 하면, 데이터 변경 결과가 실DBMS에 적용이 되고,
        rollback을 하면, 임시DB에 남아 있던 데이터 변경 결과가 버려진다.


# 2021-04-01
 - Library와 Framework
    - Libarary
      - 단순한 기능을 제공하는 도구
      - 해당 기능이 필요한 다양한 영역에 폭넓게 사용할 수 있다.
    
    - Framework
      - 기능들을 엮어서 어떤 역할을 수행하도록, 실행 흐름을 통제하는
        코드가 미리 작성되어 있다.
      - 웹프로그래밍 프레임워크라면, 웹 프로그래밍에만 사용이 가능하다.
        즉, 라이브러리에 쓰임새가 전문화(축소)된다.
    
 - XML DocType 의미
    ```
    <!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD config 3.0//EN"
            "http://mybatis.org/dtd/mybatis-3-config.dtd">
    ```
    - !DOCTYPE : XML 태그의 사용 규칙에 대한 정보.
    - configuration : 루트 태그 - 한 개의 XML파일에 한 개의 root tag만 존재해야 한다.
    - PUBLIC : 외부 공개 여부 / PUBLIC 외부 공개 규칙 - SYSTEM 애플리케이션 전용 규칙.
    - 1번째 "코드"
      - '-' : 개인 회사 단체 / '+' 국제 표준.
      - mybatis.org : 규칙을 관리하는 사람이나 단체명.
      - DTD config 3.0 : 규칙 이름.
      - EN : 규칙을 정의하는데 사용한 언어.
    - 2번째 "코드"
      - 상세 규칙이 정의된 문서의 위치.

 - SQL Mapper 와 OR Mapper
    - SQL Mapper
      - App이 도우미 클래스를 호출(with SQL ID)하고,
        도우미 클래스는 SQL ID에 해당하는 SQL문을 찾아서 실행한다.
      
      - 특징 : 개발자가 직접 SQL문을 작성해야 한다.
               각각의 DBMS에서 요구하는 SQL문법을 알고 있어야 한다.
               따라서, DBMS 교체 시, SQL문을 변경해야 한다.
               DBMS별 최적화가 가능하다는 장점이 있다.
      
    - OR Mapper
      - App이 도우미 클래스를 호출(with 객체 질의어 Ex. HQL)하고,
        도우미 클래스는 객체 제어 명령문을 각 DBMS SQL 변환기에게 보낸다.
        그리고 SQL 변환기는 DBMS에 맞춰 SQL을 생성하고 리턴한다.
        그 다음, 도우미 클래스는 SQL문을 실행시킨다.
      
      - 특징 : 도우미 클래스가 인식하는 전용 객체 질의 문법을 사용한다.
               따라서, DBMS에 비종송적이며, DBMS교체 시, 변경할 필요가 없다.

 - Mybatis 사용
    - 설치 방법
      - search.maven.org 접속
      - mybatis 검색 후, Group ID - org.mybatis / Artifact ID - mybatis 클릭
      - Gradle Groovy DSL 코드 복사
      - build.gradle의 dependencies 안에 붙여넣기
      - gradle eclipse 실행
      - 라이브러리에 mybatis 확인

    - 사용 방법
      - mybatis.org 접속
      - MyBatis for Java 리스트에서 Github Project 클릭
      - mybatis-3 repository 클릭
      - README.md의 Essentials 리스트에서 See the docs 클릭
      - 좌측 리스트에서 Gettring Started 클릭
      - Building SqlSessionFactory from XML의 두 번째 코드 문단 복사
      - Eclipse Project에서 XML파일 생성
      - 파일명 : mybatis-config.xml
      - 복사한 코드 붙여넣기
      - 코드 수정 후, jdbc.properties 일반 파일 생성하기
      - jdbc.properties에 driver, url, username, password 프로퍼티 넣기
      - Mapper.xml 파일 생성 후, 사용하기

    - 문법
      - XML은 대소문자를 구분한다.
      - version은 1.0으로 사용한다.
      - encoding 기본 값은 utf-8이며, 생략이 가능하다.
      - 그리고 반드시, XML 선언 코드는 첫 번째 줄에 있어야 한다.

      - SQL Mapper 파일
        - SQL문을 보관하는 파일이다.
        - SQL문 종류에 따라 다른 태그를 사용한다.
        ```
        <select> select문 </select>
        <insert> INSERT문 </insert>
        <update> UPDATE문 </update>
        <delete> DELETE문 </delete>
        ```
      - namespace 속성
        - SQL문장을 찾을 때 사용할 그룹명이다.
      
      - 자바 클래스 이름을 지정할 때 패키지와 패키지 사이는
        항상 . 으로 표기해야 한다.
        / <- 파일 경로를 가리킬 때 사용한다.

      - mybatis 설정 파일 읽기
        - 설정 파일을 읽기 위해서는 InputStream이 필요하다.
        ```
        InputStream mybatisConfigInputStream = new FileInputStream("경로/파일명.xml");
        ```
        - SqlSessionFactory 객체를 생성해 줄 빌더 객체를 준비한다.
        ```
        SqlSessionFactoryBuilder factoryBuilder = new SqlSessionFactoryBuilder();
        ```
        - SqlSession 객체를 생성해 줄 팩토리 객체를 준비한다.
        ```
        SqlSessionFactory factory = factoryBuilder.build(mybatisConfigInputStream);
        ```
        - SQL을 실행시킬 객체를 준비한다.
        ```
        SqlSession sqlSession = factory.openSession();
        ```

        위 코드들을 한 줄로 줄일 수 있다.
        ```
        SqlSession sqlSession = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("경로/파일명.xml")).openSession();
        ```
        그리고 마지막에 자원 해제를 해야 한다.
        ```
        sqlSession.close();
        ```

        - Mapper.xml 파일에 보관된 select문을 실행하여, 리스트를 가져오기
        ```
        sqlSession.selectList("네임스페이스명.SQL아이디");
        List<T> t = sqlSession.selectList("TMapper.selectT");
        ```

        - mybatis 설정 파일에서 fully-qualified class name을 사용하는 대신,
          짧은 이름으로 대체할 수 있다.

          ```
          <typeAliases>
          <typeAlias type="com.eomcs.mybatis.ex01.Board alias="board" />
          </typeAliases>
          ```
          alias를 별명으로 사용한다.
          그리고 지어진 별명은 Mapper 파일에서 클래스를 지정할 때 사용한다.
          ```
          <select id='selectBoard" resultType="board">
          ```

        - 패키지에 소속된 모든 클래스에 대한 별명을 자동으로 부여할 수 있다.
          따라서, 클래스 이름이 별명으로 설정된다.
          ex01.e패키지 안에 있는 board클래스가 별명으로 된다.
          ```
          <typeAliases>
          <package name="com.eomcs.mybatis.ex01.e">
          </typeAliases>
          ```
        
        - 도메인 패키지를 사용하여, 패키지 이름을 설정하면 편리하다.
          ```
          <typeAliases>
          <package name="com.eomcs.mybatis.vo">
          </typeAliases>
          ```

        - 각 문장 사용법
          - select문장
            sqlSession.selectList() - 목록 리턴
            sqlSession.selectOne() - 한 개의 결과 리턴
          - insert문장
            sqlSession.insert()
          - update문장
            sqlSession.update()
          - delete문장
            sqlSession.delete()

        - 메소드 사용법
          - selectList(SQL문 식별자, 파라미터값)
          - SQL문 식별자 = 네임스페이스명.SQL문장아이디
            네임스페이스명: <mapper namespace="네임스페이스명">...</mapper>
            SQL 문장 아이디: <select id="SQL문장 아이디">...</select>
            ```
            <mapper namespace="BoardMapper">
            <select id="selectBoard" resultType="Board">
            ```
          - 파라미터값
            - primitive type 및 모든 자바 객체가 가능하다.
            - 여러 개의 값을 전달하고 싶으면, Map에 담아서 넘기면 된다.  
        
        - select 태그
          - id : SQL문을 찾을 때 사용하는 식별자.
          - resultType : select 결과를 저장할 클래스 이름이나 별명.
          - 클래스 이름은 반드시 fully-qualified class name(패키지명을 포함한 클래스명)을 사용해야 한다.

        - 자바 규칙에 따라 값 넣는 방법
          - 컬럼명과 일치하는 Setter를 호출한다.
          - 컬럼명 -> set컬럼명()
          - num -> setNum(값)
          - 만일, 컬럼 이름에 해당하는 Setter를 못 찾으면, 호출하지 않는다.

        - mybatis 내부에서 selectList SQL문을 실행하는 순서
        ```
        List<Board> list = new LinkedList<>();
        while(rs.next()) {
          Board board = new Board();
          board.setTitle(rs.getString("title"); <- title이란 컬럼명이 있으면 문제 X
          board.setContents(rs.getString("contents"); <- contents란 컬럼명이 없으면 문제 O
          list.add(board);
        }
        ```
        Board 클래스에 컬럼명과 일치하는 Setter가 없으면,
        객체에 저장되지 못 한다.

        위 문제를 해결하기 위해서는 resultMap을 사용해야 한다.
        <resultMap type="자바 객체의 클래스명" id="연결 정보를 가리키는 식별자">
        컬럼명과 자바 객체의 프로퍼티명을 연결한다.
        column="테이블 컬럼명" property="자바 객체의 프로퍼티명"
        ```
        <result column="contents" property="content"/>
        ```
        - primary key 컬럼인 경우, id 태그를 사용한다.
        ```
        <id column="board_id" property="no"/> 
        ```

        위에서 정의한 연결 정보를 사용하려면, resultMap id와 select resultMap을 같게 하면 된다.
        ```
        <resultMap type="Board" id="BoardMap">
        <select id="selectBoard" resultMap="BoardMap">
        ```
        여기서 resultType을 resultMap으로 바꿔야 한다.


# 2021-04-02
 - XML Entity
    ```
    - &lt; : <
    - &gt; : >
    - &quot; : "
    - &apos; : '
    - &amp; : &
    ```

    ```
    select
    num, name, grade
    from board
    where num &lt; 5;
    ```
    
 - CDATA
    - CDATA 란, XML 파서(Parser)에게 해당 블록의 내용물이
      단순 텍스트임을 알려주는 명령어이다.
      ```
      <![CDATA[
        select
        num, name, grade
        from board
        where num > 3 and num < 10
      ]]>
      ```
      위 코드처럼, 해당 블록의 내용물이 텍스트로 변환되므로,
      `&lt;`와 같은 Entity를 명시할 필요가 없다.
      
      내용물 안에 XML 파서가 혼동을 일으킬 문자가 많을 때 주로 사용한다.

 - selectList() / selectOne() / insert() / update() / delete()
    - selectList(SQL ID) or selectList(SQL ID, Parameter)
      위와 같이, SQL을 실행할 때, 오직 한 개의 파라미터만 넘길 수 있다.
      여러 개의 파라미터를 넘기고 싶다면, 객체에 담아서 넘겨야 한다.

    - 특정 범위의 번호에 해당하는 값 가져 오기.
      ```
      HashMap<String,Object> params = new HashMap<>();
      params.put("startNum", 3);
      params.put("endNum", 5);
      List<Member> members = sqlSession.selectList("MemberMapper.selectMember", params);
      
      -- XML Part --
      <select id="selectMember" resultMap="MemberMap" parameterType="map">
      <![CDATA[
        select 
          num, 
          name,
          email,
          tel
        from board
        where board_id >= #{startNo} and board_id <= #{endNo}
      ]]>
      </select>
      ```
      - 실행 순서
        - 해쉬맵의 String타입의 key값에 startNum문자열을 넣고,
          Object타입의 value값에 지정한 번호를 넣는다.
          그리고 selectList의 SQL ID는 Mapper의 DML id를 넣고,
          Parameter는 선언한 HashMap 인스턴스를 넣으면 된다.
          그리고 XML select문으로 넘어와서 실행을 하고,
          조건문에 value값을 넣음으로써, 조건을 완성시킨다.
      
      - 한 맵에는 한 객체가 들어갈 수 있다.
        맵 = 레코드.
        key에는 컬럼명, value에는 컬럼의 값
        Map을 이용하여, 객체를 가져오려면 다음과 같이 선언하면 된다.
        Map<k, v> 변수명 = sqlSession.selectOne("Mapper명.id명");
        ```
        Map<String,Object> map = sqlSession.selectOne("MemberMapper.selectMember");
        ```
        하지만, 위 코드에 문제점이 있다.
        만일, 결과가 2개 이상 존재한다면 어떤 값을 꺼내와야 할 지 모른다.
        그래서 0 또는 1개의 객체만 존재할 때 사용하며,
        특정 객체를 꺼내려면, 파라미터를 추가로 명시해야 한다.
        ```
        Map<String,Object> map = sqlSession.selectOne("MemberMapper.selectMember", 3);
        
        -- XML Part --
        <select id="selectMember" resultType="map" parameterType="int">
          select 
            num,
            name, 
            email,
            tel,
            rdt
          from member
          where num=#{no}
        </select>
        ```
        parameterType을 조건문의 {}안의 값의 타입과 일치시켜야 한다.
        {} 안의 값은 no 즉, int형 타입이다.
        따라서, parameterType도 int로 선언해야 한다.
        추가로, wrapper 클래스를 사용해야 하지만, Mybatis가
        자동으로 오토박싱을 해주기 때문에 Integer가 아닌 int로
        명시해도 문제가 없다.
        
        selectOne의 두 번째 파라미터가 select문의 조건문에 들어가며,
        해당하는 번호가 있다면, 번호가 위치한 객체를 리턴한다.

      - 일반 객체를 사용하여 여러 값 넘기기
      ```
      <insert id="insert" parameterType="member">
      insert into member(name, email, tel, rdt)
      values(#{name}, #{email}, #{tel}, now())
      </insert>
      ```
      insert문을 실행할 때는 insert() 메소드를 호출한다.
      리턴 값은 executeUpdate()의 실행 결과이다.
      즉, insert 된 데이터의 개수이다.
      values의 {} 안의 값은 프로퍼티 즉, Member 클래스의 인스턴스명을 의미한다.
      따라서, 인스턴스명과 일치하지 않으면 작동하지 않는다.

 - AutoCommit
    - DML을 실행할 때, autocommit defaul값은 false이다.
      따라서, 데이터 변경 결과를 반영시키려면 반드시 commit을 해야 한다.
      만일 commit을 하지 않는다면, 임시테이블에 저장된 값이
      사라지게 되며, 데이터 변경 결과는 결국 적용이 되지 않는다.
      
    - autocommit을 true로 바꾸기 위해서는
      openSession()으로 처리하면 된다.
      ```
      openSession(true)
      ```
      true로 선언하면,
      commit을 할 필요없이, 데이터 변경 결과가 바로 적용된다.
  
 - 자동 증가 값 획득
    - userGeneratedKeys
      자동 증가한 PK 컬럼 값을 사용할 것인지 지정한다.
    - keyColumn
      자동 증가 PK 컬럼의 이름을 지정한다.
    - keyProperty
      자동 증가 PK 컬럼의 값을 저장할 자바 객체의 프로퍼티를 지정한다.
      ```
      <insert id='insert' parameterType="member"
            userGeneratedKeys="true" keyColumn="num" keyProperty="number">
      insert into member(name, email, tel, rdt)
      values(#{name}, #{email}, #{tel}, #{registeredDate})
      </insert>
      ```
      자동 증가한 PK 컬럼의 값을 사용하기 위해서는
      우선, userGeneratedKeys의 값을 "true"로 지정하고,
      keyColumn 즉, 컬럼명을 명시해야 한다.
      그리고 keyProperty는 객체의 인스턴스 필드명을 명시하면 된다.
      따라서, 컬럼명과 인스턴스 필드명이 다를 수도 있다.

    - 자동 증가된 PK 컬럼의 값을 자식 테이블의 FK 컬럼의 값에 적용하기
    ```
    Member member = new Member();
    member.setName("하나");
    member.setEmail("abc@abc.com");
    member.setTel("010-1234-1234");

    sqlSession.insert("MemberMapper.insert", member);

    HashMap<String,Object> fileInfo = new HashMap<>();
    fileInfo.put("filePath", "flower.jpg");
    fileInfo.put("memberNum", member.getNumber);
    sqlSession.insert("MemberMapper.insert2", fileInfo);
    ```
    자식 테이블의 FK 컬럼인 memberNum에
    부모 테이블의 자동 증가된 PK 컬럼값을 넣는다.
    

# 2021-04-03
 - 복습
    - 뮤텍스와 세마포어
        - 뮤텍스는 Critical Section(임계영역)에서 하나의 쓰레드만
          접근을 허용한다.
          다른 표현으로는 세마포어(1)이 될 수 있다.
          괄호안의 수는 접근할 수 있는 쓰레드의 개수를 의미한다.
          
          보통 카페에 가면, 음료를 주문한다.
          주문하는 동안은 한 명만 받으므로, 뮤텍스를 의미한다.
          하지만, 세마포어 방식을 적용하면 중간에 다른 사람이
          주문을 가로챌 수도 있게 된다.

          따라서, 상황에 따라 뮤텍스와 세마포어를 사용해야 한다.

    - stateful 과 stateless
        - stateful 방식은 클라이언트가 서버에 요청을 받고,
          서버가 받은 요청을 처리하고, 응답을 하고 끝나지 않는다.
          한 쪽이 특정 코드를 추가해서 종료시키지 않는 이상,
          계속 점유하게 된다.

        - 반면에, stateless 방식은 1:1로 이루어지며,
          요청과 응답을 각각 주고 받으면 종료된다.


# 2021-04-04
 - 복습
    - 추상클래스
      - 추상클래스는 생성자가 존재하지 않는다.
        따라서, 인스턴스 생성을 할 수가 없다.
        추가로, 추상 메소드를 선언할 수 있으며,
        추상 메소드는 메소드 몸체가 없다.
        그러므로, 추상 클래스를 상속 받은 자식 클래스에서
        오버라이딩하여 필수적으로 구현해야 한다.
        ```
        abstract class Super {
          abstract void m1();
          void m2() {
            System.out.println("Super.m2()");
          }
        }
        
        class Sub extends Super {
          @Override
          void m1() {
            System.out.println("m1() Overriding");
          }
        }
        ```
      
      - 추상클래스의 일반 메소드는 자식 클래스에서
        오버라이딩하여 사용하는 건 선택이다.
        반면에, 추상 메소드는 구현하지 않으면 오류가 발생한다.

    - 인터페이스
      - 인터페이스는 메소드 몸체가 없으며,
        메소드만 선언할 수 있다.
        기본적으로 메소드를 선언하면, 추상메소드가 되면서
        인터페이스를 구현하는 클래스는 반드시 오버라이딩하여
        선언해야 한다.
        
      ```
      interface Inter {
        void m1();
        void m2();
      }

      class Sub implements Inter {
        @Override
        void m1() {
          System.out.println("m1() Overriding");
        }
        @Override
        void m2() {
          System.out.println("m2() Overriding");
        }
      }
      ```
      
    - SQL문
      - root에서 user와 DB 생성 및 세팅
      ```
      create user 'user'@'localhost' identified by '1234';
      
      create database userdb default character set utf8
      collate utf8_general_ci;

      grant all on userdb.* to 'user'@'localhost';
      ```
      
      - DML 사용
      ```
      create table test(
        no int not null,
        name varchar(30) not null,
        email varchar(30) not null,
        tel varchar(15) default '010-0000-0000'
      );
      
      alter table test
      add constraint test_PK primary key(no);
      modify column no int auto_increment;

      insert into test(name, email, tel)
      values('first', 'abc@abc.com', '010-1234-1234');

      update test set name='changed', email='done' where no=1;

      select * from test;

      delete from test where no=1;

      alter table test2
      add constraint test2_FK foreign key(no) references test(no);
      ```


# 2021-04-05
 - Mybatis
    - ${} 와 #{} 의 차이
      - #{} 문법은 SQL에 값을 삽입할 때 사용한다.
        사용자가 입력한 값(문자열)에 SQL 코드를 포함하더라도,
        단순히 값으로 취급하기 때문에 실행에는 영향을 끼치지 않는다.
        즉, SQL 삽입 공격이 불가능하다.
      
      - ${} 문법은 SQL에 코드를 삽입할 때 사용한다.
        사용하기에 편리성이 좋지만, 그만큼 위험성도 있다.
        SQL 삽입 공격이 가능한 문법이라 주의해서 사용해야 한다.
      
    - SQL 태그 사용
      - 여러 SQL문에 공통으로 포함되는 코드가 있다면,
        다음과 같이 별도의 <sql> 태그에 작성해두고, 사용하면 편리하다.
        <include> 태그와 같이 쓰인다.
      ```
      sql 선언
      <sql id = 'sql'>
        select
          no, name, email, tel, addr
        from member
      </sql>

      include 사용

      <select id = "selectMember" resultMap = "MemberMap" parameterType="int">
        <include refid = "sql">
        where no = #{no}
      </select>
      ```

    - if 태그 사용
      - if 태그를 사용하면, 값의 유무에 따라, SQL문을 제어할 수 있다.
        Dynamic SQL을 사용하면, 여러 개의 SQL문을 만들 필요가 없다.
        if 태그는 전체 컬럼이 아니라, 일부 항목만 변경하는 SQL을 만들 수 있다.
      ```
      where
      <if test="no==1">
        name = #{name}
      </if>
      <if test="no==2">
        email = #{email}
      </if>
      ```
      위 코드의 단점은 두 if문 둘 다 만족하는 조건이 없다면,
      오류가 발생한다는 것이다.
      그래서 where 태그를 사용하여, 오류를 방지할 수 있다.

      ```
      <where>
      <if test="no==1">
        name = #{name}
      </if>
      <if test="no==2">
        email = #{email}
      </if>
      </where>
      ```
      if문 둘 다 만족하는 조건이 없으면, where태그가 사라진다.
      where 태그를 사용함으로써, 오류를 피할 수 있다.
      

    - 검색
      - 컬럼명 like concat('%', #{컬럼명}, '%')
      ```
      name like concat('%', #{name}, '%')
      ```
      
      - foreach를 이용한 다중 검색
      ```
      <foreach collection="" item="">
        or title like concat('%', #{}, '%')
      </foreach>
      ```
  
    - set 태그
      - set 태그는 문장 앞뒤에 쓸데없이 붙어 있는 콤마 를 제거한다.
        보통 수정할 컬럼값이 여러 개면, 뒤에 콤마가 붙기 마련인데
        그 부분을 해결해준다.
        하지만, 그렇다고 콤마를 생략하고 수정할 컬럼값을 바로 이어서는 안 된다.
        그러면 오류가 발생한다.
        ```
        update member set
          <set>
            <if test="name != null">
              name=#{name},
            </if>
            <if test="email != null">
              email=#{email}
            </if>
          </set>
          where no=#{no}
        ```
        

    - trim 태그
      - 여러 조건을 검사할 때 첫 검사를 생략하면,
        그 다음 검사를 할 때 and가 where 앞에 오게 되는데
        그러면 문법적으로 오류가 발생하게 된다.
        따라서, 이 문제를 해결하기 위해서는
        trim 태그와 where 태그 둘 중에 하나를 선택해서
        사용해야 한다.
      ```
      <trim prefix="where" prefixOverrides="or|and">
        <if test="name != null">
          name like concat('%', #{name}, '%')
        </if>
        <if>
          email like concat('%', #{email}, '%')
        </if>
      </trim>
      ```
      trim의 prefix는 where를 가리켜서,
      기존에 있던 where를 생략해도 된다.
      그리고 prefixOverrides의 or과 and는
      첫 조건을 생략했을 때, where 앞에 오는 and 나 or를
      제거하기 위함이다.
      하지만, trim 태그 보다는 where 태그를 더 많이 사용된다.

    - bind 태그
      - 동적 쿼리 변수를 생성할 때 사용한다.
      ```
      <bind name="pattern" value="'%' + _parameter + '%'"/>
        select
          name, email, tel, addr
        from member
        where tel like #{pattern}
      ```
      bind의 name은 변수명이고, value는 파라미터값과 추가적으로 덧붙이는 옵션이다.
      _parameter는 parameterType 으로 넘어 오는 값을 가리키는 변수명이다.
      그래서 저 쿼리는 이렇게 다시 짤 수 있다.
       ```
        select
          name, email, tel, addr
        from member
        where tel like concat('%', #{tel}, '%')
      ```
      

    - choose/when/otherwise 태그
      - choose/when/otherwise 태그는 if 태그와 비슷한 형태이다.
        자바 문법으로 예로 들자면, if와 switch문의 차이라고 보면 된다.
        따라서, 여러 조건이 있을 경우, choose를 사용하는 것이 더 편리할 수 있다.
      
      ```
      select
        name, email, tel, addr
      from member
      where
      <choose>
        <when test="no==1">
          name like concat('%', #{name}, '%')
        </when>
        <when test="no==2">
          email like concat('%', #{email}, '%')
        </when>
        <when test="no==3">
          tel like concat('%', #{tel}, '%')
        </when>
        <otherwise>
        1 = 0
        </otherwise>
      </choose>
      ```
      위 쿼리에서 choose는 switch()와 같다고 보면 된다.
      그리고 나머지 when 태그는 case문과 비슷한 형태이다.
      마지막으로 otherwise 태그는 default와 같은 의미다.
      따라서, 여러 케이스들의 조건이 다 맞지 않으면,
      마지막 otherwise가 반드시 처리한다.


    - foreach 태그
      - foreach 태그는 SQL 코드를 반복해서 사용할 때 쓰인다.
        예를 들면, SQL문을 반복해서 실행을 요청한다면,
        네트워크 사용량이 증가한다.
        그래서 단 한 번에 처리하기 위해 foreach 태그를 사용한다.
      ```
      <foreach collection="list" item="name">
        or name like concat('%', #{name}, '%')
      </foreach>
      ```
      collection은 받아 온 객체의 리스트명을 의미하고,
      item은 별명이다.

      - insert문에서의 사용
        - 여러 insert문을 작성하여 쿼리를 보내는 건 비효율적이다.
          따라서, foreach를 이용하여, 입력한 데이터들을 한 번에 처리할 수 있다.
        
        ```
        <insert id="insert" parameterType="map">
          insert into member(name, email)
          values 
          <foreach collection="files" item="attachFile" separator="," >
          (#{attachFile.filePath}, #{attachFile.boardNo})
          </foreach>
        </insert>
        ```

    - where의 조건
      - 기본 디폴트 조건을 거짓으로 할 수 있게 만들기 위해,
        where 뒤에 1=0 or 1!=0 와 같은 조건을 추가하면 된다.
        다만, 다른 조건들이 or를 사용할 때 의미가 있다.
        만일, and로 한다면, 무조건 하나라도 참이 아닐 시에는
        거짓이기 때문에 조건을 주는 의미가 사라지게 된다.
        그리고 나머지는 다 거짓이고, 단 하나의 if문만
        참이라면, 그 문장을 반드시 실행하고 끝나게 된다.
      

# 2021-04-06
 - Mybatis
    - join
      - join을 이용하여, 두 가지 종류 이상의 객체를 출력할 수 있다.
      ```
      select 
        b.board_id, 
        b.title,    
        b.contents,
        b.created_date, 
        b.view_count,
        bf.board_file_id,
        bf.file_path  
      from x_board b
        left outer join x_board_file bf on b.board_id=bf.board_id
        where b.board_id={no}
      ```
      위 SQL문은 outer join을 이용하여, 출력한다.
      left는 from 테이블명 기준으로 한다.
      right는 outer 다음에 오는 테이블명 기준으로 왼쪽으로 간다.
      따라서, 부모테이블하고 자식테이블을 join해서 값을 출력하려면,
      부모테이블이 from에 선언되야 한다.

      - if 태그를 넣어서 사용할 수도 있다.
      ```
      <if test="no != null">
        where b.board_id={no}
      </if>
      ```
      즉, no가 null이 아니라면, where절을 실행하겠다는 의미다. 
    
    - 파라미터 타입
      - primitive 타입, Wrapper 클래스, String 클래스는
        SQL문에 아무 이름을 지어도 상관없다.
        왜냐하면, 단 한 개만 넘어오기 때문에 구분 짓지 않는다.
        
        반면에, map이 파라미터 타입으로 넘어 올 경우,
        key에 넣은 값을 그대로 명시해야 한다.


# 2021-04-07
 - Mybatis
    - 포함관계에서 하나만 사용할 때는 association을 사용
      ```
      <association property="인스턴스명" javaType="클래스명">
      <id column"프로퍼티명_컬럼명" property="인스턴스필드명"/> <- PK일 경우에 id 사용.
      <result column="프로퍼티명_컬럼명" property="인스턴스필드명"/>
      ```
    - 반면에, 리스트처럼 여러 개를 사용할 때는 collection을 사용
      ```
      <collection property="인스턴스명" ofType="클래스명">
      <id column"클래스명_컬럼명" property="인스턴스필드명"/>
      <result column="클래스명_컬럼명" property="인스턴스필드명"/>
      ```
    
    - collection일 경우에는 ofType을 사용하며, association의 경우에는 javaType을 사용한다.
    
    - Select문 Collection 사용
      - 여러 데이터를 한꺼번에 조회할 때 사용한다.
    ```
    <association property="owner" javaType="member">
	    <id column="owner_no" property="no"/>
	    <result column="owner_name" property="name"/>
    </association>
    
    <collection property="members" ofType="member">
      <id column="member_no" property="no"/>
      <result column="member_name" property="name"/>
    </collection>

    select 
      p.no, p.title, p.sdt, p.edt, m.no owner_no, m.name owner_name, m2.no member_no, m2.name member_name 
    from pms_project p
      inner join pms_member m on p.owner=m.no
      left outer join pms_member_project mp on mp.project_no=p.no
      left outer join pms_member m2 on mp.member_no=m2.no
    ```
    
    - 첫 번째 조인에서 멤버 번호와 프로젝트 오너의 번호가 같으면 조인.
    - 두 번째 조인에서 멤버프로젝트의 프로젝트 번호와 프로젝트 번호가 같으면 조인.
    - 세 번째 조인에서 멤버 번호와 멤버프로젝트의 멤버 번호가 같으면 조인.

    - outer 조인을 하는 이유는 멤버가 없을 수도 있으니,
      null을 포함한 테이블도 포함시켜야 한다.
      만일, inner 조인으로 하면 아예 처리를 하지 않는다.

    - collection에서 다른 컬럼들이 null이라도, 다른 하나의 컬럼을
      조회했을 때 같은 값이 존재한다면, 객체를 생성한다.
      따라서, 개수가 추가되므로, size()로 로직을 짜면 수행을 하지 않는다.
      그래서 굳이 필요없는 컬럼은 추가하지 않고 사용해야 한다.

    - foreach에서 separater 역할은 반복 횟수가 2회 이상이면 추가된다.
      만일, 2회 미만이면 필요없다.


# 2021-04-08
 - 실습 프로젝트
    - 자동 commit 과 수동 commit
      - 여러 개의 작업을 한 단위로 묶어 수행하려면,
        수동 commit으로 설정해야 한다.

      - 자동 commit의 문제점
        - 예외가 발생하더라도, 이전에 수행한 클래스의 정보가 온전히
          입력되지 않은 상황이 발생할 수 있다.

      - 객체의 정보를 입력하면, sqlSession에 넘어가고,
        sqlSession은 connection 객체를 호출하고, 쓰레드가 지정되면서
        임시 DB가 생성된다.
        임시 DB에는 commit이나 rollback 을 실행하기 전까지는
        같은 sqlSession에서 입력한 정보들은 같은 임시 DB에 저장된다.
        따라서, 커밋을 실행할 경우 모든 데이터 변경 결과가 반영된다.

      - 한 메소드에 두 개 이상의 DML이 들어갈 때는
        예외 처리를 해야 한다. 따라서, rollback을 사용해야 할 때가 있다.
        반면에, 단 하나의 DML만 있다면 예외처리를 할 필요가 없다.
    
    - 비지니스 로직으로 분리하기 전 DAO 역할의 문제점
      - 각각의 DAO 메소드에서 트랜잭션을 제어하기 때문에
        여러DATO를 통해, 데이터 변경을 수행할 경우,
        한 틀내잭션으로 묶어 제어할 수 없다.
      
      - 각 메소드가 트랜잭션을 제어할 경우,
        데이터 변경을 위해 다른 메소드를 호출할 떄
        한 트랜잭션으로 묶을 수 없다.
      
      - 이미 commit을 호출하면, rollback을 호출해도,
        데이터 변경 결과가 적용되었기 때문에 실행이 안 된다.
    
    - 비지니스 로직으로 분리하여 DAO 역할의 문제점 해결
      - 업무에 따라, DAO의 데이터 변경 작업을 묶어서 한 트랜잭션으로 제어한다.


# 2021-04-09
 - 테스트 코드
    - 테스트 주도 개발 (TDD)
        - Red : 실패하는 테스트를 먼저 작성.
        - green : 테스트가 통과하는 프로덕션 코드 작성.
        - Refactor : 테스트가 통과하면, 프로덕션 코드를 리팩토링.

        - 단위 테스트
            - 단위 테스트는 Red 단계를 의미.
            - 단위 테스트 코드 작성의 이점
                - 개발단계 초기에 문제 발견에 도움된다.
                - 개발 후, 코드를 리팩토링을 하거나, 라이브러리
                  업그레이드 등 기존 기능이 올바르게 작동하는지 확인한다.
                  Ex) 회귀 테스트
                - 기능에 대한 불확실성을 감소시킨다.
                - 단이 테스트 자체를 문서로 사용이 가능하다.

 - 용어 이해
    - @SpringBootApplication
        - 스프링 부트의 자동 설정 및 스프링 Bean 읽기와 생성 모두 자동 설정.
          Bean이란? 애플리케이션의 핵심을 이루는 객체이며,
          Spring IoC 컨테이너에 의해 인스턴스화, 관리 및 생성이 된다.
        
        - @SpringBootApplication이 있는 클래스는 애노테이션이 있는 위치부터
          설정을 읽어 내려간다. 따라서, 이 애노케이션을 포함한 클래스는
          프로젝트의 최상단에 위치해야 한다.
    
    - SpringApplication.run
        - 내장 WAS(Web Application Server)를 실행한다.
          내장 WAS는 별도의 외부에 WAS를 두지 않고, 앱을 실행할 때
          내부에서 WAS를 실행하는 것을 의미한다.
          따라서, 따로 서버를 구축하지 않고 서버 사용이 가능하다.

          추가로, 내장 WAS 사용을 권장하는데 그 이유는
          언제 어디서든 같은 환경에서 스프링 부트 배포가 가능하기 때문이다.

    - @RestController
        - 기존의 @Controller와 @ResponseBody의 축약형.
        - 컨트롤러를 JSON을 반환하는 컨트롤러로 변환시킨다.
        - @ResponseBody를 각 메소드에 선언했던 걸, 통합시켜서
          한 번에 사용할 수 있는 장점이 있다.
    
    - @GetMapping
        - 기존의 @RequestMapping의 축약형.
        - HTTP Method인 Get의 요청을 받을 수 있는 API.

    - @ExtendWith
        - 테스트를 진행할 때, JUnit에 내장된 실행자 외에
          다른 실행자를 실행시킨다.
          스프링 부트 테스트와 JUnit 사이에 연결자 역할을 한다.
        
        - JUnit4 버전 기준으로 SpringRunner 스프링 실행자 사용.
        - JUnit5 버전 기준으로 SpringExtension 스프링 실행자 사용.

    - @WebMvcTest
        - 여러 스프링 테스트 애노테이션 중에서 Web(Spring MVC)에
          집중할 수 있는 애노테이션.
        
        - @Service, @Component, @Repository 등은 사용 불가.
    
    - @AutoWired
        - 스프링이 관리하는 빈(Bean)을 주입 받는 애노테이션.

 - JUnit 4 -> 5 바뀐 점.
    - @RunWith -> @ExtendWith
    - org.junit.runner.RunWith
    -> org.junit.jupiter.api.extension.ExtendWith

    - @RunWith(SpringRunner) -> @ExtendWith(SpringExtension)
    - org.springframework.test.context.junit4.SpringRunner
    -> org.springframework.test.context.junit.jupiter.SpringExtension

    - @After -> @AfterEach
    - org.junit.After
    -> org.junit.jupiter.api.AfterEach

    - @Before -> @BeforeEach
    - org.junit.Before
    -> org.junit.jupiter.api.BeforeEach


 - 오류 코드와 해결 방법
    - No tests found for given includes 오류
        - build.gradle 파일 내부에
          test {
              useJUnitPlatform()
          }
          이 생략되었는지 확인.

 - private MockMvc mvc
    - 웹 API를 테스트할 때 사용
    - 스프링 MVC 테스트의 시작점
    - 이 클래스를 통해 HTTP GET, POST 등에 대한 API 테스트 가능

 - MVC.PERFORM(GET("/hello"))
    - MockMvc를 통해 /hello 주소로 HTTP GET 요청
    - 체이닝이 지원됨으로서, 여러 검증 기능을 이어서 선언 가능
      체이닝이란? 메소드가 객체를 반환하면 반환 값을 이어서 호출하는 것이다.
      Ex) 인스턴스명.setName("name").setAge(10).setGrade('A') 이런 식으로 이어서 호출하는 것이다.

 - .andExpect(status().isOk())
    - mvc.perform의 결과를 검증
    - HTTP Header의 Status(200, 404, 500 등)의 검증

 - .andExpect(content().string(hello))
    - mvc.perform의 결과를 검증
    - 응답 본문의 내용을 검증
    - Controller에서 "hello"를 리턴하기 때문에 이 값이 맞는지 검증


# 2021-04-10
 - 롬복(Lombok)
    - Getter, Setter, 기본 생성자, toString 등
      애노테이션으로 자동 생성해준다.
    
    - 롬복은 프로젝트마다 설정을 해야 한다.
      플러그인은 단 한 번만 설치하면 되지만,
      새 프로젝트를 만든다면, build.gradle에 라이브러리를
      추가하고, Enable annotation processing 체크도 해야 한다.
    
 - 용어
    - @Getter
      - 선언된 모든 필드의 get 메소드를 생성해준다.
      ```
      public class Test {
        String name;
        String addr;

        public void getName() {
          return name;
        }
        public void getAddr() {
          return addr;
        }
      }
      ```
    
    - @RequiredArgsConstructor
      - 선언된 모든 final 필드가 포함된 생성자를 생성해준다.
        하지만, final이 없는 필드는 생성자에 포함시키지 않는다.
    
    - assertThat
      - assertj의 테스트 검증 라이브러리의 검증 메소드
      - 검증하고 싶은 대상을 메소드 인자로 받는다.
      ```
      public void test() {
        String name = "test";
        int age = 20;

        ResponseDto dto = new ResponseDto(name, amount);
        
        @RequiredArgsConstructor로 final 필드인 name과 age 필드를 포함한 
        생성자를 자동 생성한다.
        따라서 name에 test 문자열이 대입되고, age에는 20이 치환된다.
        그리고
        assertThat메소드와 isEqualTo메소드를 사용하여 비교한다.
        
        assertThat(dto.getName()).isEqualTo(name);
        assertThat(dto.getAge()).isEqualTo(age);

        여기서 주의할 점은, 메소드 체이닝으로 이루어지기 때문에,
        assertThat의 괄호가 끝나고, 이어서 선언해야 한다.
        코드의 내용은 dto.getName() 즉, 생성자를 생성할 때 넣어지는
        값인 test 문자열을 isEqualTo(name) 과 비교하여 true false를
        출력한다.
        현재 test() 메소드의 name은 test가 맞기 때문에, 결과는 true다.
      }
      ```

    - @RequestParam
      - 외부에서 API로 넘긴 파라미터를 가져오는 애노테이션.

      - 외부에서 NAME이란 이름으로 넘긴 파라미터를 메소드 파라미터
        name에 저장한다.
        ```
        @RequestParam("name") -> name(String name)
        ```

    - param
      - API 테스트할 때, 사용될 요청 파라미터를 설정한다.
      - 단, 값은 String만 허용한다.
        따라서, String 외의 타입은 타입캐스팅을 해야 한다.
        Ex) valueOf() 사용


# 2021-04-11
  - JPA(Java Persistence API)
    - 프로그래밍을 관계형 DB에 맞게 SQL을 대신 생성해준다.
      따라서, 개발자는 SQL에 종속적인 개발을 하지 않아도 된다.
    
    - Spring Data JPA
      - JPA는 인터페이스이며, 자바 표준 명세서이다.
        따라서, JPA를 사용하려면 구현체가 필요하며,
        대표적인 구현체가 Hibernater 다.
    
      - Spring Data JPA 모듈을 이용하여 JPA를 다룬다.
        관계식 : Spring Data JPA -> Hibernate -> JPA
        Hibernate와 Spring Data JPA의 사용에 있어,
        큰 차이는 없다.
      
      - Wrapping을 사용하는 이유
        - 구현체 교체의 용이성
          - 구현체 교체의 용이성은 Hibernate외의
            다른 구현체로 쉽게 교체가 가능하다.
            그래서 Spring Data JPA를 사용중이면,
            내부에서 구현체 매핑을 지원해주기 때문에
            다른 구현체로 바꾸더라도, 큰 어려움이 없다.
          
        - 저장소 교체의 용이성
          - 저장소 교체의 용이성은 관계형 DB 외의 다른 저장소로
            쉽게 교체가 가능하다. 예를 들면, 대규모 트래픽을
            다룰 때, MongoDB와 같은 NoSQL로 교체가 필요할 때,
            Spring Data mongoDB로 의존성만 교체하면 된다.
    
    - spring-boot-starter-data-jpa
      - 스프링 부트용 Spring Data JPA 추상화 라이브러리.
      - 스프링 부트 버전에 맞춰, 자동으로 JPA 관련 라이브러리들의
        버전을 관리한다.
    
    - h2
      - 인모메리 관계형 DB
        - 디스크가 아닌, 메인 메모리에 모든 데이터를 보유한다.
      
      - 인메모리 DB의 장점은 디스크 검색보다 자료 접근
        즉, 조회가 훨씬 빠르다.
        데이터 증가로 DB 응답 속도가 떨어지는 문제를 개선하였다.
        
        단점은 휘발성이다.
        그래서 DB 서버 전원이 나가면, 메모리 안에 있던
        데이터들은 그 즉시 삭제된다.
        그래서 이러한 단점을 보완하기 위해,
        임시 데이터를 메모리에 담아서 쓰고,
        Durability를 보장하기 위해서 DML로 실행된 값들은
        디스크 로그에 기록한다.
      
      - 별도의 설치가 필요 없기 때문에 프로젝트 의존성만으로
        관리할 수 있다.
      
      - 메모리에서 실행되기 때문에 앱을 재시작할 때마다 초기화된다.
        따라서, 이 점을 이용하여 테스트 용도로 많이 사용한다.

    - @Entity
      - JPA의 애노테이션이다.
      - 테이블과 링크될 클래스임을 나타내며,
        기본값으로 클래스의 카멜케이스 이름을 언더스코어 네이밍 _ 으로
        테이블 이름을 매칭한다.
        Ex) SalesManager.java -> sales_manager table
    
    - @Id
      - 해당 테이블의 PK 필드
    
    - @GeneratedValue
      - PK의 생성 규칙
      - GenerationType.IDENTITY 옵션을 추가해야
        auto_increment 가 적용된다.
    
    - @Column
      - 테이블 컬럼을 나타내며, 굳이 선언하지 않아도
        해당 클래스의 나머지 필드는 컬럼으로 적용된다.
      
      - 사용 이유는 기본값 외에 추가로 변경이 필요한 옵션이
        있을 때 사용한다.
      
      - 문자열은 varchar(255)가 default값이다.
        그래서 사이즈를 늘리려면, 그 이상의 수를 명시하거나,
        TEXT 타입으로 바꾸면 된다.
  
  - @Getter 와 @NoArgsConstructor는 Lombok의 애노테이션이다.
    롬복은 코드를 단순화시켜주지만, 필수 애노테이션은 아니다.

  - @NoArgsConstructor
    - 디폴트 생성자 자동 추가
    ```
    public class A {
      
      public A() {}
    }
    ```
    와 같다고 보면 된다.

  - @Getter
    - 클래스 내 모든 필드의 Getter 메소드를 자동 생성
  
  - @Builder
    - 해당 클래스의 빌더 패턴 클래스 생성
    - 생성자 상단에 선언 시, 생성자에 포함된 빌드만 빌더에 포함
    ```
    @Builder
    public Member(String name, int age, String addr) {
      this.name = name;
      this.age = age;
      this.addr = addr;
    }
    ```
  
  - Entity 클래스에는 절대 Setter 메소드를 사용하지 않는다.
    @Builder를 통해 제공되는 빌더 클래스를 사용하여 값을 넣는다.
    그 이유는 해당 클래스의 인스턴스 값들이 언제 어디서
    변해야 하는지 코드상으로 명확하게 구분할 수 없기 때문이다.
    따라서, 후에 기능 변경시 로직이 복잡해진다.
    Test.builder()
    .name(name)
    .age(age)
    .addr(addr)
    .build();

  - Repository 클래스는 인터페이스이며, DAO 라고 보면 된다.
    JpaRepository를 상속 받으며, 타입 파라미터에는
    <Entity 클래스명, PK 타입> 을 명시하면 된다.
    그러면 기본적인 CRUD 메소드가 자동으로 생성된다.

    추가로, Entity 클래스와 Entity Repository는
    함께 위치해야 한다.
    나중에 규모가 커져서, 도메인별로 프로젝트를
    분리해야 할 때는 같이 도메인 패키지에서 관리한다.

  - Given - When - Then
    - Give = 준비
    - When = 실행
    - Then = 검증
  
  - JPA를 사용하면 DB 데이터에 작업할 경우,
    쿼리를 날리기 보다는 Entity 클래스의 수정을 통해
    작업한다.
  
  - @After
    - JUnit에서 단위 테스트가 끝날 때마다,
      수행되는 메소드를 지정한다.
    
    - 배포 전, 전체 테스트를 수행할 때
      테스트간 데이터 침범을 막기 위해 사용한다.
    
    - 여러 테스트가 동시에 수행되면, 테스트용 DB인
      H2에 데이터가 그대로 남아 있어, 다음 테스트 실행 시,
      테스트가 실패할 수 있다.
      따라서, deleteAll() 메소드는 단위 테스트가 끝날 때마다
      수행된다.
  
  - Repository.save
    - 테이블에 insert/update 쿼리를 실행
    - id값이 있으면, update 실행
    - id값이 없으면, insert 실행
  
  - Repository.findAll
    - 테이블에 있는 모든 데이터를 조회해오는 메소드

  - 등록/수정/조회 API 제작
    - Request 데이터를 받을 DTO
    - API 요청을 받을 Controller
    - 트랜잭션, 도메인 기능 간의 순서를 보장하는 Service
  
  - Web Layer
    - 컨트롤러(@Controller)와 JSP/FreeMarker 등의 뷰 템플릿 영역
    - 필터(@Filter), 인터셉터, 컨트롤러 어드바이스(@ControllerAdvice)
      등 외부 요청과 응답에 대한 전반적인 영역을 의미
  
  - Service Layer
    - @Service에 사용되는 서비스 영역
    - 일반적으로 Controller와 Dao의 중간 영역에서 사용됨.
    - @transactional이 사용되어야 하는 영역
  
  - Repository Layer
    - DB와 같이 데이터 저장소에 접근하는 영역
  
  - Dtos
    - Dto(Data Transfer Object)는 계층 간의 데이터 교환을 위한 객체
  
  - Domain Model
    - 도메인이라 불리는 개발 대상을 모든 사람이
      동일한 관점에서 이해할 수 있고, 공유할 수 있도록
      단순화 시킨 것.
      Ex) 버스 앱이라고 가정하면, 배차/탑승/요금 등이 도메인이 될 수 있다.
    
  - Bean을 주입받는 방식 3가지
    - @Autowired
    - Setter
    - 생성자

    - 가장 권장하는 방법은 생성자로 주입 받는 방식
      생성자로 Bean 객체를 받도록 하면, @Autowired와
      동일한 효과를 볼 수 있다.
      그 생성자는 @RequiredArgsConstructor에서 해결한다.
      final이 선언된 모든 인스턴스 필드를 인자값으로
      하는 생성자를 롬복의 @RequiredArgsConstructor 가 대신 생성한다.

      롬복 애노테이션을 사용하는 이유는 간단하다.
      만일, 클래스의 의존성 관계가 변경된다면, 생성자 코드를
      수정해야 한다. 이런 번거로움을 해결하기 위해 롬복을 사용한다.
  
  - Entity 클래스를 Request/Response 클래스로 사용하면 안 된다.
    Entity 클래스는 DB와 맞닿는 핵심 클래스이기 때문이다.
    그래서 Entity 클래스 기준으로 테이블이 생성되고,
    스키마가 변경된다.
    수많은 서비스 클래스나 비지니스 로직들이 Entity클래스를
    기준으로 동작한다.
  
  - View Layer와 DB Layer의 역할 분리는 철저하게 하는 게 좋다.

  - HelloController와 다르게
    @WebMvcTest를 사용하지 않는다.
    그 이유는 @WebMvcTest는 JPA 기능이 작동하지 않기 때문이다.
    즉, DB에 관련된 일을 처리하지 않기 때문에 사용하지 않는다.
  
  - update 기능에서 DB에 쿼리를 보내는 SQL문이 없다.
    JPA의 영속성 컨텍스트 때문이다.
    영속성 컨텍스트는 엔티티를 영구 저장하는 환경이다.
    즉, 논리적 개념이며 JPA의 핵심은 엔티티가
    영속성 컨텍스트를 포함되냐 안 되냐에 갈린다.

  - Dirty Checking
    - 상태 변경 검사를 의미한다.
      JPA는 트랜잭션이 끝나는 시점에
      변화가 있는 모든 엔티티 객체를 DB에 자동으로 반영한다.
      그리고 변화의 기준은 최초 조회 상태이다.
      JPA는 엔티티를 조회할 때, 해당 에닡티의
      조회 상태 그대로 스냅샷을 만든다.
      그리고 트랜잭션이 끝나는 시점에 이 스냅샷과 비교하여,
      다른 점이 있으면 Update Query를 DB에 전달한다.
      물론, 이 Dirty Checking 대상은 영속성 컨텍스트가
      관리하는 엔티티에만 적용된다.
  
  - BaseTimeEntity 클래스는 모든 Entity의 상위 클래스가 되어,
    Entity들의 createdDate, modifiedDate를 자동으로 관리하는 역할이다.

  - @MappedSuperclass
    - JPA Entity 클래스들이 BaseTimeEntity를 상속할 경우,
      필드들(createdDate, modifiedDate)도 칼럼으로 인식하도록 한다.
  
  - @EntityListeners(AuditingEntityListener.class)
    - BaseTimeEntity 클래스에 Auditing 기능을 포함시킨다.
    - Auditing은 시간에 대해 자동으로 값을 넣어주는 기능이다.
      도메인을 영속성 컨텍스트에 저장하거나, 조회를 수행한 후에
      update를 하는 경우, 시간 데이터를 입력해야 하는데
      auditing이 그 역할을 대신 해준다.
  
  - @CreatedDate
    - Entity가 생성되어 저장될 때 시간이 자동 저장된다.
  
  - @LastModifiedDate
    - 조회한 Entity의 값을 변경할 때, 시간이 자동 저장된다.
  

# 2021-04-13
  - Reflection
    - 클래스 로딩
      - 클래스 로딩은 단 한 번만 한다.
      - 스태틱 멤버(변수, 메소드), 인스턴스 생성, Class.forName()을 이용할 때 로딩한다.

      - Class.forName()으로 로딩할 경우, fully qualified class name으로 해야 한다.
      ```
      Class.forName("com.yoon.pms.Test"); <- 가능
      Class.forName("Test"); <- 불가능
      ```

      - 레퍼런스 선언은 클래스 로딩과 다른 부분이다.
        그래서 레퍼런스와 레퍼런스 배열을 선언할 때는 클래스 로딩이 되지 않는다.
      
      - 스태틱 중첩 클래스를 로딩할 경우,
        바깥 클래스는 로딩하지 않는다.
      
      - 자바 코드로 중첩 클래스를 표현할 때는
        클래스 이름에 $가 아닌 . 을 붙인다.
        ```
        com.yoon.pms.Test$A = X
        com.yoonpms.Test.A = O
        ```
    
    - 배열 타입 알아내기
      - 배열타입 = `타입[].class.getComponentType().getName();`
      ```
      byte[].class.getComponentType().getName();
      char[].class.getComponentType().getName();
      String[].class.getComponentType().getName();
      ```
    
    - 메소드 정보 추출하기
      - Class<?> clazz = 클래스명.class;
      ```
      Method[] list = clazz.getMethods();
      for(Method m : list) {
        System.out.println(m.getName());
      }
      ```
    
    - 접근제어자 관계없이 모두 추출하기
      - `getDeclaredMethods()` 메소드 사용

    - 생성자 정보 가져오기
      - Class<?> clazz = 클래스명.class;

      - 단일
      ```
      Constructor<?> c = clazz.getConstructor();
      System.out.printf("생성자명 : %s 파라미터 수 : %d\n",
                        c.getName, c.getParameterCount());
      ```

      - 리스트
      ```
      Constructor<?>[] list = clazz.getConstructor();
      for(Constructor<?> c : list) {
        System.out.printf("생성자명 : %s 파라미터 수 : %d\n",
                          c.getName, c.getParameterCount());
      }
      ```
    
    - 파라미터 정보 추출
    Class<?> clazz = 클래스명.class;
    ```
    Method[] methods = clazz.getDeclaredMethods();
    for(Method m : methods) {
      Parameter[] parameters = m.getParameters();
        for(Parameter p : parameters) {
          System.out.printf("%s : %s\n", p.getName, p.getType().getName());
        }
    }


# 2021-04-14
  - 스프링
    - 템플릿 엔진
      - 지정된 템플릿 양식과 데이터가 합쳐져 HTML 문서를 출력하는 S/W
    
    - 서버 템플릿 엔진
      - 서버에서 Java 코드로 문자열을 만든 후,
        HTML로 변환하여 브라우저에 전달.
    
    - 클라이언트 템플릿 엔진
      - HTML 형태로 코드를 작상하며, DOM(문서 객체 모델)을
        그리게 해주는 역할.
    
    - 프론트엔드 라이브러리 사용 방식
      - 외부 CDN(Content Delivery Network) 사용
      - 직접 라이브러리 다운
    
    - 레이아웃 방식
      - 공통 영역을 별도의 파일로 분리하여, 필요한 곳에서
        가져다 쓰는 방식
    
    - 머스테치(Mustache)
      - `{{> }}` 
        현재 머스테치 파일을 기준으로 다른 파일 로딩
      
      - `{{클래스명.인스턴스명}}`
        클래스의 인스턴스에 대한 접근을 할 수 있다.
        Ex) `{{Test.name}}` = Test클래스의 name 인스턴스 필드에 접근
      
      - readonly = 오직, 읽기만 허용

    - JS
      - `window.location.href='/'`
        글 등록이 성공하면, 메인페이지(/)로 이동

      - `$('#btn-update').on('click')`
        btn-update란 id를 가진 HTML 엘리먼트에 click 이벤트가
        발생했을 때, update function을 실행하도록 이벤트 등록

      - `type: 'PUT'`
        여러 HTTP Method 중 PUT 메소드를 선택한다.
        PostsAPIController에 있는 API에서 이미
        `@PutMapping` 으로 선언했기 때문에
        `PUT`을 사용해야 한다.
        REST CRUD 규약
        - C - POST
        - R - GET
        - U - PUT
        - D - DELETE

      - `url: '/api/v1/posts/'+id`
        어느 게시글을 수정할지 URL Path로 구분하기 위해,
        Path에 id를 추가한다.
        즉, 단순히 구분을 위해 id를 추가한다고 보면 된다.

      - `<a href="/post/update/{{id}}">`
        타이틀(title)에 a tag를 추가한다.
        타이틀을 클릭하면, 해당 게시글의 수정 화면으로 이동한다.

      - 스프링 부트는 기본적으로
        `src/main/resources/static` 에 위치한 JS, CSS, 이미지 등
        정적 파일들은 URL에서 /로 설정된다.

      - `{{#posts}}`
        posts 라는 List를 순회한다.

      - `{{변수명}}`
        List에서 뽑아낸 객체의 필드를 사용한다
        Ex) `{{id}}, {{title}}`

    - `@Transactional`
      readOnly를 사용하기 위해서는 아래 패키지를 로딩해야 한다.
      `org.springframework.transaction.annotation.Transactional;`

    - Model
      서버 템플릿 엔진에서 사용할 수 있는 객체를 저장할 수 있다.
      findAll()로 가져온 결과를 posts로 index.mustache에 전달한다.  
      ```
      @GetMapping("/")
      public String index(Model model) {
        model.addAttribute("posts", postsService.findAllDesc());
        return "index";
      }
      ```
  
  - 스프링 시큐리티
    - 인증과 인가 기능을 가진 프레임워크
      즉, 보안의 표준이다.
    
    - 구글 등록 절차
      - console.cloud.google.com 접속
      - 프로젝트 생성
      - API 및 서비스 화면 클릭
      - 사용자 인증 정보 만들기 클릭
      - OAuth 클라이언트 ID 클릭
      - 애플리케이션 유형 - 웹 애플리케이션
      - 이름 <- 프로젝트명
      - 승인된 리디렉션 URL - 호스트 설정
      - 만들기 클릭
    
    - `spring-boot-starter-oauth2-client`
      - 소셜 로그인 등 클라이언트 입장에서
        소셜 기능 구현 시, 필요한 의존성
      
      - `spring-security-oauth2-client` 와
        `spring-security-oauth2-jose` 를 기본으로 관리한다.

  - JPA
    - 기본 컬럼 not null 설정 - `@Column(nullable = false)`

    - `@Enumerated(EnumType.STRING)`
      - JPA로 DB에 저장할 때 Enum 값을 어떤 형태로 저장할지 결정한다.
        기본적으로 int로 된 수가 저장된다.
      - 숫자로 저장되면, 확인할 때 의미 파악을 할 수가 없으므로,
        문자열로 저장될 수 있도록 선언한다.


# 2021-04-15
  - 애노테이션
    - 애노테이션 프로퍼티
    ```
    public @interface Test {
      String value(); <- 애노테이션의 프로퍼티
    }
    ```
    value는 기본 프로퍼티이다.
    그래서 이름 생략이 가능하다.
    
    - 유지정책
      - CLASS
        - .class 파일까지만 유지되며, 유지 정책을 지정하지 않으면,
          기본이 CLASS이다.
          그리고 리플랙션 API로 추출할 수 없다.
        ```
        @Retention(RetentionPolicy.CLASS)
        ```
      
      - SOURCE
        - 컴파일할 때 제거되므로, .class 파일이 포함되지 않는다.
        ```
        @Retention(RetentionPolicy.SOURCE)
        ```

      - RUNTIME
        - .class 파일까지 유지되고, runtime에 메모리가 로딩된다.
          실행 중에 리플랙션 API로 추출할 수 있다.
        ```
        @Retention(RetentionPolicy.RUNTIME)
        ```
    
    - default값을 지정하지 않으면, 필수 프로퍼티가 된다.
      즉, 사용하는 클래스에서 값을 지정하지 않으면 오류가 난다.
      따라서, 값을 지정해야만 사용할 수 있다.

      반면에, default값이 있는 애노테이션은
      값을 지정하지 않아도 문제가 없다.
      즉, 필수가 아닌 선택이다.
      ```
      @Annotation(value="값") <- 필수 프로퍼티를 가진 애노테이션
      @Annotation <- default값이 있는 애노테이션
      ```
      
      value 라는 이름을 가진 프로퍼티는 이름 생략이 가능하다.
      물론, 단수로 올 때 가능하지만 복수로 선언할 때는 반드시 명시해야 한다.
      
    - 애노테이션의 모든 프로퍼티에 기본 값이 설정되어 있다면,
      프로퍼티 설정을 생략해도 된다.
      
    - 애노테이션 프로퍼티 배열
      - 배열 값이 한 개일 경우, 중괄호 생략이 가능하다.
      ```
      String[] v() default {"하나"} -> String[] v() default "하나";
      ```

    - @Target
      - 애노테이션을 붙일 수 있는 범위를 제어할 수 있다.
        기존 : `@Target(value = {ElementType.TYPE})`
        변경 : `@Target(ElementType.TYPE)` <- 프로퍼티명이 value 일 경우 생략 가능

        `ElementType.TYPE` = 클래스에 선언
        `ElementType.METHOD` = 메소드에 선언
        `ElementType.FIELD` = 인스턴스 필드에 선언
        `ElementType.LOCAL_VARIABLE` = 로컬 변수에 선언
    
    - 애노테이션 복수 값 지정
      - `@Target({ElementType.FIELD, ElementType.PARAMETER})`
        중괄호 안에서 지정해야 한다.
    
    - 애노테이션 중복 사용
      - 같은 애노테이션을 중복으로 사용하려면, `@Repeatable`을 선언하고,
        반복에 대한 정보를 따로 정의한 애노테이션을 지정해야 한다.

     `1.` 중복으로 사용할 배열 애노테이션을 만든다.
        ```
        public @interface Interceptors {
          Carrier[] value();
        }
        ```
      
      `2.` 배열 애노테이션 클래스에 Repeatable 애노테이션을 사용한다.
        ```
        @Repeatable(value=Interceptors.class)
        public @interface Carrier {
          String value();
        }
        ```
      
      `3.` 사용할 클래스에서 애노테이션을 선언하면 된다.
        ```
        @Carrier("one");
        @Carrier("two");
        ```


# 2021-04-16
  - 쓰레드 복습
    - 쓰레드는 동시에 실행되지 않는다.
      그리고 OS가 개입하면서 순서가 바뀐다.
      `Ex) 1 -> 2 -> 4 -> 2 -> 1 -> 3 -> 1 -> 3 -> 4 -> 2 -> 3 -> 4`
    
    - 이름이 없는 서브 클래스는 익명 클래스로 구현할 수 있다.
    
    - 람다를 사용하기 위해선, `functional interface`의 전제조건이 있어야 한다.
    
    - Thread Pool 구동원리
      - 클라이언트가 서버에 접속하면,
        서버 프로그램은 `Thread Pool` 에 쓰레드 요청을 하고
        요청 받은 `Thread Pool`은 쓰레드를 생성하여, 서버 프로그램에 리턴한다.
        그리고 서버 프로그램은 쓰레드를 클라이언트에게 할당해준다.

        클라이언트가 접속을 종료하면, 쓰레드는 서버 프로그램으로 리턴되고,
        리턴된 쓰레드는 `Thread Pool` 에 반납된다.
        그리고 타 클라이언트가 접속을 요청하면,
        반납된 쓰레드를 다시 제공하여 사용하게 한다.

        이런 방식의 이점은 쓰레드를 재사용할 수 있고,
        쓰레드 생성 시간 절약을 할 수도 있다.

    - Thread 최소 개수와 최대 개수는 하드웨어 스펙과 클라이언트 수를
      고려해서 정해야 한다.

    - 메인 Thread가 종료되기 전까지는 JVM이 종료되지 않는다.


# 2021-04-17
  - Spring Security
    - 시큐리티 관련 클래스들은 한 패키지 안에 넣는다.
      Ex) config.auth 패키지
    
    - @EnableWebSecurity
      - Spring Security 설정들을 활성화시킨다.
    
    - csrf().disable().headers().frameOptions().disable()
      - hs-console 화면을 사용하기 위해, 해당 옵션들을 `disable` 한다.

    - authorizeRequests
      - URL별 권한 관리를 설정하는 옵션의 시작점
      - `authorizaRequests`가 선언되어야만 `antMatchers` 옵션 사용이 가능하다.
    
    - antMatchers
      - 권한 관리 대상을 지정하는 옵션.
      - URL, HTTP 메소드별로 관리가 가능하다.
      - "/" 등 지정된 URL들은 permitAll() 옵션을 통해,
        전체 열람 권한을 준다.
        그리고 "/api/vi/**" 주소를 가진 API는 USER 권한을
        가진 사람만 가능하게 설정한다.
    
    - antRequest
      - 설정된 값들 이외, 나머지 URL들을 나타낸다.
      - authenticated()을 추가하여, 나머지 URL들은
        모두 인증된 사용자들에게만 허용되게 한다.
      - 인증된 사용자는 로그인한 사용자들을 의미한다.
    
    - logout().logoutSuccessUrl("/")
      - 로그아웃 기능에 대한 여러 설정의 진입점이다.
      - 로그아웃 성공 시, / 주소로 이동한다.
        즉, 로그인된 상태에서 로그아웃을 하면 다시 로그인 화면
        혹은 메인페이지로 이동하게 한다.
    
    - oauth2Login
      - OAuth2 로그인 기능에 대한 여러 설정의 진입점이다.

    - userInfoEndpoint
      - OAuth2 로그인 성공 후, 사용자 정보를 로딩할 때 설정들을 담당한다.
    
    - userService
      - 소셜 로그인 성공 시, 후속 조치를 진행할 UserService 인터페이스의
        구현체를 등록하고, 리소스 서버(소셜 서비스들)에서
        사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능을 명시한다.
    
    - findByEmail
      - 소셜 로그인으로 반환되는 값 중, email을 통해 이미 생성된
        사용자인지 판단을 위한 메소드.
      ```
      import java.util.Optional
      Optional<User> findByEmail(String email);
      ```

    - registrationId
      - 현재 로그인 진행 중인 서비스를 구분하는 코드이다.
      - 하나만 한다면, 선언할 필요가 없지만,
        2개 이상의 소셜 로그인 연동을 하려면 필요하다.

    - userNameAttributeName
      - OAuth2 로그인 진행 시, Key가 되는 필드값을 의미한다.
        Primary Key 와 같은 의미다.
      - 구글의 경우, 기본적으로 코드를 지원하지만,
        국내 서비스업체인 네이버나 카카오 등은 지원하지 않는다.
        구글 기본 코드 = `sub`
    
    - OAuthAttributes
      - OAuth2UserService를 통해, 가져온 OAuth2User의
        attribute를 담을 클래스이다.
        네이버와 타 소셜 로그인도 이 클래스를 사용한다.
    
    - SessionUser
      - 세션에 사용자 정보를 저장하기 위한 Dto 클래스이다.
    
    - of()
      - OAuth2User에서 반환하는 사용자 정보는 Map이기 때문에
        값 하나하나를 변환해야 한다.
    
    - toEntity()
      - User 엔티티를 생성한다.
      - OAuthAttributes에서 엔티티를 생성하는 시점은
        처음 가입할 때이다.
      - 가입할 때의 기본 권한을 GUEST로 주기 위해,
        role 빌더값에는 role.GUEST를 사용한다.
      - OAuthAttributes 클래스 생성이 끝나면,
        같은 패키지에 SessionUser 클래스를 생성한다.


# 2021-04-18
  - 스프링 기초
    - @GetMapping 복습
      - @GetMapping("문자열")
        웹 애플리케이션에서 ("문자열")이 들어오면,
        @GetMapping이 있는 메소드를 호출한다.
        ```
        @GetMapping("test")
        public String test(Model model) {
          ...
        }
        ```
        웹 애플리케이션에서 /test 가 들어오면,
        test 메소드를 호출한다.
    
    - 타임리프 문법
    ```
    ${data} <- model.addAttribute(key, value)에서 key값이 일치하면,
    value값이 치환된다.
    
    model.addAttribute("data", "test");
    따라서, key값인 "data"와 ${data}가 같으므로,
    ${data} = "test"로 치환된다.
    ```

    - 동작 원리
      - 웹 브라우저에서 localhost:8080 으로 접속하면,
        내정 서버가 응답을 하고, Spring Container의 
        testController를 호출한다.
        @GetMapping을 통해, URL이 일치하면 test 메소드를 호출한다.
        그럼 Spring이 Model을 생성해서 test 메소드에 주입한다.
        model은 속성값을 추가해서 key와 value를 만든다.
        Ex) `model.addAttribute("data", "test")`
        그리고 "test"를 리턴한다.
        그럼 resources/templates에 리턴값과 일치하는 파일명을 찾고,
        있으면, 그 파일을 실행시켜서 화면에 렌더링한다.
        
        컨트롤러에서 리턴 값으로 문자를 반환하면,
        뷰 리졸버(viewResolver)가 화면을 찾아서 처리한다.
        즉, 아래와 같은 순서로 진행된다.
        ```
        resources/templates/ + (viewResolver) <- 리턴값 + `.html`
        뷰 리졸버는 리턴 값을 받게 되는데,
        그 값이 "test"면
        resources/templates/test.html 로 매핑이 된다.
        ```
    
    - CMD 에서 실행
      - 프로젝트 폴더에서 gradle build 실행
      - libs에 있는 파일명을 실행
      - `java -jar jar파일명`
        따라서, 내장서버가 실행된다.
    
    - 웹 개발에 필요한 기초 3가지
      - 정적 콘텐츠 / MVC와 템플릿 엔진 / API
    
    - 정적 콘텐츠
      - 웹 브라우저에 접속하고, html파일에 접속을 하면,
        내장 서버에 요청을 하고 서버는 Spring Container에
        알리고, Controller를 우선적으로 찾는다.
        하지만, 매핑된 Controller가 없으면
        내부의 `resources/static` 에서 찾는다.
        있으면 반환해서 화면에 렌더링한다.
    
    - MVC와 템플릿 엔진
      - MVC를 사용하기 전에는 Model One 방식을 사용했다.
        View에 모든 코드를 넣어서 동작시킨다.
        그래서 Controller와 View를 구분 짓지 않았다.
      
      - View는 화면을 그리는데 집중한다.
      - Controller는 비지니스 로직과 서버 부분을 처리한다.
      - Model은 처리된 Controller를 전달 받고,
        전달 받은 걸 View에 전달한다.
        즉, 브릿지 역할이다.
      
      - @RequestParam
        - 사용 시, 웹에서 구문을 작성해서 출력한다.
      
      - @GetMapping 사용 예2
      ```
      @GetMapping("test-mvc")
      public String testMvc(@RequestParan("code") String code, Model model) {
        model.addAttribute("code", code);

        return "test-template";
      }
      ```
      - 위 코드에서 code란 파라미터를 받는데,
        code를 URL창에서 컨트롤할 수 있다.
        `http://localhost:8080/test-mvc?code=Testing`
        을 실행하면, 화면에 Testing이 출력된다.
      
      - 동작 순서
        - @RequestParam("code") String code 값이 Testing으로 치환된다.
          따라서, model.addAttribute의 value값이 Testing으로 바뀌고 추가된다.
          그리고 리턴값인 test-template를 resources/templates에서 찾아서
          화면에 렌더링한다.
    
    - API
      - @ResponseBody
        - http의 header와 body 파트에서
          body에 데이터를 직접 삽입한다.
          ```
          @GetMapping("test-string")
          @ResponseBody
          public String testString(@RequestParam("name") String name) {
            return "test " + name;
          }
          ```
          - return 값은 "test name" 이 된다.
          - 템플릿 엔진과의 차이는 View가 없다.
            그래서 문자가 그대로 전달된다.
          
        - @ResponseBody 순서
          - 웹 브라우저에서 URL로 내정 서버에 접속을 요청한다.
            요청 받은 서버는 Spring Container 에서 Controller를 호출하고
            @ResponseBody가 있으면, viewResolver 대신에
            HttpMessageConverter가 동작한다.
            
            여기서 리턴값이 단순 문자열이면,
            StringConverter가 동작하고
            객체라면, JsonConverter가 동작한다.
            그래서 동작 방식을 통해 서버에 응답하고
            응답 받은 서버는 웹 브라우저에 전달된 값을 렌더링한다.

            ```
            return "test" <- 단순 문자열
            StringConverter 동작 시,
            test만 출력한다.
            ```

            ```
            Test 클래스가 있다고 가정.

            @GetMapping("test-api")
            @ResponseBody
            public Test helloApi(@RequestParam("name") String name, @RequestParam("number") int number) {
              Test test = new Test();
              test.setName(name);
              test.setNumber(number);

              return test;
            }
            Test test = new Test();
            test.setNumber(number);
            test.setName(name);
            return test <- 객체
            JsonConverter 동작 시,
            {
              "number": 50,
              "name": "test"
            }
            가 출력된다.
            
            number와 name 값은 url에서 설정
            ```
            

# 2021-04-20
  - 세션
    - 로그인 중인 기간 동안, 사용하는 객체를 가리킨다.
    - 한 작업을 여러 단계에 거치는 사용하는 객체.
  
  - static 복습
    - static은 모든 인스턴스가 공유 가능하다.
      따라서, thread safe가 아니다.
  
  - Thread 사용
    - 매번 쓰레드를 생성하는 건 비효율적이다.
      따라서, 유휴 상태인 쓰레드를 재사용한다.
  
  - shutdownNow 와 shutdown 차이
    - shutdown은 대기중인 쓰레드까지 다 처리하고 난 뒤에 종료한다.
    - shutdownNow는 대기중인 쓰레드가 있어도, 지정된 시간이 지나면 종료한다.

  - 실습 프로젝트
    - 세션을 이용한 로그인 / 로그아웃 기능 구현
    ```
    public class Session {
      private Map<String,Object> repository = new HashMap<>();

      public void setAttribute(String name, Object v) {
        repository.put(name, v);
      }

      public Object getAttribute(String name) {
        return repository.get(name);
      }

      public void invalidate() {
        repository.clear();
      }
    }
    ```
    setAttribute는 repository에 값을 저장하고,
    getAttribute는 repository의 값을 꺼내온다.
    그리고 invalidate는 세션 데이터들을 모두 초기화 시킨다.

    클라이언트가 접속해 있는 동안 유효한 객체이다.
    만일, 클라이언트가 로그아웃을 하면, 세션 데이터는 삭제된다.


# 2021-04-21
  - 멀티쓰레드에서 Connection 공유 환경의 단점
    - 여러 클라이언트가 작업을 하면 임시 DB에 저장된다.
      하지만, rollback이 실행되면, 이전에 작업했던 내용들이 다 취소된다.
      이러한 문제를 해결하기 위해 Proxy Pattern 을 사용한다.
  
  - Single Thread 의 Connection 공유
    - DBMS는 Thread별로 트랜잭션을 관리하기 때문에 두 명이 동시에 작업 중,
      어느 한 쪽이 rollback이 되더라도, 다른 Thread의 작업에는 영향을 끼치지 않는다.
  
  - Multi Thread 의 Connection 공유
    - Multi Thread는 Connection 을 공유할 때 문제가 발생한다.
      기본적으로 각 Client가 Thread를 이용하여 작업을 한다.
      두 명이 동시에 같은 작업을 하더라도 같은 Dao로 넘어가서
      Connection을 공유한다.

      여기서 문제는 Connection과 이어진 DBMS의 Thread는 하나다.
      그래서 DBMS의 Thread는 임시DB를 생성해서 작업 결과를 저장하고
      commit을 하면 전부 적용된다.
      
      하지만, 여기서 전부는 단 한 사람이 실행한 작업이 아닌,
      모두가 실행한 작업을 포함한다.
      즉, Single Thread와 흐름이 정반대다.
      그래서 어느 한 쪽이 rollback으로 이어지면,
      다른 사람의 작업 또한 rollback으로 작업이 취소된다.

  - Multi Thread 의 Connection 공유 문제점 해결 방법
    - Thread별로 SqlSession 객체를 분리해서 사용한다.
      그래서 Dao 객체에 SqlSessionFactory를 주입한다.
      각 Dao는 SqlSessionFactory와 포함관계를 이루며,
      DB작업을 수행할 때, SqlSession을 만들어 사용한다.

  - 프록시 패턴을 이용한 Connection 공유
    - SqlSessionProxy 클래스
      - SqlSession 객체는 이미 존재하며, 기존의 코드를 변경할 수 없다.
        따라서, 변경을 하기 위해서는 SqlSession 클래스를 모방하여 만들어야 한다.
        그리고 객체의 일부기능만 변경하려면, 프록시 객체를 생성해야 한다.

      ```
      public class SqlSessionProxy implements SqlSession {
        // SqlSession 인터페이스를 상속 받는다.
        // 그래야 모방하여 만들 수 있기 때문이다.

        SqlSession original; <- 프록시 객체
        boolean isInTransaction; <- 트랜잭션에 포함되는지 여부 확인 필드

        public SqlSessionProxy(SqlSession sqlSession, boolean transaction) {
          this.original = sqlSession;
          this.isInTransaction = transaction;
        }

        public void realClose() {
          original.close();
          // 트랜잭션을 완료한 상태라면, SqlSession 객체의 자원을 해제한다.
        }

        public void close() {
          if(isInTransaction) {
            return;
          }
          original.close();
          // 트랜잭션을 사용하는 경우에는 해제하면 안 된다.
          // 왜냐하면, 사용 도중 해제를 시켜버리면, 트랜잭션 적용이 되지 않기 때문이다.
        }
      }
      ```

    - SqlSessionFactoryProxy 클래스
      - SqlSessionFactory 또한 이미 존재하는 객체이다.
        따라서, SqlSessionProxy 클래스처럼 모방하여 사용한다.

      ```
      public class SqlSessionFactoryProxy implements SqlSessionFactory {
        // SqlSessionProxy 클래스와 마찬가지로 일부분만 변경해서 사용하기 위해,
        // 인터페이스 구현체를 모두 구현한다.

        SqlSessionFactory original;
        // 프록시 객체를 생성한다.

        ThreadLocal<SqlSessionProxy> threadLocal = new ThreadLocal<>();
        // Thread 보관소로 사용된다.
        // threadLocal에는 오직 SqlSession 객체만 넣을 수 있으며,
        // threadLocal 객체는 heap 영역에 인스턴스 필드로 생성되지 않는다.
        // 왜냐하면 각 Thread 마다 보유하는 필드이기 때문이다.

        public SqlSessionFactoryProxy(SqlSessionFactory sqlSessionFactory) {
          this.original = sqlSessionFactory;
        }

        public void prepareSqlSession() {
          SqlSessionProxy sqlSessionProxy = new SqlSessionProxy(original.openSession(false), true);
          // 수동 commit 으로 동작하는 SqlSessionProxy 객체를 생성한다.
        
          threadLocal.set(sqlSessionProxy);
          // Thread 보관소에 SqlSessionProxy 객체를 저장한다.
        }

        public void closeSession() {
          SqlSessionProxy sqlSessionProxy = threadLocal.get();
          // Thread 보관소에서 SqlSessionProxy 객체를 꺼낸다.

          if(sqlSessionProxy != null) {
            sqlSessionProxy.realClose();
            // 사용한 객체는 자원 해제를 실행한다.
            threadLoca.remove();
            // 보관소에서 꺼낸 객체를 삭제한다.
          }
        }

        public SqlSession openSession(boolean autoCommit) {
          if(autoCommit) {
            return original.openSession(autoCommit);
          }

          if(threadLocal.get() != null) {
            return threadLocal.get();
          }
          // 트랜잭션 관리자가 제어하기 위해, 미리 SqlSession 객체를 생성하여
          // 보관한 상태면 객체를 꺼내서 반환해야 한다.

          return original.openSession(true);
          // Thread 보관소에 저장된 SqlSession 객체가 없다는 것은
          // 트랜잭션 관리자의 통제 없이 동작하는 SqlSession이 필요하다는 의미다.
          // 따라서, 자동 commit 으로 동작하는 SqlSession 객체를 반환하면 된다.
        }
      }
      ```

      - ThreadLocal 의 개념
        - ThreadLocal는 클래스이며 오직, 한 Thread에 의해서 읽고 쓰여질 수 있는 개념이다.
          그래서 두 Thread가 같은 코드를 실행하더라도, 서로 실행되는 Thread는 동일하지 않다.
          그리고 데이터를 사용한 후에는 반드시 데이터를 삭제해야 한다.
          삭제를 하지 않고 진행할 경우, 재사용되는 Thread가 올바르지 않은 데이터를 
          참조할 수 있기 때문이다.
    

    - TransactionManager 클래스
      - 트랜잭션을 관리하기 위해 만든 클래스이다.
      
      ```
      public class TransactionManager {
        SqlSessionFactoryProxy sqlSessionFactoryProxy;
        // 트랜잭션을 이용하기 위해서는 SqlSessionFactoryProxy가 필요하다.
        // 왜냐하면, SqlSessionFactoryProxy 없이는 동작이 불가능한 클래스이기 때문이다.

        public TransactionManager(SqlSessionFactoryProxy sqlSessionFactoryProxy) {
          this.sqlSessionFactoryProxy = sqlSessionFactoryProxy;
        }

        public void beginTransaction() {
          sqlSessionFactoryProxy.prepareSqlSession();
          // 트랜잭션을 시작하면, 사용할 SqlSession 객체를 Thread 보관소에 저장한다.
        }

        public void commit() {
          SqlSessionProxy sqlSessionProxy = (SqlSessionProxy) sqlSessionFactoryProxy.openSession(false);
          sqlSessionProxy.commit();
          sqlSessionFactoryProxy.closeSession();
          // 트랜잭션이 유지되는 동안 수행된 모든 작업을 승인하려면,
          // Thread 보관소에 저장된 SqlSessionProxy 객체를 꺼내서 commit()을 호출한다.
        }

        public void rollback() {
          SqlSessionProxy sqlSessionProxy = (SqlSessionProxy) sqlSessionFactoryProxy.openSession(false);
          sqlSessionProxy.rollback();
          sqlSessionFactoryProxy.closeSession();
          // 트랜잭션이 유지되는 동안 수행된 모든 작업을 취소하려면,
          // Thread 보관소에 저장된 SqlSessionProxy 객체를 꺼내서 rollback()을 호출한다.
        }
      }


# 2021-04-22
  - 트랜잭션 제어 코드 캡슐화
    - TransactionTemplate 클래스
    ```
    public class TransactionTemplate {

      private TransactionManager txManager;
      // 트랜잭션 코드를 제어하기 위해, TransactionManager 클래스를 포함한다.

      public TransactionTemplate(TransactionManager txManager) {
        this.txManager = txManager;
      }
      
      public Object execute(TransactionCallBack cb) throws Exception {
        // execute 메소드에서 TransactionCallback 파라미터를 받는다.
        // 서비스 객체에서 new TransactionCallback 인터페이스를 생성하고,
        // doInTransaction() 메소드를 오버라이딩해서 코드들을 트랜잭션으로 묶어서 실행한다.

        txManager.beginTransaction();
        execute 메소드를 호출하면, 바로 트랜잭션을 시작한다.

        try {
          Object result = cb.doInTransaction();
          txManager.commit();
          return result;
        }
        // Object 타입으로 TransactionCallback의 doInTransaction() 메소드의 결과를 치환한다.
        // 서비스에서 오버라이딩해서 사용하기에, 호출된 doInTransaction() 메소드의 리턴 타입은
        // Object 타입이지만, Integer 타입으로 타입 캐스팅하여 리턴 타입을 맞춰서 결과를 보낸다.
        // 그래서 Object 타입으로 값을 오류없이 받을 수 있다.
        // 그리고 commit() 메소드를 실행하여, 데이터 변경 결과를 반영한다.

        catch (Exception e) {
          txManager.rollback();
          throw e;
        }
        // 예외가 발생할 경우, rollback() 메소드를 실행하여 데이터 변경 결과를 반영하지 않는다.
      }
    }
    ```


# 2021-04-23
  - Consumer
    - Consumer는 함수적 인터페이스이다.
      매개변수를 받아 소비만 하고, 리턴 값을 가지지 않는다.
      그리고 매개변수를 받는 메소드는 accept 이다.

    - 응용
    ```
    public class Test {
      public static void main(String[] args) {
        Consumer<String> c = t -> System.out.printf(t <- 는 Consumer 매개변수\n", t);
        
        c.accept("test code");
      }

      // BiConsumer 사용 (매개변수를 복수로 사용할 때)
      BiConsumer<Integer, String> bi = (x, y) -> 
      System.out.printf("%d <- 매개변수 x값 / %s <- 매개변수 y값\n");
      bi.accept(100, "test code");
    }
    ```
    
    람다식을 사용한다.
    
  - Supplier
    - Consumer와 반대 기능을 가진다.
      그래서 매개변수는 없고, 리턴 값을 가진다.
      
    - 응용
    ```
    public class Test {
      public static void main(String[] args) {
        Supplier<String> s = () -> {
          String result = "test code";
          return result;
        };

        System.out.printf("%s <- Supplier 리턴 값\n", s.get());

        Supplier<List<User>> list = () -> {
          List<User> name = new ArrayList<>();
          name.add(new User("하나"));
          name.add(new User("미니"));
          name.add(new User("재희"));

          return name;
        };

        for(User user : list.get()) {
          System.out.printf("이름 : %s\n", user.getName());
        }
      }
    }
    ```


# 2021-04-24
  - 스프링 이해
    - 컨트롤러(Controller)의 역할
      - 웹 MVC의 C - Controller 이며,
        MVC 중에서 가장 먼저 실행되는 부분이다.
        클라이언트가 요청을 하면,
        요청 받은 데이터를 가공해서 모델에 전달한다.
      
    - 서비스(Service)의 역할
      - 핵심 비지니스 로직이다.
        클라이언트의 요청을 받으면, 받은 요청을
        서버에서 처리하고 응답을 한다.
        그래서 DAO 역할을 하는 부분이다.
    
    - 리포지토리(Repository)의 역할
      - DB에 접근하고, 도메인 객체를 DB에 저장 및 관리를 한다.
        그래서 서비스는 직접 DB에 접근하지 않고,
        리포지토리가 직접 접근하여, 서비스의 요청을 수행한다.
    
    - 도메인(Domain)의 역할
      - 비지니스 도메인 객체이다.
        예를 들면, 회원/주문/상품 등 주로 DB에 저장되고
        관리되는 객체들을 의미한다.
    
    - Optional 기능
      - 조회중에 값을 찾지 못 하면, null값을 리턴한다.
        그래서 if문을 사용할 필요가 없다.
        `Optional.ofNullable(map.get(id));`
        map에서 id를 꺼낼 때, 찾는 값이 아니라면 null을 준다.
    
    - filter 기능
    ```
    public Optional<Member> findByName(String name) {
    return store.values().stream()
            .filter(member -> member.getName().equals(name))
            .findAny();
    }
    ```
    
    store.values() 는 멤버들의 값들을 의미한다.
    즉, List인 셈이다.
    member의 name이 파라미터 name과 일치하면, findAny() 메소드를 통해 리턴한다.
    만일, 일치하지 않는다면 null을 리턴한다.

    - @AfterEach
      - 콜백 메소드이다.
        메소드가 끝날 때마다, 호출하는 애노테이션이다.
    
    - save, findByName, findAll 테스트 코드 작성
    ```
    MemoryMemberRepository repository = new MemoryMemberRepository();

    @Test
    public void save() {
      Member member = new MembeR();
      member.setName("spring");

      repository.save(member);
      Member result = repository.findById(member.getId()).get();

      assertThat(member).isEqualTo(result);
    }
    ```

    Member 인스턴스를 생성하고,
    name 필드에 값을 넣는다.
    그리고 MemoryMemberRepository 객체의 save 메소드에
    member 파라미터를 넣고 실행한다.
    그럼 member의 id와 name은 저장되고 리턴된다.
    그 다음, Member 객체 result에 repository.findById 메소드를
    member.getId()을 넣고 호출하고, 결과를 넣는다.
    
    findById 메소드 내부에서 전달받은 id 파라미터를 가지고,
    Optional.ofNullable 메소드를 통해,
    id와 일치한 값이 존재하면 result는 그 값으로 치환되고,
    일치한 값이 없으면, result는 null 값으로 치환된다.
    
    member 중에 result와 일치하는 값이 있는지 assertThat을 통해 검증한다.
    값이 존재하면, 테스트는 통과한다.

    ```
    @Test
    public void findByName() {
      Member m = new Member();
      m.setName("test");
      repository.save(m);

      Member m2 = new Member();
      m2.setName("test2");
      repository.save(m2);

      Member result = repository.findByName("test2").get();
      assertThat(m).isEqualTo(result);
    }
    ```
    
    Member 인스턴스를 생성하고, name 필드에 test, test2 문자열을 넣는다.
    그리고 save 메소드를 통해 저장한다.
    result에 repository.findByName("test2").get() 을 치환한다.
    만약, test2 값이 없다면, result의 값은 null이다.
    반대로 있으면, 파라미터 값 그대로 치환된다.
    그리고 m의 값과 result의 값이 같은지 assertThat을 통해 검증한다.
    
    여기서 주의 깊게 볼 부분은 assertThat(값) 이다.
    m이 m2로 바뀐다면, 테스트는 통과하지만,
    m을 그대로 실행하면 통과하지 못 한다.
    왜냐하면, m의 값과 직접 비교하기 때문이다.
    m의 값은 test이고, m2의 값은 test2이다.
    따라서, 결과는 참이 될 수 없다.

    ```
    @Test
    public void findAll() {
      Member m = new Member();
      m.setName("test1");
      repository.save(m);
      m.setName("test2");
      repository.save(m);

      List<Member> list = repository.findAll();
      assertThat(list.size()).isEqualTo(2);
    }
    ```

    list에는 총 2개의 값들이 치환된다.
    저장된 값들이 총 2개이기 때문이다.
    그리고 list.size()는 2가 되며, isEqualTo(2)와 일치하므로,
    테스트는 통과한다.

    - @AfterEach의 필요성
      - 테스트 코드는 순서대로 진행을 보장하지 않는다.
        그러므로, @AfterEach를 추가하고 진행하는 게 좋다.
        간단히 예를 들자면,
        @AfterEach를 사용하지 않는다면
        맨 처음 save()가 실행되면, repository에는 하나의 데이터가
        추가된다. 그 상태로 findAll()로 넘어가서 실행하면,
        list.size()는 2개가 아닌, 3개가 된다.
        따라서, 기존 isEqualTo(2)와 일치하지 않게 되면서 오류를 초래한다.
        이러한 이유 때문에 @AfterEach가 필요하다.

        @AfterEach 가 있는 메소드는 저장된 값을 비우는 clear()를 실행한다.
    

# 2021-04-25
  - 스프링 응용
    - ifPresent 사용
      - Consumer 함수가 제공된다. 따라서, 람다식으로 표현이 가능하다.
      ```
      private void validatePremadeMember(Member member) {
        memberRepository.findByName(member.getName())
          .ifPresent(m -> { throw new IllegalStateException("이미 존재하는 회원"); });
      }
      ```
      
      memberRepository의 findByName의 리턴 타입은 Optional<Member> 이다.
      findByName 메소드를 이용하여, 파라미터 값으로 받은 member의 name을
      확인한다. ifPresent로 넘어가서, name과 일치하는 값이 있다면,
      이미 존재하는 회원이라는 문자열을 예외로 발생시킨다.

    - @Controller 의미
      - @Controller를 포함한 클래스는
        Spring Container가 MemberController 객체를 생성해서
        넣어두고 관리한다.

        스프링이 관리하면, 스프링 컨테이너에 등록하고
        스프링 컨테이너로부터 받아서 사용한다.

        만일, 인스턴스를 생성해서 사용한다면 여러 컨트롤러들이 공유해서
        사용 가능해진다. 그래서 하나만 생성해놓고 같이 공용으로 쓰면 된다.
        
    - @Autowired
      - 자동으로 연결을 한다는 의미이다.
      ```
      private final MemberService memberService;

      @Autowired
      public MemberController(MemberService memberService) {
        this.memberService = memberService;
      }
      ```
      
      @Autowired는 memberService를 스프링 컨테이너에 올리고,
      연결을 시킨다.

      MemberService 클래스에 @Service를 붙이면,
      스프링이 MemberService를 등록한다.

      @Autowired를 사용한 객체들 순서
      MemberService는 MemberRepository를 주입 받는다.
      MemberController는 MemberService를 주입 받는다.
      따라서, 위 연결고리는 아래와 같다.
      MemberController -> MemberService -> MemberRepository
      
    - @Bean을 이용하여 스프링 컨테이너에 등록
      - Config 클래스 생성
      - @Configuration 추가
      - @bean으로 MemberService를 올리고,
        MemberRepository도 올린다.
        그리고 생성자가 필요한 memberService는
        memberRepository를 추가한다.
      
      - 장점
        - 설정 파일을 본인이 관리하기 때문에
          상황에 따라 구현 클래스를 변경해야 할 때,
          config 클래스 코드만 변경하면 된다.
          그래서 유지보수에 용이하다.
      
    - DI의 3가지 방법
      - 필드 주입
        - `@Autowired private MemberService memberService;`
      
      - Setter 주입
        ```
        @Autowired
        public void setMemberService(MemberService memberService) {
          this.memberService = memberService;
        }
        ```
      
      - 생성자 주입
        ```
        private MemberService memberService;

        @Autowired
        public MemberController(MemberService memberService) {
          this.memberService = memberService;
        }
        ```
    
    - @Autowired 사용 시기
      - 스프링 Bean에 등록이 되어 있어야 사용 가능하기 때문에
        클래스에 애노테이션을 추가하거나, 아니면 @Bean을 사용해야 한다.

    - @Transactional 역할
      - 테스트 케이스에 이 애노테이션이 있을 경우, 
        테스트 시작 전에 트랜잭션을 시작하고, 테스트 종류 후, 롤백한다.
        그래서 DB에 영향을 미치지 않는다.
    
    - auto_increment 역할
      - 스프링에서는 IDENTITY가 대신한다.
        - `@GeneratedValue(Strategy = GenerationType.IDENTITY)`
    
    - EntityManager 역할
      - JPA는 EntityManager로 동작이 된다.
        build.gradle에서 data-jpa를 주입하면,
        SpringBoot가 자동으로 EntityManager를 생성한다.
        그래서 JPA를 사용하려면, EntityManager를 주입 받아야 한다.

    - JPQL(Java Persistence Query Language)
      - 보통 테이블 대상으로 쿼리를 날리지만,
        JPQL은 객체를 대상으로 쿼리를 날린다.
        그럼 SQL로 번역된다.
    
    - AOP 개념
      - 공통 관심 사항을 등록해서 원하는 곳에 적용한다.
        cross-cutting concern 이라 불리며, 프록시를 사용한다.
        예를 들면, 기존 스프링 컨테이너에서
        MemberController가 MemberService를 의존하고 있었다면,
        AOP를 적용하면,
        MemberController가 프록시 MemberService를 의존하고,
        프록시 MemberService는 기존 MemberService를 의존한다.
      
      - 코드 적용
        - AOP 클래스에 @Aspect를 추가한다.
          해당 클래스가 횡단관심사 클래스임을 알리는 애노테이션이다.
          자동으로 Bean 등록이 되는 것이 아니므로, 추가로
          @Component 애노테이션을 추가하거나, 직접 @Bean을 등록해야 한다.


# 2021-04-26
  - HTTP
    - 인터넷 통신
      - 클라이언트와 서버의 통신 방식
        인터넷망을 거쳐서 통신한다.
        Ex) 해저 케이블, 인공위성 등
    
    - IP
      - 통신을 위해선 IP주소를 부여 받아야 한다.
        지정한 IP 주소에 데이터를 전송하며,
        `패킷` 이라는 통신 단위로 데이터를 전달한다.
    
    - IP의 한계
      - 비연결성
        - 패킷을 받을 대상이 없거나, 서비스 불능 상태여도
          패킷 전송이 가능하다.
      
      - 비신뢰성
        - 중간에서 패킷이 사라지면? (하이재킹 위험)
        - 패킷이 순서대로 오지 않는다면? (무결성 위반)
      
      - 프로그램 구분
        - 같은 IP를 사용하는 서버에서 통신하는 애플리케이션이
          둘 이상이라면? (접속 모호)
    
    - TCP / UDP
      - 인터넷 프로토콜의 4계층
        - 애플리케이션 계층
          - HTTP, FTP Ex) Browser Socket 라이브러리
        
        - 전송 계층
          - TCP, UDP
        
        - 인터넷 계층
          - IP
        
        - 네트워크 인터페이스 계층
          - 하드웨어 LAN 장비 및 드라이버 Ex) 랜카드
        
      - 데이터 전송 순서
        - 프로그램이 메시지를 생성해서 SOCKET 라이브러리를 통해
          전달한다. 그리고 TCP 정보 생성(메시지 데이터 포함)을 하고,
          IP 패킷(TCP 정보 포함)을 생성하여 TCP 정보를 감싼다.
          마지막으로, LAN 카드를 통해 나간다.

      - TCP 특징
        - 전송 제어 프로토콜

        - 연결지향 - TCP 3 way handshake(가상 연결)
          - 클라이언트가 서버에 SYN을 보낸다.
            그리고 서버는 SYN과 ACK를 클라이언트에게 보낸다.
            이렇게 서로 주고 받고 난 뒤, 클라이언트는 서버에 ACK를 보낸다.
            SYN : 접속 요청 / ACK : 요청 수락
            
            클라이언트가 서버에 접속 요청을 보내고,
            서버는 요청을 받고 수락한 다음, 클라이언트에 접속을 요청한다.
            그러면 이제 서로 연결이 되므로, 데이터 전송을 할 수 있게 된다.
            그래서 클라이언트가 서버로부터 SYN과 ACK를 받고 난 다음,
            ACK를 보낼 때, 데이터도 함께 전송이 가능하다.

        - 데이터 전달 보증
          - 서버에 요청을 하면, 서버는 응답을 한다.
            만일, 응답하지 않으면 문제가 있다는 의미다.
            (패킷이 중간에 누락되면 알 수 있다.)

        - 순서 보장
          - 클라이언트가 서버에 데이터 전송을 했는데
            서버에서 순서대로 받지 못 하면, 서버는 클라이언트에게 요청을 한다.
      
      - UDP 특징
        - 사용자 데이터그램 프로토콜

        - 데이터 전달 및 순서가 보장되지 않지만,
          단순하고 빠르다는 장점이 있다.
          IP와 거의 유사하다.

    - PORT
      - 같은 IP 내에서 프로세스를 구분한다.
        그래서 PORT 번호는 여러 개 존재한다.
        예를 들면, 아파트가 IP면, PORT는 호수 라고 보면 된다.

        0 ~ 65535까지 할당이 가능하며,
        0 ~ 1023까지는 잘 알려진 포트여서 사용을 지양해야 한다.

    - DNS
      - IP는 기억하기 어렵고, 변경될 가능성이 있다.
        따라서, DNS(Domain Name System)을 사용한다.
        그래서 DNS를 등록해서 사용하면, IP로 접속할 필요가 없다.
        물론, IP로 접속하는 것이 DNS로 하는 것보다 더 빠르다.
    

# 2021-04-27
  - URI 개념
    - Uniform Resources Identifier
      - URI 안에 URL, URN이 있다.
    
    - Uniform : 리소스 식별하는 통일된 방식
    - Resource : 자원, URI로 식별할 수 있는 모든 것(제한이 없다.)
    - Identifier : 다른 항목과 구분하는데 필요한 정보
    
    - URL : 리소스가 있는 위치를 지정
    - URN : 리소스에 이름을 부여

    - URL 문법
      - scheme://[userinfo@]host[:port][/path][?query][#fragment]
      - https://www.google.com:443/search?q=hello&hl=ko

      - https = 프로토콜
      - www.google.com = 호스트명
      - 443 = 포트번호
      - /search = 패스
      - q=hello&hl=ko = 쿼리 파라미터
        key=value 형태

      - userinfo는 URL에 사용자 정보를 포함해서 인증하는 용도로 사용하지만, 
        실제로는 거의 사용하지 않는다.
      
      - http 는 80 포트 사용
      - https 는 443 포트 사용
  
  - 웹 브라우저 요청 흐름
    - 웹 브라우저를 통해 서버 접속을 할 때의 흐름
      주소를 입력해서 접속을 할 때, HTTP 요청 메시지 생성을 한다.
      
    - 요청 메시지의 예시
    ```
    GET /search?q=hello&hl=ko HTTP/1.1
    Host: www.google.com
    ```

    - HTTP 메시지 전송
      - 1. 웹 브라우저가 HTTP 메시지를 생성한다.
      - 2. SOCKET 라이브러리를 통해 TCP/IP에 연결하여 데이터를 전달한다.
      - 3. TCP/IP 패킷을 HTTP 메시지를 포함해서 생성한다.
      - 4. LAN카드를 통해 인터넷으로 전송된다.
      - HTTP 메시지는 TCP/IP 패킷이 감싸서 전송한다.

    - HTTP 응답 메시지
    ```
    HTTP/1.1 200 OK
    Content-Type: text/html;charset=UTF-8
    Content-Length: 3423
    <html> ... </html>
    ```

    응답 메시지를 브라우저에 전달해서 화면에 렌더링한다.

  - HTTP
    - HyperText Transfer Protocol
    - HTTP 메시지에 모든 것을 전송할 수 있다.
      HTML, TEXT, IMAGE, 음성, 영상, 파일, JSON, XML 등
    
    HTTP/2 는 2015년에 나왔으며, 성능 개선을 하였다.
    HTTP/3 는 현재진행중이며, TCP 대신 UDP를 사용하며 성능 개선중이다.

    - 기반 프로토콜
      - TCP : HTTP/1.1, HTTP/2
      - UDP : HTTP/3
    
  - Stateful 과 Stateless
    - Stateful = 상태 유지
    - Stateless = 상태 유지 X
    
    - Stateful은 중간에 상태 정보가 바뀌면, 장애를 초래한다.
    - Stateless는 중간에 상태 정보가 바뀌어도 상관없다.
    
    - Stateful은 항상 같은 서버가 유지되어야 한다.
      반면에, Stateless는 서버가 교체되어도 이상이 없다.
    
    - Stateless의 한계
      - 로그인의 경우, Stateful 방식을 사용해야 한다.
        추가적인 단점은 데이터를 Stateful에 비해 더 많이 보낸다.
    
  - 비연결성
    - 장점
      - HTTP는 기본이 연결을 유지하지 않는 모델이다.
        빠른 속도로 응답을 한다.
        자원을 효율적으로 사용할 수 있다.
    
    - 단점
      - TCP/IP 연결을 새로 맺어야 한다 -> 3 WAY HANDSHAKE 시간 추가
        그래서 HTTP 지속 연결(Persistence Connections)로 문제 해결
    
    - 기존 방식 : 연결 -> 요청/응답 -> 종료 = 반복
    - 지속 연결 방식 : 연결 -> 요청/응답 -> 연결 유지 = 반복
  
  - 시작 라인(요청 메시지)
    - start-line = request-line / status-line
    - request-line = method SP(공백) request-target SP HTTP-version CRLF(엔터)
    
    - HTTP 메소드 (GET : 조회)
    - 요청 대상 (/search?q=hello&hl=ko)
    - HTTP Version

  - HTTP 메소드
    - 종류 : GET / POST / PUT / DELETE
      - 서버가 수행해야 할 동작 지정
      - GET : 리소스 조회
      - POST : 요청 내역 처리, 주로 등록에 사용
      - PUT : 리소스를 대체, 해당 리소스가 없으면 생성
      - PATCH : 리소스 부분 변경
      - DELETE : 리소스 삭제
    
    - 요청 대상
      - 절대 경로로 시작한다.

    - 응답 메시지
      - status-line = HTTP-version SP status-code SP reason-phrase CRLF
      - HTTP 상태 코드
        - 200 성공
        - 400 클라이언트 요청 오류
        - 500 서버 내부 오류
    
    - HTTP 헤더
      - header-field = field-name ":" OWS field-value OWS
        Ex) Content-Type: text/html;charset=UTF-8
      
      - 용도
        - HTTP 전송에 필요한 모든 부가 정보
          Ex) 메시지 Body의 내용, 메시지 Body의 크기, 압축, 인증 등
      
    - HTTP 메시지 Body
      - 실제 전송할 데이터를 포함한다.
      - HTML문서, 이미지, 영상, JSON 등등
        Byte로 표현할 수 있는 모든 데이터는 전송 가능하다.
    
    - 기타 메소드
      - HEAD, OPTION, CONNECT, TRACE
    
    - GET 메소드
      - 서버에 전달하고 싶은 데이터는 QUERY(쿼리 파라미터)를 통해서 전달

      - 요청
      ```
      GET /members/100 HTTP/1.1
      Host: localhost:8080
      ```
      - 응답
      ```
      HTTP/1.1 200 OK
      Content-Type: application/json
      Content-Length: 34
      
      {
        "username": "tester",
        "age": 30
      }
      ```
    
    - POST 메소드
      - 요청 데이터를 처리한다.
      - 메시지 Body를 통해, 서버로 요청 데이터를 전달한다.
      - 서버는 요청 데이터를 처리한다.
      - 주로, 전달된 데이터로 신규 리소스 등록, 프로세스 처리에 사용된다.

      - 요청
      ```
      POST /members HTTP/1.1
      Content-Type: application/json

      {
        "username": "tester",
        "age": 30
      }
      ```

      - 응답
      ```
      HTTP/1.1 201 Created
      Content-Type: application/json
      Content-Length: 34
      Location: /members/100
      {
        "username": "tester"
        "age": 30
      }
      ```

      - POST는 주로 다음과 같은 기능에 사용된다.
        게시판 글쓰기, 댓글 쓰기, 신규 생성, 내용 추가 등등
        
        - 새 리소스 생성해서 등록한다.
        - 요청 데이터 처리
          - 상품주문 -> 결제완료 -> 배달시작 -> 배달완료
    
    - PUT 메소드
      - 리소스가 있으면 대체하고, 없으면 생성한다.
      - 클라이언트가 리소스를 식별한다
        클라이언트가 리소스 위치를 알고 URI를 지정한다

      - 주의할 점
        - 3개의 필드가 있는데, 1개의 필드만 수정 요청하면,
          나머지 2개의 필드는 삭제된다.
          따라서, 전부 명시를 해야 하거나 PATCH 메소드를 사용해야 한다.
        ```
        PUT /members/100 HTTP/1.1
        Content-Type: application/json

        {
          "username"= "tester",
          "age"= 30;
        }
        ```
        여기서 age만 수정하려고 하면, username의 필드는 삭제된다.
    
    - PATCH 메소드
      - 리소스 부분을 변경한다
        말 그대로 부분 변경을 할 수 있기 때문에
        명시되지 않은 필드가 있어도 그 필드는 삭제되지 않는다.
        결론은 PUT 메소드 보다 안정적이다.

    - DELETE 메소드
      - 리소스를 제거한다.
      ```
      DELETE /members/100 HTTP/1.1
      Host: localhost:8080
      ```
  
  - HTTP 메소드 속성
    - 안전
      - 호출해도 리소스를 변경하지 않는다.
        GET 방식이 대표적이다.
    
    - 멱등 (Idempotent)
      - 몇 번을 호출하더라도, 결과값이 바뀌지 않는다.
      - GET : 몇 번을 조회하든 같은 결과가 조회된다.
      - PUT : 결과를 대체한다. 딸서 같은 요청을 여러 번 해도 결과는 동일하다.
      - DELETE : 결과를 삭제한다. 위와 같다.

      - POST : 멱등이 아니다. 예를 들면, 결제를 두 번 이상 호출하면,
               중복 처리 된다.
      
    - 활용
      - 자동 복구 매커니즘
        - DELETE를 호출했는데 서버에서 응답을 하지 않으면,
          클라이언트는 재시도를 한다.
    
      - 재요청 중간에 다른 곳까지 리소스를 변경하면,
        멱등은 그것까지는 고려하지 않는다.
        Ex) Original GET -> PUT(중간에 값 변경) -> Changed GET
  
    - 캐시가능 (Cacheable)
      - GET, HEAD,  POST, PATCH 는 캐시가 가능하다.


# 2021-04-29
  - 클라이언트에서 서버로 데이터 전송할 때의 4가지 상황
    - 정적 데이터 조회
      - 이미지, 텍스트 문서
    
    - 동적 데이터 조회
      - 검색, 게시판 정렬 및 필터 등
    
    - HTML Form 을 통한 데이터 전송
      - 회원 가입, 상품 주문, 데이터 변경
    
    - HTML API를 통한 데이터 전송
      - 회원 가입, 상품 주문, 데이터 변경
      - 서버 to 서버, 앱 클라이언트, 웹 클라이언트(Ajax)

    
    - 정적 데이터를 조회할 때
      - 쿼리 파라미터 없이, 리소스 경로로 단순하게 조회가 가능하다
    
    - 정적 데이터를 조회할 때
      - 쿼리 파라미터(key=value)를 사용해서 데이터를 전달한다.
    
    - HTML Form 데이터 전송을 할 때
    ```
    <form action="/save" method="post" enctype="multipart/form-data">
      <input type="text" name="username" />
      <input type="text" name="age" />
      <input type="file" name="file1" />
      <button type="submit">전송</button>
    </form>
    ```
  
  - HTTP 요청 메시지
  ```
  POST /save HTTP/1.1
  Host: localhost:8080
  Content-Type: multipart/form-data; boundary=---XXX
  Content-Length: 10457

  ---XXX
  Content-Disposition: form-data; name="username"

  kim
  ---XXX
  Content-Disposition: form-data; name="age"

  20
  ---XXX
  Content-Disposition: form-data; name="file1"; filename="test.png"
  Content-Type: image/png
  ```

  - HTTP API 데이터 전송
  ```
  POST /members HTTP/1.1
  Content-Type: application/json
  {
    "username": "test",
    "age": 20
  }
  ```
  - 앱 클라이언트 (모바일)
  - 서버 to 서버
  - 웹 클라이언트
    - HTML에서 Form 전송 대신에 JS를 통한 통신에 사용(Ajax)
  
  - HTTP API 설계 예시
    - HTTP API 컬렉션
      - POST 기반 등록
      - Ex) 회원 관리 API 제공
    
    - HTTP API 스토어
      - PUT 기반 등록
      - Ex) 정적 콘텐츠 관리, 원격 파일 관리
    
    - HTTP FORM 사용
      - 웹 페이지 회원 관리
      - GET, POST만 지원
  
    - 회원 목록 및 조회 = GET
    - 회원 등록 = POST
    - 회원 수정 = PATCH, PUT, POST
    - 회원 삭제 = DELETE

  - POST 신규 자원 등록 특징
    - 클라이언트는 등록될 리소스의 URI를 모른다.
      - 회원등록 /members -> POST
      - POST /members
    
    - 서버가 새로 등록된 리소스 URI를 생성해준다.
      - HTTP/1.1 201 Created
        Location: /members/100
    
  - 컬렉션
    - 서버가 관리하는 리소스 디렉토리
    - 서버가 리소스의 URI를 생성하고 관리
    - 여기서 컬렉션은 /members
    
  - 파일 관리 시스템
    - API 설계 - PUT 기반 등록
    - 파일 목록 및 조회 - GET
    - 파일 등록 - PUT (없으면 새로 생성, 있으면 덮어쓰기)
    - 파일 삭제 - DELETE
    - 파일 대량 등록 -> POST
    
  - 컨트롤 URI
    - 문서, 컬렉션, 스토어로 해결하기 어려운 추가 프로세스 실행
    - 동사로 직접 사용
      Ex) /members/{id}/delete
    
  - 상태 코드
    - 100번대 - 요청이 수신되어 처리중
    - 200번대 - 요청 정상 처리
    - 300번대 - 요청을 완료하려면, 추가 행동 필요
    - 400번대 - 클라이언트 오류
    - 500번대 - 서버 오류

  - Servlet / GenericServlet / HttpServlet
    - Servlet
      - 메소드 종류
        - init()
          - Servlet 객체 생성 후, 생성자 다음으로 호출되는 메소드
        
        - service()
          - 클라이언트 요청에 의해 실행되는 메소드
        
        - destroy()
          - 웹 애플리케이션을 종료할 때 호출되는 메소드
        
        - getServletInfo()
          - Servlet Container 가 관리자 화면을 출력할 때 호출되는 메소드
        
        - getServletConfig()
          - Servlet 에서 작어을 수행하는 중, 관련 설정 정보를 꺼낼 때
            호출되는 메소드.
            이 메소드의 리턴타입은 ServletConfig인데, 이 객체를 이용하여
            Servlet 설정 정보를 알아낼 수 있다.

    - GenericServlet
      - 추상클래스이며, Servlet 인터페이스를 상속 받는다.
      - GenericServlet 을 상속 받은 클래스는
        service() 메소드만 오버라이딩하면 된다.
        왜냐하면, GenericServlet 내부에서 이미 Servlet 인터페이스
        추상 메소드들을 오버라이딩했기 때문이다.
        추가로, java.io.Serialize 인터페이스를 구현하기에
        serialVersionUID 변수 값을 설정해야 한다.

    - HttpServlet
      - 추상클래스이며, GenericServlet 추상클래스를 상속 받는다.
      - doGet(), doPost(), doHead(), doPut() 등을 호출하게 프로그램 되어 있다.
      - HTTP 프로토콜을 다루려면, GenericServlet이 아닌 HttpServlet을 상속 받아
        Servlet 클래스를 생성한다.

    - Servlet 구동 과정
      - 1. 웹 브라우저가 Servlet 실행을 요청한다.
      - 2. Servlet Container는 해당 URL의 Servlet 객체를 찾는다.
      - 3. Servlet 객체가 생성되지 않았다면,
           Servlet 클래스에 대해 인스턴스를 생성하고, 생성자를 호출한다.
           init()을 호출한다 -> service()를 호출한다.

           Servlet 객체가 생성되어 있다면,
           service()를 호출한다.
      - 4. 웹 애플리케이션이 종료되면, destroy()를 호출한다.

      - 특별한 옵션을 주지 않는 이상, 클라이언트가 최초로
        Servlet 실행을 요청하면, Servlet 인스턴스를 생성한다.
        그리고 시작과 끝을 알리는 init(), destroy()는 오직 한 번만 호출된다.
        반면에, service()는 클라이언트가 요청할 때마다 호출된다.


    - Servlet 사용 방법
      - Servlet 클래스를 생성 후, Servlet Container 에 등록해야만
        사용이 가능하다.

    - 등록 방법 1.
      - 웹 애플리케이션 배치 파일(web.xml)에 Servlet 정보를 등록한다.
      - WEB-INF/web.xml DD File : Deployment Descriptor File
      ```
      <servlet>
        <servlet-name>서블릿별명</servlet-name>
        <servlet-class>서블릿 클래스의 전체 이름(패키지명 포함)</servlet-class>
      </servlet>

      <servlet-mapping>
        <servlet-name>서블릿별명</servlet-name>
        <url-pattern>클라이언트에서 요청할 때 사용할 URL(IT MNUST START AS /)</url-pattern>
      </servlet-mapping>
      ```

    - 등록 방법 2.
      - Servlet 클래스 선언부에 @WebServlet 애노테이션을 추가한다.
      `@WebServlet("URL")` 등등


    - Filter 사용 방법
      - javax.servlet.Filter 인터페이스 규칙에 따라 작성한다.
      - Filter의 용도
        - Servlet 을 실행하기 전/후에 필요한 작업을 수행한다.
        - 실행 전 Ex
          - 웹브라우저로부터 받은 암호화된 파라미터 값을 Servlet에
            전달하기 전에 암호 해제 한다. 혹은 압축된 데이터를
            전달하기 전에 압축 해제한다.
          - 클라이언트가 Servlet 을 실행할 권한이 있는지 검사한다.
            Ex) 로그인 사용자인지 검사
        
        - 실행 후 Ex
          - 클라이언트로 보낼 데이터를 압축하거나 암호화한다.
        
        - 구현 코드
        ```
        public class Filter implements Filter {
          @Override
          public void init(FilterConfig filterConfig) throws ServletException {}
          // 웹 애플리케이션을 실행할 때 자동으로 Filter 객체를 생성하고,
          // 자동으로 init 메소드를 호출한다. Filter가 사용할 자원을 이 메소드에서 준비한다.

          @Override
          public void destroy() {}
          // 종료할 때 호출되는 메소드

          @Override
          public void doFilter(ServletRequest req, ServletResponse res, FilterChain chin) throws ServletException, IOException {}
          // 요청이 들어올 때마다 호출되는 메소드이다.
          // 단, 지정된 URL의 요청에만 호출된다.
          // Servlet이 실행되기 전에 먼저 실행되고, Servlet을 실행한 후에
          // Filter로 리턴한다. 그리고 FilterChain에 의해 다음 Filter를
          // 실행한다. 만일, 다음 Filter 가 없으면 요청한 Servlet 클래스의
          // service() 메소드를 호출한다.
        }
        ```
        
        - 등록 방법 1.
        ```
        <filter>
          <filter-name>f1</filter-name>
          <filter-class>com.test.web.Filter</filter-class>
        </filter>

        <filter-mapping>
          <filter-name>f1</filter-name>
          <url-pattern>/test/*</url-pattern>
        </filter-mapping>
        ```

        - 등록 방법 2.
          - 애노테이션 설정
          `@WebFilter(URL)`


    - Listener
      - Servlet Container 또는 Servlet, Session 등의 객체 Status가 
        변경될 때 보고 받는 형태다.
        즉, Observer 패턴이 적용된다.
      
      - 용도 : Servlet Container, Session 등 특별한 상태일 때 
               필요한 작업을 수행한다.
      
      - `ServletContextListener`
        - Servlet Container를 시작하거나 종료할 때,
          보고 받고 싶으면 이 인터페이스를 구현한다.
      
      - `ServletRequestListener`
        - 요청과 응답을 보고 받고 싶으면, 이 인터페이스를 구현한다.
          Ex) 로그 기록 남기기
      
      - `HttpSessionListener`
        - Sessio이 생성되거나 종료될 때 보도 싶으면, 이 인터페이스를 구현한다.

      - 등록 방법 1.
      ```
      <listener>
        <listener-class>com.test.web.Listener</listener-class>
      </listener>
      ```
      - 등록 방법 2.
        `@WebListener` 애노테이션 추가


  - Cloud Service
    - 서버를 임대하는 것. Ex) 파일 서버, DB 서버 등

  - Monolithic / MicroService
    - Monolithic 서비스의 경우, 한 서비스에 전부 배치한다.
      Project 내부에 회원관리, 게시글관리, 프로젝트관리 등
    
    - MicroService는 여러 서버에 각각 배치한다.
      회원관리 서버, 게시글관리 서버 등등
      장점은 기능 별로 서비스 제어가 가능하다.
      단점은 세션 관리 및 서비스간 연계가 어렵다.
    
  - Function Service
    - 메소드 한 개를 서비스한다. Ex) 아마존 람다 서비스

  - 분산 컴포넌트 기술
    - 분산 = 타 컴퓨터 및 프로세스
    - 컴포넌트 = 변수, 메소드, 인스턴스 등
  
    - RPC = Remote Procedure Call
    - RMI = 객체지향 분산 컴포넌트 기술
    - ORB = Object Request Broker 브릿지 역할
    - CORBA = Common ORB Architecture 프로그래밍 언어에 비종속
    - EJB = Enterprise Java (RMI의 상위) / ORB를 도구로 자동 생성
      따라서, Java Component 부분만 개발자가 작성
    - Web Service = CORBA 처럼 프로그래밍 언어에 비종속적이며,
      SOAP(Simple Object Access Protocl)을 사용하여 통신한다.
      하지만, 여전히 stub이 필요하다.
    - RESTful API = URL을 사용하여 요청하고, XML 혹은 JSON으로 응답한다.
      JS의 경우 AJAX, JAVA/C/C++/PYTHON 등은 HTTP 요청 API 기술을 사용한다.
      특정 원격 객체를 사용하기 위해, 더이상 객체 전용 STUB API를
      다운 받아서 사용할 필요가 없다.


# 2021-05-01
  - 200번대
    - 200 OK = 이상 없이 요청 성공.

    - 201 Created = 요청으로 서버에서 리소스 생성.

    - 202 Accepted = 요청을 접수됬지만, 처리가 완료되지 않을 때.
                     배치 처리 같은 곳에서 주로 사용된다.

    - 204 No Content = 서버가 요청을 성공적으로 수행했지만, 응답할 데이터가
                       없을 때.
                       Ex) 저장할 때, 팝업창만 띄울 뿐, 화면에 새로 렌더링할 게 없다.

  - 300번대
    - 리다이렉션 개념이다.
      웹 브라우저는 3xx 응답의 결과에 Location 헤더가 있으면, Location 위치로
      자동 이동한다.
    
    - 흐름 : 클라이언트가 요청을 하면, 서버는 받은 요청이 더이상 사용하지
             않는 경로라면, 새 경로를 클라이언트에게 응답하고, 클라이언트는
             자동 리다이렉트를 해서 서버에 재요청하고, 서버는 올바르게 응답한다.
    
    - 영구 리다이렉션 = 특정 리소스의 URI가 영구적으로 이동.
      Ex) /members -> /users
    
    - 301 = 리다이렉트시, 요청 메소드가 GET으로 변한다.

    - 308 = 리다이렉트시, 본문 유지. 301과 다르게 POST와 메시지 Body를 유지한다.

    - 302 Found = 리다이렉트시, 요청 메소드가 GET으로 변하고, 본문이 제거될 수 있다.
                  301과 비슷한 상황이다.

    - 307 Temporary Redirect = 리다이렉트시, 요청 메소드와 본문 유지.
                               (요청 메소드를 변경하면 안 된다.)

    - 302 See Other = 302와 기능은 같다.
                      리다이렉트시, 요청 메소드가 GET으로 변경된다.

    - 304 Not Modified = 캐시를 목적으로 사용한다.
                         클라이언트에게 리소스가 수정되지 않았음을 알린다.
                         따라서, 클라이언트는 로컬PC에 저장된 캐시를 재사용한다.
                         (캐시로 리다이렉트)
                         304 응답은 응답에 메시지 Body를 포함하면 안 된다.
                         (로컬 캐시를 사용해야 되기 때문)

    - 일시 리다이렉션 = 일시적인 변경.
      Ex) 주문 완료 후, 주문 내역 화면으로 이동.
    
    - PRG : Post/Redirect/Get

    - PRG 사용 전 상황
      - POST로 상품 주문 요청 메시지 전달 -> 서버는 DB에 주문데이터를 저장하고,
        주문 완료 메시지 응답을 한다. 이후에, 새로고침을 누르면, 재 주문 요청을 보낸다. 그래서 중복 주문 처리가 된다.
        따라서, 이와 같은 상황을 방지하기 위해서는
        POST로 주문 후, 주문 결과 화면을 GET 메소드로 리다이렉트 해야 한다.

    - PRG 사용 후 상황
      - POST로 상품 주문 요청 메시지 전달 -> 
        서버는 위치와 함께 302(요청 메소드 -> GET으로 변경)로 응답한다. 클라이언트는 리다이렉트되면서 GET으로 받은 위치 정보와 함께 요청한다.
        서버는 주문 정보를 조회 후, 화면을 만들고 클라이언트에 200으로 응답한다.
  
  - 400번대
    - 요청 구문, 메시지 등등 오류가 발생할 때.
    - 클라이언트는 요청 내용을 재검토하고 재요청을 해야 한다.
      Ex) 요청 파라미터가 잘못됬거나, API 스펙이 맞지 않을 때 발생.
    
    - 401 = 클라이언트가 해당 리소스에 대한 인증이 필요함.
            인증이 되지 않을 때 발생한다.
    
    - 403 Forbidden = 서버가 요청을 이해했지만, 승인을 거부한다.
      인증 자격 증명은 있지만, 접근 권한이 없을 때 발생한다.
      Ex) 네이버 카페는 회원 등급이 존재한다.
          그래서 게시판에 읽기 및 쓰기 권한을 등급에 따라 줄 수 있으며,
          상위 등급만 읽기/쓰기 가능한 게시판에는 하위 등급이 접근할 수 없다.
    
    - 404 Not Found = 요청 리소스를 찾을 수 없을 때 발생.
    
  - 500번대
    - 서버 문제로 오류가 발생할 때.

    - 503 Service Unavailable
      - 서비스 이용 불가.
      - 서버가 일시적인 과부하 또는 예정된 작업으로 요청 처리 불가할 때.

  - HTTP 헤더
    - 메시지 본문(Message Body)을 통해, 표현 데이터를 전달한다.
    - 메시지 본문 = 페이로드
      표현은 요칭이나 응답에서 전달할 실제 데이터를 의미한다.
    
    - 표현
      - Content-Type = 표헌 데이터의 형식
        미디어 타입, 문자 인코딩
        Ex) `application/json` = json 타입의 데이터가 있다.
            `text/html;charset=UTF-8` = html 코드를 UTF-8로 인코딩한다.
      
      - Content-Language = 표헌 데이터의 자연 언어
        Ex) Content-Language: ko / en
      
      - Content-Length = 표헌 데이터의 길이 (바이트 단위)
    
    - 콘텐츠 협상
      - Accept-Language 적용 전
        - 클라이언트가 한국어 브라우저 사용 시,
          애플 공홈에 접속하면 영어로 작성된 페이지를 볼 수 있다.
      
      - Accept-Language 적용 후
        - 애플 공홈에 접속하면 한국어로 작성된 페이지를 볼 수 있다.
      
      - 복잡한 예시
        - 한국어를 지원하지 않는 페이지라면, 우선순위로 렌더링한다.
      
      - 협상과 우선순위
        - Quality Values (q) 값 사용
        - 0~1, 클수록 높은 우선순위다.
          생략하면 디폴트가 1이다. 즉, 가장 높은 순위.
        
        - `Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8`
          한국어가 영어보다 0.1 더 높다. 따라서 우선순위는
          `1.` 한국어 `2.` 영어 이다.
      
      - 협상과 우선순위 2
        - 구체적인 것을 우선시한다.
        `Accept: text/*, text/plain, text/plain;format=flowed, */*`
        `1.` text/plain;format=flowed
        `2.` text/plain
        `3.` text/*
        `4.` */*
      
      - 협상과 우선순위 3
        - 구체적인 것을 기준으로 미디어 타입을 맞춘다.
      
  - 전송 방식의 4가지
    - 단순 전송
      - Length 만큼 전송한다.
        따라서, `Content-Length`를 알고 있을 때 가능하다.
    
    - 압축 전송
      - `Content-Encoding: gzip`으로 하고, 압축해서 전송한다.
    
    - 분할 전송
      - `Transfer-Encoding: chucked`(덩어리)
        `Content-Length`를 적을 수 없다.
        왜냐하면, 가늠할 수 업시 때문이다.
        대용량의 경우, 분할 전송 방식을 사용하면
        오는대로 받을 수 있다.
        따라서, 페이지를 로딩할 때 차례차례 나오는 이유가
        분할 전송 방식을 사용하기 때문이다.
    
    - 범위 전송
      - 중간에 끊기면, 끊어진 부분부터 전송이 가능하다.
  
  - 일반 정보
    - From
      - 유저 에이전트의 이메일 정보
        - 일반적으로 잘 사용되지 않는다.
        - 검색 엔진 같은 곳에서 주로 사용하고,
          요청에서 사용한다.
    
    - Referer
      - 이전 웹 페이지 주소
        - 현재 요청된 페이지의 웹 페이지 주소를 의미한다.
        - Ex) 구글에서 hello를 검색하고, 어떤 사이트에 접속하면,
          Referer는 google.com이 된다. 그리고 그 접속한 사이트에서
          다시 한 번 타 사이트를 접속하면, 바로 이전의 사이트가 Referer가 된다.
    
    - User-Agent
      - 유저 에이전트 애플리케이션 정보
        - 클라이언트의 애플리케이션 정보(웹 브라우저 정보 등등)
        - 통계 정보
        - 어떤 종류의 브라우저에서 장애가 발생하는지 파악이 가능하며,
          요청에서 사용한다.
    
    - Server
      - 요청을 처리하는 ORIGIN 서버의 소프트웨어 정보
        - HTTP 요청을 보내면, 여러 Proxy 서버를 경유하게 되며,
          응답에서 사용한다.
    
    - Date
      - 메시지가 발생한 날짜와 시간
        - 응답에서 사용한다.
    
  - 특별한 정보
    - Host
      - 요청한 호스트 정보(도메인)
        - 요청에서 사용하며, 필수다.
        - 하나의 서버가 여러 도메인을 처리해야 할 때
        - 하나의 IP주소에 여러 도메인이 적용되어 있을 때
        - Ex) 서버 안에 여러 애플리케이션을 실제 다른 도메인으로 구동할 때.
          그래서 클라이언트는 접속할 때 어느 애플리케이션에 접속할지 모른다.
          따라서, 헤더에 Host: xxx.com 을 명시한다.

    - Location
      - 페이지 리다이렉션
        - 웹 브라우저는 3xx 응답의 결과에 Location 헤더가 있으면,
          Location 위치로 자동 이동한다(리다이렉트)

    - Allow
      - 허용 가능한 HTTP 메소드
        - 405 (Method Not Allowed) 에서 응답에 포함해야 한다.
        - Allow: GET, HEAD, PUT

    - Retry-After
      - 유저 에이전트가 다음 요청을 하기까지 기다려야 하는 시간
        - 503 : 서비스가 언제까지 불능인지 알려줄 수 있다.
        - Retry-After: 날짜 표기
        - Retry-After: 초단위 표기

  - 인증 헤더
    - Authorization
      - 클라이언트 인증 정보를 서버에 전달
        - Authorization: Basic xxxxxx
      
      - WWW-Authenticate
        - 리소스 접근시, 필요한 인증 방법 정의
        - 401 Unauthorized 응답과 함께 사용한다.

  - 쿠키
    - Set-Cookie
      - 서버에서 클라이언트로 쿠키 전달(응답)
    - Cookie
      - 클라이언트가 서버에서 받은 쿠키를 저장하고, HTTP 요청시 서버로 전달한다.
    
    - 쿠키 미사용
      - 클라이언트는 웹 브라우저를 통해, 서버에 로그인 정보를 요청한다.
        서버는 200 OK로 응답하면서 로그인에 성공한다.
        하지만, 재접속시, 서버는 클라이언트가 로그인 상태라는 걸 인식하지 못 한다.
        왜냐하면, Stateless 방식을 사용하기 때문에, 이전 요청을 기억하지 못 하기
        때문이다.
      
      - 대안책
        - 모든 요청에 사용자 정보 포함
          - 이 방법은 모든 링크에 사용자 정보를 포함해야 하기 때문에
            비효율적이며 또한, 정보가 노출되기 때문에 보안에 치명적이다.
      
      - 해결책
        - 쿠키 사용
          - 웹 브라우저가 POST(파라미터를 Body로 보내기 때문에 은닉)로 
            로그인을 하고, 서버는 Set-Cookie(유저 정보)를 포함해서
            클라이언트에 응답한다.
            웹 브라우저 내부에는 쿠키 저장소가 존재한다.
            그래서 저장소에 서버의 응답을 저장하고, 로그인 이후에
            클라이언트가 재접속할 때 쿠키 저장소에서 유저 정보를 불러와서
            GET 으로 로그인한다.
        
        - 세션 방식
          - 로그인에 성공하면, 서버는 세션 ID를 만들고,
            클라이언트에게 세션 ID를 부여한다.
            그다음, 클라이언트가 재접속할 때, 세션 ID를 서버의 세션 ID와
            비교하여 검증한다.
        
    - 쿠키 정보는 항상 서버에 전송된다.
      - 네트워크 트래픽 추가 유발
      - 최소한의 정보만 사용(세션 ID, 인증 토큰)
    
    - 쿠키 생명 주기
      - `Set-Cookie: expires= 날짜(GMT 기준)`
      - 날짜가 만료되면, 쿠키는 자동으로 삭제된다.
      
      - `Set-Cookie: max-age=4000 (4000초)`
      - 0이나 음수를 지정하면 쿠키가 삭제된다.
    
    - 세션 쿠키 : 만료 날짜를 생략하면, 브라우저 종료시 까지만 유지된다.
    - 영속 쿠키 : 만료 날짜를 입력하면, 해당 날짜까지 유지된다.

    - 쿠키 - 도메인
      - Ex) domain=example.org

      - 명시 : 명시한 문서 기준 도메인 + 서브 도메인 포함
      - domain=example.org를 지정해서 쿠키 생성
        - example.org는 물론이고, dev.example.org도 쿠키 접근
      
      - 생략 : 현재 문서 기준 도메인만 적용
        - example.org에서 쿠키를 생성하고, domain 지정을 생략
        - example.org 에서만 쿠키 접근
        - dev.example.org는 쿠키 미접근
    
    - 쿠키 - 경로
      - Ex) path=/home
      - 이 경로를 포함한 하위 경로 페이지만 쿠키 접근
      - 일반적으로 path=/ 루트로 지정한다.
        path=/home 지정시,
        /home - /home/test1 - /home/test1/test2 이 가능하며,
        /home 이 아닌, /house 는 불가능하다.
    
    - 쿠키 - 보안
      - Secure
        - 쿠키는 http, https를 구분하지 않고 전송한다.
          Secure를 적용하면, https인 경우에만 전송한다.
      
      - httpOnly
        - XSS(웹 사이트에 악성 스크립트 삽입) 공격을 방지한다.
        - 자바스크립트에서 접근이 불가하다.
        - HTTP 전송에만 사용이 가능하다.
      
      - SameSite
        - XSRF(사이트 간 위조 요청) 공격을 방지한다.
        - 요청 도메인과 쿠키에 설정된 도메인이 같은 경우에만 쿠키를 전송한다.
      
  - 캐시 기본 동작
    - 캐시가 없을 때, 데이터가 변경되지 않아도 계속 네트워크를 통해서 다운
      받아야 한다. 그래서 브라우저 로딩 속도가 느려진다.
    
    - 캐시 적용
      - HTTP 헤더에 `cache-control: max-age=60`를 선언한다.
        즉, 브라우저 캐시에 응답 결과를 60초 동안 캐시 저장소에 보관한다.
        그래서 2번째 요청할 땐, 캐시에서 꺼내서 바로 사용하기 때문에
        로딩 속도가 더 빠르다.
        만일, 캐시 시간이 초과되면, 새로 캐시를 생성한다.
    
    - 캐시 유효 시간이 초과해서 서버에 다시 요청하면, 두 가지 상황이 나타난다.
      - `1.` 서버에서 기존 데이터를 변경
      - `2.` 데이터 유지
    
    - 저 상황들을 해결하기 위해, 검증 헤더를 추가한다.
      그래서 데이터가 마지막에 수정된 시간을 헤더에 추가한다.
    - `Last-Modified: 시간표기(UTC)`

    - 응답 결과를 캐시에 저장한다.
      2번째 요청할 때 클라이언트 요청 헤더는
      `if-modified-since: 시간표기` 를 추가해서 요청한다.
      서버는 데이터 최종 수정일과 요청 헤더의 `if-modified-since` 시간을
      비교하여 판단한다.
      수정이 안 됬다면,
      `HTTP/1.1 304 Not Modified` 라고 응답한다.
      여기서 HTTP Body가 없다.
      이유는 수정된 부분이 없기 때문이다.
      따라서 응답 헤더만 전송된다.

    - 검증 헤더와 조건부 요청
      - 캐시 유효 시간이 초과해도, 서버의 데이터가 갱신되지 않으면,
        304 Not Modified + 헤더 메타 정보만 응답(Body가 없음)
      
      - 클라이언트는 서버가 보낸 응답 헤더 정보로 캐시의 메타 정보를 갱신한다.
        클라이언트는 캐시에 저장되어 있는 데이터를 재활용한다.
        결과적으로 네트워크 다운로드가 발생하지만, 용량이 적은 헤더 정보만
        다운 받는다. 따라서, 매우 효율적이다.
        ■ 개발자 모드에서 색이 진한 부분은 캐시 사용을 의미한다.
      
      - 검증 헤더
        - 캐시 데이터와 서버 데이터가 같은지 검증하는 데이터
        - Last-Modified, ETag(Entity Tag)
      
      - 조건부 요청 헤더
        - 검증 헤더로 조건에 따른 분기
        - if-Modified-Since: Last Modified 사용
        - if-None-Match: ETag 사용
        - 조건이 만족하면, 200 OK
        - 조건이 만족하지 않으면, 304 Not Modified
      
        - if-Modified-Since = 이후에 데이터가 수정되었으면...
        - 데이터 미변경 - 304 Not Modified = 전송 용량(헤더)
        - 데이터 변경 - 200 OK = 전송 용량(헤더 + 바디)

        - Last-Modified 와 if-Modified-Since의 단점
          - 1초 미만(0.x초) 단위로 캐시 조정이 불가능하다.
          - 날짜 기반의 로직을 사용한다. Ex) GMT(협정 세계시)
          - 데이터를 수정해서 날짜가 다르지만,
            같은 데이터를 수정해서 데이터 결과가 같은 경우
          - 서버에서 별도의 캐시 로직을 관리하고 싶은 경우
            Ex) 스페이스나 주석처럼 크게 영향이 없는 변경에서
            캐시를 유지하고 싶을 때

        - ETag 와 if-None-Match
          - ETag(Entity Tag)
            - 캐시용 데이터에 임의의 고유한 버전 이름을 달아둔다.
              Ex) `Etag: "v1.0", ETag: "test1234"`
            - 데이터가 변경되면, 이 이름을 바꿔서 변경함(Hash 재생성)
              Ex) `ETag: "aa" -> ETag: "bb"`
              정말 단순하게 ETag만 보내서 같으면 유지, 다르면 다시 받기
          ```
          HTTP/1.1 200 OK
          Content-Type: image/jpeg
          cache-control: max-age=60
          ETag: "asdf"
          Content-Length: 1234

          Body values
          ```
          
          브라우저 캐시에 ETag 값을 저장한다.
          시간 초과 후, 재요청할 때 if-None-Match: "asdf"를 포함한 헤더를 보낸다.
          만일, 서버의 ETag 값과 일치하면, 304 Not Modified 응답.
          
          캐시 제어 로직은 서버에서 완전히 관리한다.
          
      - 캐시 제어 헤더
        - Cache-Control : 캐시 제어
        - Pragma : 캐시 제어(하위 호환)
        - Expires : 캐시 유효 기간(하위 호환)

        - Cache-Control: max-age
          - 캐시 유효 시간, 초 단위
        
        - Cache-Control: no-cache
          - 데이터는 캐시해도 되지만, 항상 Original 서버에 검증하고 사용한다.
            중간에 캐시 서버, 프록시 서버 등 존재한다.
        
        - Cache-Control: no-store
          - 데이터에 민감한 정보가 있으므로, 저장하지 않는다.
          - 메모리에서 사용하고 최대한 빨리 삭제한다.
      
      - 프록시 캐시
        - 한국에 위치한 웹 브라우저가 미국에 있는 Origin 서버의 데이터를
          요청 및 응답할 때 오래 걸린다. 그래서 프록시 캐시 서버를 한국에 두고
          접속한다.

        - Cache-Control: public
          - 응답이 public 캐시에 저장되어도 된다.
        
        - Cache-Control: private
          - 응답이 해당 사용자만을 위한 것이다.
            private 캐시에 저장해야 함(default)
        
        - Cache-Control: s-maxage
          - 프록시 캐시에만 적용되는 max-age
        
        - Age: 60 (HTTP 헤더)
          - Origin 서버에서 응답 후, 프록시 캐시 내에 머문 시간
        
        - Cache-Control: must-revalidate
          - 캐시 만료 후, 최초 조회시 Origin 서버에 검증해야 한다.
          - Origin 서버에 접근 실패시, 반드시 오류가 발생해야 한다.
            504(Gateway Timeout)
          - must-revalidate는 캐시 유효 시간이라면, 캐시를 사용한다.
        
        - no-cache 기본 동작
          - 웹 브라우저 -> 프록시 캐시[캐시 서버 요청(no-cache + ETag)]
            -> Origin 서버[원 서버 요청(no-cache + ETag)] ->
            응답 304 Not Modified(원서버 -> 프록시) ->
            응답 304 Not Modified(프록시 -> 웹 브라우저) ->
            웹 브라우저는 브라우저 캐시를 통해 캐시 데이터를 사용ㄴ

        - must-revalidate 동작
          - 프록시 캐시가 Origin 서버에 접근할 수 없는 경우,
            항상 오류가 발생해야 하므로 504 Gateway Timeout 으로 응답한다.
         

# 2021-05-02
  - 객체 지향 설계의 5가지 원칙 SOLID
    - SRP (Single Responsibility Principle) 단일 책임 원칙
      - 한 클래스는 하나의 책임만 가져야 한다.
        한 클래스를 변경하는데, 여러 클래스도 다 변경해야 한다면,
        단일 원칙이 깨진다.
        Ex) UI 변경, 객체의 생성과 사용을 분리

    - OCP (Open/Closed Principal) 개방-폐쇄 원칙
      - 확장은 열려있고, 변경은 닫혀있다.
        Ex) 인터페이스를 구현한 새로운 클래스를 만들어서 새로운 기능을 구현

    - LSP (Liskov Substitution Principle) 리스코프 치환 원칙
      - 프로그램의 객체는 프로그램의 정확성을 깨뜨리지 않으면서
        하위 타입의 인스턴스로 바꿀 수 있어야 한다.
      - 다형성에서 하위 클래스는 인터페이스 규칙을 다 지켜야 한다.
        Ex) 자동차의 엑셀 기능은 원칙적으로 앞으로 가게 설계되어 있다.
        하지만, 자동차의 인터페이스를 구현하는 클래스가 엑셀 메소드를
        오버라이딩하여, 뒤로 가게 설계하면 그건 리스코프 치환 원칙을 깨는 것이다.

    - ISP (Interface Segregation Principle) 인터페이스 분리 원칙
      - 특정 클라이언트를 위한 인터페이스 여러 개가 범용 인터페이스 보다 낫다.
        Ex) 하나의 인터페이스에 수많은 기능을 추가하면 복잡하다.
        따라서, 여러 인터페이스로 나누어서 하면 코드를 수정해야 할 때
        관리가 더 쉬워진다. 예를 들면, 자동차 인터페이스가 운전 인터페이스와
        정비 인터페이스로 분리되면, 정비의 기능을 변경할 때 운전에 관련된
        클래스를 변경할 필요가 없다.

    - DIP (Dependency Inservion Principle) 의존관계 역전 원칙
      - 구현 클래스에 의존하지 말고, 인터페이스에 의존해야 한다.
        Ex) MemberService 클래스가 있다고 가정하자.
        의존 객체인 MemberRepository 를 사용하지만,
        MemberRepository를 구현한 클래스인 MemoryMemberRepository로
        인스턴스를 생성한다. 그러면 인터페이스와 구현 클래스를 동시에 의존하므로,
        의존관계 역전 원칙이 깨진다.


# 2021-05-03
  - URI 와 URL Encoding
    - URI 작성 규칙
      - ABNF(문법을 만들 때 사용하는 규칙)를 사용하여 URI 문법을 정의
        그래서 ABNF 표기법에 허용되는 문자만 사용할 수 있다.
        따라서 URI는 US-ASCII 문자로만 표현해야 한다.
        그 외 문자는 퍼센트 인코딩(= URL Encoding)으로 표현해야 한다.

      - 퍼센트 인코딩
        - 예약어를 일반 데이터로 작성할 때 URL Encoding을 한다.
          Ex) 공백 -> %20, % -> %25, + -> %2B, : -> 3A, ? -> %3F

      - URL 인코딩(RFC 3986)
        - URI 표기법으로 사용할 수 없는 문자를 %기호를 사용하여
          문자 코드값으로 표현하는 것
  
  - 관심사의 분리
    - 객체를 생성하고, 연결하는 역할과 실행하는 역할이
      명확히 분리된다.

  - AppConfig 역할
    - 생성자를 통해, 어떤 구현 객체를 주입할지는 외부
      즉, AppConfig에서 결정한다.
      그래서 서비스 클래스는 실행 코드에만 집중하면 된다.

    - AppConfig 리팩토링
      - 인스턴스 생성 클래스를 따로 분리해서 생성한다.
      ```
      return new MemberServiceImpl(new MemberRepository());
      위 문을 아래로 분리한다.
      return new MemberServiceImpl(memberRepository());
      
      private MemberRepository memberRepository() {
        return new MemberRepository();
      }
      ```
      
      바로 인스턴스를 생성하지 않고, 메소드를 실행시켜 생성한다.
      장점은 같은 인스턴스를 생성하는 메소드가 여러 개일 때,
      단 하나의 메소드로 여러 곳에서 사용이 가능해서 유지보수에 용이하다.
      싱클톤 패턴을 적용시킨 것이다.
      

# 2021-05-04
  - IoC (제어의 역전)
    - 프로그램의 제어 흐름을 외부에서 관리하는 것을 의미한다.
      예를 들면, 
  
  - DI (의존관계 주입)
    - 정적인 클래스 의존관계
      - 클래스가 사용하는 import 코드만 보고, 의존관계를
        쉽게 판단할 수 있다.
        그래서 앱을 실행하지 않아도 분석이 가능하다.
    
    - 동적인 클래스 의존관계
      - 애플리케이션 실행 시점에 실제 생성된 객체 인스턴스의 참조가
        연결된 의존 관계다.
  
  - AppConfig 처럼 객체를 생성하고, 관리하면서 의존관계를 연결해
    주는 것을 IoC 컨테이너 혹은 DI 컨테이너 라고 한다.
  
  - @Configuration = 설정 정보가 담긴 클래스
    AppConfig 클래스에 선언을 한다.
  
  - 스프링 컨테이너
    - ApplicationContext 를 스프링 컨테이너 라고 한다.
      기존에는 개발자가 AppConfig를 사용해서 직접 객체를
      생성하고 DI를 했다.

      스프링 컨테이너는 @Configuration 이 붙은 AppConfig 를
      설정 정보로 사용한다. 그리고 @Bean 이 붙은 메소드를 모두
      호출해서 반환된 객체를 스프링 컨테이너에 등록한다.
      
      이렇게 스프링 컨테이너에 등록된 객체를 스프링 빈 이라고 한다.

      스프링 빈은 @Bean이 붙은 메소드명을 스프링 빈의 이름으로 사용한다.
      ```
      @Bean
      public MemberService memberSerivce() {}
      ```
      memberService 메소드 명이 스프링 빈의 이름이 된다.
      
      원래는 개발자가 필요한 객체를 AppConfig 를 사용해서
      직접 조회했지만, 스프링 컨테이너를 사용하면 스프링 빈을
      찾아서 사용해야 한다.
      스프링 빈은 applicationContext.getBean() 메소드로 호출할 수 있다.
      
      ```
      ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);

      applicationContext.getBean("스프링빈 이름", 클래스명.class);
      applicationContext.getBean("memberService", MemberService.class);


# 2021-05-05
  - 스프링 빈 저장소
    - 빈 저장소 안에는 빈 이름과 빈 객체가 있다.
    - key = 빈 이름(memberService) / value = 빈 객체(리턴값);
  
  - 스프링 컨테이너 생성 과정
    - ApplicationContext 는 스프링 컨테이너며 인터페이스이다.
      스프링 컨테이너는 애노테이션 기반의 자바 설정 클래스로 만들 수 있다.
    
    - `1.` 
    ```
    ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
    ```
    - `2.` 
    ```
    @Bean
    public MemberService memberService() {
      return new MemberServiceImpl(memberRepository());
    }
    ```
    빈 이름 : `memberSerivce` / 빈 객체 : `memberSerivceImpl`

  - * 빈 이름은 항상 다른 이름을 부여 해야 한다.
      왜냐하면, 같은 경우 다른 빈이 무시되거나,
      기존 빈을 덮어버릴 수 있기 때문이다. 

  - 스프링 빈 조회 방법
    - `ac.getBean(빈 이름, 타입)`
    - `ac.getBean(타입)`
    - 둘 다 가능하다.

    - 타입으로 조회시, 같은 타입의 스프링 빈이 둘 이상이면 오류가 발생한다.
      getBeansOfType() 을 사용하면, 해당 타입의 모든 빈을 조회할 수 있다.

    - 부모 타입으로 조회하면, 자식 타입도 함께 조회한다.
      그래서 최상위 객체인 Object 타입으로 조회하면,
      모든 스프링 빈을 조회한다.
    
    - BeanFactory
      - 스프링 컨테이너의 최상위 인터페이스이다.
        스프링 빈을 관리하고 조회하는 역할을 담당하고,
        getBean() 메소드를 제공한다.

    - ApplicationContext
      - BeanFactory 인터페이스를 상속 받아서 구현한다.
        스프링 컨테이너이며, 빈 관리기능 + 편리한 부가 기능을 제공한다.
        부가 기능을 예로 들자면,
        메시지 소스를 활용한 국제화 기능이 있다.
        한국에서 서버에 접속하면, 한국어로 출력을 해주고,
        미국에서 접속하면 영어로 출력해준다.
    
  - 환경변수
    - 로컬 = PC에서 개발하는 환경
    - 개발 = 테스트 서버에 올려서 테스트하는 환경
    - 운영 = 실제 프로덕션에 나가는 환경 (스테이지 = 운영)


# 2021-05-10
  - Mybatis 복습
    - association의 역할
      - 도메인에 있는 타 객체를 사용할 때 쓰인다.
        예를 들면, 게시판 작성을 할 때 작성자 이름이 필요한데
        여기서 도메인으로는 포함관계를 형성하여 가져와야 한다.
        따라서 코드는 아래의 형태로 이루어진다.
      
      ```
      private int no;
      private String title;
      private String content;
      private Date createdDate;
      private int viewCount;
      private int commentCount;
      private Member writer;
      ```

      그리고 Member의 writer는
      Mybatis Mapper에서 association으로 선언을 해야 사용이 가능하다.

      ```
      <association property="writer" javaType="member">
      <id column="writer_no" property="no"/>
      <result column="writer_name" property="name"/>
      </association>
      ```

      javaType은 도메인의 클래스명을 의미하고,
      property 는 임의로 지정할 수 있다.
      그리고 property는 인스턴스 필드의 네임이 오며,
      column 은 association property명을 붙이고, _를 사용해서
      property를 붙여서 사용한다.

      실제 사용할 때는
      ```
      insert into board(title, cont, writer) 
      values(#{title}, #{content}, #{writer.no})
      ```
      
      writer.no 가 writer_no 와 일치한다.


# 2021-05-11
  - Filter 추가
    - Filter 적용 방법 2가지
      - web.xml 에 코드 추가
      ```
      로딩 코드
      <filter>
        <filter-name>적용할 클래스명</filter-name>
        <filter-class>패키지명을 포함한 클래스명</filter-class>
        <init-param>
      <param-name>encoding</param-name>
      <param-value>UTF-8</param-value>
        </init-param>
      </filter>

      적용 코드
      <filter-mapping>
        <filter-name>적용된 클래스명</filter-name>
        <url-pattern>적용할 URL</url-pattern>
      </filter-mapping>

      구현 코드
        <filter>
        <filter-name>CharacterEncodingFilter</filter-name>
        <filter-class>com.pms.petopia.web.filter.CharacterEncodingFilter</filter-class>
        <init-param>
      <param-name>encoding</param-name>
      <param-value>UTF-8</param-value>
        </init-param>
      </filter>

      <filter-mapping>
        <filter-name>CharacterEncodingFilter</filter-name>
        <url-pattern>/*</url-pattern>
      </filter-mapping>
      ```

      - 클래스 선언부에 애노테이션 추가
      ```
      @WebFilter(value="/*")
      public class 클래스명 {}
      ```

  - Listener 추가
    ```
    <listener>
      <listener-class>패키지명을 포함한 클래스명</listener-class>
    </listener>
    ```

  - Listener, Filter, Servlet이 공동으로 사용할 초기화 파라미터 설정
    ```
    InputStream mybatisConfigStream = Resources.getResourceAsStream(
    servletContext.getInitParameter("mybatis-config"));
    위 코드는 아래 코드를 로딩한다.

    web.xml

    <context-param>
    <param-name>mybatis-config</param-name>
    <param-value>com/pms/test/conf/mybatis-config.xml</param-value>
    </context-param>
    ```


# 2021-05-12
  - git의 pull 실행 순서
    - 서버 Repository 에서 로컬 Repository에 fetch하고,
      로컬 Repository 는 작업 파일에 merge 한다.
  
  - HttpServlet 인터페이스의 service 메소드 역할 범위
    - 호출하는 메소드에 따라, doPost, doGet 등 모두 처리가 가능하다.
      클래스 파일을 보면, 전부 처리할 수 있게 코드가 구성되어 있다.
  
  - forwarding 방식
    - 클라이언트가 요청을 하면, 요청 받은 클래스는 타 클래스에 위임하고
      위임받은 클래스는 요청을 처리하고 클라이언트에게 응답한다.
  
  - include 방식
    - 클라이언트가 요청을 하면, 요청 받은 클래스는 타 클래스와 협업을 하고
      처리된 모든 내용을 클라이언트에게 한 번에 응답한다.


# 2021-06-07
  - Servlet Context 보관소의 개념
    - 크게 4가지로 나뉘며, 각 파트는 종료되는 시점이 각기 다르다.
    - ServletContext는 가장 바깥에 있으며, 애플리케이션이 시작되면 생성이 되고,
      종료될 때 유효기간이 만료된다.
      즉, 서버를 종료할 때 사라진다고 보면 된다.
    
    - HttpSession은 주로 로그인 정보나 요청하는 동안 공유할 정보를 담는다.
      그래서 로그인을 하면, 정보가 생성되고 로그아웃을 하면 생성된 정보는 사라진다.
      공유할 정보의 경우에는 사용 후, 반드시 제거를 해야 한다.
    
    - ServletRequest는 요청을 처리하는 동안 공유할 정보를 생성한다.
      그리고 응답을 하면 정보는 사라진다.
    
    - JspContext는 ServletRequest에서 JSP를 실행하면
      생성되고, forward나 include를 통해 JSP를 실행할 경우,
      Tag Handler를 포함하여 생성한다.
    
  - Spring WebMVC 아키텍처
    - 클라이언트가 요청을 하면, 프론트 컨트롤러인 DisPatcherServlet가 요청을 받고
      PageController를 실행한다. 요청 받은 PageController는 출력에 필요한 데이터를 가공해야 한다.
      따라서, DAO 및 Mybatis등을 사용하여 데이트베이스에 접근하고 가공하여 원하는 데이터를 리턴한다.
      그다음, 리턴받은 PageController는 작업 결과를 DispatcherServlet에게 리턴하고,
      DispatcherServlet은 ServletRequest 보관소에 작업 결과를 보관한다.
      그리고 View Component에게 출력을 위임하고, View Component는 ServletRequest 보관소에 저장된
      데이터를 사용해서 JSON, HTML, XML등 출력물을 생성한다.
      생성이 완료되면, DispatcherServlet에게 리턴하고 최종적으로 클라이언트가 요청한 출력 결과물을
      화면에 렌더링한다.

      ```
      Client Request -> DispatcherServlet -> PageController -> Service -> DAO -> DB
      -> Return to PageController in order -> Return to DispatcherServlet with Results of Processed Data
      -> Store Data in ServletRequest Archive -> Delegate Output Work to View Component
      -> Use Stored Data in ServletRequest Archive -> Create Output like JSON, XML, HTML etc.
      -> Return to DispatcherServlet -> Response to Client
      -> Render Requested Data By Client on Client's Screen
      ```


# 2021-06-08
  - 리눅스 기초
    - 하드웨어 -> 커널 -> 쉘 -> 응용 프로그램

    - GNU 프로젝트
      - 1984년 리처드 스톨먼이 시작한 프로젝트로
        모두가 공유할 수 있는 소프트웨어를 만드는 것을 목표로 함.

        1985년 스톨먼은 프로그램 복제, 변경 및 사용 제한을 없애고,
        자유 소프트웨어 재단(Free Software Foundation, FSF)을 설립함.

    
# 2021-06-09
  - 리눅스 2일차
    - Vmware Player는 개인이 사용 시, 무료로 사용 가능

    - 가상 머신은 가짜 OS
    - 가상 머신을 사용할 폴더는 따로 생성해야 한다.
    - 가상 하드디스크를 장착하면, 가상 머신은 진짜 하드디스크로 인식한다.

    - 호스트가 게스트에 메모리를 나누어주는 시점은 가상 머신을 부팅할 때
    - 가상 머신에는 여러 코어를 장착해도 성능에 큰 변화가 없다.
      따라서, 싱클 코어 사용을 권장한다.

    - 네트워크 NAT 방식 - 호스트 IP 공유를 위해 사용된다.
    - snapshot의 기능 - 지정한 시점으로 돌아가는 것

    - RAID (Redundant Array of Inexpensive Disks)
      - 여러 개의 디스크를 배열해서 속도와 안정성을 높인다.
    
    - Suspend 기능 - 일종의 절전 모드와 비슷함


# 2021-06-10
  - 리눅스 3일차
    - 터미널 명령어
    - Root 암호 변경
    - `sudo su - root` <- sudo는 super user do의 약자이다.
    - 암호 입력
    - `passwd`
    - 새 암호 입력 (재입력 포함)
    
    - AutomaticLogin을 Root로 변경하기
      - `gedit /etc/gdm3/custom.conf`
      - AutomaticLoginEnable = true <- 주석 제거
      - AutomaticLogin = user1 <- 주석 제거 후, user1 -> root 변경
      - AllowRoot = true 추가

    - 기타 설정
      - `gedit /etc/pam.d/gdm-password`
      - auth required pam 라인 주석 처리
      - `gedit /etc/pam.d/gdm-autologin`
      - auto required pam 라인 주석 처리
      - `gedit /root/.profile`
      - mesg n || true 라인 주석 처리
    
    - 파일명 수정
      - mv 기존파일명 바꿀파일명
        Ex) `mv resource.list resource.list.exe`
      
    - 다운로드 명령어
      - wget 웹주소
        Ex) `wget http://www.naver.com/download/file01.list`
    
    - 설정한 내용 적용 명령어
      - `apt-get update`

    - 삭제 명령어
      - `rm 경로 or 파일명`
      - `rm -rf /boot`
        위 명령어는 부팅에 필요한 부트 디렉토리를 강제적으로 날린다는 의미다.
        r은 recursive 즉, 재귀를 의미하며, f는 force 강제를 의미한다.
      

# 2021-06-11
  - 리눅스 4일차
    - `shutdown` 명령어
      - `shutdown -P +시간` = P는 PowerOff
        Ex) `shutdown -P +30` = 30분 후에 종료
      
      - `shutdown -r 시간` = r는 reboot
        Ex) `shutdown -r 23:00` = 오후 11시에 재부팅
      
      - `shutdown -c` = 예약된 shutdown 취소

      - `shutdown -k +시간` 
        현재 접속한 유저에게 +시간 후에 종료된다는 메시지 전송
        실제로 종료되지는 않는다.
        따라서, 시간 종료와 함께 메시지 전송을 하려면,
        ```
        shutdown -p +10
        shutdown -k +10
        ````
    
    - `init 수`
      - Linux RunLevel
        - 0 = Power Off
        - 1 = Rescue 시스템 복구 모드
        - 2 = 사용하지 않음
        - 3 = 텍스트 모드의 다중 사용자 모드
        - 4 = 사용하지 않음
        - 5 = 그래픽 모드의 다중 사용자 모드
        - 6 = Reboot
      - 일반적으로 Level 3 모드를 사용한다.
      
      - RunLevel 조회 명령어
        - `cd /lib/systemd/system`
        - `ls -l runlevel?.target`

    - 그래픽 모드에서 텍스트 모드로 전환
      - `ln -sf /lib/systemd/system/multi-user.target /lib/systemd/system/default.target`
      - 현재 모드를 multi-user.target 으로 전환한다.

      - 모드 전환 확인
        - `ls -l /lib/systemd/system/default.target`
      
    - vi 명령어
      - `:wq` = 저장 후, 종료
      - `:q!` = 저장하지 않고 종료

      - 파일 저장하고 바로 종료하기
        - `vi` -> 파일 작성 -> `:wq 파일명.확장자`
      
      - 특정 문자열로 치환하기
        - `:%s/기존/치환`
          Ex) `%s/test/done`

      - 첫 행으로 이동
        - ESC -> `gg`
      
      - 마지막 행으로 이동
        - `:$`

      - 현재 행 복사 후, 다음 행에 붙여넣기
        - ESC -> `yy` -> `p`


# 2021-06-12
  - 리눅스 5일차
    - umount 사용시, 경로 확인 필수
    - 현재 작업중인 디렉토리에 있거나, 하위 디렉토리에 있다면
      상위 디렉토리로 이동하고 삭제를 해야 한다.
    
    - ISO 파일 생성 명령어
      - `genisoimage -r -J -o ISO파일명 포함될디렉토리`
      - `genisoimage -r -J -o boot.iso /boot`
      - -r -J = 여덟 글자 이상의 파일 이름과 대/소문자 구분해서
        인식하기 위한 옵션이다.
      - -o는 출력할 파일을 위한 옵션이다.
    
    - 마운트 및 언마운트
      - `mount /경로`
        Ex) `mount /media/test` = media 하위 폴더인 test폴더에 마운트를 함
      
      - `umount /경로`
        Ex) `umount /media/test` = 마운트 해제
      
    - 기본 명령어
      - ls
        - `ls` = 현재 디렉토리의 파일 목록 출력
        - `ls /test1/test2` = test2 디렉토리의 파일 목록 출력
        - `ls -a` = 현재 디렉토리의 숨김 파일 포함 출력
        - `ls -l` = 현재 디렉토리의 파일 목록을 자세히 출력
          - 여기서 자세히란, 권한, 생성일자 등 정보까지 포함한다.
        - `ls *.txt` = .txt 확장자 목록 출력

      - `pwd` = 현재 위치의 디렉토리 경로 출력

      - touch
        - `touch test.txt` = test.txt란 파일이 없으면, 생성하고
          있으면, 최종 수정 시간을 현재 시간으로 변경한다.
      
      - mkdir
        - `mkdir -p /def/test` = /test 디렉토리를 생성하는데
          -p는 test의 상위 디렉토리인 /def가 존재하지 않을 때,
          부모 역할로 지정하고, /def 디렉토리를 먼저 생성한 다음,
          /test 디렉토리를 그 다음에 생성한다.
      
      - rmdir
        - `rmdir test` = test 디렉토리를 삭제한다.
      
      - cp
        - `cp test.txt test2.txt` = test.txt 파일을 test2.txt로 파일명을 바꾸고 복사
        - `cp -r test test2` = test 디렉토리를 test2로 이름을 바꾸고 복사
      
      - mv
        - `mv test.txt /a/b` = test.txt를 /a/b 디렉토리로 이동시킨다.
        - `mv a b c d` = a, b, c 파일을 d 디렉토리로 이동시킨다.
          - 여기서 유의할 점은 맨 끝에 있는 명이 디렉토리가 된다.
        - `mv a.txt b.txt` = a.txt 파일명을 b.txt 로 바꾼다.

      - cat
        - `cat a.txt b.txt` = a와 b를 연결해서 내용을 출력한다.
      
      - head, tail
        - `head 파일명` = 앞 10행을 화면에 출력한다.
        - `head -n 파일명` = 앞 n행에 따라 화면에 출력한다.
        - `tail 파일명` = 뒤 10행을 화면에 출력한다.
        - `tail -n 파일명` = 뒤 n행에 따라 화면에 출력한다.

      - more
        - `more 파일명` = 페이지 단위로 출력한다.
        - `more +10 파일명` = 해당 파일의 10행부터 출력한다.
      
      - less
        - `less 파일명` = more보다 확장된 기능을 사용할 수 있다.
          Ex) 화살표키, PageUp, PageDown
      
      - file
        - `file 파일명` = 어떤 타입의 파일인지 알아낼 수 있다.
        - `file a.txt` = 텍스트 파일이므로, ASCII text 표시
        - `file a` = 디렉토리이므로, directory 표시
      
      - 네트워크 명령어
        - `ifconfig` = IP Address 확인
        - `ip route` = 게이트웨이 정보 확인
        - `netstat -rn` = 게이트웨이 정보 확인
        - `systemd-resolve --status ens32` = DNS 서버의 정보 확인
        - 클라이언트의 경우, `ens32`가 아닌 `ens33`으로 해야 한다.
        - `ifdown --all` = 네트워크 장치 정지
        - `ifup --all` = 네트워크 장치 가동
        - `nm-connection-editor` = 네트워크 설정 변경창 활성화
          - 자동 or 고정 IP 주소 사용 결정
          - IP주소, 서브넷 마스크, 게이트웨이 정보 입력
          - DNS 정보 입력
          - 네트워크 카드 드라이브 설정
          - 네트워크 장치(ens32, ens33) 설정
        - `systemctl start/stop/restart/status networking`
          - 네트워크 설정 변경 후, 시스템에 적용하는 명령어
            따라서, `nm-connection-editor`에서 설정 정보를 변경하고,
            적용시키려면, 위 명령어를 반드시 실행해야 한다.
        - `nslookup` = DNS주소 확인
          - `nslookup` 입력하면, > 활성화
            www.google.com 을 입력하면, google.com에 대한 정보가 출력된다.
      
      - 네트워크 경로
        - 호스트 OS에는 실제로 사용하는 IP와 가상 IP 주소가 존재한다.
        - 하나의 IP 주소가 게이트웨이 겸 DNS서버 역할을 하며,
          게이트웨이와 DHCP 서버를 통해 리눅스에 자동으로 IP를 부여한다. 
      
      - DHCP(Dynamic Host Configuration Protocol)
        - 컴퓨터가 부팅되면, IP 주소 등 네트워크 정보들이 자동으로 할당되고 관리한다.
      
      - 브로드캐스트 주소
        - 내부 네트워크에 존재하는 모든 컴퓨터가 수신하는 주소이다.
          예를 들면, 학교에서 방송을 하면, 모든 반이 들을 수 있다.
          여기서 어떤 반의 어떤 학생을 호출하면, 다른 반은 단순히 수신만 하고,
          호출된 반의 학생은 반응을 해야 한다.

      - 게이트웨이
        - 내부 네트워크를 외부와 연결하기 위한 컴퓨터 또는 장비를 일컫는다.
          내부끼리 통신을 할 때는 게이트웨이가 필요하지 않다.
          하지만, 내부가 외부와 통신을 하려면 반드시 필요하다.
          따라서, 게이트웨이는 외부 네트워크로 나가는 관문이다.
      
      - 네트워크의 클래스
        - A, B, C, D, E 총 5개의 클래스로 나뉘어진다.
          하지만, 주로 A, B, C 클래스를 사용하며, 나머지 D와 E 클래스는
          멀티캐스트 및 연구용으로 사용된다.
          IP주소를 32진수로 표현했을 때,
          A = 0 (1 ~ 126) 2의 7승 - 1
          B = 10 (128.0 ~ 191.255) 2의 14승
          C = 110 (192.0.0 ~ 223.255.255) 2의 22승
          이다.


# 2021-06-13
  - 리눅스 6일차
    - 압축 명령어
      - xz
        - `xz 파일명` = 파일명.xz 압축 파일 생성 및 기존 파일 제거
        - `xz -d 파일명.xz` = 파일명.xz의 압축을 풀어 '파일명' 파일 생성
          - d는 decompress의 약자이다.
        - `xz -l 파일명.xz` = 파일명.xz에 포함된 파일 목록과 압축률 등 출력한다.
        - `xz -k 파일명` = 압축 후, 기존 파일을 삭제하지 않고 유지한다.

      - bzip2
        - `bzip2 파일명` = 파일명.bz2 압축 파일 생성
        - `bzip2 -d 파일명.bz2 == bunzip2 파일명.bz2`
          = 파일명.bz2의 압축을 해제하고, 2번째 파일명 파일 생성
        - `bzip2 -k 파일명` = 압축 후, 기존 파일을 삭제하지 않고 유지한다.
      
      - gzip
        - `gzip 파일명` = 파일명.gz 압축 파일 생성
        - `gzip -d 파일명.gz == gunzip 파일명.gz` = bzip2와 동일

      - zip
        - `zip 새파일명.zip 파일명` = 압축 파일 생성 후, 기존 파일 유지
        - `unzip 파일명.zip` = 파일명.zip 압축 해제하고, 파일명 파일 생성

    - 파일 묶기
      - a와 b파일을 압축한다면, 하나의 압축 폴더에 압축된다.
      
      - tar
        - c = 새로운 묶음 파일을 만든다.
        - x = 묶음 파일을 해제한다.
        - t = 묶음 파일을 해제하기 전에 경로를 보여준다.
        - C = 지정된 디렉토리에 묶음 파일을 해제한다.
              지정하지 않으면, 묶음 파일이 존재하는 디렉토리에 해제한다.
        - f = 묶음 파일명을 지정한다.
        - v = 파일을 묶거나 해제 과정을 보여준다.
        - z = tar + gzip
        - j = tar + bzip2

        - Example
          - `tar cvf test.tar /etc/fonts/` = fonts 디렉토리에 있는 파일들을
            test.tar 파일명으로 묶는다.
          - `tar cvfJ test.tar /etc/fonts/` = 파일들을 묶고, xz로 압축한다.
          - `tar cvfz test.tar /etc/fonts/` = 파일들을 묶고, gzip으로 압축한다.
          - `tar cvfj test.tar /etc/fonts/` = 파일들을 묶고, bzip2로 압축한다.
          - `tar tvf test.tar` = 파일 확인
          - `tar xvf test.tar` = tar 풀기
          - `tar Cxvf new test.tar` = new 디렉토리에 tar를 푼다.
          - `tar xfJ test.tar.xz` = xz 압축 해제 + tar 풀기
          - `tar xfz test.tar.gz` = gzip 압축 해제 + tar 풀기
          - `tar xfj test.tar.bz2` = bzip2 압축 해제 + tar 풀기

    - 파일 위치 검색
      - find
        - 옵션
          - -name, -user(소유자), -newer(전, 후), -perm(허가권), -size(크기)
        - 액션
          - -print(기본값), -exec(외부 명령 실행)
          - 외부 명령이란, find 명령어가 아닌, 다른 명령어 즉,
            `-exec rm {} \;`를 실행할 경우, {} 안에는 앞의 find 명령어
            구문이 들어가고, 삭제 명령어인 rm을 실행시킨다.
            그리고 `\;`은 `-exec`의 끝을 의미한다.
        
        - `find /etc -name "*.txt"` = etc 디렉토리 하위에 있고, 확장자명이
          .txt 인 파일 검색
        - `find /home -user ubuntu` = etc 디렉토리 하위에 있고, 소유자가 
          ubuntu인 파일 검색
        - `find ~ -perm 644` = 현재 사용자의 홈 디렉토리 하위에 있고, 허가권이
          644인 파일 검색
        - `find /usr/bin -size +10k -size -100k` = /usr/bin 디렉토리 하위에 있고,
          크기가 10~100KB인 파일 검색
        - `find ~ -size 0k -exec ls -l {} \;` = 현재 사용자의 홈 디렉토리 하위에
          있고, 크기가 0인 파일의 목록을 상세히 출력
        - `find /home -name "*.swp" -exec rm {} \;` = /home 디렉토리 하위에 있고,
          확장명이 *.swp인 파일 삭제
      
      - `whereis 실행파일명`
      
      - `locate 파일명`