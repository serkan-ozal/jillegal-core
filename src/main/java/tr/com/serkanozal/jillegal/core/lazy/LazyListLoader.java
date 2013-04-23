/**
 * @author SERKAN OZAL
 *         
 *         E-Mail: <a href="mailto:serkanozal86@hotmail.com">serkanozal86@hotmail.com</a>
 *         GitHub: <a>https://github.com/serkan-ozal</a>
 */

package tr.com.serkanozal.jillegal.core.lazy;

import java.util.List;

public abstract class LazyListLoader<T> implements net.sf.cglib.proxy.LazyLoader {
	
	public abstract List<T> load() throws Exception;
	
	@Override
	public Object loadObject() throws Exception {
		return load();
	}
	
}
