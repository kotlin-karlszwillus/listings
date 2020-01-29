package com.openpromos.jni.nrexpressionlib.ast.astbinaryoperators

import com.openpromos.jni.nrexpressionlib.ast.astbase.ASTBinaryOperator
import com.openpromos.jni.nrexpressionlib.ast.astbase.ASTExpression
import com.openpromos.jni.nrexpressionlib.value.BoolValue
import com.openpromos.jni.nrexpressionlib.value.NullValue
import com.openpromos.jni.nrexpressionlib.value.NumberValue
import com.openpromos.jni.nrexpressionlib.value.Value

/**
 * Created by voigt on 29.06.16.
 */
class ASTAddition(lhs: ASTExpression? = null, rhs: ASTExpression? = null) : ASTBinaryOperator(lhs, rhs) {

    @Throws(Exception::class)
    override fun evaluate(runtime: com.openpromos.jni.nrexpressionlib.Runtime): Value {
        try {
            val lhsVal = lhs?.evaluate(runtime)
            val rhsVal = rhs?.evaluate(runtime)
            return lhsVal?.add(rhsVal ?: NumberValue(0.toDouble())) ?: NullValue()
        } catch (e: Exception) {
            throw e
        }

    }

    override fun toString(): String {
        return super.toString("+")
    }
}
