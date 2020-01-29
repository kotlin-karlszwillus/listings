package com.openpromos.jni.nrexpressionlib.syntaxtree.operators.other

import com.openpromos.jni.nrexpressionlib.value.nrx_Callable
import com.openpromos.jni.nrexpressionlib.syntaxtree.ASTExpression
import com.openpromos.jni.nrexpressionlib.value.Value
import com.openpromos.jni.nrexpressionlib.runtime.Runtime
import com.openpromos.jni.nrexpressionlib.value.NullValue


/**
 * Created by voigt on 30.06.16.
 */
class ASTCall(internal var callable: ASTExpression, internal var arguments: ArrayList<ASTExpression>) : ASTExpression {

    override fun evaluate(runtime: Runtime): Value {
        val nrxCallable: nrx_Callable
        val values: ArrayList<Value>

        try {
            nrxCallable = this.callable.evaluate(runtime).callable()
            values = ArrayList<Value>()
        } catch (e: Exception) {
            throw e
        }

        try {
            for (i in this.arguments.indices) {
                val argument = this.arguments[i]
                val value = argument.evaluate(runtime)
                values.add(value)
            }
        } catch (e: Exception) {
            throw e
        }

        try {
            return runtime.call(nrxCallable, values, false) ?: NullValue()
        } catch (e: Exception) {
            throw e
        }

    }

    override fun toString(): String {
        var returnString = "("
        returnString = returnString + this.callable.toString() + "("

        for (i in this.arguments.indices) {
            if (i < this.arguments.size - 1) {
                returnString = returnString + this.arguments[i].toString() + ", "
            } else {
                returnString = returnString + this.arguments[i].toString()
            }
        }
        returnString = "$returnString))"

        return returnString
    }
}
