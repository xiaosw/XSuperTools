package com.xiaosw.aspectj.aspacet;

import android.util.Log;

import com.xiaosw.aspectj.annotation.UserBehaviour;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * <p><br/>ClassName : {@link UserBehaviourAspacet}
 * <br/>Description :
 * <br/>
 * <br/>Author : xiaosw<xiaosw0802@163.com>
 * <br/>Create date : 2017-03-21 11:11:17</p>
 */

@Aspect
public class UserBehaviourAspacet {

    /** @see UserBehaviourAspacet#getClass().getSimpleName() */
    private static final String TAG = "UserBehaviourAspacet";

    private static final String METHOD_INPUTCUT = "execution(@com.xiaosw.aspectj.annotation.UserBehaviour * *(..))";
    private static final String CONSTRUCTOR_INPUTCUT = "execution(@com.xiaosw.aspectj.annotation.UserBehaviour *.new(..))";

    @Pointcut(METHOD_INPUTCUT)
    public void methodAnnotatedWithUserBehaviour() {}

    @Pointcut(CONSTRUCTOR_INPUTCUT)
    private void constructorAnnotatedWithUserBehaviour() {}

    /**
     * 同步方法不作为切点
     */
    @Pointcut("execution(!synchronized * *(..))")
    private void noSynchronized() {}

//    @Before("methodAnnotatedWithUserBehaviour() || constructorAnnotatedWithUserBehaviour()")
//    public void callBefore() {
//        Log.e(TAG, "callBefore()");
//    }
//
//    @After("methodAnnotatedWithUserBehaviour() || constructorAnnotatedWithUserBehaviour()")
//    public void callAfter() {
//        Log.e(TAG, "callAfter()");
//    }


    @Around("noSynchronized() && (methodAnnotatedWithUserBehaviour() || constructorAnnotatedWithUserBehaviour())")
    public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        Log.e(TAG, "weaveJoinPoint: ");
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        if (method.isAnnotationPresent(UserBehaviour.class)) {
            UserBehaviour userBehaviour = method.getAnnotation(UserBehaviour.class);
            Log.e(TAG, "weaveJoinPoint: parsms = " + userBehaviour.value());
            result = joinPoint.proceed();
        } else {
            Log.e(TAG, "weaveJoinPoint: method not instaceof UserBehaviour.class");
        }
        return result;
    }

}
