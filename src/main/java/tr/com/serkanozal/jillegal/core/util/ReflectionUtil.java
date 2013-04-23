/**
 * @author SERKAN OZAL
 *         
 *         E-Mail: <a href="mailto:serkanozal86@hotmail.com">serkanozal86@hotmail.com</a>
 *         GitHub: <a>https://github.com/serkan-ozal</a>
 */

package tr.com.serkanozal.jillegal.core.util;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class ReflectionUtil {
	
	private ReflectionUtil() {
		
	}
	
	public static String getGetterMethodName(String fieldName) {
		return "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
	}
	
	public static String getSetterMethodName(String fieldName) {
		return "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
	}
	
	public static Field getField(Class<?> cls, String fieldName) {
		if (cls == null || cls.equals(Object.class)) {
			return null;
		}
		try {
			return cls.getDeclaredField(fieldName);
		} 
		catch (Exception e) {
			return getField(cls.getSuperclass(), fieldName);
		} 
	}
	
	public static Class<?> getFieldType(Class<?> cls, String fieldName) {
		if (cls == null || cls.equals(Object.class)) {
			return null;
		}
		Field field = getField(cls, fieldName);
		if (field == null) {
			return null;
		}
		else {
			return field.getType();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getFieldValue(Object obj, String fieldName) 
			throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Class<?> cls = obj.getClass();
		Field field = getField(cls, fieldName);
		field.setAccessible(true);
		return (T)field.get(obj);
	}
	
	@SuppressWarnings("unchecked")
	public static List<Field> getAllFields(Class<?> cls) {
		List<Field> fields = new ArrayList<Field>();
		createFields(cls, fields);
		return fields;
	}
	
	public static List<Field> getAllFields(Class<?> cls, Class<? extends Annotation> ... annotationFilters) {
		List<Field> fields = new ArrayList<Field>();
		createFields(cls, fields, annotationFilters);
		return fields;
	}
	
	private static void createFields(Class<?> cls, List<Field> fields, Class<? extends Annotation> ... annotationFilters) {
		if (cls == null || cls.equals(Object.class)) {
			return;
		}
		
		Class<?> superCls = cls.getSuperclass();
		createFields(superCls, fields, annotationFilters);
		
		for (Field f : cls.getDeclaredFields()) {
			f.setAccessible(true);
			if (annotationFilters == null || annotationFilters.length == 0) {
				fields.add(f);
			}	
			else {
				for (Class<? extends Annotation> annotationFilter : annotationFilters) {
					if (f.getAnnotation(annotationFilter) != null) {
						fields.add(f);
						break;
					}
				}	
			}
		}	
	}
	
	@SuppressWarnings("unchecked")
	public static List<Method> getAllMethods(Class<?> cls) {
		List<Method> methods = new ArrayList<Method>();
		createMethods(cls, methods);
		return methods;
	}
	
	public static List<Method> getAllMethods(Class<?> cls, Class<? extends Annotation> ... annotationFilters) {
		List<Method> methods = new ArrayList<Method>();
		createMethods(cls, methods, annotationFilters);
		return methods;
	}
	
	private static void createMethods(Class<?> cls, List<Method> methods, Class<? extends Annotation> ... annotationFilters) {
		if (cls == null || cls.equals(Object.class)) {
			return;
		}
		
		Class<?> superCls = cls.getSuperclass();
		createMethods(superCls, methods, annotationFilters);
		
		for (Method m : cls.getDeclaredMethods()) {	
			if (m.isBridge() || m.isSynthetic()) {
				continue;
			}
			if (annotationFilters == null || annotationFilters.length == 0) {
				if (methods.contains(m) == false) {
					methods.add(m);
				}	
			}	
			else {
				for (Class<? extends Annotation> annotationFilter : annotationFilters) {
					if (m.getAnnotation(annotationFilter) != null) {
						if (methods.contains(m) == false) {
							methods.add(m);
							break;
						}	
					}	
				}	
			}
		}	
	}
	
	public static boolean isPrimitiveType(Class<?> cls) {
		if (cls.equals(boolean.class) || cls.equals(Boolean.class)) {
			return true;
		}	
		else if (cls.equals(byte.class) || cls.equals(Byte.class)) {
			return true;
		}	
		else if (cls.equals(char.class) || cls.equals(Character.class)) {
			return true;
		}	
		else if (cls.equals(short.class) || cls.equals(Short.class)) {
			return true;
		}	
		else if (cls.equals(int.class) || cls.equals(Integer.class)) {
			return true;
		}	
		else if (cls.equals(float.class) || cls.equals(Float.class)) {
			return true;
		}	
		else if (cls.equals(long.class) || cls.equals(Long.class)) {
			return true;
		}	
		else if (cls.equals(double.class) || cls.equals(Double.class)) {
			return true;
		}
		else if (cls.equals(String.class)) {
			return true;
		}
		else {
			return false;
		}	
	}
	
	public static boolean isNonPrimitiveType(Class<?> cls) {
		return !isPrimitiveType(cls);
	}
	
	public static boolean isComplexType(Class<?> cls) {
		if (isPrimitiveType(cls)) {
			return false;
		}
		else if (cls.isEnum()) {
			return false;
		}
		else if (cls.equals(String.class)) {
			return false;
		}
		else if (isCollectionType(cls)) {
			return false;
		}
		else if (List.class.isAssignableFrom(cls)) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public static boolean isCollectionType(Class<?> cls) {
		if (cls.isArray()) {
			return true;
		}
		else if (List.class.isAssignableFrom(cls)) {
			return true;
		}
		else if (Set.class.isAssignableFrom(cls)) {
			return true;
		}
		else if (Map.class.isAssignableFrom(cls)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static Class<?> getNonPrimitiveType(Class<?> cls) {
		if (cls.equals(boolean.class)) {
			return Boolean.class;
		}	
		else if (cls.equals(byte.class)) {
			return Byte.class;
		}	
		else if (cls.equals(char.class)) {
			return Character.class;
		}
		else if (cls.equals(short.class)) {
			return Short.class;
		}	
		else if (cls.equals(int.class)) {
			return Integer.class;
		}	
		else if (cls.equals(float.class)) {
			return Float.class;
		}	
		else if (cls.equals(long.class)) {
			return Long.class;
		}	
		else if (cls.equals(double.class)) {
			return Double.class;
		}	
		else {
			return cls;
		}	
	}
	
	public static boolean isDecimalType(Class<?> cls) {
		if (cls.equals(byte.class) || cls.equals(Byte.class)) {
			return true;
		}	
		else if (cls.equals(short.class) || cls.equals(Short.class)) {
			return true;
		}	
		else if (cls.equals(int.class) || cls.equals(Integer.class)) {
			return true;
		}	
		else if (cls.equals(long.class) || cls.equals(Long.class)) {
			return true;
		}	
		else {
			return false;
		}
	}	
	
	public static Class<?> getClassFromPath(String path, Class<?> baseClass) {
		if(path == null || path.length() == 0) {
			return baseClass;
		}
		else {
			return createClassFromPath(path, baseClass);
		}	
	}
	
	private static Class<?> createClassFromPath(String path, Class<?> baseClass) {
		return createClassFromPath(path, baseClass, null);
	}
	
	private static Class<?> createClassFromPath(String path, Class<?> baseClass, Object baseObject) {
		if (isComplexType(baseClass) == false) {
			return baseClass;
		}
		
		if (path == null || path.length() == 0) {
			return baseClass;
		}

		try {
			int dotIndex = path.indexOf('.');
			if (dotIndex > 0) {
				String accessName = path.substring(0, dotIndex);
				path = path.substring(dotIndex + 1);
				if (accessName.matches("\\w+\\(.*\\)")) {
					accessName = accessName.substring(0, accessName.indexOf('('));
					Method method = baseClass.getDeclaredMethod(accessName);
					if (method == null) {
						return null;
					}
					else {
						if (method.getReturnType().equals(void.class)) {
							return null;
						}
						else {
							return createClassFromPath(path, method.getReturnType(), baseObject);
						}	
					}
				}
				else {
					Field member = getField(baseClass, accessName);
					if (member == null) {
						return null;
					}
					else {
						return createClassFromPath(path, member.getType(), baseObject);
					}
				}	
			}
			else {
				String accessName = path;
				if (accessName.matches("\\w+\\(.*\\)")) {
					accessName = accessName.substring(0, accessName.indexOf('('));
					Method method = baseClass.getDeclaredMethod(accessName);
					if (method == null) {
						return null;
					}
					else {
						if (method.getReturnType().equals(void.class)){
							return null;
						}
						else {
							return method.getReturnType();
						}	
					}
				}
				else {
					Field member = getField(baseClass, accessName);
					if (member == null) {
						return null;
					}
					else {
						return member.getType();
					}
				}	
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static FieldInfo getLightFieldInfo(Class<?> cls) {
		if (cls == null) {
			return null;
		}
		FieldInfo fi = new FieldInfo(cls);
		createFieldInfo(fi, false);
		return fi;
	}
	
	public static FieldInfo getLightFieldInfoFromPath(String path, Class<?> baseClass) {
		return getLightFieldInfo(getClassFromPath(path, baseClass));
	}
	
	public static FieldInfo getFieldInfo(Class<?> cls) {
		FieldInfo fi = new FieldInfo(cls);
		createFieldInfo(fi, true);
		return fi;
	}
	
	public static FieldInfo getFieldInfoFromPath(String path, Class<?> baseClass) {
		return getFieldInfo(getClassFromPath(path, baseClass));
	}
	
	private static void createFieldInfo(FieldInfo fieldInfo, boolean isRecursive) {
		Class<?> cls = fieldInfo.getFieldType();
		if (cls == null || cls.equals(Object.class) || isComplexType(cls) == false) {
			return;
		}
		
		List<Field> fields = getAllFields(cls);
		for (Field f : fields) {
			fieldInfo.addField(new FieldInfo(f));
		}
		
		List<Method> methods = getAllMethods(cls);
		for (Method m : methods) {
			MethodInfo mi = new MethodInfo(m);
			fieldInfo.addMethod(mi);
		}
	}
	
	public static FieldInfo getCollectionFieldInfo(Object collectionObj) {
		FieldInfo fieldInfo = null;
		Class<?> cls = collectionObj.getClass();
		
		if (List.class.isAssignableFrom(cls)) {
			fieldInfo = new FieldInfo(cls);
			List<?> list = (List<?>)collectionObj;
			for (Object o : list) {
				if (o == null) {
					fieldInfo.addField(null);
				}
				else {
					FieldInfo fi = getFieldInfo(o.getClass());
					fi.setOwnerObject(o);
					fieldInfo.addField(fi);
				}	
			}
		}
		else if (Set.class.isAssignableFrom(cls)) {
			fieldInfo = new FieldInfo(cls);
			Set<?> set = (Set<?>)collectionObj;
			for (Object o : set) {
				if (o == null) {
					fieldInfo.addField(null);
				}
				else {
					FieldInfo fi = getFieldInfo(o.getClass());
					fi.setOwnerObject(o);
					fieldInfo.addField(fi);
				}	
			}
		}
		else if (Map.class.isAssignableFrom(cls)) {
			fieldInfo = new FieldInfo(cls);
			Map<?, ?> map = (Map<?, ?>)collectionObj;
			for (Object key : map.keySet()) {
				Object value = map.get(key);
				if (value == null) {
					fieldInfo.addField(null);
				}
				else {
					FieldInfo fi = getFieldInfo(value.getClass());
					fi.setOwnerObject(value);
					fieldInfo.addField(fi);
				}	
			}
		}
		else if (cls.isArray()) {
			fieldInfo = new FieldInfo(cls);
			Class<?> arrayType = cls.getComponentType();
			
			if (arrayType.equals(boolean.class)) {
				boolean[] array = (boolean[])collectionObj;
				for (Boolean element : array) {
					FieldInfo fi = getFieldInfo(element.getClass());
					fi.setOwnerObject(element);
					fieldInfo.addField(fi);
				}
			}
			else if (arrayType.equals(Boolean.class)) {
				Boolean[] array = (Boolean[])collectionObj;
				for (Boolean element : array) {
					FieldInfo fi = getFieldInfo(element.getClass());
					fi.setOwnerObject(element);
					fieldInfo.addField(fi);
				}
			}
			else if (arrayType.equals(byte.class)) {
				byte[] array = (byte[])collectionObj;
				for (Byte element : array) {
					FieldInfo fi = getFieldInfo(element.getClass());
					fi.setOwnerObject(element);
					fieldInfo.addField(fi);
				}
			}
			else if (arrayType.equals(Byte.class)) {
				Byte[] array = (Byte[])collectionObj;
				for (Byte element : array) {
					FieldInfo fi = getFieldInfo(element.getClass());
					fi.setOwnerObject(element);
					fieldInfo.addField(fi);
				}
			}
			else if (arrayType.equals(char.class)) {
				char[] array = (char[])collectionObj;
				for (Character element : array) {
					FieldInfo fi = getFieldInfo(element.getClass());
					fi.setOwnerObject(element);
					fieldInfo.addField(fi);
				}
			}
			else if (arrayType.equals(Character.class)) {
				Character[] array = (Character[])collectionObj;
				for (Character element : array) {
					FieldInfo fi = getFieldInfo(element.getClass());
					fi.setOwnerObject(element);
					fieldInfo.addField(fi);
				}
			}
			else if (arrayType.equals(short.class)) {
				short[] array = (short[])collectionObj;
				for (Short element : array) {
					FieldInfo fi = getFieldInfo(element.getClass());
					fi.setOwnerObject(element);
					fieldInfo.addField(fi);
				}
			}
			else if (arrayType.equals(Short.class)) {
				Short[] array = (Short[])collectionObj;
				for (Short element : array) {
					FieldInfo fi = getFieldInfo(element.getClass());
					fi.setOwnerObject(element);
					fieldInfo.addField(fi);
				}
			}
			else if (arrayType.equals(int.class)) {
				int[] array = (int[])collectionObj;
				for (Integer element : array) {
					FieldInfo fi = getFieldInfo(element.getClass());
					fi.setOwnerObject(element);
					fieldInfo.addField(fi);
				}
			}
			else if (arrayType.equals(Integer.class)) {
				Integer[] array = (Integer[])collectionObj;
				for (Integer element : array) {
					FieldInfo fi = getFieldInfo(element.getClass());
					fi.setOwnerObject(element);
					fieldInfo.addField(fi);
				}
			}
			else if (arrayType.equals(float.class)) {
				float[] array = (float[])collectionObj;
				for (Float element : array) {
					FieldInfo fi = getFieldInfo(element.getClass());
					fi.setOwnerObject(element);
					fieldInfo.addField(fi);
				}
			}
			else if (arrayType.equals(Float.class)) {
				Float[] array = (Float[])collectionObj;
				for (Float element : array) {
					FieldInfo fi = getFieldInfo(element.getClass());
					fi.setOwnerObject(element);
					fieldInfo.addField(fi);
				}
			}
			else if (arrayType.equals(long.class)) {
				long[] array = (long[])collectionObj;
				for (Long element : array) {
					FieldInfo fi = getFieldInfo(element.getClass());
					fi.setOwnerObject(element);
					fieldInfo.addField(fi);
				}
			}
			else if (arrayType.equals(Long.class)) {
				Long[] array = (Long[])collectionObj;
				for (Long element : array) {
					FieldInfo fi = getFieldInfo(element.getClass());
					fi.setOwnerObject(element);
					fieldInfo.addField(fi);
				}
			}
			else if (arrayType.equals(double.class)) {
				double[] array = (double[])collectionObj;
				for (Double element : array) {
					FieldInfo fi = getFieldInfo(element.getClass());
					fi.setOwnerObject(element);
					fieldInfo.addField(fi);
				}
			}
			else if (arrayType.equals(Double.class)) {
				Double[] array = (Double[])collectionObj;
				for (Double element : array) {
					FieldInfo fi = getFieldInfo(element.getClass());
					fi.setOwnerObject(element);
					fieldInfo.addField(fi);
				}
			}
			else if (arrayType.equals(String.class)) {
				String[] array = (String[])collectionObj;
				for (String element : array) {
					FieldInfo fi = getFieldInfo(element.getClass());
					fi.setOwnerObject(element);
					fieldInfo.addField(fi);
				}
			}
			else {
				Object[] array = (Object[])collectionObj;
				for (Object element : array) {
					FieldInfo fi = getFieldInfo(element.getClass());
					fi.setOwnerObject(element);
					fieldInfo.addField(fi);
				}
			}
		}
		
		return fieldInfo;
	}
	
	public static <T> T copy(Class<T> cls, T source) {
		try {
			T copied = cls.newInstance();
			copy(cls, source, copied);
			return copied;
		} 
		catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	public static <T> void copy(Class<T> cls, T source, T target) {
		try {
			List<Field> fields = getAllFields(cls);
			for (Field f : fields) {
				f.set(target, f.get(source));
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static <T> T deepCopy(Class<T> cls, T source) {
		try {
			T copied = cls.newInstance();
			deepCopy(cls, source, copied);
			return copied;
		} 
		catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	public static <T> void deepCopy(Class<T> cls, T source, T target) {
		try {
			Map<Integer, Object> copiedMap = new HashMap<Integer, Object>();
			List<Field> fields = getAllFields(cls);
			for (Field f : fields) {
				if (ReflectionUtil.isCollectionType(f.getType())) {
					Object nonPrimitiveValue = f.get(source);
					Integer identityCode = System.identityHashCode(nonPrimitiveValue);
					Object cachedObject = copiedMap.get(identityCode);
					if (cachedObject == null) {
						copiedMap.put(identityCode, nonPrimitiveValue);
						f.set(target, copyCollection(f.getType(), nonPrimitiveValue, copiedMap));
					}
					else {
						f.set(target, cachedObject);
					}
				}
				else {
					Object nonPrimitiveValue = f.get(source);
					Integer identityCode = System.identityHashCode(nonPrimitiveValue);
					Object cachedObject = copiedMap.get(identityCode);
					if (cachedObject == null) {
						copiedMap.put(identityCode, nonPrimitiveValue);
						f.set(target, copyObject(f.getType(), nonPrimitiveValue, copiedMap));
					}
					else {
						f.set(target, cachedObject);
					}
				}
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	private static Object copyObject(Class<?> cls, Object source, Map<Integer, Object> copiedMap) {
		try {
			if (isPrimitiveType(cls)) {
				return source;
			}
			Object copied = cls.newInstance();
			List<Field> fields = getAllFields(cls);
			for (Field f : fields) {
				if (isPrimitiveType(f.getType())) {
					f.set(copied, f.get(source));
				}
				else {
					Object nonPrimitiveValue = f.get(source);
					Integer identityCode = System.identityHashCode(nonPrimitiveValue);
					Object cachedObject = copiedMap.get(identityCode);
					if (cachedObject == null) {
						copiedMap.put(identityCode, nonPrimitiveValue);
						f.set(copied, copyObject(f.getType(), nonPrimitiveValue, copiedMap));
					}
					else {
						f.set(copied, cachedObject);
					}
				}
			}
			return copied;
		} 
		catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	@SuppressWarnings("unchecked")
	private static Object copyCollection(Class<?> cls, Object source, Map<Integer, Object> copiedMap) {
		try {
			if (List.class.isAssignableFrom(cls)) {
				List<Object> copiedList = (List<Object>)cls.newInstance();
				List<Object> sourceList = (List<Object>)source;
				for (Object o : sourceList) {
					if (o != null) {
						Integer identityCode = System.identityHashCode(o);
						Object cachedObject = copiedMap.get(identityCode);
						if (cachedObject == null) {
							copiedMap.put(identityCode, o);
							copiedList.add(copyObject(o.getClass(), o, copiedMap));
						}	
						else {
							copiedList.add(cachedObject);
						}
					}
				}
				return copiedList;
			}
			else if (Set.class.isAssignableFrom(cls)) {
				Set<Object> copiedSet = (Set<Object>)cls.newInstance();
				Set<Object> sourceSet = (Set<Object>)source;
				for (Object o : sourceSet) {
					if (o != null) {
						Integer identityCode = System.identityHashCode(o);
						Object cachedObject = copiedMap.get(identityCode);
						if (cachedObject == null) {
							copiedMap.put(identityCode, o);
							copiedSet.add(copyObject(o.getClass(), o, copiedMap));
						}	
						else {
							copiedSet.add(cachedObject);
						}	
					}
				}
				return copiedSet;
			}
			else if (Map.class.isAssignableFrom(cls)) {
				Map<Object, Object> copyMap = (Map<Object, Object>)cls.newInstance();
				Map<Object, Object> sourceMap = (Map<Object, Object>)source;
				for (Object key : sourceMap.keySet()) {
					Object value = sourceMap.get(key);
					Integer identityCode = System.identityHashCode(value);
					Object cachedObject = copiedMap.get(identityCode);
					if (cachedObject == null) {
						copiedMap.put(identityCode, value);
						copyMap.put(key, copyObject(value.getClass(), value, copiedMap));
					}
					else {
						copyMap.put(key, cachedObject);
					}	
				}
				return copiedMap;
			}
			else {
				return null;
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T serializableDeepCopy(Class<T> cls, T source) {
		try {
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        ObjectOutputStream out = new ObjectOutputStream(bos);
	        out.writeObject(source);
	        out.flush();
	        out.close();
	
	        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
	        T copied = (T)in.readObject();
	        
	        return copied;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static <T extends Serializable> void serializableDeepCopy(Class<T> cls, T source, T target) {
		T copied = serializableDeepCopy(cls, source);
		try {
			List<Field> fields = getAllFields(cls);
			for (Field f : fields) {
				Object val = f.get(copied);
				if (val != null) {
					f.set(target, val);
				}	
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static boolean haveReturnType(Method m) {
		return m.getReturnType().equals(void.class) == false;
	}
	
	public static abstract class JavaInfo implements Comparable<JavaInfo> {
		
		protected Object ownerObject;
		
		public Object getOwnerObject() {
			return ownerObject;
		}
		
		public void setOwnerObject(Object ownerObject) {
			this.ownerObject = ownerObject;
		}
		
		public abstract String getModifiers();
		public abstract String getType();
		public abstract String getBody();
		public abstract String getCompareValue();
		
		@Override
		public int compareTo(JavaInfo ji) {
			return getCompareValue().compareTo(ji.getCompareValue());
		}
		
	}
	
	public static class FieldInfo extends JavaInfo {
		
		private Field field;
		private String fieldName;
		private Class<?> fieldType;
		private List<FieldInfo> fields = new ArrayList<FieldInfo>();
		private List<MethodInfo> methods = new ArrayList<MethodInfo>();
		
		public FieldInfo(Field field) {
			this.field = field;
			if (field != null) {
				fieldName = field.getName();
				fieldType = field.getType();
			}
		}
		
		public FieldInfo(Class<?> cls) {
			if (cls != null) {
				fieldName = cls.getSimpleName();
				fieldName = Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1);
				fieldType = cls;
			}
		}
		
		public Field getField() {
			return field;
		}
		
		public String getFieldName() {
			return fieldName;
		}
		
		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}
		
		public Class<?> getFieldType() {
			return fieldType;
		}
		
		public void setFieldType(Class<?> fieldType) {
			this.fieldType = fieldType;
		}

		public void addField(FieldInfo fi) {
			fields.add(fi);
		}
		
		public List<FieldInfo> getFields() {
			return fields;
		}
		
		public void addMethod(MethodInfo mi) {
			methods.add(mi);
		}
		
		public List<MethodInfo> getMethods() {
			return methods;
		}
		
		
		public String getSignature() {
			if (field == null) {
				return 
					getFieldType().getSimpleName() 	+ " " + 
					getFieldName();	
			}
			else {
				return 
					getModifiers() + " " +
					getFieldType().getSimpleName() + " " + 
					getFieldName();	
			}
		}
		
		public String toString() {
			return getSignature();
		}
		
		@Override
		public String getModifiers() {
			return Modifier.toString(field.getModifiers());
		}

		@Override
		public String getType() {
			return getFieldType().getSimpleName();
		}

		@Override
		public String getBody() {
			return getFieldName();
		}

		@Override
		public String getCompareValue() {
			return getBody();
		}
		
	}
	
	public static class MethodInfo extends JavaInfo {
		
		private Method method;
		
		public MethodInfo(Method method) {
			this.method = method;
		}
		
		public Method getMethod() {
			return method;
		}
		
		public String getMethodName() {
			return method.getName();
		}
		
		public Class<?> getReturnType() {
			return method.getReturnType();
		}
		
		public String getParameters() {
			String parameters = "";
			for (Class<?> pt : method.getParameterTypes()) {
				if (parameters.length() > 0) {
					parameters += ", ";
				}
				parameters += pt.getSimpleName();
			}
			return parameters;
		}
		
		public String getSignature() {
			String parameters = "";
			for (Class<?> pt : method.getParameterTypes()) {
				if (parameters.length() > 0) {
					parameters += ", ";
				}
				parameters += pt.getSimpleName();
			}
			
			return
				Modifier.toString(method.getModifiers()) 	+ " " +
				method.getReturnType().getSimpleName() 		+ " " +
				getBody();
		}
		
		public String toString() {
			return getSignature();
		}

		public String getModifiers() {
			return Modifier.toString(method.getModifiers());
		}
		
		@Override
		public String getType() {
			return getReturnType().getSimpleName();
		}

		@Override
		public String getBody() {
			return method.getName() + "(" + getParameters() + ")";
		}

		@Override
		public String getCompareValue() {
			return getBody();
		}
		
	}
	
	public static interface Filter<T> {
		public boolean allow(T objToFilter);
	}
	
	public static interface FieldFilter extends Filter<Field> {
		// Override public boolean allow(Field fieldToFilter) ...
	}
	
	public static interface MethodFilter extends Filter<Method> {
		// Override public boolean allow(Method methodToFilter) ...
	}
	
	public static interface ClassFilter extends Filter<Class<?>> {
		// Override public boolean allow(Method clsToFilter) ...
	}
	
	public static abstract class AnnotatedFilter {
		
		protected Class<? extends Annotation> annotation;
		
		public AnnotatedFilter(Class<? extends Annotation> annotation) {
			this.annotation = annotation;
		}

	}
	
	public static class AnnotatedFieldFilter extends AnnotatedFilter implements FieldFilter {

		public AnnotatedFieldFilter(Class<? extends Annotation> annotation) {
			super(annotation);
		}

		@Override
		public boolean allow(Field fieldToFilter) {
			return fieldToFilter.getAnnotation(annotation) != null;
		}
		
	}
	
	public static class AnnotatedMethodFilter extends AnnotatedFilter implements MethodFilter {

		public AnnotatedMethodFilter(Class<? extends Annotation> annotation) {
			super(annotation);
		}

		@Override
		public boolean allow(Method methodToFilter) {
			return methodToFilter.getAnnotation(annotation) != null;
		}
		
	}
	
	public static class AnnotatedClassFilter extends AnnotatedFilter implements ClassFilter {

		public AnnotatedClassFilter(Class<? extends Annotation> annotation) {
			super(annotation);
		}

		@Override
		public boolean allow(Class<?> clsToFilter) {
			return clsToFilter.getAnnotation(annotation) != null;
		}
		
	}
	
	public static List<Field> getFilteredFields(Class<?> cls, ClassFilter clsFilter, FieldFilter fieldFilter) {
		List<Field> fields = new ArrayList<Field>();
		createFilteredFields(cls, fields, clsFilter, fieldFilter);
		return fields;
	}
	
	private static void createFilteredFields(Class<?> cls, List<Field> fields, ClassFilter clsFilter, FieldFilter filter) {
		if (cls == null || cls.equals(Object.class)) {
			return;
		}
		
		Class<?> superCls = cls.getSuperclass();
		if (clsFilter == null || clsFilter.allow(superCls)) {
			createFilteredFields(superCls, fields, clsFilter, filter);
		}
		
		for (Field f : getAllFields(cls)) {
			f.setAccessible(true);
			if (filter == null || filter.allow(f)) {
				fields.add(f);
			}	
		}	
	}
	
	public static List<Method> getFilteredMethods(Class<?> cls, ClassFilter clsFilter, MethodFilter methodFilter) {
		List<Method> methods = new ArrayList<Method>();
		createFilteredMethods(cls, methods, clsFilter, methodFilter);
		return methods;
	}
	
	private static void createFilteredMethods(Class<?> cls, List<Method> methods, ClassFilter clsFilter, 
			MethodFilter methodFilter) {
		if (cls == null || cls.equals(Object.class)) {
			return;
		}
		
		Class<?> superCls = cls.getSuperclass();
		if (clsFilter == null || clsFilter.allow(superCls)) {
			createFilteredMethods(superCls, methods, clsFilter, methodFilter);
		}
		
		for (Method m : getAllMethods(cls)) {	
			m.setAccessible(true);
			if (methodFilter == null || methodFilter.allow(m)) {
				methods.add(m);
			}	
		}	
	}
	
}
