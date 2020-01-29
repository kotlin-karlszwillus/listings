package com.openpromos.jni.nrexpressionlib.value

import com.openpromos.jni.nrexpressionlib.Bridgeable
import com.openpromos.jni.nrexpressionlib.nrx_Callable

/**
 * Created by voigt on 30.06.16.
 */
class ObjectValue(var `object`: Bridgeable) : Value() {

    init {
        this.valueType = ValueType.Object
    }

    override fun typeString(): String {
        return this.`object`.nrx_typeString()
    }

    override fun description(): String {
        return this.`object`.nrx_debugDescription()
    }

    @Throws(Exception::class)
    override fun callable(): nrx_Callable {
        val nrxCallable = this.`object`.nrx_callable()
        return nrxCallable ?: super.callable()

    }

    override fun isEqualTo(otherValue: Value): Boolean {
        if (!super.isEqualTo(otherValue)) {
            return false
        }
        val otherObject = otherValue as ObjectValue
        val bridgeable = otherObject.`object`
        return this.`object`.nrx_isEqual(bridgeable)
    }

    override fun toString(): String {
        return this.`object`.nrx_debugDescription()
    }

}
