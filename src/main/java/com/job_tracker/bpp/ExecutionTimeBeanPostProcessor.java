package com.job_tracker.bpp;

import com.job_tracker.annotation.TrackExecutionTime;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


@Component
public class ExecutionTimeBeanPostProcessor implements BeanPostProcessor {

    @Override
    public @Nullable Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        Method[] className = bean.getClass().getDeclaredMethods();
        boolean hasAnnotation = false;

        for (int i = 0; i < className.length; i++) {
            if(className[i].isAnnotationPresent(TrackExecutionTime.class)){
                hasAnnotation = true;
            }
        }
        if(hasAnnotation){
            if(bean.getClass().getInterfaces().length > 0){

            }else {

            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
