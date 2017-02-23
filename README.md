[![Build Status](https://travis-ci.org/akaGelo/another-live-refresh.svg?branch=master)](https://travis-ci.org/akaGelo/another-live-refresh)


```
<dependency>
    <groupId>ru.vyukov</groupId>
    <artifactId>another-live-refresh-spring-boot-starter</artifactId>
    <version>0.1</version>
    <optional>true</optional>
</dependency>

```

and disable in develipment profile
```
liverefresh:
  enable: true
```

RUSSIAN

Гифка с захватом экрана


Это дополнение к Spring Boot Developer Tools. 

Оно предназначено для автоматического обновления страницы в браузере при обнаружении изменений в проекте.

Похоже на livereload.com, вдохновлено им, но в отличии от livereload не занимает определенный порт.  
Каждое приложение ожидает соедиения на своем порту. Это очень удобно при работе с несколькими проектами одновременно 
(к примеру админ панель и сайт).


Поведение опирается на Dev Tools, (должно отключатсья в production автоматически).
Есть возможность ручного отключения:


Работоспособность тестировалась со spring boot 1.5.1
При различии версий Developer Tools могут быть проблемы 


ENGLISH