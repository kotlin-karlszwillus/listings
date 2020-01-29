package com.openpromos.jni.nrexpressionlib.ast.astotheroperators

import com.openpromos.jni.nrexpressionlib.nrx_Callable
import com.openpromos.jni.nrexpressionlib.EvaluationError
import com.openpromos.jni.nrexpressionlib.ast.astbase.ASTExpression
import com.openpromos.jni.nrexpressionlib.value.ListValue
import com.openpromos.jni.nrexpressionlib.value.Value
import com.openpromos.jni.nrexpressionlib.Runtime

import java.util.ArrayList

/**
 * Created by voigt on 28.06.16.
 */
class ASTMapOperator(internal var iterableExpression: ASTExpression, internal var identifier: String, internal var transformExpression: ASTExpression) : ASTExpression(), nrx_Callable {

    @Throws(Exception::class)
    override fun evaluate(runtime: Runtime): Value {
        var iterable: Value? = null
        try {
            iterable = this.iterableExpression.evaluate(runtime)
        } catch (e: Exception) {
            throw e
        }

        val transformedElements = ArrayList<Value>()

        try {
            for (element in iterable.sequence()) {
                val arguments = ArrayList<Value>()
                if (element is Value) arguments.add(element)

                val transformedElement: Value?
                try {
                    transformedElement = runtime.call(this, arguments, true)
                } catch (e: EvaluationError) {
                    throw e
                }

                if (transformedElement != null) transformedElements.add(transformedElement)
            }
        } catch (e: Exception) {
            throw e
        }

        return ListValue(transformedElements)
    }

    override fun getParameterNames(): ArrayList<String> {
        val returnArray = ArrayList<String>()
        returnArray.add(this.identifier)
        return returnArray
    }

    @Throws(Exception::class)
    override fun body(runtime: Runtime): Value {
        return this.transformExpression.evaluate(runtime)
    }

    override fun toString(): String {
        return "(" + this.iterableExpression.toString() + " map " + this.identifier + " : " + this.transformExpression.toString() + ")"
    }
}
