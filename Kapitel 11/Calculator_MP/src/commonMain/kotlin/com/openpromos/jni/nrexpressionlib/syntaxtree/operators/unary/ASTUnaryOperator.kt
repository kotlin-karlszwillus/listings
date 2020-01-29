package com.openpromos.jni.nrexpressionlib.syntaxtree.operators.unary

import com.openpromos.jni.nrexpressionlib.syntaxtree.ASTExpression

/**
 * Created by voigt on 22.06.16.
 * Base class for prefix operators with a single operand.
 */
abstract class ASTUnaryOperator(val operand: ASTExpression) : ASTExpression