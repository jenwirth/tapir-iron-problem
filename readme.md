
run:

```shell
sbt reStart
```

Swagger: http://localhost:7777/docs/

Using [httpie](https://httpie.io/):

- returns "Invalid value for: body (expected phoneNumber to match:…)"

```shell 
$ http POST ':7777/toto/vanilla' phoneNumber="1111111111"
```

- returns just "Invalid value for: body"


```shell 
$ http POST ':7777/toto/iron' phoneNumber="1111111111"
```

