package com.openpromos.jni.nrexpressionlib.syntaxtree.operators.binary

import com.openpromos.jni.nrexpressionlib.runtime.Runtime
import com.openpromos.jni.nrexpressionlib.syntaxtree.ASTExpression
import com.openpromos.jni.nrexpressionlib.value.BoolValue
import com.openpromos.jni.nrexpressionlib.value.Value

/**
 * Representation of a logical AND
 */
class ASTLogicAnd(lhs: ASTExpression? = null, rhs: ASTExpression? = null) : ASTBinaryOperator(lhs, rhs) {

    override fun evaluate(runtime: Runtime): Value {
        try {
            return BoolValue(lhs!!.evaluate(runtime).boolValue() && rhs!!.evaluate(runtime).boolValue())
        } catch (e: Exception) {
            throw e
        }
    }

    override fun toString() = super.toString("&&")
}
