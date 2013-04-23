/**
 * @author SERKAN OZAL
 *         
 *         E-Mail: <a href="mailto:serkanozal86@hotmail.com">serkanozal86@hotmail.com</a>
 *         GitHub: <a>https://github.com/serkan-ozal</a>
 */

package tr.com.serkanozal.jillegal.core.domain.model.module;

public class ModuleInfo {

	private String moduleName;
	private String moduleDescription;
	private String moduleMain;
	
	public ModuleInfo() {
		
	}
	
	public ModuleInfo(String moduleName, String moduleDescription, String moduleMain) {
		this.moduleName = moduleName;
		this.moduleDescription = moduleDescription;
		this.moduleMain = moduleMain;
	}

	public String getModuleName() {
		return moduleName;
	}
	
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	
	public String getModuleDescription() {
		return moduleDescription;
	}
	
	public void setModuleDescription(String moduleDescription) {
		this.moduleDescription = moduleDescription;
	}
	
	public String getModuleMain() {
		return moduleMain;
	}
	
	public void setModuleMain(String moduleMain) {
		this.moduleMain = moduleMain;
	}
	
}
