package com.openpromos.jni.nrexpressionlib.value

import com.openpromos.jni.nrexpressionlib.nrx_Callable
import com.openpromos.jni.nrexpressionlib.EvaluationError

import java.util.ArrayList

/**
 * Created by voigt on 22.06.16.
 */
open class Value {

    var valueType: ValueType

    enum class ComparisonResult private constructor(val value: Int) {
        OrderedAscending(-1),
        OrderedSame(0),
        OrderedDescending(1),
        Unrelated(2)
    }

    init {
        this.valueType = ValueType.Null
    }

    open fun description(): String {
        return "NULL"
    }

    open fun typeString(): String {
        return "Null"
    }

    @Throws(Exception::class)
    open fun boolValue(): Boolean {
        throw EvaluationError("Bool type expected")
    }

    @Throws(Exception::class)
    open fun stringValue(): String {
        throw EvaluationError("String type expected")
    }

    @Throws(Exception::class)
    open fun numberValue(): Double {
        throw EvaluationError("Number type expected!")
    }

    @Throws(Exception::class)
    open fun sequence(): ArrayList<*> {
        throw EvaluationError("List type expected!")
    }

    @Throws(Exception::class)
    open fun negate(): Value {
        throw EvaluationError("type does not support negation!")
    }

    @Throws(Exception::class)
    open fun logicalNot(): Value {
        throw EvaluationError("type does not support logical not")
    }

    open fun isEqualTo(otherValue: Value): Boolean {
        return this.valueType == otherValue.valueType
    }

    @Throws(Exception::class)
    open fun compare(otherValue: Value): ComparisonResult {
        throw EvaluationError("types do not support comparison")
    }

    @Throws(Exception::class)
    open fun add(otherValue: Value): Value {
        throw EvaluationError("types do not support addition")
    }

    @Throws(Exception::class)
    open fun subtract(otherValue: Value): Value {
        throw EvaluationError("types do not support subtraction")
    }

    @Throws(Exception::class)
    open fun multiplyWith(otherValue: Value): Value {
        throw EvaluationError("types do not support multiplication")
    }

    @Throws(Exception::class)
    open fun divideBy(otherValue: Value): Value {
        throw EvaluationError("types do not support division")
    }

    @Throws(Exception::class)
    open fun modulo(otherValue: Value): Value {
        throw EvaluationError("types do not support modulo")
    }

    @Throws(Exception::class)
    open fun performAccess(name: String): Value {
        if (name == "typeString") {
            return StringValue.withValue(this.typeString())
        }

        throw EvaluationError("type " + this.javaClass.simpleName + " does not support perform access for " + name)
    }

    @Throws(Exception::class)
    open fun callable(): nrx_Callable {
        throw EvaluationError("not a nrxCallable: " + this.typeString())
    }

    @Throws(Exception::class)
    open fun performSubscript(key: Value): Value {
        throw EvaluationError("type does not support subscription")
    }

    override fun toString(): String {
        return "NULL"
    }
}
