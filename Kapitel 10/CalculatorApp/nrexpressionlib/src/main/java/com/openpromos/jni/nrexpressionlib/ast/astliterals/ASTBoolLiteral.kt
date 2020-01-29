package com.openpromos.jni.nrexpressionlib.ast.astliterals

import com.openpromos.jni.nrexpressionlib.ast.astbase.ASTLiteral
import com.openpromos.jni.nrexpressionlib.value.BoolValue
import com.openpromos.jni.nrexpressionlib.Runtime
import com.openpromos.jni.nrexpressionlib.value.Value

/**
 * Created by voigt on 22.06.16.
 */
class ASTBoolLiteral(internal var value: Boolean) : ASTLiteral() {

    @Throws(Exception::class)
    override fun evaluate(runtime: Runtime): Value {
        return BoolValue(this.value)
    }

    override fun toString(): String {
        return if (this.value) "true" else "false"
    }
}
