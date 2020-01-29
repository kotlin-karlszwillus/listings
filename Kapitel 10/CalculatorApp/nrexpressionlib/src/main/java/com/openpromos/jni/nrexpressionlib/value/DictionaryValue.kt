package com.openpromos.jni.nrexpressionlib.value

import com.openpromos.jni.nrexpressionlib.EvaluationError

import java.util.ArrayList
import java.util.Arrays
import java.util.Enumeration
import java.util.Hashtable

/**
 * Created by voigt on 23.06.16.
 */
class DictionaryValue(var dictionary: Hashtable<String, Value>) : Value() {

    init {
        this.valueType = ValueType.Dictionary
    }

    override fun description(): String {
        val keySet = this.dictionary.keys
        val keyArray = keySet.toTypedArray()
        Arrays.sort(keyArray)

        var returnString = "["

        for (i in keyArray.indices) {
            val key = keyArray[i] as String
            val value = this.dictionary[key]

            if (i < this.dictionary.size - 1) {
                returnString = returnString + key + ":" + value?.description() + ", "
            } else {
                returnString = returnString + key + ":" + value?.description() + "]"
            }
        }

        return returnString
    }

    override fun typeString(): String {
        return "Dictionary"
    }

    @Throws(Exception::class)
    override fun boolValue(): Boolean {
        throw EvaluationError("Bool Type expected!")
    }

    @Throws(Exception::class)
    override fun sequence(): ArrayList<Value> {
        val keys = this.dictionary.keys()
        val keysList = ArrayList<Value>()

        while (keys.hasMoreElements()) {
            val key = keys.nextElement()
            keysList.add(StringValue.withValue(key))
        }

        return keysList
    }

    override fun isEqualTo(otherValue: Value): Boolean {
        if (!super.isEqualTo(otherValue)) {
            return false
        }

        val otherDictValue = otherValue as DictionaryValue
        val isEqual = otherDictValue.dictionary.size == this.dictionary.size

        if (isEqual) {
            val keySet = this.dictionary.keys
            val keyArray = keySet.toTypedArray()
            Arrays.sort(keyArray)

            val otherKeySet = otherDictValue.dictionary.keys
            val otherKeyArray = otherKeySet.toTypedArray()
            Arrays.sort(otherKeyArray)

            for (i in keyArray.indices) {
                val key = keyArray[i] as String
                val otherKey = otherKeyArray[i] as String

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

    @Throws(Exception::class)
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

        Arrays.sort(keyArray)
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

    @Throws(Exception::class)
    override fun performAccess(name: String): Value {
        return if (name == "count") {
            NumberValue(this.dictionary.size.toDouble())
        } else super.performAccess(name)

    }
}
