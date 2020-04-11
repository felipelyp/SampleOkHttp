# Exemplo do uso de OkHttp

Simples POST/GET

## Instalação

```
implementation 'com.squareup.okhttp3:okhttp:+'
```
## Erro com protocolos http:// no app?

Solução no manifest
```
<application
        android:usesCleartextTraffic="true"
```

## Codigos

* [Java](https://github.com/xzflow/SampleOkHttp/blob/master/ServiceApi.java) - ServiceApi.java
* [Kotlin](https://github.com/xzflow/SampleOkHttp/blob/master/ServiceApi.kt) - ServiceApi.kt


## R8 / ProGuard

[Arquivo R8ProGuard](https://github.com/square/okhttp/blob/master/okhttp/src/main/resources/META-INF/proguard/okhttp3.pro)
