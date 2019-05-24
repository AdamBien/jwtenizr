
package com.airhacks.jwtenizr.control;

public class JWTenizrException extends IllegalStateException {

    public JWTenizrException(String message) {
        super(message);
        Terminal.error(message);
    }

}
