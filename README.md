# JWTenizr

jwtenizr creates tokens and ready to use microprofile configuration. 

## installation

Download the executable and self-contained [jwtenizr.jar](https://github.com/AdamBien/jwtenizr/releases) and execute:
`java -jar jwtenizr.jar`

`java -jar jwtenizr.jar` creates:

- `jwtenizr-config.json` with public, private key
- token stored in `token.jwt`
- default `jwt-token.json` template with principal and groups
- `microprofile-config.properties` with generated public key and default issuer (corresponds with jwt-token.json)
- `curl` command with the generated JWT token

## sample output

The generated token token.jwt contains information loaded from: jwt-token.json
Adjust the groups[] to configure roles and upn to change the principal in jwt-token.json then re-execute JWTenizr
The iss in jwt-token.json has to correspond with the mp.jwt.verify.issuer in microprofile-config.properties
Copy the microprofile-config.properties to your WAR/src/main/resources/META-INF
Use the following command for testing:
```curl -i -H'Authorization: Bearer eyJraWQiOiJqd3Qua2V5IiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJkdWtlIiwidXBuIjoiZHVrZSIsImF1dGhfdGltZSI6MTU1ODc0OTc3MywiaXNzIjoiYWlyaGFja3MiLCJncm91cHMiOlsiY2hpZWYiLCJoYWNrZXIiXSwiZXhwIjoxNTU4NzUwNzczLCJpYXQiOjE1NTg3NDk3NzMsImp0aSI6IjQyIn0.E6aAzN0HVsoqXTcGO-7KFjPtT1HVJH0cp1FOs-ybtB5nv1XAkxxupa9j2pAuTI8uTk-kfqDYPeh-96VsrPtOcjG0KNhsgIeH720k12tStbIgzK1IxOC9JFoBkbJwkIyYoe8RzUheGBMUALvrImNb_BvrOK9Om7UIULRnERFACDnZB8yKVa6cZdZlUL5Kv9iHMU9PRpKCSvv6x45fGI_OsH6FO0gJUxS8QsMbVzDCKEanF7I1DYFvsD3qv2ZenGiF6nTqhU0-WHUH8Cznhb2y0WgArHNSR_PWctY-w6OVTJOhC361B97SYdXk-i_tFK4idKda13PNuImwZczs8to2jg' http://localhost:8080
```

## run from anywhere

A a shell script:

```shell
#!/bin/bash
BASEDIR=$(dirname $0)
java -jar ${BASEDIR}/jwtenizr.jar "$@"%
```

will install JWTenizr "globally". Now you can launch JWTenizr from any directory you like
