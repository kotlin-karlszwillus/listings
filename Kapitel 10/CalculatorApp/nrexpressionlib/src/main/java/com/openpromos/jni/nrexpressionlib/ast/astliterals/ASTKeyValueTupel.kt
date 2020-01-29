package com.openpromos.jni.nrexpressionlib.ast.astliterals

import com.openpromos.jni.nrexpressionlib.Runtime
import com.openpromos.jni.nrexpressionlib.ast.astbase.ASTExpression
import com.openpromos.jni.nrexpressionlib.ast.astbase.ASTLiteral
import com.openpromos.jni.nrexpressionlib.value.ListValue
import com.openpromos.jni.nrexpressionlib.value.StringValue
import com.openpromos.jni.nrexpressionlib.value.Value

import java.util.ArrayList

/**
 * Created by voigt on 04.07.16.
 */
class ASTKeyValueTupel(internal var key: ASTExpression?, internal var value: ASTExpression?) : ASTLiteral() {

    @Throws(Exception::class)
    override fun evaluate(runtime: Runtime): Value {
        val arrayList = ArrayList<Value>()

        try {
            val keyValue = this.key?.evaluate(runtime)
            val elementValue = this.value?.evaluate(runtime)
            if (keyValue != null) arrayList.add(keyValue)
            if (elementValue != null) arrayList.add(elementValue)
            return ListValue(arrayList)
        } catch (e: Exception) {
            throw e
        }

    }
}
