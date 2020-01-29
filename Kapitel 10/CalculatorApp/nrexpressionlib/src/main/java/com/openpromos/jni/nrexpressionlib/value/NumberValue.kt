package com.openpromos.jni.nrexpressionlib.value

import com.openpromos.jni.nrexpressionlib.EvaluationError

/**
 * Created by voigt on 22.06.16.
 */
class NumberValue(var numberValue: Double) : Value() {

    init {
        this.valueType = ValueType.Number
    }

    override fun description(): String {
        return "Number"
    }

    override fun typeString(): String {
        return "Number"
    }

    @Throws(Exception::class)
    override fun boolValue(): Boolean {
        throw EvaluationError("Bool type expected!")
    }

    @Throws(Exception::class)
    override fun stringValue(): String {
        return toString()
    }

    @Throws(Exception::class)
    override fun numberValue(): Double {
        return this.numberValue
    }

    @Throws(Exception::class)
    override fun negate(): Value {
        return NumberValue(this.numberValue() * -1.0)
    }

    override fun isEqualTo(otherValue: Value): Boolean {
        if (!super.isEqualTo(otherValue)) {
            return false
        }
        try {
            return this.numberValue() == otherValue.numberValue()
        } catch (e: Exception) {
            return false
        }

    }

    @Throws(Exception::class)
    override fun compare(otherValue: Value): Value.ComparisonResult {
        try {
            return if (this.numberValue() < otherValue.numberValue())
                Value.ComparisonResult.OrderedAscending
            else if (this.numberValue() == otherValue.numberValue())
                Value.ComparisonResult.OrderedSame
            else
                Value.ComparisonResult.OrderedDescending
        } catch (e: Exception) {
            throw e
        }

    }

    @Throws(Exception::class)
    override fun add(otherValue: Value): Value {
        try {
            val addedValue = this.numberValue + otherValue.numberValue()!!
            return NumberValue(addedValue)
        } catch (e: Exception) {
            throw e
        }

    }

    @Throws(Exception::class)
    override fun subtract(otherValue: Value): Value {
        try {
            val substractedValue = this.numberValue - otherValue.numberValue()!!
            return NumberValue(substractedValue)
        } catch (e: Exception) {
            throw e
        }

    }

    @Throws(Exception::class)
    override fun multiplyWith(otherValue: Value): Value {
        try {
            val multipliedValue = this.numberValue * otherValue.numberValue()!!
            return NumberValue(multipliedValue)
        } catch (e: Exception) {
            throw e
        }

    }

    @Throws(Exception::class)
    override fun divideBy(otherValue: Value): Value {
        try {
            if (otherValue.numberValue() != 0.toDouble()) {
                val dividedValue = this.numberValue / otherValue.numberValue()!!
                return NumberValue(dividedValue)
            } else {
                throw EvaluationError("Division by zero!")
            }
        } catch (e: Exception) {
            throw e
        }

    }

    @Throws(Exception::class)
    override fun modulo(otherValue: Value): Value {
        try {
            if (otherValue.numberValue() != 0.toDouble()) {
                val moduloValue = this.numberValue % otherValue.numberValue()!!
                return NumberValue(moduloValue)
            } else {
                throw EvaluationError("Modulo by zero!")
            }
        } catch (e: Exception) {
            throw e
        }

    }

    override fun toString(): String {
        val integer = Math.round(this.numberValue).toInt()

        return if (integer.toDouble() == this.numberValue) {
            integer.toString()
        } else this.numberValue.toString()

    }
}
