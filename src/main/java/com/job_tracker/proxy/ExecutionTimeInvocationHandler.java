package com.job_tracker.proxy;

import com.job_tracker.annotation.TrackExecutionTime;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ExecutionTimeInvocationHandler implements InvocationHandler {
    private final Object target;

    public ExecutionTimeInvocationHandler(Object target) {
        this.target = target;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        TrackExecutionTime annotation = target.getClass()
                .getMethod(method.getName(),
                method.getParameterTypes())
                .getAnnotation(TrackExecutionTime.class);

        if (annotation == null) {
            return method.invoke(target, args);
        }

        long start = System.nanoTime();
        try {
            return method.invoke(target, args);
        }finally {

            long end = System.nanoTime();
            long time = annotation.unit().convert(end - start, TimeUnit.NANOSECONDS);

            String message = "Class: " + "[" + target.getClass().getSimpleName() + "]" + " | " + "Method: " + "[" + method.getName() + "]" + " | " + "Time"
                    + "[" + time + "]";

            if(!annotation.debug()){
                log.info(message);
            }else {
                log.debug(message);
            }
        }

    }
}
