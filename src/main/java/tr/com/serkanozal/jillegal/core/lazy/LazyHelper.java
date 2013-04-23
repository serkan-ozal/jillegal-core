/**
 * @author SERKAN OZAL
 *         
 *         E-Mail: <a href="mailto:serkanozal86@hotmail.com">serkanozal86@hotmail.com</a>
 *         GitHub: <a>https://github.com/serkan-ozal</a>
 */

package tr.com.serkanozal.jillegal.core.lazy;

import net.sf.cglib.proxy.Enhancer;

import java.util.ArrayList;
import java.util.List;

public class LazyHelper {

	private LazyHelper() {
		
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T lazyObject(Class<T> clazz, LazyLoader<T> loader) {
		return (T)Enhancer.create(clazz, loader);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> lazyList(Class<T> clazz, LazyListLoader<T> loader) {
		return (List<T>)Enhancer.create(new ArrayList<T>().getClass(), loader);
	}
	
}
