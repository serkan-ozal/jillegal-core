/**
 * @author SERKAN OZAL
 *         
 *         E-Mail: <a href="mailto:serkanozal86@hotmail.com">serkanozal86@hotmail.com</a>
 *         GitHub: <a>https://github.com/serkan-ozal</a>
 */

package tr.com.serkanozal.jillegal.core;

import tr.com.serkanozal.jillegal.core.initializer.CoreInitializer;
import tr.com.serkanozal.jillegal.core.initializer.ModulesInitializer;

public class Jillegal {

	public static final String GROUP_ID = "tr.com.serkanozal";
	public static final String ARTIFACT_ID = "jillegal-core";
	public static final String VERSION = "1.0.0-RELEASE";

	private Jillegal() {
		
	}
	
	public static void init() {
		CoreInitializer.init();
		ModulesInitializer.init();
	}
	
}
