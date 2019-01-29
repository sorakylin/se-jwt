# JwtBuilder-CryptographicComponent
This is a jwt build tool and some cryptographic components.
<br>
<br>

#### 说明  
此工具主要用于生成 jwt (Json Web Token)。 必须使用JDK1.8及以上。 

com.skypyb.jwt 包下为可以舒适的创建 jwt 所必需的类
- 通过 JwtBuilder 来进行建造,使用优雅的链式调用法指定好header(默认HmacSHA256)、payload、secret。  
- 可通过 enum 选择多种签名加密算法,配合 com.skypyb.cryptography.Encrypt 接口,随意手写专属签名加密。  
- jwt 负载(payload)创建时,可方便的放入私有属性/Json 对象以供业务处理,支持 String 类型或是 JsonNode 类型。
- 需要使用到 jwt 的应该都用 Spring 吧,选择的json处理库为 Spring 默认集成的 [jackson](https://github.com/FasterXML/jackson)。
- 若是不需要生成 jwt ,只需要加密组件的话,删除此包即可,无任何影响。
<br>

com.skypyb.cryptography 包下封装许多加密/解密(包括hash/散列/编码等非加密算法 , 以下同)的实现
- 拥有一个加密/解密的接口 Encrypt ,能够实现它来自由定制不同的加密方式,可扩展性优秀。
- 目前实现的加密方式(可使用 enum Encrypt.Type 快速创建的实体):  
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

#### jwt 创建方式/调用方式 示例
```java
String jwt = new JwtBuilder().header(Encrypt.Type.HS512)//指定加密方式为 HmacSHA512
    .payload().iss("Koishi").exp(TimeUnit.MINUTES.toMillis(3))
    .data("abc", "abc").data("666", "qwe")//自定义负载的参数
    .and().build("This is key secret");
    
//or
    
Payload payload = new Payload.PayloadBuilder().exp(30000L).build();
String jwt2 = new JwtBuilder().payload(payload).header().build("This is key secret");
    
```
