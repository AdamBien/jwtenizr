
package jwtenizr;

import com.airhacks.jwtenizr.boundary.Flow;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author airhacks.com
 */
public class App {

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, Exception {
        String tokenSourceFile = System.getProperties().getProperty("tokenSourceFile");
        if (args.length == 1) {
            Flow.generateToken(args[0], tokenSourceFile);
        } else {
            Flow.generateToken(null, tokenSourceFile);
        }
    }
}
