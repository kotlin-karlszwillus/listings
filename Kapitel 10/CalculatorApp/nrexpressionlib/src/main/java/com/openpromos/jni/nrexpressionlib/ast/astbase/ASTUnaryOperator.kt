package com.openpromos.jni.nrexpressionlib.ast.astbase

/**
 * Created by voigt on 22.06.16.
 * Base class for prefix operators with a single operand.
 */
open class ASTUnaryOperator(val operand: ASTExpression) : ASTExpression()