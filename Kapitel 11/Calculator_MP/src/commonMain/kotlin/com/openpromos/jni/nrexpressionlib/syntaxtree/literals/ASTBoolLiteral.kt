package com.openpromos.jni.nrexpressionlib.syntaxtree.literals

import com.openpromos.jni.nrexpressionlib.value.BoolValue
import com.openpromos.jni.nrexpressionlib.runtime.Runtime
import com.openpromos.jni.nrexpressionlib.value.Value

/**
 * Created by voigt on 22.06.16.
 */
class ASTBoolLiteral(internal var value: Boolean) : ASTLiteral() {

    override fun evaluate(runtime: Runtime): Value {
        return BoolValue(this.value)
    }

    override fun toString(): String {
        return if (this.value) "true" else "false"
    }
}
