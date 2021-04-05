# TIL

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
      