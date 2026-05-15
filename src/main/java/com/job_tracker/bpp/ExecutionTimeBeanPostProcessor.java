package com.job_tracker.bpp;

import com.job_tracker.annotation.TrackExecutionTime;
import com.job_tracker.proxy.ExecutionTimeInvocationHandler;
import com.job_tracker.proxy.ExecutionTimeMethodInterceptor;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.stereotype.Component;

@Component
public class ExecutionTimeBeanPostProcessor implements BeanPostProcessor {
  @Override
  public @Nullable Object postProcessBeforeInitialization(Object bean, String beanName)
      throws BeansException {

    var beanClass = bean.getClass();
    var beanInterface = beanClass.getInterfaces();

    var methods = beanClass.getDeclaredMethods();
    var methodsWithAnnotation =
        Arrays.stream(methods)
            .filter(m -> m.isAnnotationPresent(TrackExecutionTime.class))
            .toList();

    if (methodsWithAnnotation.isEmpty()) {
      return bean;
    } else {
      var mapAnnotation =
          methodsWithAnnotation.stream()
              .collect(Collectors.toMap(m -> m, m -> m.getAnnotation(TrackExecutionTime.class)));

      if (beanInterface.length > 0) {
        ExecutionTimeInvocationHandler invocationHandler =
            new ExecutionTimeInvocationHandler(bean, mapAnnotation);
        return Proxy.newProxyInstance(beanClass.getClassLoader(), beanInterface, invocationHandler);
      } else {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(beanClass);
        enhancer.setCallback(new ExecutionTimeMethodInterceptor(bean, mapAnnotation));
        return enhancer.create();
      }
    }
  }
}
