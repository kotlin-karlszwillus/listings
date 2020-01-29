package com.openpromos.jni.nrexpressionlib.ast.astotheroperators

import com.openpromos.jni.nrexpressionlib.ast.astbase.ASTExpression
import com.openpromos.jni.nrexpressionlib.value.Value
import com.openpromos.jni.nrexpressionlib.Runtime

/**
 * Created by voigt on 28.06.16.
 */
class ASTConditionalOperator(var condition: ASTExpression, var positiveExpression: ASTExpression, var negativeExpression: ASTExpression) : ASTExpression() {

    @Throws(Exception::class)
    override fun evaluate(runtime: Runtime): Value {
        var branch: ASTExpression? = null

        try {
            val condition = this.condition.evaluate(runtime).boolValue()
            branch = if (condition) this.positiveExpression else this.negativeExpression
        } catch (e: Exception) {
            throw e
        }

        try {
            return branch.evaluate(runtime)
        } catch (e: Exception) {
            throw e
        }

    }

    override fun toString(): String {
        return "(" + this.condition.toString() + " ? " + this.positiveExpression.toString() + " : " + this.negativeExpression.toString() + ")"
    }
}
