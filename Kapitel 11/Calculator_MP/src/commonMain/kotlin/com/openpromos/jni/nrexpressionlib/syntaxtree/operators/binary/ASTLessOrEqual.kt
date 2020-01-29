package com.openpromos.jni.nrexpressionlib.syntaxtree.operators.binary

import com.openpromos.jni.nrexpressionlib.syntaxtree.ASTExpression
import com.openpromos.jni.nrexpressionlib.value.BoolValue
import com.openpromos.jni.nrexpressionlib.value.Value
import com.openpromos.jni.nrexpressionlib.runtime.Runtime

/**
 * Representation of an LessOrEqual-Check
 */
class ASTLessOrEqual(lhs: ASTExpression? = null, rhs: ASTExpression? = null) : ASTBinaryOperator(lhs, rhs) {

    override fun evaluate(runtime: Runtime): Value {

        try {
            val lhsVal = this.lhs!!.evaluate(runtime)
            val rhsVal = this.rhs!!.evaluate(runtime)
            val comparisonResult = lhsVal.compare(rhsVal)

            return BoolValue(comparisonResult == Value.ComparisonResult.OrderedAscending || comparisonResult == Value.ComparisonResult.OrderedSame)
        } catch (e: Exception) {
            throw e
        }

    }

    override fun toString() = super.toString("<=")
}
