package com.openpromos.jni.nrexpressionlib.syntaxtree.operators.other

import com.openpromos.jni.nrexpressionlib.runtime.Runtime
import com.openpromos.jni.nrexpressionlib.syntaxtree.ASTExpression
import com.openpromos.jni.nrexpressionlib.value.Value

/**
 * Created by voigt on 23.06.16.
 */
class ASTIdentifier(name: String) : ASTExpression{
    var name: String? = null

    init {
        this.name = name
    }

    override fun evaluate(runtime: Runtime): Value {
        return runtime.resolve(this.name ?: "")
    }

    override fun toString(): String {
        return this.name ?: ""
    }
}
