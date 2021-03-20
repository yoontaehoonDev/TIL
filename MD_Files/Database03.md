# TI

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