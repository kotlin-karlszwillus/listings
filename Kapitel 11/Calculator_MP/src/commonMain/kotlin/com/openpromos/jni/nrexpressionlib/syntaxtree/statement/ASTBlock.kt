package com.openpromos.jni.nrexpressionlib.syntaxtree.statement

import com.openpromos.jni.nrexpressionlib.runtime.Runtime
import com.openpromos.jni.nrexpressionlib.value.NullValue
import com.openpromos.jni.nrexpressionlib.value.Value

/**
 * Block of expressions
 */
class ASTBlock(private val statements: ArrayList<ASTStatement>) : ASTStatement() {

    override fun evaluate(runtime: Runtime): Value {
        for (statement in this.statements) {
            try {
                statement.evaluate(runtime)
            } catch (e: Exception) {
                throw e
            }

        }

        return NullValue()
    }
}
