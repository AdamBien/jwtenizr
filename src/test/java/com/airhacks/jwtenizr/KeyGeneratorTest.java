/*
 */
package com.airhacks.jwtenizr;

import java.security.KeyPair;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

/**
 *
 * @author airhacks.com
 */
public class KeyGeneratorTest {

    @Test
    public void testGenerateKeys() throws Exception {
        KeyGenerator keyGenerator = new KeyGenerator("keyfile");
        KeyPair pair = keyGenerator.generateKeys();
        assertNotNull(pair);
    }

}
