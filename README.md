# JWTenizr

jwtenizr creates tokens and ready to use microprofile configuration. 

## installation

Download the executable and self-contained [jwtenizr.jar](https://github.com/AdamBien/jwtenizr/releases) and execute:
`java -jar jwtenizr.jar`

## usage

`java -jar jwtenizr.jar` creates:

- `jwtenizr-config.json` with public, private key
- The generated token `token.jwt` contains information loaded from: `jwt-token.json`
- Adjust the `groups[]` to configure roles and `upn` to change the principal in `jwt-token.json` then re-execute JWTenizr
- `microprofile-config.properties` with generated public key and default issuer (corresponds with jwt-token.json)
- The iss in `jwt-token.json` has to correspond with the `mp.jwt.verify.issuer` in `microprofile-config.properties`
- Copy the `microprofile-config.properties` to your WAR/src/main/resources/META-INF
- Optional: Adjust the trailing URI in the `curl` command and use it for testing:

```curl -i -H'Authorization: Bearer eyJraW¢...(generated JWT token)' http://localhost:8080[RESOURCE and SUB-RESOURCES]```


## run from anywhere

A a shell script:

```shell
#!/bin/bash
BASEDIR=$(dirname $0)
java -jar ${BASEDIR}/jwtenizr.jar "$@"%
```

will install JWTenizr "globally". Now you can launch JWTenizr from any directory you like
