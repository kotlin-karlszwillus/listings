package com.openpromos.jni.nrexpressionlib.syntaxtree.operators.other

import com.openpromos.jni.nrexpressionlib.syntaxtree.ASTExpression
import com.openpromos.jni.nrexpressionlib.value.Value
import com.openpromos.jni.nrexpressionlib.runtime.Runtime

/**
 * Created by voigt on 30.06.16.
 */
class ASTSubscript(internal var container: ASTExpression, internal var key: ASTExpression) : ASTExpression {

    override fun evaluate(runtime: Runtime): Value {
        var container: Value? = null
        try {
            container = this.container.evaluate(runtime)
        } catch (e: Exception) {
            throw e
        }

        var key: Value? = null
        try {
            key = this.key.evaluate(runtime)
        } catch (e: Exception) {
            throw e
        }

        try {
            return container.performSubscript(key)
        } catch (e: Exception) {
            throw e
        }

    }

    override fun toString(): String {
        return "(" + this.container.toString() + "->[" + this.key.toString() + "])"
    }
}
