package com.openpromos.jni.nrexpressionlib.runtime

/**
 * Control flow exception
 */
class ControlFlowException(reason: String, var exceptionType: ControlFlow) : Exception(reason) {
    enum class ControlFlow {
        RETURN,
        CONTINUE,
        BREAK
    }
}
