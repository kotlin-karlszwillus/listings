package com.openpromos.jni.nrexpressionlib

import com.openpromos.jni.nrexpressionlib.value.Value

import java.util.ArrayList

/**
 * Created by voigt on 28.06.16.
 *
 * A function like object that takes arguments and returns a value when called.
 */
interface nrx_Callable {
    fun getParameterNames(): ArrayList<String>
    @Throws(Exception::class)
    fun body(runtime: Runtime): Value
}
