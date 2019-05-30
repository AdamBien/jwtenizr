/*
 */
package com.airhacks.jwtenizr.control;

import java.io.IOException;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
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

    @Test
    public void writeDefaultIfNotExists() throws IOException {
        Configuration.delete();
        Configuration configuration = Configuration.writeDefaultIfNotExists();
        assertNotNull(configuration);
    }

    @Test(expected = JWTenizrException.class)
    public void loadMpConfigurationLocationFromNull() throws IOException {
        Configuration.delete();
        String mpConfigurationLocation = Configuration.mpConfigurationLocation();
        assertNotNull(mpConfigurationLocation);
    }

    @Test
    public void mpConfigurationLocationFromDefault() throws IOException {
        String defaultFolder = ".";
        Configuration.delete();
        Configuration.writeDefaultIfNotExists();
        String mpConfigurationLocation = Configuration.mpConfigurationLocation();
        assertNotNull(mpConfigurationLocation);
        assertThat(mpConfigurationLocation, is(defaultFolder));
    }

}
