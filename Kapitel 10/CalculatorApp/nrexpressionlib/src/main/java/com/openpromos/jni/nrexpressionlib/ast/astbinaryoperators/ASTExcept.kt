package com.openpromos.jni.nrexpressionlib.ast.astbinaryoperators

import com.openpromos.jni.nrexpressionlib.ast.astbase.ASTBinaryOperator
import com.openpromos.jni.nrexpressionlib.ast.astbase.ASTExpression
import com.openpromos.jni.nrexpressionlib.value.Value
import com.openpromos.jni.nrexpressionlib.Runtime

/**
 * Created by voigt on 28.06.16.
 */
class ASTExcept(lhs: ASTExpression? = null, rhs: ASTExpression? = null) : ASTBinaryOperator(lhs, rhs) {

    @Throws(Exception::class)
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
