/**
 * @author SERKAN OZAL
 *         
 *         E-Mail: <a href="mailto:serkanozal86@hotmail.com">serkanozal86@hotmail.com</a>
 *         GitHub: <a>https://github.com/serkan-ozal</a>
 */

package tr.com.serkanozal.jillegal.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import tr.com.serkanozal.jillegal.core.domain.builder.module.ModuleInfoBuilder;
import tr.com.serkanozal.jillegal.core.domain.model.module.ModuleInfo;

public class ModuleUtil {

	private static final String MODULE_NAME_PROPERTY = "name";
	private static final String MODULE_DESCRIPTION_PROPERTY = "description";
	private static final String MODULE_MAIN_PROPERTY = "main";
		
	private static final String MODULES_INFO_PROPERTIES_FILE_NAME = "jillegal-modules.properties";
	private static final String MODULES_SEPARATOR = ",";
	private static final String MODULES_PROPERTY = "modules";
	
	private static final String MODULE_INFO_PROPERTIES_FILE_NAME = "jillegal-module.properties";
	
	private static final Logger logger = Logger.getLogger(ModuleUtil.class);
	
	private ModuleUtil() {
		
	}
	
	public static List<ModuleInfo> getModulesInfo() {
		return getModulesInfoFromPropertiesFile();
	}
	
	private static Properties getProperties(InputStream is) {
		try {
			Properties props = new Properties();
			props.load(is);
			return props;
		}
		catch (IOException e) {
			return null;
		}
	}
	
	public static List<ModuleInfo> getModulesInfoFromPropertiesFile() {
		InputStream is = null;
		try {
			is = ModuleUtil.class.getClassLoader().getResourceAsStream(MODULES_INFO_PROPERTIES_FILE_NAME);
	    	Properties props = getProperties(is);
			String[] moduleNames = props.getProperty(MODULES_PROPERTY).split(MODULES_SEPARATOR);
			if (moduleNames != null && moduleNames.length > 0) {
				List<ModuleInfo> modulesInfo = new ArrayList<ModuleInfo>();
				for (String moduleName : moduleNames) {
					moduleName = moduleName.trim();
					String moduleDescription = StringUtils.trim(props.getProperty(moduleName + "." + MODULE_DESCRIPTION_PROPERTY));
					String moduleMain = StringUtils.trim(props.getProperty(moduleName + "." + MODULE_MAIN_PROPERTY));
					
					ModuleInfo moduleInfo = 
							new ModuleInfoBuilder().
									moduleName(moduleName).
									moduleDescription(moduleDescription).
									moduleMain(moduleMain).
								build();
					modulesInfo.add(moduleInfo);
				}
				return modulesInfo;
			}
			else {
				logger.warn("Could not be found any module");
			}
		} 
		catch (Exception e) {
			logger.error("Error occured while getting modules info", e);
		}
		finally {
			if (is != null) {
				try {
					is.close();
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public static List<ModuleInfo> getModulesInfoFromClassPath() {
		List<ModuleInfo> modulesInfo = new ArrayList<ModuleInfo>();
		String classPath = getClassPath();
		
		for (String classPathEntry : classPath.split(File.pathSeparator)) {
			classPathEntry = classPathEntry.trim();
			if (classPathEntry.length() == 0) {
				continue;
			}
			
			File classPathEntryFile = new File(classPathEntry);
            if (classPathEntryFile.isDirectory()) {
            	Properties props = lookInDirectory(classPathEntryFile);
            	if (props != null) {
            		ModuleInfo mi = getModuleInfoFromPropertiesFile(props);
            		if (mi != null) {
            			logger.info("Found module: " + mi.getModuleName() + " " + "(" + mi.getModuleDescription() + ")");
            			modulesInfo.add(mi);
            		}
            	}
            }
            else if (classPathEntryFile.isFile()) {
                String classPathEntryName = classPathEntryFile.getName().toLowerCase();
                if (classPathEntryName.startsWith("jillegal") && classPathEntryName.endsWith(".jar")) {
                	Properties props = lookInArchive(classPathEntryFile);
                	if (props != null) {
                		ModuleInfo mi = getModuleInfoFromPropertiesFile(props);
                		if (mi != null) {
                			logger.info("Found module: " + mi.getModuleName() + " " + "(" + mi.getModuleDescription() + ")");
                			modulesInfo.add(mi);
                		}
                	}
                }
            }
		}
		
		return null;
	}
	
	@SuppressWarnings("deprecation")
	private static String getClassPath() {
		StringBuilder classPathBuilder = new StringBuilder();
		
        String classPath = System.getProperty("java.class.path");
        String surefireClassPath = System.getProperty("surefire.test.class.path");
        String pathSeparator = System.getProperty("path.separator");
		
        // Add default classpath
        if (StringUtils.isNotBlank(classPath)) {
        	classPathBuilder.append(URLDecoder.decode(classPath)).append(pathSeparator);
        }
        
        URL classesLocation = ModuleUtil.class.getClassLoader().getResource("/");
		if (classesLocation != null) {
			String classesLocationPath = classesLocation.getPath();
			
			// Add library folder as classpath
			String libsLocationPath = classesLocationPath + "../lib";
			classPathBuilder.append(URLDecoder.decode(libsLocationPath)).append(pathSeparator);
			
			// Add web application library folder as classpath
			String parentPath = classesLocationPath + "../";
			File parentDir = new File(parentPath);
			for (File f : parentDir.listFiles()) {
				String webInfPath = f.getAbsolutePath() + File.separator + "WEB-INF";
				File webInfDir = new File(webInfPath);
				if (webInfDir.exists()) {
					classPathBuilder.append(URLDecoder.decode(webInfDir + File.separator + "classes")).append(pathSeparator);
					classPathBuilder.append(URLDecoder.decode(webInfDir + File.separator + "lib")).append(pathSeparator);
				}
			}
		}	
        
		// Add surefire (for unit-test) classpath
        if (StringUtils.isNotBlank(surefireClassPath)) {
        	classPathBuilder.append(URLDecoder.decode(surefireClassPath)).append(pathSeparator);
        }
        
		return classPathBuilder.toString().trim();
	}
	
	private static ModuleInfo getModuleInfoFromPropertiesFile(Properties props) {
		String moduleName = StringUtils.trim(props.getProperty(MODULE_NAME_PROPERTY));
		String moduleDescription = StringUtils.trim(props.getProperty(MODULE_DESCRIPTION_PROPERTY));
		String moduleMain = StringUtils.trim(props.getProperty(MODULE_MAIN_PROPERTY));
		return 
			new ModuleInfoBuilder().
					moduleName(moduleName).
					moduleDescription(moduleDescription).
					moduleMain(moduleMain).
				build();
	}
	
	private static Properties lookInDirectory(File directory) {
		File moduleFilePath = new File(directory + File.separator + MODULE_INFO_PROPERTIES_FILE_NAME);
    	if (moduleFilePath.exists()) {
    		try {
				return getProperties(new FileInputStream(moduleFilePath));
			} 
    		catch (FileNotFoundException e) {
    			logger.error("Unable to read module information file: " + MODULE_INFO_PROPERTIES_FILE_NAME, e);
    			return null;
			}
    	}
    	else {
    		logger.warn("Unable to find module information file: " + MODULE_INFO_PROPERTIES_FILE_NAME);
        	return null;
    	}
    }

    private static Properties lookInArchive(File archive) {
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(archive);
        } 
        catch (IOException e) {
            logger.error("Unable to read jar file: " + archive.getAbsolutePath(), e);
            return null;
        }
        
        JarEntry jarEntry = (JarEntry) jarFile.getEntry(MODULE_INFO_PROPERTIES_FILE_NAME);
        if (jarEntry == null) {
        	logger.warn("Unable to find module information file: " + MODULE_INFO_PROPERTIES_FILE_NAME);
        	return null;
        }
        
        try {
			return getProperties(jarFile.getInputStream(jarEntry));
		} 
        catch (IOException e) {
        	logger.error("Unable to get jar entry: " + jarEntry.getName());
        	return null;
		}
    }
	
}
