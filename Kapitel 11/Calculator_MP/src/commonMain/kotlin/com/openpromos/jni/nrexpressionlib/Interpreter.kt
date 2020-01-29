package com.openpromos.jni.nrexpressionlib

import com.openpromos.jni.nrexpressionlib.lexer.Lexer
import com.openpromos.jni.nrexpressionlib.parser.Parser
import com.openpromos.jni.nrexpressionlib.runtime.Runtime
import com.openpromos.jni.nrexpressionlib.runtime.RuntimeDelegate
import com.openpromos.jni.nrexpressionlib.value.Value

/**
 * Simple implementation of the interpreter to demo the first
 * working expression - this will be subject to change as the implementation
 * moves on (as of 18th of July)
 */
object Interpreter {

    fun evaluateExpression(source: String, delegate: RuntimeDelegate, uuid: String): Value? {

        val parser = Parser(Lexer(source))
        val runtime = Runtime(delegate)

        try {
            println("Interpreter: $source");
            delegate.setUuid(uuid)
            val node = parser.parseExpression()
            val returnValue = node.evaluate(runtime)
            delegate.resetUuid()
            return returnValue
        } catch (e: Exception) {
            //Log.e("Interpreter", "Caught an interpreter exception", e);
            return null
        }

    }
}
