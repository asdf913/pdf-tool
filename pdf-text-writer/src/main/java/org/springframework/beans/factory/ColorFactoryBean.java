package org.springframework.beans.factory;

import java.awt.Color;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.base.Objects;

public class ColorFactoryBean implements FactoryBean<Color> {

	private String name = null;

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public Color getObject() throws Exception {
		return getColor(Color.class.getDeclaredFields(), name);
	}

	private static Color getColor(final Field[] fs, final String name) throws IllegalAccessException {
		//
		final List<Field> fields = collect(filter(stream(fs), f -> Objects.equal(getName(f), name)),
				Collectors.toList());
		//
		int size = size(fields);
		//
		List<Color> colors = null;
		Field field = null;
		Object object = null;
		//
		for (int i = 0; i < size; i++) {
			//
			if ((field = fields.get(i)) == null || !Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			//
			if ((object = field.get(null)) instanceof Color) {
				if (colors == null) {
					colors = new ArrayList<>();
				}
				colors.add((Color) object);
			}
			//
		} // for
			//
		if ((size = size(colors)) == 0) {
			return null;
		} else if (size == 1) {
			return colors.get(0);
		}
		//
		throw new IllegalStateException();
		//
	}

	private static String getName(final Member instance) {
		return instance != null ? instance.getName() : null;
	}

	private static <T> Stream<T> stream(final T[] instance) {
		return instance != null ? Arrays.stream(instance) : null;
	}

	private static <T> Stream<T> filter(final Stream<T> instance, final Predicate<? super T> predicate) {
		return instance != null && predicate != null ? instance.filter(predicate) : null;
	}

	private static <T, R, A> R collect(final Stream<T> instance, final Collector<? super T, A, R> collector) {
		return instance != null && collector != null ? instance.collect(collector) : null;
	}

	private static int size(final Collection<?> instance) {
		return instance != null ? instance.size() : 0;
	}

	@Override
	public Class<?> getObjectType() {
		return Color.class;
	}
}