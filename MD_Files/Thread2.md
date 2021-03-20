# TIL    

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


