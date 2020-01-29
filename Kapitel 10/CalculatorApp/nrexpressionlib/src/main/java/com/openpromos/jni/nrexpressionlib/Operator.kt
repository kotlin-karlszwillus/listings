package com.openpromos.jni.nrexpressionlib

import com.openpromos.jni.nrexpressionlib.ast.astbinaryoperators.ASTAddition
import com.openpromos.jni.nrexpressionlib.ast.astbinaryoperators.ASTContains
import com.openpromos.jni.nrexpressionlib.ast.astbinaryoperators.ASTDivision
import com.openpromos.jni.nrexpressionlib.ast.astbinaryoperators.ASTEqual
import com.openpromos.jni.nrexpressionlib.ast.astbinaryoperators.ASTExcept
import com.openpromos.jni.nrexpressionlib.ast.astbinaryoperators.ASTGreaterOrEqual
import com.openpromos.jni.nrexpressionlib.ast.astbinaryoperators.ASTGreaterThan
import com.openpromos.jni.nrexpressionlib.ast.astbinaryoperators.ASTLessOrEqual
import com.openpromos.jni.nrexpressionlib.ast.astbinaryoperators.ASTLessThan
import com.openpromos.jni.nrexpressionlib.ast.astbinaryoperators.ASTLogicAnd
import com.openpromos.jni.nrexpressionlib.ast.astbinaryoperators.ASTLogicOr
import com.openpromos.jni.nrexpressionlib.ast.astbinaryoperators.ASTModulo
import com.openpromos.jni.nrexpressionlib.ast.astbinaryoperators.ASTMultiplication
import com.openpromos.jni.nrexpressionlib.ast.astbinaryoperators.ASTNotEqual
import com.openpromos.jni.nrexpressionlib.ast.astbinaryoperators.ASTSubtraction
import com.openpromos.jni.nrexpressionlib.value.OperatorType

/**
 * Created by voigt on 28.06.16.
 */
class Operator {
    var type: OperatorType? = null
    var precedence: Precedence? = null
    var associativity: Associativity? = null

    constructor() {}

    constructor(token: Token) {

        when (token.type) {
            TokenType.Except -> this.type = OperatorType.Except
            TokenType.Questionmark -> this.type = OperatorType.Conditional
            TokenType.Where -> this.type = OperatorType.Where
            TokenType.Map -> this.type = OperatorType.Map
            TokenType.Contains -> this.type = OperatorType.Contains
            TokenType.Or -> this.type = OperatorType.LogicOr
            TokenType.And -> this.type = OperatorType.LogicAnd
            TokenType.NotEqual -> this.type = OperatorType.NotEqual
            TokenType.Equal -> this.type = OperatorType.Equal
            TokenType.Greater -> this.type = OperatorType.GreaterThan
            TokenType.GreaterOrEqual -> this.type = OperatorType.GreaterOrEqual
            TokenType.Less -> this.type = OperatorType.LessThan
            TokenType.LessOrEqual -> this.type = OperatorType.LessOrEqual
            TokenType.Plus -> this.type = OperatorType.Addition
            TokenType.Minus -> this.type = OperatorType.Subtraction
            TokenType.Star -> this.type = OperatorType.Multiplication
            TokenType.Divis -> this.type = OperatorType.Division
            TokenType.Modulo -> this.type = OperatorType.Modulo
            TokenType.LeftParen -> this.type = OperatorType.Call
            TokenType.Dot -> this.type = OperatorType.Access
            TokenType.LeftBracket -> this.type = OperatorType.Subscript
            else -> {
                this.type = null
                return
            }
        }

        associativity = this.associativity()
        precedence = this.precedence()
    }

    private fun associativity(): Associativity {
        // The only right associative operators are unary not and unary minus.
        // They are not parsed in _parsePrimaryExpression, so they are not listed here.
        return Associativity.Left
    }

    private fun precedence(): Precedence? {
        when (this.type) {
            OperatorType.Except -> return Precedence.Exceptional
            OperatorType.Conditional -> return Precedence.Conditional
            OperatorType.Where, OperatorType.Map -> return Precedence.Functional
            OperatorType.Contains -> return Precedence.Contains
            OperatorType.LogicOr -> return Precedence.LogicalOr
            OperatorType.LogicAnd -> return Precedence.LogicalAnd
            OperatorType.Equal, OperatorType.NotEqual -> return Precedence.Equality
            OperatorType.GreaterThan, OperatorType.GreaterOrEqual, OperatorType.LessThan, OperatorType.LessOrEqual -> return Precedence.Relational
            OperatorType.Addition, OperatorType.Subtraction -> return Precedence.Additive
            OperatorType.Multiplication, OperatorType.Division, OperatorType.Modulo -> return Precedence.Multiplicative
            OperatorType.Call, OperatorType.Access, OperatorType.Subscript -> return Precedence.Member
        }
        return null
    }

    fun bindsStrongerThan(leftOperator: Operator): Boolean {
        return if (this.precedence!!.value > leftOperator.precedence!!.value) {
            true
        } else this.associativity === Associativity.Right && this.precedence == leftOperator.precedence

    }

    fun binaryOperatorNodeType(): Class<*>? {
        when (this.type) {
            OperatorType.Except -> return ASTExcept::class.java
            OperatorType.Contains -> return ASTContains::class.java
            OperatorType.LogicOr -> return ASTLogicOr::class.java
            OperatorType.LogicAnd -> return ASTLogicAnd::class.java
            OperatorType.Equal -> return ASTEqual::class.java
            OperatorType.NotEqual -> return ASTNotEqual::class.java
            OperatorType.GreaterThan -> return ASTGreaterThan::class.java
            OperatorType.GreaterOrEqual -> return ASTGreaterOrEqual::class.java
            OperatorType.LessThan -> return ASTLessThan::class.java
            OperatorType.LessOrEqual -> return ASTLessOrEqual::class.java
            OperatorType.Addition -> return ASTAddition::class.java
            OperatorType.Subtraction -> return ASTSubtraction::class.java
            OperatorType.Multiplication -> return ASTMultiplication::class.java
            OperatorType.Division -> return ASTDivision::class.java
            OperatorType.Modulo -> return ASTModulo::class.java
        }

        return null
    }
}
