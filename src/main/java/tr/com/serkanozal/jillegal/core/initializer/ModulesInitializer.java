/**
 * @author SERKAN OZAL
 *         
 *         E-Mail: <a href="mailto:serkanozal86@hotmail.com">serkanozal86@hotmail.com</a>
 *         GitHub: <a>https://github.com/serkan-ozal</a>
 */

package tr.com.serkanozal.jillegal.core.initializer;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.log4j.Logger;

import tr.com.serkanozal.jillegal.core.domain.model.module.ModuleInfo;
import tr.com.serkanozal.jillegal.core.util.ModuleUtil;

public class ModulesInitializer {

	private static final Logger logger = Logger.getLogger(ModulesInitializer.class);
	
	private static volatile boolean initialized = false;
	
	private ModulesInitializer() {
		
	}
	
	public synchronized static void init() {
		if (initialized == false) {
			List<ModuleInfo> modulesInfo = ModuleUtil.getModulesInfo();
			if (modulesInfo != null) {
				for (ModuleInfo moduleInfo : modulesInfo) {
					try {
						Class<?> moduleMain = Class.forName(moduleInfo.getModuleMain());
						logger.debug("\"" + moduleInfo.getModuleName() + "\"" + " module found");
						Method initMethod = null;
						try {
							initMethod = moduleMain.getMethod("init");
						}
						catch (Exception e) {
							logger.debug("Initialization method could not be found for module " + moduleInfo.getModuleName() , e);
							continue;
						} 
						if (initMethod != null) {
							try {
								initMethod.invoke(null);
								logger.info("\"" + moduleInfo.getModuleName() + "\"" + " successfully initialized");
							}
							catch (Exception e) {
								logger.debug("Initialization method could not be invoked for module " + moduleInfo.getModuleName() , e);
								continue;
							} 
						}
					} 
					catch (ClassNotFoundException e) {
						logger.debug("Main class could not be found for module " + moduleInfo.getModuleName() , e);
						continue;
					} 
				}
			}
			initialized = true;
		}
	}
	
}
