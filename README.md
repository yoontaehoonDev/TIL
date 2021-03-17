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
     
    - 프로세스 복제 방식의 멀티태스킹이 있지만,
      메모리도 똑같이 복제해서 사용하므로, 메모리 낭비가 심해진다.
      따라서, 프로그램이 무거워진다.

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
    
    - Mutex는 Critical Section에 오직, 1개의 쓰레드만 접근 허용하게 하는 방식
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
