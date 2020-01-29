package com.openpromos.jni.nrexpressionlib.syntaxtree.operators.binary

import com.openpromos.jni.nrexpressionlib.syntaxtree.ASTExpression
import com.openpromos.jni.nrexpressionlib.value.Value
import com.openpromos.jni.nrexpressionlib.runtime.Runtime

/**
 * Representation of an Except-operation
 */
class ASTExcept(lhs: ASTExpression? = null, rhs: ASTExpression? = null) : ASTBinaryOperator(lhs, rhs) {

    override fun evaluate(runtime: Runtime): Value {
        try {
            return this.lhs!!.evaluate(runtime)
        } catch (e: Exception) {
            try {
                return this.rhs!!.evaluate(runtime)
            } catch (e1: Exception) {
                throw e
            }
        }
    }

    override fun toString(): String {
        return super.toString("except")
    }
}
