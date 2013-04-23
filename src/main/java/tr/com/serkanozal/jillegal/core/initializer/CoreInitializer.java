/**
 * @author SERKAN OZAL
 *         
 *         E-Mail: <a href="mailto:serkanozal86@hotmail.com">serkanozal86@hotmail.com</a>
 *         GitHub: <a>https://github.com/serkan-ozal</a>
 */

package tr.com.serkanozal.jillegal.core.initializer;

public class CoreInitializer {

	private static volatile boolean initialized = false;
	
	private CoreInitializer() {
		
	}
	
	public synchronized static void init() {
		if (initialized == false) {
			// TODO Initialize core module
			initialized = true;
		}
	}
	
}
