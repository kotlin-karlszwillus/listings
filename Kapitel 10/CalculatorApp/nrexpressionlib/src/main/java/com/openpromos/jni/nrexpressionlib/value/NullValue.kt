package com.openpromos.jni.nrexpressionlib.value

import android.util.Log

import com.openpromos.jni.nrexpressionlib.EvaluationError

/**
 * NullValue is a special case that returns an indicator for
 * null that is able to react to certain accessors (like meta)
 * that are frequently used.
 */
class NullValue : Value() {
    init {
        this.valueType = ValueType.Null
    }

    override fun description(): String {
        return "NULL"
    }

    @Throws(Exception::class)
    override fun stringValue(): String {
        throw EvaluationError("Requesting access to null string")
    }

    override fun typeString(): String {
        return "Null"
    }

    @Throws(Exception::class)
    override fun numberValue(): Double {
        return 0.0
    }

    override fun isEqualTo(otherValue: Value): Boolean {
        return if (otherValue == null) true else super.isEqualTo(otherValue)
    }

    @Throws(Exception::class)
    override fun performAccess(name: String): Value {
        //Log.d("NullValue", "Perform Access to " + name);
        return NullValue()
    }

    override fun toString(): String {
        return "NULL"
    }
}
