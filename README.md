# JwtBuilder-CryptographicComponent
This is a jwt build tool and some cryptographic components


    jwt 创建方式/调用方式
```java
    String jwt = new JwtBuilder().header(Encrypt.Type.HS512)//指定加密方式为 HmacSHA512
                    .payload().iss("Koishi").exp(TimeUnit.MINUTES.toMillis(3))
                    .data("abc", "abc").data("666", "qwe")//自定义负载的参数
                    .and().build("This is key secret");
    
    //or
    
    Payload payload = new Payload.PayloadBuilder().exp(30000L).build();
    String jwt2 = new JwtBuilder().payload(payload).header().build("This is key secret");
    
```
