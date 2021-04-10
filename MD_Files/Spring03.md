# TIL

# 2021-04-10
 - 롬복(Lombok)
    - Getter, Setter, 기본 생성자, toString 등
      애노테이션으로 자동 생성해준다.
    
    - 롬복은 프로젝트마다 설정을 해야 한다.
      플러그인은 단 한 번만 설치하면 되지만,
      새 프로젝트를 만든다면, build.gradle에 라이브러리를
      추가하고, Enable annotation processing 체크도 해야 한다.
    
 - 용어
    - @Getter
      - 선언된 모든 필드의 get 메소드를 생성해준다.
      ```
      public class Test {
        String name;
        String addr;

        public void getName() {
          return name;
        }
        public void getAddr() {
          return addr;
        }
      }
      ```
    
    - @RequiredArgsConstructor
      - 선언된 모든 final 필드가 포함된 생성자를 생성해준다.
        하지만, final이 없는 필드는 생성자에 포함시키지 않는다.
    
    - assertThat
      - assertj의 테스트 검증 라이브러리의 검증 메소드
      - 검증하고 싶은 대상을 메소드 인자로 받는다.
      ```
      public void test() {
        String name = "test";
        int age = 20;

        ResponseDto dto = new ResponseDto(name, amount);
        
        @RequiredArgsConstructor로 final 필드인 name과 age 필드를 포함한 
        생성자를 자동 생성한다.
        따라서 name에 test 문자열이 대입되고, age에는 20이 치환된다.
        그리고
        assertThat메소드와 isEqualTo메소드를 사용하여 비교한다.
        
        assertThat(dto.getName()).isEqualTo(name);
        assertThat(dto.getAge()).isEqualTo(age);

        여기서 주의할 점은, 메소드 체이닝으로 이루어지기 때문에,
        assertThat의 괄호가 끝나고, 이어서 선언해야 한다.
        코드의 내용은 dto.getName() 즉, 생성자를 생성할 때 넣어지는
        값인 test 문자열을 isEqualTo(name) 과 비교하여 true false를
        출력한다.
        현재 test() 메소드의 name은 test가 맞기 때문에, 결과는 true다.
      }
      ```

    - @RequestParam
      - 외부에서 API로 넘긴 파라미터를 가져오는 애노테이션.

      - 외부에서 NAME이란 이름으로 넘긴 파라미터를 메소드 파라미터
        name에 저장한다.
        ```
        @RequestParam("name") -> name(String name)
        ```

    - param
      - API 테스트할 때, 사용될 요청 파라미터를 설정한다.
      - 단, 값은 String만 허용한다.
        따라서, String 외의 타입은 타입캐스팅을 해야 한다.
        Ex) valueOf() 사용