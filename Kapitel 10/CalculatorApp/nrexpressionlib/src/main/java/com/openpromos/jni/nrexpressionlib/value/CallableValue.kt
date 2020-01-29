package com.openpromos.jni.nrexpressionlib.value

import com.openpromos.jni.nrexpressionlib.nrx_Callable

/**
 * Created by voigt on 30.06.16.
 */
class CallableValue(internal var nrxCallable: nrx_Callable) : Value() {

    init {
        this.valueType = ValueType.Callable
    }

    override fun description(): String {
        return "<nrx_Callable " + nrxCallable.toString() + ">"
    }

    override fun typeString(): String {
        return "nrx_Callable"
    }

    @Throws(Exception::class)
    override fun callable(): nrx_Callable {
        return this.nrxCallable
    }

    override fun isEqualTo(otherValue: Value): Boolean {
        if (!super.isEqualTo(otherValue)) {
            return false
        }

        val otherCallableValue = otherValue as CallableValue
        return this == otherCallableValue
    }

    override fun toString(): String {
        return "<nrx_Callable " + nrxCallable.toString() + ">"
    }

}
