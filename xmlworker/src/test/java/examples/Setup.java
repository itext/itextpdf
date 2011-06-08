/**
 *
 */
package examples;

import com.itextpdf.text.FontFactory;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.log.SysoLogger;

/**
 * @author Balder Van Camp
 *
 */
public class Setup {

	static {
		LoggerFactory.getInstance().setLogger(new SysoLogger());
		FontFactory.registerDirectories();
	}
}
