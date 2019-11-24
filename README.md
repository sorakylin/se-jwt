# se-jwt
This is a jwt build tool and some cryptographic components.

[![version](https://img.shields.io/badge/version-v1.2.0-orange.svg)](../)
[![License](https://img.shields.io/badge/License-MIT-red.svg)](https://mit-license.org/)
[![Jdk](https://img.shields.io/badge/jdk-1.8-green)](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
[![jackson](https://img.shields.io/badge/jackson-orange)](https://github.com/FasterXML/jackson)

<br>
<br>


### 说明  
此工具主要用于生成 jwt (Json Web Token)。 必须使用JDK1.8及以上。 

com.skypyb.sejwt.jwt 包下为可以舒适的创建 jwt 所必需的类
- 通过 Builder 来进行建造,使用优雅的链式调用法指定好 header、payload、secret。`默认使用 HmacSHA256 进行签名加密`  
- 可通过枚举快速选择多种签名加密算法,当然也可以自己实现 com.skypyb.sejwt.cryptography.Codec 接口,随意手写专属签名加密。  
- jwt 负载(payload) 创建时,可方便的放入私有属性/json 对象以供业务处理,支持 String 类型或是 JsonNode 类型。
- payload 中的 jti(编号) 除直接set的方式外，也可使用指定的 jti 生成器来进行生成，默认情况下使用的的是long型ID自增生成器。
- 选择的 json 处理库为 [jackson](https://github.com/FasterXML/jackson)。
<br>

com.skypyb.sejwt.cryptography 包下封装许多加密/解密的实现。`包括hash/散列/编码等非加密算法 , 以下同`
- 拥有一个加密/解密的接口 Codec ,能够实现它来自由定制不同的加密方式,可扩展性优秀。
- 可使用 enum Codec.Type 快速的创建 Codec 实例。`使用枚举创建的算法实体默认是单例的`
- 该包是高内聚的,可不依靠jwt模块单独使用
- 目前实现的加密方式:  
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

### 快速开始:
<br>
  
#### Codec 加解密接口使用
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

#### jwt 创建方式/调用方式
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

#### jwt 验证:
```java
String[] tempArr = jwt.split("\\.");
  
//通过 key secret 进行签名匹配 (这里以上面的jwt创建示例1作为演示)
String ciphertext = Codec.Type.HS512.encrypt(tempArr[0] + "." + tempArr[1], "This is key secret");
boolean isMatch = ciphertext.equals(tempArr[2]);//true 

```

<br>
<br>
