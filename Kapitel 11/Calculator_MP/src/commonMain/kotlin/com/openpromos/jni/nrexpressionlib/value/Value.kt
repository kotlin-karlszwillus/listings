package com.openpromos.jni.nrexpressionlib.value

import com.openpromos.jni.nrexpressionlib.EvaluationError
import kotlin.math.round

fun emptyCompare(otherValue: Value) = false

/**
 * Base class to represent all Values
 */
sealed class Value {

    abstract val compareFunc: (Value)-> Boolean

    enum class ComparisonResult private constructor(val value: Int) {
        OrderedAscending(-1),
        OrderedSame(0),
        OrderedDescending(1),
        Unrelated(2)
    }

    open fun description() = "NULL"
    open fun typeString() = "Null"

    open fun isEqualTo(otherValue: Value): Boolean {
       try {
            return compareFunc(otherValue)
        }
        catch (e: Exception) {
            return false
        }
    }

    override fun toString() = description()

    open fun performAccess(name: String): Value {
        if (name == "typeString") {
            return StringValue.withValue(this.typeString())
        }
        throw EvaluationError("type ${this::class.simpleName} does not support perform access for $name")
    }

    open fun boolValue(): Boolean { throw EvaluationError("Bool type expected") }
    open fun stringValue(): String { throw EvaluationError("String type expected") }
    open fun numberValue(): Double { throw EvaluationError("Number type expected!") }
    open fun sequence(): ArrayList<*> { throw EvaluationError("List type expected!") }
    open fun negate(): Value { throw EvaluationError("type does not support negation!") }
    open fun logicalNot(): Value { throw EvaluationError("type does not support logical not") }
    open fun compare(otherValue: Value): ComparisonResult { throw EvaluationError("type do not support comparison") }
    open fun add(otherValue: Value): Value { throw EvaluationError("type do not support addition") }
    open fun subtract(otherValue: Value): Value { throw EvaluationError("type do not support subtraction") }
    open fun multiplyWith(otherValue: Value): Value { throw EvaluationError("types do not support multiplication") }
    open fun divideBy(otherValue: Value): Value { throw EvaluationError("type do not support division") }
    open fun modulo(otherValue: Value): Value { throw EvaluationError("type do not support modulo") }
    open fun callable(): nrx_Callable { throw EvaluationError("not a nrxCallable: " + this.typeString()) }
    open fun performSubscript(key: Value): Value { throw EvaluationError("type does not support subscription") }
}

/**
 * NullValue is a special case that returns an indicator for
 * null that is able to react to certain accessors (like meta)
 * that are frequently used.
 */
class NullValue : Value() {
    override val compareFunc =  { otherValue:Value -> otherValue is NullValue }
}

/**
 * Representation of Boolean value in the App
 */
class BoolValue(var value: Boolean?) : Value() {
    override val compareFunc = { otherValue:Value -> this.boolValue() == otherValue.boolValue() }

    override fun description() = if (value == true) "true" else "false"
    override fun typeString() = "Boolean"

    override fun boolValue() = value ?: false
    override fun logicalNot() = BoolValue(!this.boolValue())
}

/**
 * Created by voigt on 22.06.16.
 */
class NumberValue(var value: Double) : Value() {
    override val compareFunc = { otherValue:Value -> this.numberValue() == otherValue.numberValue() }

    override fun description() = "Number"
    override fun typeString() = "Number"

    override fun stringValue() = toString()
    override fun numberValue() = this.value

    override fun negate() = NumberValue(this.numberValue() * -1.0)

    override fun compare(otherValue: Value) =
        when {
            this.numberValue() < otherValue.numberValue() -> ComparisonResult.OrderedAscending
            this.numberValue() == otherValue.numberValue() -> ComparisonResult.OrderedSame
            else -> ComparisonResult.OrderedDescending
    }

    override fun add(otherValue: Value) = NumberValue(this.value + otherValue.numberValue())
    override fun subtract(otherValue: Value) = NumberValue(this.value - otherValue.numberValue())
    override fun multiplyWith(otherValue: Value) = NumberValue(this.value * otherValue.numberValue())

    override fun divideBy(otherValue: Value): Value {
        if (otherValue.numberValue() != 0.toDouble()) {
            return NumberValue(this.value / otherValue.numberValue())
        } else {
            throw EvaluationError("Division by zero!")
        }
    }

    override fun modulo(otherValue: Value): Value {
        if (otherValue.numberValue() != 0.toDouble()) {
            return NumberValue(this.value % otherValue.numberValue())
        } else {
            throw EvaluationError("Modulo by zero!")
        }
    }

    override fun toString(): String {
        val roundedValue = round(value)

        return if (roundedValue == this.value) {
            roundedValue.toInt().toString()
        } else this.value.toString()
    }
}

/**
 * Representation of a String Value
 */
class StringValue(val value: String?) : Value() {
    override val compareFunc = { otherValue:Value ->
        if (this.value == null && (otherValue is StringValue && otherValue.value == null)) {
            println("nullcase")
            true
        }
        else this.value == otherValue.stringValue()
    }

    override fun description() = value ?: ""
    override fun stringValue() = value ?: ""

    override fun typeString() = "String"

    override fun compare(otherValue: Value): ComparisonResult {
        if (otherValue !is StringValue) throw EvaluationError("types don´t match!")

        val result = this.value?.compareTo(otherValue.value ?: "") ?: 0
        return if (result < 0) ComparisonResult.OrderedAscending else if (result == 0) ComparisonResult.OrderedSame else ComparisonResult.OrderedDescending
    }

    override fun add(otherValue: Value): Value {
        if (otherValue is NullValue) return NullValue()

        val addedValue = this.value + otherValue.stringValue()
        return StringValue(addedValue)
    }

    override fun performAccess(name: String): Value {
        if (name == "len") {
            return NumberValue(value?.length?.toDouble() ?: 0.toDouble())
        } else if (name == "percentescape") {
            return StringValue(encodeURIComponent(value ?: ""))
        }
        return super.performAccess(name)
    }

    override fun toString(): String {
        if (value == null) return ""
        if (!(value.contains("\"") || value.contains("\\"))) {
            // fast path for strings that need no escaping
            return "\"" + this.value + "\""
        }

        // loop over characters and prefix any that need escaping
        var result = "\""

        for (i in 0 until value.length) {
            val character = value[i]

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

expect fun encodeURIComponent(str: String): String

/**
 * Representation of a List
 */
class ListValue(var elements: ArrayList<Value>) : Value() {
    override val compareFunc = ::emptyCompare

    override fun description() = elements.joinToString(", ", "[", "]")
    override fun typeString()= "List"

    override fun sequence(): ArrayList<Value> {
        return this.elements
    }

    override fun isEqualTo(otherValue: Value): Boolean {
        if (otherValue is ListValue) {
            if (otherValue.elements.size == this.elements.size) {
                for ((index, thisValue) in this.elements.withIndex()) {
                    if (!thisValue.isEqualTo(otherValue.elements[index])) {
                        return false
                    }
                }
            }
            return true
        }
        return false
    }

    override fun performSubscript(key: Value): Value {
        val index = key.numberValue().toInt()

        if (index < 0 || index >= this.elements.size) {
            throw EvaluationError("index out of range")
        }
        return this.elements[index]
    }

    override fun performAccess(name: String): Value {
        return if (name == "count") {
            NumberValue(this.elements.size.toDouble())
        } else super.performAccess(name)
    }
}

/**
 * Representation of Dictionary value in the App
 */
class DictionaryValue(var dictionary: HashMap<String, Value>) : Value() {
    override val compareFunc = { otherValue: Value -> otherValue is DictionaryValue }

    override fun description() =
        dictionary.map { entry -> "${entry.key}: ${entry.value.description()}" }
            .joinToString(", ", "[", "]")

    override fun typeString() = "Dictionary"

    override fun sequence(): ArrayList<Value> {
        val keys = this.dictionary.keys.iterator()
        val keysList = ArrayList<Value>()

        while (keys.hasNext()) {
            val key = keys.next()
            keysList.add(StringValue.withValue(key))
        }

        return keysList
    }

    override fun isEqualTo(otherValue: Value): Boolean {
        if (!super.isEqualTo(otherValue)) {
            return false
        }
        println("Type matches")

        val otherDictValue = otherValue as DictionaryValue
        val isEqual = otherDictValue.dictionary.size == this.dictionary.size

        if (isEqual) {
            println("Sizes matches")
            val keySet = this.dictionary.keys
            val keyArray = keySet.toTypedArray()
            keyArray.sort()

            val otherKeySet = otherDictValue.dictionary.keys
            val otherKeyArray = otherKeySet.toTypedArray()
            otherKeyArray.sort()

            for (i in keyArray.indices) {
                val key = keyArray[i]
                val otherKey = otherKeyArray[i]

                if (key != otherKey) {
                    return false
                }

                val thisValue = this.dictionary[key]
                val thatValue = otherDictValue.dictionary[key]

                if (thisValue != null && thatValue != null && !thisValue.isEqualTo(thatValue)) {
                    return false
                }
            }
        }
        return isEqual
    }

    override fun performSubscript(key: Value): Value {
        try {
            val keyString = key.stringValue()

            return dictionary[keyString] ?: throw EvaluationError("unknown key: $keyString")
        } catch (e: Exception) {
            throw e
        }

    }

    override fun toString(): String {
        val keySet = this.dictionary.keys
        val keyArray = keySet.toTypedArray()

        if (keyArray.size == 0) {
            return "[:]"
        }

        keyArray.sort()
        var returnString = "["

        for (i in keyArray.indices) {
            val key = keyArray[i] as String
            val keyValue = StringValue.withValue(key)
            val value = this.dictionary[key]

            if (i < this.dictionary.size - 1) {
                returnString += keyValue.toString() + ":" + value.toString() + ", "
            } else {
                returnString += keyValue.toString() + ":" + value.toString() + "]"
            }
        }

        return returnString
    }

    override fun performAccess(name: String): Value {
        return if (name == "count") {
            NumberValue(this.dictionary.size.toDouble())
        } else super.performAccess(name)

    }
}

/**
 * Representation of Callable values in the App (compare to Macros/stored functions)
 */
class CallableValue(internal var nrxCallable: nrx_Callable) : Value() {
    override val compareFunc = { otherValue: Value -> this == otherValue }

    override fun description() = "<nrx_Callable $nrxCallable>"
    override fun typeString() = "nrx_Callable"

    override fun callable() = this.nrxCallable
}

/**
 * Currently unused Object Type
 */
class ObjectValue(var `object`: Bridgeable) : Value() {
    override val compareFunc = { otherValue: Value -> otherValue as ObjectValue; this.`object`.nrx_isEqual(otherValue.`object`) }

    override fun typeString() = this.`object`.nrx_typeString()
    override fun description() = this.`object`.nrx_debugDescription()
    override fun callable() = this.`object`.nrx_callable()
}