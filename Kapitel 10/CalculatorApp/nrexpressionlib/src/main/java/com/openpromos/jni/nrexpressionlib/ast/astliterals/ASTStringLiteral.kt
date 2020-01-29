package com.openpromos.jni.nrexpressionlib.ast.astliterals

import com.openpromos.jni.nrexpressionlib.Runtime
import com.openpromos.jni.nrexpressionlib.ast.astbase.ASTLiteral
import com.openpromos.jni.nrexpressionlib.value.StringValue
import com.openpromos.jni.nrexpressionlib.value.Value

/**
 * Created by voigt on 22.06.16.
 */
class ASTStringLiteral(internal val value: String?) : ASTLiteral() {

    @Throws(Exception::class)
    override fun evaluate(runtime: Runtime): Value {
        return StringValue.withValue(this.value)
    }

    override fun toString(): String {
        if (this.value == null) return ""
        else {
            if (!(value.contains("\"") || value.contains("\\"))) {
                // fast path for strings that need no escaping
                return "\"" + this.value + "\""
            }

            // loop over characters and prefix any that need escaping
            var result = "\""

            for (i in 0 until value.length) {
                val character = value[i]

                when (character.toInt()) {
                    34 -> result += "\\\""
                    92 -> result += "\\\\"
                    else -> result += character
                }
            }
            result += "\""
            return result
        }

    }
}
