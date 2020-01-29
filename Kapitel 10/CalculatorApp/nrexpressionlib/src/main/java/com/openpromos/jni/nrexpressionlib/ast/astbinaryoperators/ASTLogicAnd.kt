package com.openpromos.jni.nrexpressionlib.ast.astbinaryoperators

import com.openpromos.jni.nrexpressionlib.ast.astbase.ASTBinaryOperator
import com.openpromos.jni.nrexpressionlib.ast.astbase.ASTExpression
import com.openpromos.jni.nrexpressionlib.value.BoolValue
import com.openpromos.jni.nrexpressionlib.value.Value

/**
 * Created by voigt on 29.06.16.
 */
class ASTLogicAnd(lhs: ASTExpression? = null, rhs: ASTExpression? = null) : ASTBinaryOperator(lhs, rhs) {

    @Throws(Exception::class)
    override fun evaluate(runtime: com.openpromos.jni.nrexpressionlib.Runtime): Value {
        try {
            return BoolValue(lhs!!.evaluate(runtime).boolValue() && rhs!!.evaluate(runtime).boolValue())
        } catch (e: Exception) {
            throw e
        }
    }

    override fun toString(): String {
        return super.toString("&&")
    }
}
