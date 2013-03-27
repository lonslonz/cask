# Cask (Korean)
====

### What is it

It's a java multi-threaded server container.
서버로 동작하는 하나의 프로세스임.
Http API를 제공하는 서버를 구현하기 용이하도록 구현되었음. 

자신이 개발한 클래스를 간단한 설정을 통해 cask위에 띄울 수 있음. 
http request를 받을 수 있도록 할 수 있으며, 요청이 올때 thread 하나가 이 클래스를 실행

### Why not tomcat

tomcat과 같은 웹 서버를 사용해도 같은 결과를 낼 수 있으나, 웹이 아닌 서버 레벨에서 사용하기에는 불편한 점이 있음. 

- UI(웹) 서버 + 내부 기능 서버와 같은 구조로 구성되는 경우, 내부 기능 서버는 UI, 웹 보안등 다수의 기능은 불필요함. 
오직 http request를 받아 처리하여 return을 주기만 하면 됨. 
- 개발환경(eclipse)에서 tomcat 서버, web context를 별개로 등록해야 함
- debug시 startup 속도가 상대적으로 느림.
- Http request만을 처리할 수 있으며 background로 수행되는 방식은 처리할 수 없음.

### 서버 레벨에서 개발시 필요한 것

- get, post 방식의 http request만 받아, DB 혹은 hadoop에 접근하여 기능을 수행할 수 있으면 됨.
- 로컬 PC에서 개발 및 디버깅이 손쉬워야 함. 
- 개발자 편의를 위해서는 startup 속도가 빨라야 함. 
- 일반 http request 방식의 처리 뿐 아니라 background(batch)로 동작하는 방식의 처리도 필요함. 
- DB, memory에 저장된 정보를 확인하려면 Junit으로는 불편함. 
mock을 사용해 테스트하는 것은 실환경과 달라 문제가 발생할 수 있음. 
DB, memory에 제대로 저장되었는지 확인하기 위한 테스트가 손쉬워야 함. 

### Cask 

- 하나의 jvm 프로세스로 동작함. 
tomcat과 같이 다른 프로세스에 내 모듈을 띄우는 것이 아니라 , 내 프로세스에 내 모듈을 띄움. 
- 따라서 개발 단계(eclipse 상)에서 디버깅이 수월 
- startup 속도가 빠름. 일반 java application을 수행하는 것과 같음. 
- 가볍게 사용할 수 있음. UI, 웹 보안과 같이 서버 레벨에서 불필요한 기능은 삭제되었음. 
- version 관리, relelase, build 구성 포함. ant + ivy로 이루어짐
maven은 웹 환경에서는 장점이 있지만, 서버 레벨에서 사용하기는 불편함. 
또한 makefile에 익숙한 c/c++ 사용자는 ant에 쉽게 적응이 가능
- request시 동작뿐 아니라, 서버가 시작하자 마자 batch로 수행할 수 있는 기능 제공

전반적으로 (jetty embedded 를 사용하는 것 + 서버 레벨에서 사용)하는 특징을 가짐.
cask 내부 구현은 tomcat embedded + spring 으로 되어 있음.

### Server test  

서버 레벨의 테스트를 위해 다음과 같은 기능을 제공

- Junit 테스트를 추가하면 됨
- diff 방식의 테스트 결과를 제공
- 메모리, DB를 dump해서 올바른 결과와 비교할 수 있음
- hudson에서 결과를 확인할 수 있도록 JUnit test 결과를 산출
    

### Constraints

- UI, 보안 등 다양한 기능이 필요한 경우는 tomcat, jetty를 써야 함
- http request는 json만 지원

# How to use

### download

cask_build_template 를 svn에서 다운받아 사용하면 됨
이는 ant + ivy 빌드 구성과 version관리를 위한 구성을 포함하고 있음

### install

### execute

### request api 

### test

### eclipse setting











