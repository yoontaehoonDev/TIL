# TIL

# 2021-04-09
 - 테스트 코드
    - 테스트 주도 개발 (TDD)
        - Red : 실패하는 테스트를 먼저 작성.
        - green : 테스트가 통과하는 프로덕션 코드 작성.
        - Refactor : 테스트가 통과하면, 프로덕션 코드를 리팩토링.

        - 단위 테스트
            - 단위 테스트는 Red 단계를 의미.
            - 단위 테스트 코드 작성의 이점
                - 개발단계 초기에 문제 발견에 도움된다.
                - 개발 후, 코드를 리팩토링을 하거나, 라이브러리
                  업그레이드 등 기존 기능이 올바르게 작동하는지 확인한다.
                  Ex) 회귀 테스트
                - 기능에 대한 불확실성을 감소시킨다.
                - 단이 테스트 자체를 문서로 사용이 가능하다.

 - 용어 이해
    - @SpringBootApplication
        - 스프링 부트의 자동 설정 및 스프링 Bean 읽기와 생성 모두 자동 설정.
          Bean이란? 애플리케이션의 핵심을 이루는 객체이며,
          Spring IoC 컨테이너에 의해 인스턴스화, 관리 및 생성이 된다.
        
        - @SpringBootApplication이 있는 클래스는 애노테이션이 있는 위치부터
          설정을 읽어 내려간다. 따라서, 이 애노케이션을 포함한 클래스는
          프로젝트의 최상단에 위치해야 한다.
    
    - SpringApplication.run
        - 내장 WAS(Web Application Server)를 실행한다.
          내장 WAS는 별도의 외부에 WAS를 두지 않고, 앱을 실행할 때
          내부에서 WAS를 실행하는 것을 의미한다.
          따라서, 따로 서버를 구축하지 않고 서버 사용이 가능하다.

          추가로, 내장 WAS 사용을 권장하는데 그 이유는
          언제 어디서든 같은 환경에서 스프링 부트 배포가 가능하기 때문이다.

    - @RestController
        - 기존의 @Controller와 @ResponseBody의 축약형.
        - 컨트롤러를 JSON을 반환하는 컨트롤러로 변환시킨다.
        - @ResponseBody를 각 메소드에 선언했던 걸, 통합시켜서
          한 번에 사용할 수 있는 장점이 있다.
    
    - @GetMapping
        - 기존의 @RequestMapping의 축약형.
        - HTTP Method인 Get의 요청을 받을 수 있는 API.

    - @ExtendWith
        - 테스트를 진행할 때, JUnit에 내장된 실행자 외에
          다른 실행자를 실행시킨다.
          스프링 부트 테스트와 JUnit 사이에 연결자 역할을 한다.
        
        - JUnit4 버전 기준으로 SpringRunner 스프링 실행자 사용.
        - JUnit5 버전 기준으로 SpringExtension 스프링 실행자 사용.

    - @WebMvcTest
        - 여러 스프링 테스트 애노테이션 중에서 Web(Spring MVC)에
          집중할 수 있는 애노테이션.
        
        - @Service, @Component, @Repository 등은 사용 불가.
    
    - @AutoWired
        - 스프링이 관리하는 빈(Bean)을 주입 받는 애노테이션.

 - JUnit 4 -> 5 바뀐 점.
    - @RunWith -> @ExtendWith
    - org.junit.runner.RunWith
    -> org.junit.jupiter.api.extension.ExtendWith

    - @RunWith(SpringRunner) -> @ExtendWith(SpringExtension)
    - org.springframework.test.context.junit4.SpringRunner
    -> org.springframework.test.context.junit.jupiter.SpringExtension

    - @After -> @AfterEach
    - org.junit.After
    -> org.junit.jupiter.api.AfterEach

    - @Before -> @BeforeEach
    - org.junit.Before
    -> org.junit.jupiter.api.BeforeEach


 - 오류 코드와 해결 방법
    - No tests found for given includes 오류
        - build.gradle 파일 내부에
          test {
              useJUnitPlatform()
          }
          이 생략되었는지 확인.

 - private MockMvc mvc
    - 웹 API를 테스트할 때 사용
    - 스프링 MVC 테스트의 시작점
    - 이 클래스를 통해 HTTP GET, POST 등에 대한 API 테스트 가능

 - MVC.PERFORM(GET("/hello"))
    - MockMvc를 통해 /hello 주소로 HTTP GET 요청
    - 체이닝이 지원됨으로서, 여러 검증 기능을 이어서 선언 가능
      체이닝이란? 메소드가 객체를 반환하면 반환 값을 이어서 호출하는 것이다.
      Ex) 인스턴스명.setName("name").setAge(10).setGrade('A') 이런 식으로 이어서 호출하는 것이다.

 - .andExpect(status().isOk())
    - mvc.perform의 결과를 검증
    - HTTP Header의 Status(200, 404, 500 등)의 검증

 - .andExpect(content().string(hello))
    - mvc.perform의 결과를 검증
    - 응답 본문의 내용을 검증
    - Controller에서 "hello"를 리턴하기 때문에 이 값이 맞는지 검증