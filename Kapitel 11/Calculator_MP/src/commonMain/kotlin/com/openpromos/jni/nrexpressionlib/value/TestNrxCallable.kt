package com.openpromos.jni.nrexpressionlib.value

import com.openpromos.jni.nrexpressionlib.runtime.Runtime

/**
 * Created by voigt on 05.07.16.
 */
class TestNrxCallable : nrx_Callable {
    internal var parameterNames = ArrayList<String>()

    override fun getParameterNames(): ArrayList<String> {
        return this.parameterNames
    }

    override fun body(runtime: Runtime): Value {
        return StringValue.withValue("testFunction's result")
    }
}
