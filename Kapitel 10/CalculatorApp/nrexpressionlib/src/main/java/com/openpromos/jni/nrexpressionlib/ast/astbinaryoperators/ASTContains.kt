package com.openpromos.jni.nrexpressionlib.ast.astbinaryoperators

import com.openpromos.jni.nrexpressionlib.ast.astbase.ASTBinaryOperator
import com.openpromos.jni.nrexpressionlib.ast.astbase.ASTExpression
import com.openpromos.jni.nrexpressionlib.value.BoolValue
import com.openpromos.jni.nrexpressionlib.value.Value


/**
 * Created by voigt on 28.06.16.
 */
class ASTContains(lhs: ASTExpression? = null, rhs: ASTExpression? = null) : ASTBinaryOperator(lhs, rhs) {

    @Throws(Exception::class)
    override fun evaluate(runtime: com.openpromos.jni.nrexpressionlib.Runtime): Value {
        try {
            val lhsValue = this.lhs!!.evaluate(runtime)
            val rhsValue = this.rhs!!.evaluate(runtime)

            val sequence = lhsValue.sequence()
            var contain = false
            for (value in sequence) {
                value as Value
                if (value.isEqualTo(rhsValue)) {
                    contain = true
                    break
                }
            }
            return BoolValue(contain)

        } catch (e: Exception) {
            throw e
        }
    }

    override fun toString(): String {
        return super.toString("contains")
    }
}
