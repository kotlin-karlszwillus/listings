package com.openpromos.jni.nrexpressionlib.ast.astotheroperators

import com.openpromos.jni.nrexpressionlib.*
import com.openpromos.jni.nrexpressionlib.ast.astbase.ASTExpression
import com.openpromos.jni.nrexpressionlib.value.Value

/**
 * Created by voigt on 23.06.16.
 */
class ASTIdentifier(name: String) : ASTExpression() {
    var name: String? = null

    init {
        this.name = name
    }

    @Throws(Exception::class)
    override fun evaluate(runtime: com.openpromos.jni.nrexpressionlib.Runtime): Value {
        return runtime.resolve(this.name ?: "")
    }

    override fun toString(): String {
        return this.name ?: ""
    }
}
