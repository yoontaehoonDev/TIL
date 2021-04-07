# TIL

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