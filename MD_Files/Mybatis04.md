# TIL

# 2021-04-06
 - Mybatis
    - join
      - join을 이용하여, 두 가지 종류 이상의 객체를 출력할 수 있다.
      ```
      select 
        b.board_id, 
        b.title,    
        b.contents,
        b.created_date, 
        b.view_count,
        bf.board_file_id,
        bf.file_path  
      from x_board b
        left outer join x_board_file bf on b.board_id=bf.board_id
        where b.board_id={no}
      ```
      위 SQL문은 outer join을 이용하여, 출력한다.
      left는 from 테이블명 기준으로 한다.
      right는 outer 다음에 오는 테이블명 기준으로 왼쪽으로 간다.
      따라서, 부모테이블하고 자식테이블을 join해서 값을 출력하려면,
      부모테이블이 from에 선언되야 한다.

      - if 태그를 넣어서 사용할 수도 있다.
      ```
      <if test="no != null">
        where b.board_id={no}
      </if>
      ```
      즉, no가 null이 아니라면, where절을 실행하겠다는 의미다. 
    
    - 파라미터 타입
      - primitive 타입, Wrapper 클래스, String 클래스는
        SQL문에 아무 이름을 지어도 상관없다.
        왜냐하면, 단 한 개만 넘어오기 때문에 구분 짓지 않는다.
        
        반면에, map이 파라미터 타입으로 넘어 올 경우,
        key에 넣은 값을 그대로 명시해야 한다.