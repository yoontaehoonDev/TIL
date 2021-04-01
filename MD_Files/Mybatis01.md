# TIL

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