package com.openpromos.jni.nrexpressionlib.ast.aststatement

import com.openpromos.jni.nrexpressionlib.value.NullValue
import com.openpromos.jni.nrexpressionlib.value.Value

import java.util.ArrayList

/**
 * Created by voigt on 05.07.16.
 */
class ASTBlock(private val statements: ArrayList<ASTStatement>) : ASTStatement() {

    @Throws(Exception::class)
    override fun evaluate(runtime: com.openpromos.jni.nrexpressionlib.Runtime): Value {
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
