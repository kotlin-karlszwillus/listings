package com.openpromos.jni.nrexpressionlib.ast.astotheroperators

import com.openpromos.jni.nrexpressionlib.*
import com.openpromos.jni.nrexpressionlib.Runtime
import com.openpromos.jni.nrexpressionlib.ast.astbase.ASTExpression
import com.openpromos.jni.nrexpressionlib.value.ListValue
import com.openpromos.jni.nrexpressionlib.value.Value

import java.util.ArrayList

/**
 * Created by voigt on 28.06.16.
 */
class ASTWhereOperator(private val iterableExpression: ASTExpression, private val identifier: String, private val predicateExpression: ASTExpression) : ASTExpression(), nrx_Callable {

    @Throws(Exception::class)
    override fun evaluate(runtime: Runtime): Value {
        var iterable: Value? = null
        val filteredElements = ArrayList<Value>()

        try {
            iterable = this.iterableExpression.evaluate(runtime)
        } catch (e: Exception) {
            throw e
        }

        val sequence = iterable.sequence()

        try {
            for (element in sequence) {
                val arguments = ArrayList<Value>()
                if (element is Value) arguments.add(element)

                val shouldAdd: Boolean

                try {
                    shouldAdd = runtime.call(this, arguments, true)!!.boolValue()
                } catch (e: EvaluationError) {
                    throw e
                }

                if (shouldAdd) {
                    if (element is Value) filteredElements.add(element)
                }
            }

            return ListValue(filteredElements)
        } catch (e: Exception) {
            throw e
        }

    }

    override fun getParameterNames(): ArrayList<String> {
        val returnArray = ArrayList<String>()
        returnArray.add(this.identifier)
        return returnArray
    }

    @Throws(Exception::class)
    override fun body(runtime: Runtime): Value {
        return this.predicateExpression.evaluate(runtime)
    }

    override fun toString(): String {
        return "(" + this.iterableExpression.toString() + " where " + this.identifier + " : " + this.predicateExpression.toString() + ")"
    }
}
