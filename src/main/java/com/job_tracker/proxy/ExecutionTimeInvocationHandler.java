package com.job_tracker.proxy;

import com.job_tracker.annotation.TrackExecutionTime;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ExecutionTimeInvocationHandler implements InvocationHandler {
  private final Object target;
  private final Map<Method, TrackExecutionTime> map;

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

    if (map.containsKey(method)) {
      var startTime = System.nanoTime();
      var annotation = map.get(method);
      var unitTime = annotation.unit();
      var mode = annotation.debug();
      try {
        return method.invoke(target, args);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      } finally {
        var endTime = System.nanoTime();
        var executionTime = unitTime.convert(endTime - startTime, TimeUnit.NANOSECONDS);
        String message =
            target.getClass().getSimpleName() + " " + method.getName() + " " + executionTime;
        if (!mode) {
          log.info(message);
        } else {
          log.debug(message);
        }
      }
    }
    return method.invoke(target, args);
  }
}
