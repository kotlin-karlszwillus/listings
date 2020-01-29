package com.openpromos.jni.nrexpressionlib.ast.aststatement

import com.openpromos.jni.nrexpressionlib.ast.astbase.ASTNode
import com.openpromos.jni.nrexpressionlib.value.NullValue
import com.openpromos.jni.nrexpressionlib.value.Value

/**
 * Created by voigt on 05.07.16.
 */
open class ASTStatement : ASTNode() {

    @Throws(Exception::class)
    override fun evaluate(runtime: com.openpromos.jni.nrexpressionlib.Runtime): Value {
        return NullValue()
    }
}
