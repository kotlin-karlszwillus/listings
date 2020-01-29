package com.openpromos.jni.nrexpressionlib.syntaxtree.operators.binary

import com.openpromos.jni.nrexpressionlib.runtime.Runtime
import com.openpromos.jni.nrexpressionlib.syntaxtree.ASTExpression
import com.openpromos.jni.nrexpressionlib.value.BoolValue
import com.openpromos.jni.nrexpressionlib.value.Value

/**
 * Representation of an GreaterThan-Check
 */
class ASTGreaterThan(lhs: ASTExpression? = null, rhs: ASTExpression? = null) : ASTBinaryOperator(lhs, rhs){

    override fun evaluate(runtime: Runtime): Value {
        try {
            val lhsVal = this.lhs!!.evaluate(runtime)
            val rhsVal = this.rhs!!.evaluate(runtime)

            return BoolValue(lhsVal.compare(rhsVal) == Value.ComparisonResult.OrderedDescending)
        } catch (e: Exception) {
            throw e
        }
    }

    override fun toString(): String {
        return super.toString(">")
    }
}
