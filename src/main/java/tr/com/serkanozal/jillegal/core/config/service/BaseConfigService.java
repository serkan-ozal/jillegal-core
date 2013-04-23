/**
 * @author SERKAN OZAL
 *         
 *         E-Mail: <a href="mailto:serkanozal86@hotmail.com">serkanozal86@hotmail.com</a>
 *         GitHub: <a>https://github.com/serkan-ozal</a>
 */

package tr.com.serkanozal.jillegal.core.config.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import tr.com.serkanozal.jillegal.core.config.provider.ConfigProvider;

public abstract class BaseConfigService<P extends ConfigProvider> implements ConfigService<P> {

	protected final Logger logger = Logger.getLogger(getClass());
	
	protected List<P> configProviderList = new ArrayList<P>();
	
	public BaseConfigService() {
		init();
	}
	
	abstract protected void init();
	
	protected void addConfigProviderIfAvailable(P configProvider) {
		if (configProvider.isAvailable()) {
			configProviderList.add(configProvider);
		}
	}
	
	@Override
	public void addConfigProvider(P configProvider) {
		configProviderList.add(configProvider);
	}
	
	@Override
	public void removeConfigProvider(P configProvider) {
		configProviderList.remove(configProvider);
	}
	
	@Override
	public List<P> getAllConfigProviders() {
		return configProviderList;
	}
	
}
