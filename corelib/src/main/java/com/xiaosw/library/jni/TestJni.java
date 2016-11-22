package com.xiaosw.library.jni;

/**
 * <p><br/>ClassName : {@link TestJni}
 * <br/>Description : generate jni.h command   javah -d ../../../jni/ com.xiaosw.library.jni.TestJni
 * <br/>
 * <br/>Author : xiaosw<xiaoshiwang@putao.com>
 * <br/>Create date : 2016-11-22 18:18:36</p>
 */
public class TestJni {

    /**
     * @see TestJni#getClass().getSimpleName()
     */
    private static final String TAG = "xiaosw-TestJni";

    public native boolean test(int a, String b, boolean c);

}
