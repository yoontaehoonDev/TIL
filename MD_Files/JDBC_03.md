# TIL

# 2021-03-26
 - ER Diagram
    - 식별관계와 비식별관계
      - 식별관계 (identifying relationship)
        - FK = PK
        - 자식 테이블의 FK가 부모 테이블의 PK를 참조하면,
          식별관계가 성립된다.

      - 비식별관계 (non-identifying relationship)
        - FK ≠ PK
        - 자식 테이블의 FK가 부모 테이블의 PK가 아닌 FK를
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