# TIL

# 2021-04-02
 - XML Entity
    - &lt; : <
    - &gt; : >
    - &quot; : "
    - &apos; : '
    - &amp; : &
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
      &lt;와 같은 Entity를 명시할 필요가 없다.
      
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
    