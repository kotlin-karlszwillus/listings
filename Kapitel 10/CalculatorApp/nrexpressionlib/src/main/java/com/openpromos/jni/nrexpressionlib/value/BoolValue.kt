package com.openpromos.jni.nrexpressionlib.value

/**
 * Created by voigt on 22.06.16.
 */
class BoolValue(var boolValue: Boolean?) : Value() {

    init {
        this.valueType = ValueType.Bool
    }

    override fun description(): String {
        return if (this.boolValue == true) "true" else "false"
    }

    override fun typeString(): String {
        return "Boolean"
    }

    override fun boolValue(): Boolean {
        return this.boolValue ?: false
    }

    @Throws(Exception::class)
    override fun logicalNot(): Value {
        return BoolValue(!this.boolValue())
    }

    override fun isEqualTo(otherValue: Value): Boolean {
        if (!super.isEqualTo(otherValue)) {
            return false
        }
        try {
            return this.boolValue() == otherValue.boolValue()
        } catch (e: Exception) {
            return false
        }

    }

    override fun toString(): String {
        return if (this.boolValue == true) "true" else "false"
    }
}
