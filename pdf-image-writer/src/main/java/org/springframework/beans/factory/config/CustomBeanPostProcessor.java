package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;

public class CustomBeanPostProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException {
		//
		return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
		//
	}

}