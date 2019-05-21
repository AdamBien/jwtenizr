package com.airhacks.jwtenizr;

import java.security.KeyPair;
import static junit.framework.Assert.assertNotNull;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class KeyGeneratorTest {

    @Test
    public void testGenerateKeys() throws Exception {
        KeyGenerator keyGenerator = new KeyGenerator("target/keyfile");
        KeyPair pair = keyGenerator.generateKeys();
        assertNotNull(pair);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createWithInvalidFileName() {
        new KeyGenerator("key.file");
    }


}
