# JWTenizr

jwtenizr creates tokens and ready to use microprofile configuration. 


`java -jar jwtenizr.jar` creates:

- `jwtenizr-config.json` with public, private key
- token stored in `token.jwt`
- default `jwt-token.json` template with principal and groups
- `microprofile-config.properties` with generated public key and default issuer (corresponds with jwt-token.json)
- `curl` command with the generated JWT token

## run from anywhere

A a shell script:

```shell
#!/bin/bash
BASEDIR=$(dirname $0)
java -jar ${BASEDIR}/jwtenizr.jar "$@"%
```

will install JWTenizr "globally". Now you can launch JWTenizr from any directory you like
