/**
 * @author SERKAN OZAL
 *         
 *         E-Mail: <a href="mailto:serkanozal86@hotmail.com">serkanozal86@hotmail.com</a>
 *         GitHub: <a>https://github.com/serkan-ozal</a>
 */

package tr.com.serkanozal.jillegal.core.lazy;

public abstract class LazyLoader<T> implements net.sf.cglib.proxy.LazyLoader {
	
	public abstract T load() throws Exception;
	
	@Override
	public Object loadObject() throws Exception {
		return load();
	}
	
}