/**
 * @author SERKAN OZAL
 *         
 *         E-Mail: <a href="mailto:serkanozal86@hotmail.com">serkanozal86@hotmail.com</a>
 *         GitHub: <a>https://github.com/serkan-ozal</a>
 */


package tr.com.serkanozal.jillegal.core.util;

import junit.framework.Assert;
import org.junit.Test;

import tr.com.serkanozal.jillegal.core.util.ReflectionUtil.FieldInfo;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings("deprecation")
public class ReflectionUtilTest {

	@SuppressWarnings("serial")
	public static class SampleClass implements Comparable<SampleClass>, Serializable {
		
		private int i;
		private String s;
		
		public SampleClass() {
			
		}
		
		public SampleClass(int i) {
			this.i = i;
		}
		
		public SampleClass(String s) {
			this.s = s;
		}
		
		public SampleClass(int i, String s) {
			this.i = i;
			this.s = s;
		}
		
		public int getI() {
			return i;
		}
		
		public void setI(int i) {
			this.i = i;
		}
		
		public String getS() {
			return s;
		}
		
		public void setS(String s) {
			this.s = s;
		}

		@Override
		public int compareTo(SampleClass o) {
			if (i < o.i) {
				return -1;
			}
			else if (i > o.i) {
				return +1;
			}
			else {
				return 0;
			}
		}
	}
	
	@Test
	public void getField() {
		Assert.assertEquals("i", ReflectionUtil.getField(SampleClass.class, "i").getName());
	}
	
	@Test
	public void getFieldType() {
		Assert.assertEquals(int.class, ReflectionUtil.getFieldType(SampleClass.class, "i"));
	}
	
	@Test
	public void getFieldValue() throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
		Assert.assertEquals(1, ReflectionUtil.getFieldValue(new SampleClass(1), "i"));
	}
	
	@Test
	public void getAllFields() {
		Assert.assertEquals(2, ReflectionUtil.getAllFields(SampleClass.class).size());
	}
	
	@Test
	public void getAllMethods() {
		Assert.assertEquals(5, ReflectionUtil.getAllMethods(SampleClass.class).size());
	}
	
	@Test
	public void isPrimitiveType() {
		Assert.assertTrue(ReflectionUtil.isPrimitiveType(int.class));
		Assert.assertTrue(ReflectionUtil.isPrimitiveType(Integer.class));
		Assert.assertFalse(ReflectionUtil.isPrimitiveType(SampleClass.class));
	}
	
	@Test
	public void isNonPrimitiveType() {
		Assert.assertFalse(ReflectionUtil.isNonPrimitiveType(int.class));
		Assert.assertFalse(ReflectionUtil.isNonPrimitiveType(Integer.class));
		Assert.assertTrue(ReflectionUtil.isNonPrimitiveType(SampleClass.class));
	}
	
	@Test
	public void isComplexType() {
		Assert.assertFalse(ReflectionUtil.isComplexType(String.class));
		Assert.assertTrue(ReflectionUtil.isComplexType(SampleClass.class));
	}
	
	@Test
	public void isCollectionType() {
		Assert.assertFalse(ReflectionUtil.isCollectionType(String.class));
		Assert.assertTrue(ReflectionUtil.isCollectionType(List.class));
	}
	
	@Test
	public void getNonPrimitiveType() {
		Assert.assertEquals(Boolean.class, ReflectionUtil.getNonPrimitiveType(boolean.class));
		Assert.assertEquals(Byte.class, ReflectionUtil.getNonPrimitiveType(byte.class));
		Assert.assertEquals(Character.class, ReflectionUtil.getNonPrimitiveType(char.class));
		Assert.assertEquals(Short.class, ReflectionUtil.getNonPrimitiveType(short.class));
		Assert.assertEquals(Integer.class, ReflectionUtil.getNonPrimitiveType(int.class));
		Assert.assertEquals(Float.class, ReflectionUtil.getNonPrimitiveType(float.class));
		Assert.assertEquals(Long.class, ReflectionUtil.getNonPrimitiveType(long.class));
		Assert.assertEquals(Double.class, ReflectionUtil.getNonPrimitiveType(double.class));
		Assert.assertEquals(String.class, ReflectionUtil.getNonPrimitiveType(String.class));
	}
	
	@Test
	public void isDecimalType() {
		Assert.assertTrue(ReflectionUtil.isDecimalType(byte.class));
		Assert.assertTrue(ReflectionUtil.isDecimalType(short.class));
		Assert.assertTrue(ReflectionUtil.isDecimalType(int.class));
		Assert.assertTrue(ReflectionUtil.isDecimalType(long.class));
		Assert.assertFalse(ReflectionUtil.isCollectionType(double.class));
	}
	
	@Test
	public void getClassFromPath() {
		Assert.assertEquals(int.class, ReflectionUtil.getClassFromPath("i", SampleClass.class));
		Assert.assertEquals(int.class, ReflectionUtil.getClassFromPath("getI()", SampleClass.class));
	}

	@Test
	public void getLightFieldInfo() {
		Assert.assertEquals(2, ReflectionUtil.getLightFieldInfo(SampleClass.class).getFields().size());
	}
	
	@Test
	public void getLightFieldInfoFromPath() {
		Assert.assertEquals(int.class, 
				ReflectionUtil.getLightFieldInfoFromPath("i", SampleClass.class).getFieldType());
		Assert.assertEquals(int.class, 
				ReflectionUtil.getLightFieldInfoFromPath("getI()", SampleClass.class).getFieldType());
	}
	
	@Test
	public void getFieldInfo() {
		FieldInfo fi = ReflectionUtil.getFieldInfo(SampleClass.class);
		List<FieldInfo> fields = fi.getFields();
		
		Assert.assertEquals(2, fields.size());
		
		Collections.sort(fields, new Comparator<FieldInfo>() {
			@Override
			public int compare(FieldInfo o1, FieldInfo o2) {
				return o1.getFieldName().compareTo(o2.getFieldName());
			}
		});
		FieldInfo f1 = fields.get(0);
		FieldInfo f2 = fields.get(1);
		
		Assert.assertEquals("i", f1.getFieldName());
		Assert.assertEquals(int.class, f1.getFieldType());
		Assert.assertEquals("private", f1.getModifiers());
		Assert.assertEquals("private int i", f1.getSignature());
		
		Assert.assertEquals("s", f2.getFieldName());
		Assert.assertEquals(String.class, f2.getFieldType());
		Assert.assertEquals("private", f2.getModifiers());
		Assert.assertEquals("private String s", f2.getSignature());
	}
	
	@Test
	public void gtFieldInfoFromPath() {
		Assert.assertEquals(int.class, 
				ReflectionUtil.getFieldInfoFromPath("i", SampleClass.class).getFieldType());
		Assert.assertEquals(int.class, 
				ReflectionUtil.getFieldInfoFromPath("getI()", SampleClass.class).getFieldType());
	}
	
	@Test
	public void copyAndReturnCopiedNewObject() {
		SampleClass sc = new SampleClass(1, "str");
		SampleClass scCopy = ReflectionUtil.copy(SampleClass.class, sc);
		Assert.assertEquals(sc.i, scCopy.i);
		Assert.assertEquals(sc.s, scCopy.s);
	}
	
	@Test
	public void copyToParameterObject() {
		SampleClass sc = new SampleClass(1, "str");
		SampleClass scCopy = new SampleClass();
		ReflectionUtil.copy(SampleClass.class, sc, scCopy);
		Assert.assertEquals(sc.i, scCopy.i);
		Assert.assertEquals(sc.s, scCopy.s);
	}
	
	@Test
	public void deepCopyAndReturnCopiedNewObject() {
		SampleClass sc = new SampleClass(1, "str");
		SampleClass scCopy = ReflectionUtil.deepCopy(SampleClass.class, sc);
		Assert.assertEquals(sc.i, scCopy.i);
		Assert.assertEquals(sc.s, scCopy.s);
	}
	
	@Test
	public void deepCopyToParameterObject() {
		SampleClass sc = new SampleClass(1, "str");
		SampleClass scCopy = new SampleClass();
		ReflectionUtil.deepCopy(SampleClass.class, sc, scCopy);
		Assert.assertEquals(sc.i, scCopy.i);
		Assert.assertEquals(sc.s, scCopy.s);
	}
	
	@Test
	public void serializableDeepCopyAndReturnCopiedNewObject() {
		SampleClass sc = new SampleClass(1, "str");
		SampleClass scCopy = ReflectionUtil.serializableDeepCopy(SampleClass.class, sc);
		Assert.assertEquals(sc.i, scCopy.i);
		Assert.assertEquals(sc.s, scCopy.s);
	}
	
	@Test
	public void serializableDeepCopyToParameterObject() {
		SampleClass sc = new SampleClass(1, "str");
		SampleClass scCopy = new SampleClass();
		ReflectionUtil.serializableDeepCopy(SampleClass.class, sc, scCopy);
		Assert.assertEquals(sc.i, scCopy.i);
		Assert.assertEquals(sc.s, scCopy.s);
	}

	@Test
	public void getFilteredFields() {
		List<Field> filteredFields =
			ReflectionUtil.getFilteredFields(SampleClass.class, 
				new ReflectionUtil.ClassFilter() {
					@Override
					public boolean allow(Class<?> objToFilter) {
						return true;
					}
				},
				new ReflectionUtil.FieldFilter() {
					@Override
					public boolean allow(Field objToFilter) {
						return objToFilter.getName().equals("i");
					}
			});
		
		Assert.assertEquals(1, filteredFields.size());
		Field filteredField = filteredFields.get(0);
		Assert.assertEquals("i", filteredField.getName());
		Assert.assertEquals(int.class, filteredField.getType());
	}
	
	@Test
	public void getFilteredMethods() {
		List<Method> filteredMethods =
			ReflectionUtil.getFilteredMethods(SampleClass.class, 
				new ReflectionUtil.ClassFilter() {
					@Override
					public boolean allow(Class<?> objToFilter) {
						return true;
					}
				},
				new ReflectionUtil.MethodFilter() {
					@Override
					public boolean allow(Method objToFilter) {
						return objToFilter.getName().startsWith("get");
					}
			});
		
		Assert.assertEquals(2, filteredMethods.size());
		Method filteredMethod1 = filteredMethods.get(0);
		Method filteredMethod2 = filteredMethods.get(1);
		
		Assert.assertEquals("getI", filteredMethod1.getName());
		Assert.assertEquals(int.class, filteredMethod1.getReturnType());
		
		Assert.assertEquals("getS", filteredMethod2.getName());
		Assert.assertEquals(String.class, filteredMethod2.getReturnType());
	}
	
}
