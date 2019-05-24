
package com.airhacks.jwtenizr.control;

/**
 *
 * @author airhacks.com
 */
public interface Curl {

    public static String command(String uri, String token) {
        return String.format("curl -i -H\'Authorization: Bearer %s\' %s", token, uri);
    }

}
