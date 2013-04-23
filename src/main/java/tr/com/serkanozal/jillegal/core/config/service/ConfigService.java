/**
 * @author SERKAN OZAL
 *         
 *         E-Mail: <a href="mailto:serkanozal86@hotmail.com">serkanozal86@hotmail.com</a>
 *         GitHub: <a>https://github.com/serkan-ozal</a>
 */

package tr.com.serkanozal.jillegal.core.config.service;

import java.util.List;

import tr.com.serkanozal.jillegal.core.config.provider.ConfigProvider;

public interface ConfigService<P extends ConfigProvider> {

	void addConfigProvider(P configProvider);
	void removeConfigProvider(P configProvider);
	List<P> getAllConfigProviders();
	
}
