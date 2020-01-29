package com.openpromos.jni.nrexpressionlib.syntaxtree.operators.binary

import com.openpromos.jni.nrexpressionlib.runtime.Runtime
import com.openpromos.jni.nrexpressionlib.syntaxtree.ASTExpression
import com.openpromos.jni.nrexpressionlib.value.BoolValue
import com.openpromos.jni.nrexpressionlib.value.Value


/**
 * Representation of a Contains check in a sequence
 */
class ASTContains(lhs: ASTExpression? = null, rhs: ASTExpression? = null) : ASTBinaryOperator(lhs, rhs) {

    override fun evaluate(runtime: Runtime): Value {
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

    override fun toString() = super.toString("contains")
}
