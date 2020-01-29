package com.openpromos.jni.nrexpressionlib.ast.astotheroperators

import com.openpromos.jni.nrexpressionlib.ast.astbase.ASTExpression
import com.openpromos.jni.nrexpressionlib.value.Value
import com.openpromos.jni.nrexpressionlib.Runtime

/**
 * Created by voigt on 29.06.16.
 */
class ASTAccess(internal var `object`: ASTExpression, internal var name: String) : ASTExpression() {

    @Throws(Exception::class)
    override fun evaluate(runtime: Runtime): Value {
        try {
            return this.`object`.evaluate(runtime).performAccess(this.name)
        } catch (e: Exception) {
            throw e
        }

    }

    override fun toString(): String {
        return "(" + this.`object`.toString() + "." + this.name + ")"
    }
}
