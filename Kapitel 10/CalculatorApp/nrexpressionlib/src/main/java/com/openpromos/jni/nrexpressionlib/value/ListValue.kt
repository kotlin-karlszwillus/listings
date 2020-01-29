package com.openpromos.jni.nrexpressionlib.value

import com.openpromos.jni.nrexpressionlib.EvaluationError

import java.util.AbstractList
import java.util.ArrayList

/**
 * Created by voigt on 23.06.16.
 */
class ListValue(var elements: ArrayList<Value>) : Value() {

    init {
        this.valueType = ValueType.List
    }

    override fun description(): String {
        var returnString = "["
        for (temp in this.elements) {
            returnString += temp.description() + ", "
        }
        returnString += "]"
        return returnString
    }

    override fun typeString(): String {
        return "List"
    }

    @Throws(Exception::class)
    override fun boolValue(): Boolean {
        throw EvaluationError("Bool Type expected!")
    }

    @Throws(Exception::class)
    override fun numberValue(): Double {
        throw EvaluationError("Number type expected!")
    }

    @Throws(Exception::class)
    override fun sequence(): ArrayList<Value> {
        return this.elements
    }

    override fun isEqualTo(otherValue: Value): Boolean {
        if (!super.isEqualTo(otherValue)) {
            return false
        }

        val otherListValue = otherValue as ListValue
        val isEqual = otherListValue.elements.size == this.elements.size

        if (isEqual) {
            for (i in this.elements.indices) {
                val thisValue = this.elements[i]

                if (!thisValue.isEqualTo(otherListValue.elements[i])) {
                    return false
                }
            }
        }

        return isEqual
    }

    @Throws(Exception::class)
    override fun performSubscript(key: Value): Value {
        try {
            val index = key.numberValue().toInt()

            if (index < 0 || index >= this.elements.size) {
                throw EvaluationError("index out of range")
            }
            return this.elements[index]
        } catch (e: Exception) {
            throw e
        }

    }

    override fun toString(): String {
        var returnString = "["

        for (i in this.elements.indices) {
            val element = this.elements[i]
            if (i < elements.size - 1) {
                returnString += element.toString() + ", "
            } else {
                returnString += element.toString()
            }
        }

        returnString += "]"
        return returnString
    }

    @Throws(Exception::class)
    override fun performAccess(name: String): Value {
        return if (name == "count") {
            NumberValue(this.elements.size.toDouble())
        } else super.performAccess(name)

    }
}
