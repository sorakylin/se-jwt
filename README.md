# JwtBuilder-CryptographicComponent
This is a jwt build tool and some cryptographic components.
<br>
<br>

#### 说明  
此工具主要用于生成 jwt (Json Web Token)。 必须使用JDK1.8及以上。 

com.skypyb.jwt 包下为可以舒适的创建 jwt 所必需的类
- 通过 JwtBuilder 来进行建造,使用优雅的链式调用法指定好 header(默认HmacSHA256)、payload、secret。  
- 可通过 enum 选择多种签名加密算法,配合 com.skypyb.cryptography.Encrypt 接口,随意手写专属签名加密。  
- jwt 负载(payload) 创建时,可方便的放入私有属性/json 对象以供业务处理,支持 String 类型或是 JsonNode 类型。
- 需要使用到 jwt 的应该都用 Spring 吧,选择的 json 处理库为 Spring 默认集成的 [jackson](https://github.com/FasterXML/jackson)。
- 若是不需要生成 jwt ,只需要加密组件的话,删除此包即可,无任何影响。
<br>

com.skypyb.cryptography 包下封装许多加密/解密`包括hash/散列/编码等非加密算法 , 以下同`的实现
- 拥有一个加密/解密的接口 Encrypt ,能够实现它来自由定制不同的加密方式,可扩展性优秀。
- 目前实现的加密方式(可使用 enum Encrypt.Type 快速创建的实体):  
    - AES
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

  
#### Encrypt 加解密接口使用示例
```java
String str = "666你 好好nihaohao=@!!%#$--/";
String key = "key_A123666";

//得到一个AES实现
Encrypt aes = Encrypt.Type.AES.create();
String result = aes.encrypt(str, key);
System.out.println(result);
System.out.println(aes.decrypt(result, key));
        
/*
console:
    E99No6eLAZ0yeq8KeY6AhECjs2roEgeEdCViZfKbnkg=
    666你 好好nihaohao=@!!%#$--/
*/
```
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

#### jwt 验证示例 
```java
String[] tempArr = jwt.split("\\.");
  
//1、先得到头部json
String headerJson = Encrypt.Type.Base64URL.create().decrypt(tempArr[0], null);
  
//2、进行 json 处理,取头部 algorithm 属性,即获取 signature 的算法
Map map = new ObjectMapper().readValue(headerJson, Map.class);
String encryptType = map.get("alg").toString();
  
//3、通过 valueOf 方法获取对应算法的 enum 并进行算法实体的创建
Encrypt encrypt = Encrypt.Type.valueOf(encryptType).create();
  
//4、通过 key secret 进行签名匹配
String ciphertext = encrypt.encrypt(tempArr[0] + "." + tempArr[1], "This is key secret");
boolean isMatch = ciphertext.equals(tempArr[2]);//true (这里以上面的创建示例1作为演示)
  
//注: 服务端知道签名算法的话省略1/2/3 步骤
```
