package com.openpromos.jni.nrexpressionlib.ast.astbinaryoperators

import com.openpromos.jni.nrexpressionlib.ast.astbase.ASTBinaryOperator
import com.openpromos.jni.nrexpressionlib.ast.astbase.ASTExpression
import com.openpromos.jni.nrexpressionlib.value.BoolValue
import com.openpromos.jni.nrexpressionlib.value.Value
import com.openpromos.jni.nrexpressionlib.Runtime

/**
 * Created by voigt on 29.06.16.
 */
class ASTLessThan(lhs: ASTExpression? = null, rhs: ASTExpression? = null) : ASTBinaryOperator(lhs, rhs) {

    @Throws(Exception::class)
    override fun evaluate(runtime: Runtime): Value {

        try {
            val lhsVal = this.lhs!!.evaluate(runtime)
            val rhsVal = this.rhs!!.evaluate(runtime)

            return BoolValue(lhsVal.compare(rhsVal) == Value.ComparisonResult.OrderedAscending)
        } catch (e: Exception) {
            throw e
        }

    }

    override fun toString(): String {
        return super.toString("<")
    }
}
