# Cask 
====

### What is it

It's a java multi-threaded HTTP server (container).

- A java process act as a HTTP server (container).
- It's a container. Your classes can be run on the cask server with simple configuration. 
- Very easy to implement the server that provide HTTP api.
- A thread run your class when a request is received.

![Cask](http://www.luxeinacity.com/images/blogs/2013/Glenmorangie-Cask-Masters-Whisky-Programme-2.jpg)


### Why not tomcat

When you use the Tomcat server, you can get the same result of the Cask server. But the Tomcat is not convenient for using in server level. 

- When a system constructed by UI(web) server + internal server, internal server doesn't need the functions such as UI, security, etc. It has only business logic.
- Your application doesn't execute indepentantly in eclipse's debug mode. You need to register application to tomcat, and execute tomcat not your application
- Executing tomcat in eclipse is slower than executing normal java program.
- Tomcat can handle only http requests, background processes which executes forever when server startup are not supported.

### What is needed in server level. 

- Needs implimentation of business logic that executed by GET, POST http requets, accessing DB or hadoop. not ui.
- Development and debugging have to be easy in local PC. 
- Startup speed has to be fast for debugging.
- Needs to handle background(batch) work, in addition to handle Http request. 

### Cask 

- Run as a jvm process. My module doesn't run on other process like tomcat, runs on my process.
- So, very easy to debug in developing(on eclipse).
- Startup fast. It's the same as normal java application.  
- Lightweight. Unnessary functions like UI, web security are removed.
- It includes bulid configurations constrcted by ant + ivy, so easy to compile, deploy, manage version. sometimes, c/c++ server developers who usually use 'make' confuse using maven. They can use easily ant, because 'ant' and 'make' is similar and 'ant' is easier than 'make'.
- It includes scripts for startup, stop server.
- Not only handling logic per http request, but also handling background logic that starts when server startup and executes forever until server shutdown.

Internally, it implimented by tomcat embedded + spring.

### Constraints

- If you need various functions as a web server such as UI, security, you have to use Tomcat or jetty.
- Only support json as a http POST request.

# How to use

- [Tutorial : install & execute - Korean](https://github.com/lonslonz/cask/wiki/Tutorial--:-install-&-execute-%5BKorean%5D)
- [Tutorial : eclipse project setting & debugging - Korean](https://github.com/lonslonz/cask/wiki/Tutorial-:-eclipse-project-setting-&-debugging-%5BKorean%5D)
- [Tutorial : add services - Korean](https://github.com/lonslonz/cask/wiki/Tutorial-:-add-your-services-%5BKorean%5D)
