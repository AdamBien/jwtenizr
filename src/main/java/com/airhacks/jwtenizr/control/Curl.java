
package com.airhacks.jwtenizr.control;

/**
 *
 * @author airhacks.com
 */
public interface Curl {

    public static String generate(String uri, String token) {
        return String.format("curl -H\'Authorization: Bearer %s\' %s", token, uri);
    }

}
