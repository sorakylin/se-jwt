# se-jwt
This is a jwt build tool and some cryptographic components.

[![version](https://img.shields.io/badge/version-v1.2.0-orange.svg)](../)
[![License](https://img.shields.io/badge/License-MIT-red.svg)](https://mit-license.org/)
[![Jdk](https://img.shields.io/badge/jdk-1.8-green)](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
[![jackson](https://img.shields.io/badge/jackson-orange)](https://github.com/FasterXML/jackson)

<br>
<br>


## 用途  
此工具主要用于生成 jwt (Json Web Token)。 JDK版本最低支持1.8。 

<br>
<br>



## 功能特性 
**com.skypyb.sejwt.jwt** 包下为创建 jwt 所需要的类

- 简洁快速, 通过链式调用法建造 header、payload、secret。`默认使用 HmacSHA256 进行签名加密`  
- Payload 中的 jti(编号) 可自行实现 jti 生成器来获取，默认情况使用内置的线程安全long型ID自增生成器。
- 提供方便的工具类对已生成的 JWT 进行处理, 快速效验、取参
<br>

**com.skypyb.sejwt.cryptography** 包为编解码的算法实现。`包括hash/散列/编码/加密等`

- 具体算法均实现自接口 Codec , 可自行实现接口完成动态水平扩展。
- 内聚，可不依靠 JWT 模块单独使用
- 此工具内部默认集成的编解码算法:  
    - AES
    - DES
    - DES3
    - HmacMD5
    - HmacSHA1
    - HmacSHA224
    - HmacSHA256
    - HmacSHA384
    - HmacSHA512
    - Base64
    - Base64URL
    - Base64MIME
    - MD5
    - SHA

<br>
<br>

## 快速开始:
<br>

#### 默认集成的 Codec 组件使用
```java
String str = "666你 好好nihaohao=@!!%#$--/";
String key = "key_A123666";

//得到一个AES实现
Codec aes = Codec.Type.AES.create();
String result = aes.encrypt(str, key);
System.out.println(result);
System.out.println(aes.decrypt(result, key));
        
/*
console:
    RlPSP1CQwWyU+RSKuG0aXmREDUN9j12vCBQEns+gLyc=
    666你 好好nihaohao=@!!%#$--/
*/
```
<br>
<br>

#### JWT 创建方式
```java
String jwt = new JwtBuilder()
        .header(Codec.Type.HS512)//指定签名加密算法为 HmacSHA512,默认为 HmacSHA256
        .payload() //设置payload负载
        .iss("Koishi").exp(TimeUnit.MINUTES.toMillis(3))
        .data("abc", "abc").data("666", "qwe")//自定义负载的参数
        .and().build("This is key secret");//建造jwt, 使用指定的秘钥进行签名
    
//or
    
Payload payload = new Payload.PayloadBuilder().build(); //所有值都是默认的payload
String jwt2 = new JwtBuilder().payload(payload).build("This is key secret");
  

/*
例子 1 取得的 jwt :
eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.
eyJqdGkiOjEsImV4cCI6MTU0ODkxNTkwNDc2NywibmJmIjoxNTQ4OTE1NzI0NzY3LCJpYXQiOjE1NDg5MTU3MjQ3NjcsImlzcyI6IktvaXNoaSIsImFiYyI6ImFiYyIsIjY2NiI6InF3ZSJ9.
48AB9034F794D71B7BFA8B3D660DEF0C78D3CBE666721A9D8F140E7097709AFC425F8F26454668C8A0B9A75A8A90D4954FAF6F84EE5FE4EE998CF9FD9669CD86
  
header 部分经过Base64URL解码后: {"alg":"HS512","typ":"JWT"}
payload 部分经过Base64URL解码后: {"jti":1,"exp":1548915904767,"nbf":1548915724767,"iat":1548915724767,"iss":"Koishi","abc":"abc","666":"qwe"}
*/
```

<br>
<br>

#### JWT 效验:
```java
String[] tempArr = jwt.split("\\.");
  
//通过 key secret 进行签名匹配 (这里以上面的jwt创建示例1作为演示)
String ciphertext = Codec.Type.HS512.encrypt(tempArr[0] + "." + tempArr[1], "This is key secret");
boolean isMatch = ciphertext.equals(tempArr[2]);//true 

```

<br>
<br>

#### 自集成工具类的使用:
```java
Payload payload = new Payload.PayloadBuilder()
                .nbf(LocalDateTime.now())
                .exp(3 * 1000L)
                .data("test", "test value")
                .build();
String jwt = new JwtBuilder().payload(payload).build("This is key secret");
  
System.out.println(jwt);
  
  
Token token = SeJwt.asToken(jwt).signatureSecret("Wrong secret");
System.out.println("------ Token with wrong secret");
System.out.println(token.getHeaderJson());
System.out.println(token.getPayloadJson());
System.out.println(token.tokenIsCorrect());
System.out.println(token.tokenHasExpired());
System.out.println(token.validateToken());
System.out.println(token.getAttributeValue(JwtAttribute.Payload.NBF).get());
System.out.println(token.getAttributeValue("test").get()); //getAttributeValue() return Optional
  
token = token.signatureSecret("This is key secret");
System.out.println("------ Correct token");
System.out.println(token.tokenIsCorrect());
System.out.println(token.tokenHasExpired());
System.out.println(token.validateToken());
  
Thread.sleep(3 * 1000L);
System.out.println("------ After the token expires");
System.out.println(token.tokenIsCorrect());
System.out.println(token.tokenHasExpired());
System.out.println(token.validateToken());
  
  
Token wrongToken = SeJwt.asToken("12312325afvbascAJ--ASD=");
System.out.println("------ Wrong token");
System.out.println(wrongToken.getHeaderJson());
System.out.println(wrongToken.getPayloadJson());
System.out.println(wrongToken.tokenIsCorrect());
System.out.println(wrongToken.tokenHasExpired());
System.out.println(wrongToken.validateToken());
  
//-----------------------  console out:  ------------------------------
/*
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiIxIiwiZXhwIjoxNTc2Mzc3MTUxNTIwLCJuYmYiOjE1NzYzNzcxNDg1MjAsImlhdCI6MTU3NjM3NzE0ODUyMCwidGVzdCI6InRlc3QgdmFsdWUifQ==.6DF7D32E6464ABAF5D836D3084D6489FB684FF6BF1D21BE030F61EADD6D4AE3E
------ Token with wrong secret
{"alg":"HS256","typ":"JWT"}
{"jti":"1","exp":1576377151520,"nbf":1576377148520,"iat":1576377148520,"test":"test value"}
false
false
false
1576377148520
test value
------ Correct token
true
false
true
------ After the token expires
true
true
false
------ Wrong token
  
  
false
false
false
*/

```

<br>
<br>

## 依赖
- 选择的 json 处理库为 [jackson](https://github.com/FasterXML/jackson)。
  