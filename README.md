[![Build Status](https://travis-ci.org/akaGelo/another-live-refresh.svg?branch=master)](https://travis-ci.org/akaGelo/another-live-refresh)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/ru.vyukov/another-live-refresh/badge.svg)](https://maven-badges.herokuapp.com/maven-central/ru.vyukov/another-live-refresh)

```xml
<dependency>
    <groupId>ru.vyukov</groupId>
    <artifactId>another-live-refresh-spring-boot-starter</artifactId>
    <version>0.3.1</version>
    <optional>true</optional>
</dependency>

```

and disable in production profile
```yml
logging:
  level:
    ru.vyukov.anotherliverefresh: DEBUG

spring:
  thymeleaf:
    cache: false


#liverefresh:
#    enable: true is default
  
---
#production profile
 
spring:
  profiles: production
  thymeleaf:
    cache: true

liverefresh:
    enable: false

```

![demo gif](https://raw.githubusercontent.com/akaGelo/another-live-refresh/master/demo.gif)

ENGLISH



RUSSIAN



Этот проект вдохновлен livereload.com, однако в отличии от него встраивается в приложение в виде maven зависимости.
Преимущество перед реализацией из Spring Developer Tools, заключается в отсутствии привязки к соединению на определенный порт, что позволяет использовать продукт одновременно в нескольких приложениях.Так же не требуется утановка расширений в браузере.



