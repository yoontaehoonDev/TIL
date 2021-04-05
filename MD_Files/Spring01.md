# TIL

# 2021-04-05
 - build.gradle 파일
    ```
    buildscript {
        ext {
            springBootVersion = '2.1.7.RELEASE'
        }
        repositories {
            mavenCentral()
            jcenter()
        }
        dependencies {
            classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
        }
    }

    apply plugin: 'java'
    apply plugin: 'eclipse'
    apply plugin: 'org.springframework.boot'
    apply plugin: 'io.spring.dependency-management'
    ```
    
    - 프로젝트의 플로그인 의존성 관리를 위한 설정.
    - ext : build.gradle에서 사용하는 전역변수를 설정하는 의미.
            따라서, springBootVersion 전역변수를 생성하고,
            그 값을 '2.1.7.RELEASE'로 치환한다.
    - io.spring.dependency-management
        - 스프링 부트의 의존성들을 관리해주는 플러그인
    
    - repositories
        - 각종 의존성(라이브러리 포함)들을 어떤 원격 저장소에서
          받을 지, 정하는 곳.
    
    - dependencies
        - 프로젝트 개발에 필요한 의존성들을 선언하는 곳.
        - 의존성 코드는 특정 버전을 명시하면 안 된다.
          그래야 자동으로 버전을 따라가는 장점이 있기 때문이다.