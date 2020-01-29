package com.openpromos.jni.nrexpressionlib.ast.astbase

import com.openpromos.jni.nrexpressionlib.Runtime
import com.openpromos.jni.nrexpressionlib.value.NullValue
import com.openpromos.jni.nrexpressionlib.value.Value

/**
 * Created by voigt on 22.06.16.
 * The ASTNode class is the base class for types in the abstract syntax tree (AST).
 * Subclasses serve as a hierarchical representation of the source code (compile time), and,
 * implement the means for evaluation of the code (runtime).
 */
open class ASTNode {

    @Throws(Exception::class)
    open fun evaluate(runtime: Runtime): Value {
        throw RuntimeException("evaluate must be implemented in concrete subclasses")
    }
}
