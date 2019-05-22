/*
 */
package com.airhacks.jwtenizr.control;

import java.io.IOException;
import static junit.framework.Assert.assertFalse;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class ConfigurationTest {

    @Test
    public void keyExists() throws IOException {

        Configuration.delete();
        assertFalse(Configuration.keysExist());

    }
}
