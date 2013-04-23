/**
 * @author SERKAN OZAL
 *         
 *         E-Mail: <a href="mailto:serkanozal86@hotmail.com">serkanozal86@hotmail.com</a>
 *         GitHub: <a>https://github.com/serkan-ozal</a>
 */

package tr.com.serkanozal.jillegal.core.domain.builder.module;

import tr.com.serkanozal.jillegal.core.domain.builder.Builder;
import tr.com.serkanozal.jillegal.core.domain.model.module.ModuleInfo;

public class ModuleInfoBuilder implements Builder<ModuleInfo> {

	private String moduleName;
	private String moduleDescription;
	private String moduleMain;

	public ModuleInfoBuilder moduleName(String moduleName) {
		this.moduleName = moduleName;
		return this;
	}

	public ModuleInfoBuilder moduleDescription(String moduleDescription) {
		this.moduleDescription = moduleDescription;
		return this;
	}

	public ModuleInfoBuilder moduleMain(String moduleMain) {
		this.moduleMain = moduleMain;
		return this;
	}
	
	@Override
	public ModuleInfo build() {
		return new ModuleInfo(moduleName, moduleDescription, moduleMain);
	}

}
