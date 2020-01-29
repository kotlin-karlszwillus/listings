package com.openpromos.jni.nrexpressionlib.ast.astliterals

import com.openpromos.jni.nrexpressionlib.EvaluationError
import com.openpromos.jni.nrexpressionlib.ast.astbase.ASTExpression
import com.openpromos.jni.nrexpressionlib.ast.astbase.ASTLiteral
import com.openpromos.jni.nrexpressionlib.value.DictionaryValue
import com.openpromos.jni.nrexpressionlib.value.ListValue
import com.openpromos.jni.nrexpressionlib.value.Value
import com.openpromos.jni.nrexpressionlib.Runtime

import java.util.ArrayList
import java.util.Hashtable

/**
 * Created by voigt on 23.06.16.
 */
class ASTDictLiteral(var pairs: ArrayList<ASTExpression>) : ASTLiteral() {

    @Throws(Exception::class)
    override fun evaluate(runtime: Runtime): Value {
        val dictionary = Hashtable<String, Value>()
        val tupels = this.pairs.iterator()

        try {
            while (tupels.hasNext()) {
                val tupel = tupels.next() as ASTKeyValueTupel
                val tupelValue: ListValue

                try {
                    tupelValue = tupel.evaluate(runtime) as ListValue
                } catch (e: Exception) {
                    throw e
                }

                val tupelElements = tupelValue.elements
                val keyValue = tupelElements[0]
                val elementValue = tupelElements[1]

                if (dictionary.containsKey(keyValue.stringValue())) {
                    throw EvaluationError("duplicate key in Dictionary literal")
                } else {
                    dictionary[keyValue.stringValue()] = elementValue
                }
            }

            return DictionaryValue(dictionary)
        } catch (e: Exception) {
            throw e
        }

    }

    override fun toString(): String {
        if (this.pairs.isEmpty()) {
            return "[:]"
        }

        var returnString = "["
        val tupels = this.pairs.iterator()
        var i = 0

        while (tupels.hasNext()) {
            val tupel = tupels.next() as ASTKeyValueTupel
            val key = tupel.key
            val value = tupel.value

            if (i < this.pairs.size - 1) {
                returnString = returnString + key.toString() + ":" + value.toString() + ", "
            } else {
                returnString = returnString + key.toString() + ":" + value.toString() + "]"
            }

            i++
        }

        return returnString
    }
}
