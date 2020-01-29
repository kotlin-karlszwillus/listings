package com.openpromos.jni.nrexpressionlib.value

import com.openpromos.jni.nrexpressionlib.EvaluationError

import java.net.URLEncoder

/**
 * Created by voigt on 22.06.16.
 */
class StringValue(val stringValue: String?) : Value() {

    init {
        this.valueType = ValueType.String
    }

    override fun description(): String {
        return stringValue ?: ""
    }

    @Throws(Exception::class)
    override fun stringValue(): String {
        return stringValue ?: ""
    }

    override fun typeString(): String {
        return "String"
    }

    override fun isEqualTo(otherValue: Value): Boolean {
        if (!super.isEqualTo(otherValue)) {
            return false
        }
        try {
            return if (this.stringValue() == null && otherValue.stringValue() == null) {
                true
            } else this.stringValue == otherValue.stringValue()
        } catch (e: Exception) {
            return false
        }

    }

    @Throws(Exception::class)
    override fun compare(otherValue: Value): Value.ComparisonResult {
        val otherStringValue: StringValue
        try {
            otherStringValue = otherValue as StringValue
        } catch (e: Exception) {
            throw EvaluationError("types don´t match!")
        }

        // TODO Check usage of null values
        val result = this.stringValue?.compareTo(otherStringValue.stringValue ?: "") ?: 0

        return if (result < 0) Value.ComparisonResult.OrderedAscending else if (result == 0) Value.ComparisonResult.OrderedSame else Value.ComparisonResult.OrderedDescending
    }

    @Throws(Exception::class)
    override fun add(otherValue: Value): Value {
        if (otherValue == null || otherValue is NullValue) return NullValue()
        try {
            val addedValue = this.stringValue + otherValue.stringValue()
            return StringValue(addedValue)
        } catch (e: Exception) {
            throw e
        }

    }

    @Throws(Exception::class)
    override fun performAccess(name: String): Value {
        //Log.d("StringValue", String.format("Access to %s requested", name));
        if (name == "len") {
            return NumberValue(stringValue?.length?.toDouble() ?: 0.toDouble())
        } else if (name == "percentescape") {
            return StringValue(URLEncoder.encode(stringValue, "UTF-8"))
        }

        return super.performAccess(name)
    }

    override fun toString(): String {
        if (stringValue == null) return ""
        if (!(stringValue.contains("\"") || stringValue.contains("\\"))) {
            // fast path for strings that need no escaping
            return "\"" + this.stringValue + "\""
        }

        // loop over characters and prefix any that need escaping
        var result = "\""

        for (i in 0 until stringValue.length) {
            val character = stringValue[i]

            when (character.toInt()) {
                34 -> result += "\\\""
                92 -> result += "\\\\"
                else -> result += character
            }
        }

        result += "\""
        return result
    }

    companion object {

        fun withValue(stringValue: String?): Value {
            return if (stringValue == null)
                NullValue()
            else
                StringValue(stringValue)
        }
    }
}
