// package com.job_tracker.proxy;
//
// import com.job_tracker.annotation.TrackExecutionTime;
//
// import java.lang.reflect.InvocationTargetException;
// import java.lang.reflect.Method;
// import java.util.Map;
// import java.util.concurrent.TimeUnit;
//
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.cglib.proxy.MethodInterceptor;
// import org.springframework.cglib.proxy.MethodProxy;
//
// @Slf4j
// @RequiredArgsConstructor
// public class ExecutionTimeMethodInterceptor implements MethodInterceptor {
//    private final Object target;
//    private final Map<Method, TrackExecutionTime> map;
//
//    @Override
//    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws
// Throwable {
//
//        if (map.containsKey(method)){
//            var startTime = System.nanoTime();
//            var annotation = map.get(method);
//            var unitTime = annotation.unit();
//            var mode = annotation.debug();
//            try{
//                return proxy.invoke(target,args);
//            }catch (InvocationTargetException e){
//                throw e.getCause();
//            }finally {
//                var endTime = System.nanoTime();
//                var executionTime = unitTime.convert(endTime - startTime, TimeUnit.NANOSECONDS);
//                String message = target.getClass().getSimpleName() + " " + method.getName() + " "
// + executionTime;
//                if (!mode){
//                    log.info(message);
//                }else {
//                    log.debug(message);
//                }
//            }
//        }
//        return proxy.invoke(target,args);
//    }
// }
