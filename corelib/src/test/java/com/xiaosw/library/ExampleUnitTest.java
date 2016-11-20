package com.xiaosw.library;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    /** getClass().getSimpleName() */
    private static final String TAG = "xiaosw-ExampleUnitTest";

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test() {
        System.out.println(TAG + "------------------> test()");
    }
}