package com.openpromos.jni.nrexpressionlib.runtime

import com.openpromos.jni.nrexpressionlib.EvaluationError
import com.openpromos.jni.nrexpressionlib.lexer.Lexer
import com.openpromos.jni.nrexpressionlib.value.nrx_Callable
import com.openpromos.jni.nrexpressionlib.parser.Parser
import com.openpromos.jni.nrexpressionlib.syntaxtree.statement.ASTBlock
import com.openpromos.jni.nrexpressionlib.value.LookupDescription
import com.openpromos.jni.nrexpressionlib.value.Value

/**
 * Created by voigt on 22.06.16.
 *
 * The Runtime class is used for evaluation of AST nodes. It represents the current state
 * and holds values like variables or globals, serves as a proxy for application specific
 * functionality like lookup and is the delegate for error reporting.
 */
class Runtime {
    private val delegate: RuntimeDelegate?
    private val stack = ArrayList<Scope>()
    private val globalScope = Scope()

    constructor() {
        this.delegate = null
        this.stack.add(Scope())
    }

    constructor(delegate: RuntimeDelegate) {
        this.stack.add(Scope())
        this.delegate = delegate
    }

    fun resolve(symbol: String): Value {
        val scope: Scope
        try {
            scope = this.currentScope()
        } catch (e: Exception) {
            throw e
        }
        var value: Value? = scope.getSymbol(symbol)

        if (value != null) {
            return value
        }

        value = this.globalScope.getSymbol(symbol)

        if (value != null) {
            return value
        }

        if (this.delegate != null) {
            value = delegate.resolve(symbol)
            if (value != null) {
                return value
            }
        }

        throw EvaluationError("could not resolve $symbol")
    }

    fun lookup(lookup: LookupDescription): Value {
        if (delegate != null) {
            return delegate.lookup(lookup)
        }

        throw EvaluationError("lookup error: not found")
    }

    fun call(nrxCallable: nrx_Callable, arguments: ArrayList<Value>, inNestedScope: Boolean): Value? {
        // push an empty or copied (nested) scope
        try {
            this.pushScope(inNestedScope)
        } catch (e: Exception) {
            popScope()
            throw e
        }

        // arguments and parameters must match
        if (arguments.size != nrxCallable.getParameterNames().size) {
            popScope()
            throw EvaluationError("number of arguments do not match parameters")
        }

        val parameterNames = nrxCallable.getParameterNames()
        // assign arguments to parameter names in the new scope
        var i = 0
        for (argument in arguments) {
            val parameterName = parameterNames[i]
            this.assign(argument, parameterName, false)
            i++
        }

        var value : Value? = null
        try {
            // call the body of the function
            value = nrxCallable.body(this)

        } catch (e: ControlFlowException) {
            when (e.exceptionType) {
                ControlFlowException.ControlFlow.RETURN -> {
                }
                ControlFlowException.ControlFlow.BREAK -> {
                    this.popScope()
                    throw EvaluationError("break without enclosing loop")
                }
                ControlFlowException.ControlFlow.CONTINUE -> {
                    this.popScope()
                    throw EvaluationError("continue without enclosing loop")
                }
            }
        }
        this.popScope()
        return value
    }

    
    private fun assign(value: Value, symbol: String, inGlobalScope: Boolean) {
        val scope: Scope

        if (inGlobalScope) {
            scope = this.globalScope
        } else {
            try {
                scope = this.currentScope()
            } catch (e: Exception) {
                throw e
            }

        }

        scope.setSymbol(symbol, value)
    }

    private fun setProperty(parent: Value, propertyName: String, value: Value) {
        // TODO: implement
    }

    private fun pushScope(nested: Boolean) {
        if (this.stack.size >= maxCallDepth) {
            throw EvaluationError("call stack exceeded")
        }

        val newScope: Scope
        val currentScope: Scope

        if (nested) {
            try {
                currentScope = this.currentScope()
            } catch (e: Exception) {
                throw e
            }

            newScope = Scope(currentScope)
        } else {
            newScope = Scope()
        }

        this.stack.add(newScope)
    }

        private fun currentScope(): Scope {
        if (this.stack.size == 0) {
            throw RuntimeException("stack is empty")
        }

        return this.stack[this.stack.size - 1]
    }

        private fun popScope() {
        if (this.stack.size < 2) {
            throw RuntimeException("call stack push/pop mismatch")
        }

        this.stack.removeAt(this.stack.size - 1)
    }

    companion object {
        private val maxCallDepth = 1024

        fun run(source: String, delegate: RuntimeDelegate) {
            val parser = Parser(Lexer(source))

            try {
                val program = parser.parseProgram()
                try {
                    run(program, delegate)
                } catch (e: Exception) {
                    throw e
                }

            } catch (e: Exception) {
                throw e
            }

        }

        internal fun run(program: ASTBlock, delegate: RuntimeDelegate) {
            val runtime = Runtime(delegate)
            try {
                program.evaluate(runtime)
            } catch (e: Exception) {
                throw e
            }

        }
    }
}
