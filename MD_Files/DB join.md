# TIL

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
      select count(컬럼명 or *) from 테이블명 [where 조건];
      select count(*) from test;
      ```
      
      +----------+
      | count(*) |
      +----------+
      |        6 |
      +----------+

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

        +-----+------+------+----------------+
        | num | room | loc  | titl           |
        +-----+------+------+----------------+
        |   1 | 501  | 강남 | Solaris        |
        |   4 | 301  | 종로 | Linux          |
        +-----+------+------+----------------+


        outer 사용 후

         +-----+------+------+----------------+
        | num | room | loc  | titl           |
        +-----+------+------+----------------+
        |   1 | 501  | 강남 | Solaris        |
        |   4 | 301  | 종로 | Linux          |
        |   5 | NULL | NULL | System         |
        +-----+------+------+----------------+

        
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
