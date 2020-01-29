package com.openpromos.jni.nrexpressionlib.syntaxtree.operators.binary

import com.openpromos.jni.nrexpressionlib.runtime.Runtime
import com.openpromos.jni.nrexpressionlib.syntaxtree.ASTExpression
import com.openpromos.jni.nrexpressionlib.value.NullValue
import com.openpromos.jni.nrexpressionlib.value.NumberValue
import com.openpromos.jni.nrexpressionlib.value.Value

/**
 * Represents the result of an addition lhs + rhs
 */
class ASTAddition(lhs: ASTExpression? = null, rhs: ASTExpression? = null) : ASTBinaryOperator(lhs, rhs) {

    override fun evaluate(runtime: Runtime): Value {
        try {
            val lhsVal = lhs?.evaluate(runtime)
            val rhsVal = rhs?.evaluate(runtime)
            return lhsVal?.add(rhsVal ?: NumberValue(0.toDouble())) ?: NullValue()
        } catch (e: Exception) {
            throw e
        }
    }

    override fun toString() = super.toString("+")
}
