# TIL

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
    