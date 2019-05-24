
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
        if (args.length == 1) {
            Flow.generateToken(args[0]);
        } else {
            Flow.generateToken(null);
        }
    }
}
