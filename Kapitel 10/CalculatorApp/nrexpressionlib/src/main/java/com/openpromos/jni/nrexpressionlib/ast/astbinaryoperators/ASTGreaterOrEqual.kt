package com.openpromos.jni.nrexpressionlib.ast.astbinaryoperators

import com.openpromos.jni.nrexpressionlib.ast.astbase.ASTBinaryOperator
import com.openpromos.jni.nrexpressionlib.ast.astbase.ASTExpression
import com.openpromos.jni.nrexpressionlib.value.BoolValue
import com.openpromos.jni.nrexpressionlib.value.Value

/**
 * Created by voigt on 29.06.16.
 */
class ASTGreaterOrEqual(lhs: ASTExpression? = null, rhs: ASTExpression? = null) : ASTBinaryOperator(lhs, rhs) {

    @Throws(Exception::class)
    override fun evaluate(runtime: com.openpromos.jni.nrexpressionlib.Runtime): Value {

        try {
            val lhsVal = this.lhs!!.evaluate(runtime)
            val rhsVal = this.rhs!!.evaluate(runtime)
            val comparisonResult = lhsVal.compare(rhsVal)

            return BoolValue(comparisonResult == Value.ComparisonResult.OrderedDescending || comparisonResult == Value.ComparisonResult.OrderedSame)
        } catch (e: Exception) {
            throw e
        }

    }

    override fun toString(): String {
        return super.toString(">=")
    }
}
