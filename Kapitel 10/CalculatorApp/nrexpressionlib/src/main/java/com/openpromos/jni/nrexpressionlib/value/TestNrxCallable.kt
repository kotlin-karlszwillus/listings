package com.openpromos.jni.nrexpressionlib.value

import com.openpromos.jni.nrexpressionlib.*

import java.util.ArrayList

/**
 * Created by voigt on 05.07.16.
 */
class TestNrxCallable : nrx_Callable {
    internal var parameterNames = ArrayList<String>()

    override fun getParameterNames(): ArrayList<String> {
        return this.parameterNames
    }

    @Throws(Exception::class)
    override fun body(runtime: com.openpromos.jni.nrexpressionlib.Runtime): Value {
        return StringValue.withValue("testFunction's result")
    }
}
