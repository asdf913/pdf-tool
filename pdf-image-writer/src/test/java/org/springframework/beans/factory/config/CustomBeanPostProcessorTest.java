package org.springframework.beans.factory.config;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.FieldOrMethod;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.pdfbox.pdmodel.ImagePdfWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CustomBeanPostProcessorTest {

	private static Pattern PATTERN = null;

	private static java.lang.reflect.Method METHOD_STREAM, METHOD_FILTER, METHOD_FIND_ANY, METHOD_GET,
			METHOD_GET_METHODS, METHOD_GET_CODE1, METHOD_GET_CODE2, METHOD_GET_NAME, METHOD_GET_CONSTANT_POOL,
			METHOD_MATCHER, METHOD_MATCHES = null;

	@BeforeAll
	static void beforeAll() throws ReflectiveOperationException {
		//
		final Class<?> clz = CustomBeanPostProcessor.class;
		//
		(METHOD_STREAM = clz.getDeclaredMethod("stream", Object[].class)).setAccessible(true);
		//
		(METHOD_FILTER = clz.getDeclaredMethod("filter", Stream.class, Predicate.class)).setAccessible(true);
		//
		(METHOD_FIND_ANY = clz.getDeclaredMethod("findAny", Stream.class)).setAccessible(true);
		//
		(METHOD_GET = clz.getDeclaredMethod("get", Optional.class)).setAccessible(true);
		//
		(METHOD_GET_METHODS = clz.getDeclaredMethod("getMethods", JavaClass.class)).setAccessible(true);
		//
		(METHOD_GET_CODE1 = clz.getDeclaredMethod("getCode", Method.class)).setAccessible(true);
		//
		(METHOD_GET_CODE2 = clz.getDeclaredMethod("getCode", Code.class)).setAccessible(true);
		//
		(METHOD_GET_NAME = clz.getDeclaredMethod("getName", FieldOrMethod.class)).setAccessible(true);
		//
		(METHOD_GET_CONSTANT_POOL = clz.getDeclaredMethod("getConstantPool", FieldOrMethod.class)).setAccessible(true);
		//
		(METHOD_MATCHER = clz.getDeclaredMethod("matcher", Pattern.class, CharSequence.class)).setAccessible(true);
		//
		(METHOD_MATCHES = clz.getDeclaredMethod("matches", Matcher.class)).setAccessible(true);
		//
		final Object object = FieldUtils.readDeclaredStaticField(clz, "PATTERN", true);
		PATTERN = object instanceof Pattern ? (Pattern) object : null;
		//
	}

	@Test
	void testPostProcessBeforeInitialization() throws ReflectiveOperationException {
		//
		final CustomBeanPostProcessor instance = new CustomBeanPostProcessor();
		Assertions.assertNull(instance.postProcessBeforeInitialization(null, null));
		//
		final Constructor<ImagePdfWriter> constructor = ImagePdfWriter.class.getDeclaredConstructor();
		if (constructor != null) {
			constructor.setAccessible(true);
		}
		final ImagePdfWriter imagePdfWriter = constructor != null ? constructor.newInstance() : null;
		Assertions.assertSame(imagePdfWriter, instance.postProcessBeforeInitialization(imagePdfWriter, null));
		//
	}

	@Test
	void testStream() throws Throwable {
		//
		Assertions.assertNull(stream(null));
		Assertions.assertNotNull(stream(new Object[0]));
		//
	}

	private static <T> Stream<T> stream(final T[] instance) throws Throwable {
		try {
			final Object obj = METHOD_STREAM.invoke(null, (Object) instance);
			if (obj == null) {
				return null;
			} else if (obj instanceof Stream) {
				return (Stream) obj;
			}
			throw new Throwable(toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	private static String toString(final Object instance) {
		return instance != null ? instance.toString() : null;
	}

	private static Class<?> getClass(final Object instance) {
		return instance != null ? instance.getClass() : null;
	}

	@Test
	void testFilter() throws Throwable {
		//
		Assertions.assertNull(filter(null, null));
		Assertions.assertNull(filter(Stream.empty(), null));
		//
	}

	private static <T> Stream<T> filter(final Stream<T> instance, final Predicate<? super T> predicate)
			throws Throwable {
		try {
			final Object obj = METHOD_FILTER.invoke(null, instance, predicate);
			if (obj == null) {
				return null;
			} else if (obj instanceof Stream) {
				return (Stream) obj;
			}
			throw new Throwable(toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testFindAny() throws Throwable {
		Assertions.assertNull(findAny(null));
	}

	private static <T> Optional<T> findAny(final Stream<T> instance) throws Throwable {
		try {
			final Object obj = METHOD_FIND_ANY.invoke(null, instance);
			if (obj == null) {
				return null;
			} else if (obj instanceof Optional) {
				return (Optional) obj;
			}
			throw new Throwable(toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testGet() throws Throwable {
		Assertions.assertNull(get(null));
	}

	private static <T> T get(final Optional<T> instance) throws Throwable {
		try {
			return (T) METHOD_GET.invoke(null, instance);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testGetMethods() throws Throwable {
		Assertions.assertNull(getMethods(null));
	}

	private static Method[] getMethods(final JavaClass instance) throws Throwable {
		try {
			final Object obj = METHOD_GET_METHODS.invoke(null, instance);
			if (obj == null) {
				return null;
			} else if (obj instanceof Method[]) {
				return (Method[]) obj;
			}
			throw new Throwable(toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testGetCode() throws Throwable {
		//
		Assertions.assertNull(getCode((Method) null));
		Assertions.assertNull(getCode((Code) null));
		//
	}

	private static Code getCode(final Method instance) throws Throwable {
		try {
			final Object obj = METHOD_GET_CODE1.invoke(null, instance);
			if (obj == null) {
				return null;
			} else if (obj instanceof Code) {
				return (Code) obj;
			}
			throw new Throwable(toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	private static byte[] getCode(final Code instance) throws Throwable {
		try {
			final Object obj = METHOD_GET_CODE2.invoke(null, instance);
			if (obj == null) {
				return null;
			} else if (obj instanceof byte[]) {
				return (byte[]) obj;
			}
			throw new Throwable(toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testGetName() throws Throwable {
		Assertions.assertNull(getName(null));
	}

	private static String getName(final FieldOrMethod instance) throws Throwable {
		try {
			final Object obj = METHOD_GET_NAME.invoke(null, instance);
			if (obj == null) {
				return null;
			} else if (obj instanceof String) {
				return (String) obj;
			}
			throw new Throwable(toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testGetConstantPool() throws Throwable {
		Assertions.assertNull(getConstantPool(null));
	}

	private static ConstantPool getConstantPool(final FieldOrMethod instance) throws Throwable {
		try {
			final Object obj = METHOD_GET_CONSTANT_POOL.invoke(null, instance);
			if (obj == null) {
				return null;
			} else if (obj instanceof ConstantPool) {
				return (ConstantPool) obj;
			}
			throw new Throwable(toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testMatcher() throws Throwable {
		//
		Assertions.assertNull(matcher(null, null));
		Assertions.assertNull(matcher(PATTERN, null));
		//
	}

	private static Matcher matcher(final Pattern instance, final CharSequence input) throws Throwable {
		try {
			final Object obj = METHOD_MATCHER.invoke(null, instance, input);
			if (obj == null) {
				return null;
			} else if (obj instanceof Matcher) {
				return (Matcher) obj;
			}
			throw new Throwable(toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Test
	void testMatches() throws Throwable {
		Assertions.assertFalse(matches(null));
	}

	private static boolean matches(final Matcher instance) throws Throwable {
		try {
			final Object obj = METHOD_MATCHES.invoke(null, instance);
			if (obj instanceof Boolean) {
				return ((Boolean) obj).booleanValue();
			}
			throw new Throwable(toString(getClass(obj)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

}