package com.openpromos.jni.nrexpressionlib.value

import com.openpromos.jni.nrexpressionlib.runtime.Runtime
import com.openpromos.jni.nrexpressionlib.value.Value


/**
 * Created by voigt on 28.06.16.
 *
 * A function like object that takes arguments and returns a value when called.
 */
interface nrx_Callable {
    fun getParameterNames(): ArrayList<String>
    fun body(runtime: Runtime): Value
}
