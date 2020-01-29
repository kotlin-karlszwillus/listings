package com.openpromos.jni.nrexpressionlib.ast.astliterals

import com.openpromos.jni.nrexpressionlib.Runtime
import com.openpromos.jni.nrexpressionlib.ast.astbase.ASTExpression
import com.openpromos.jni.nrexpressionlib.ast.astbase.ASTLiteral
import com.openpromos.jni.nrexpressionlib.value.ListValue
import com.openpromos.jni.nrexpressionlib.value.Value

import java.util.ArrayList

/**
 * Created by voigt on 22.06.16.
 */
class ASTListLiteral(internal var elements: ArrayList<ASTExpression>) : ASTLiteral() {

    @Throws(Exception::class)
    override fun evaluate(runtime: Runtime): Value {
        val elementValues = ArrayList<Value>()

        try {
            for (i in this.elements.indices) {
                val element = this.elements[i]
                val value = element.evaluate(runtime)
                elementValues.add(value)
            }
            return ListValue(elementValues)
        } catch (e: Exception) {
            throw e
        }

    }

    override fun toString(): String {
        var returnString = "["

        for (i in elements.indices) {
            val element = elements[i]
            if (i < elements.size - 1) {
                returnString = returnString + element.toString() + ", "
            } else {
                returnString = returnString + element.toString()
            }
        }

        returnString = "$returnString]"
        return returnString
    }
}
