package org.springframework.beans.factory.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.FieldOrMethod;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.Utility;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.ImagePdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;

public class CustomBeanPostProcessor implements BeanPostProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(CustomBeanPostProcessor.class);

	private static final Pattern PATTERN = Pattern.compile("^\\d+\\:\\s+[bs]ipush\\s+(\\d+)$");

	@Override
	public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException {
		//
		if (bean instanceof ImagePdfWriter) {
			try {
				((ImagePdfWriter) bean).setEncryptionKeyLengths(getEncryptionKeyLengths());
			} catch (final IOException e) {
				LOG.error(e.getMessage(), e);
			}
		}
		//
		return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
		//
	}

	private static List<Integer> getEncryptionKeyLengths() throws IOException {
		//
		List<Integer> list = null;
		//
		try (final InputStream is = CustomBeanPostProcessor.class
				.getResourceAsStream("/org/apache/pdfbox/pdmodel/encryption/ProtectionPolicy.class")) {
			//
			final Method method = get(findAny(filter(stream(getMethods(new ClassParser(is, null).parse())),
					m -> Objects.equals(getName(m), "setEncryptionKeyLength"))));
			//
			final String[] lines = StringUtils
					.split(Utility.codeToString(getCode(getCode(method)), getConstantPool(method), 0, -1, true), '\n');

			Matcher matcher = null;
			//
			for (int i = 0; lines != null && i < lines.length; i++) {
				//
				if (!matches(matcher = matcher(PATTERN, lines[i]))) {
					continue;
				}
				//
				if (list == null) {
					list = new ArrayList<>();
				}
				//
				list.add(Integer.valueOf(matcher.group(1)));
				//
			} // for
				//
		} // try
			//
		return list;
		//
	}

	private static <T> Stream<T> stream(final T[] instance) {
		return instance != null ? Arrays.stream(instance) : null;
	}

	private static <T> Stream<T> filter(final Stream<T> instance, final Predicate<? super T> predicate) {
		return instance != null && predicate != null ? instance.filter(predicate) : null;
	}

	private static <T> Optional<T> findAny(final Stream<T> instance) {
		return instance != null ? instance.findAny() : null;
	}

	private static <T> T get(final Optional<T> instance) {
		return instance != null ? instance.get() : null;
	}

	private static Method[] getMethods(final JavaClass instance) {
		return instance != null ? instance.getMethods() : null;
	}

	private static Code getCode(final Method instance) {
		return instance != null ? instance.getCode() : null;
	}

	private static byte[] getCode(final Code instance) {
		return instance != null ? instance.getCode() : null;
	}

	private static String getName(final FieldOrMethod instance) {
		return instance != null ? instance.getName() : null;
	}

	private static ConstantPool getConstantPool(final FieldOrMethod instance) {
		return instance != null ? instance.getConstantPool() : null;
	}

	private static Matcher matcher(final Pattern instance, final CharSequence input) {
		return instance != null && input != null ? instance.matcher(input) : null;
	}

	private static boolean matches(final Matcher instance) {
		return instance != null && instance.matches();
	}

}