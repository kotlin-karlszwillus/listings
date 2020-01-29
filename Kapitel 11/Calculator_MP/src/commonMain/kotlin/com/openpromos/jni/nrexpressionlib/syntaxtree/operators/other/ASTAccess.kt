package com.openpromos.jni.nrexpressionlib.syntaxtree.operators.other

import com.openpromos.jni.nrexpressionlib.syntaxtree.ASTExpression
import com.openpromos.jni.nrexpressionlib.value.Value
import com.openpromos.jni.nrexpressionlib.runtime.Runtime

/**
 * Created by voigt on 29.06.16.
 */
class ASTAccess(private var `object`: ASTExpression, internal var name: String) : ASTExpression {

    override fun evaluate(runtime: Runtime): Value {
        try {
            return this.`object`.evaluate(runtime).performAccess(this.name)
        } catch (e: Exception) {
            throw e
        }

    }

    override fun toString(): String {
        return "(${this.`object`.toString()}.${this.name})"
    }
}
