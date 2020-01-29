package com.openpromos.jni.nrexpressionlib.parser

import com.openpromos.jni.nrexpressionlib.lexer.Token
import com.openpromos.jni.nrexpressionlib.lexer.TokenType
import com.openpromos.jni.nrexpressionlib.syntaxtree.ASTNode
import com.openpromos.jni.nrexpressionlib.syntaxtree.operators.binary.ASTAddition
import com.openpromos.jni.nrexpressionlib.syntaxtree.operators.binary.ASTContains
import com.openpromos.jni.nrexpressionlib.syntaxtree.operators.binary.ASTDivision
import com.openpromos.jni.nrexpressionlib.syntaxtree.operators.binary.ASTEqual
import com.openpromos.jni.nrexpressionlib.syntaxtree.operators.binary.ASTExcept
import com.openpromos.jni.nrexpressionlib.syntaxtree.operators.binary.ASTGreaterOrEqual
import com.openpromos.jni.nrexpressionlib.syntaxtree.operators.binary.ASTGreaterThan
import com.openpromos.jni.nrexpressionlib.syntaxtree.operators.binary.ASTLessOrEqual
import com.openpromos.jni.nrexpressionlib.syntaxtree.operators.binary.ASTLessThan
import com.openpromos.jni.nrexpressionlib.syntaxtree.operators.binary.ASTLogicAnd
import com.openpromos.jni.nrexpressionlib.syntaxtree.operators.binary.ASTLogicOr
import com.openpromos.jni.nrexpressionlib.syntaxtree.operators.binary.ASTModulo
import com.openpromos.jni.nrexpressionlib.syntaxtree.operators.binary.ASTMultiplication
import com.openpromos.jni.nrexpressionlib.syntaxtree.operators.binary.ASTNotEqual
import com.openpromos.jni.nrexpressionlib.syntaxtree.operators.binary.ASTSubtraction
import com.openpromos.jni.nrexpressionlib.value.OperatorType
import kotlin.reflect.KClass

/**
 * Translation from Tokens to Syntax-Tree Operators and Nodes
 */
class Operator(token: Token) {
    var type: OperatorType? = null
    var precedence: Precedence? = null
    var associativity: Associativity? = null

    init {
        this.type = when (token.type) {
            TokenType.Except -> OperatorType.Except
            TokenType.Questionmark -> OperatorType.Conditional
            TokenType.Where -> OperatorType.Where
            TokenType.Map -> OperatorType.Map
            TokenType.Contains -> OperatorType.Contains
            TokenType.Or -> OperatorType.LogicOr
            TokenType.And -> OperatorType.LogicAnd
            TokenType.NotEqual -> OperatorType.NotEqual
            TokenType.Equal -> OperatorType.Equal
            TokenType.Greater -> OperatorType.GreaterThan
            TokenType.GreaterOrEqual -> OperatorType.GreaterOrEqual
            TokenType.Less -> OperatorType.LessThan
            TokenType.LessOrEqual -> OperatorType.LessOrEqual
            TokenType.Plus -> OperatorType.Addition
            TokenType.Minus -> OperatorType.Subtraction
            TokenType.Star -> OperatorType.Multiplication
            TokenType.Divis -> OperatorType.Division
            TokenType.Modulo -> OperatorType.Modulo
            TokenType.LeftParen -> OperatorType.Call
            TokenType.Dot -> OperatorType.Access
            TokenType.LeftBracket -> OperatorType.Subscript
            else -> null
        }
        associativity = this.associativity()
        precedence = this.precedence()
    }

    private fun associativity(): Associativity {
        // The only right associative operators are unary not and unary minus.
        // They are not parsed in _parsePrimaryExpression, so they are not listed here.
        return Associativity.Left
    }

    private fun precedence(): Precedence? =
        when (this.type) {
            OperatorType.Except -> Precedence.Exceptional
            OperatorType.Conditional -> Precedence.Conditional
            OperatorType.Where, OperatorType.Map -> Precedence.Functional
            OperatorType.Contains -> Precedence.Contains
            OperatorType.LogicOr -> Precedence.LogicalOr
            OperatorType.LogicAnd -> Precedence.LogicalAnd
            OperatorType.Equal, OperatorType.NotEqual -> Precedence.Equality
            OperatorType.GreaterThan, OperatorType.GreaterOrEqual, OperatorType.LessThan, OperatorType.LessOrEqual -> Precedence.Relational
            OperatorType.Addition, OperatorType.Subtraction -> Precedence.Additive
            OperatorType.Multiplication, OperatorType.Division, OperatorType.Modulo -> Precedence.Multiplicative
            OperatorType.Call, OperatorType.Access, OperatorType.Subscript -> Precedence.Member
            else -> null
        }

    fun bindsStrongerThan(leftOperator: Operator) =
        if (this.precedence!!.value > leftOperator.precedence!!.value) {
            true
        } else this.associativity === Associativity.Right && this.precedence == leftOperator.precedence
    

    fun binaryOperatorNodeType(): KClass<out ASTNode>? =
        when (this.type) {
            OperatorType.Except -> ASTExcept::class
            OperatorType.Contains -> ASTContains::class
            OperatorType.LogicOr -> ASTLogicOr::class
            OperatorType.LogicAnd -> ASTLogicAnd::class
            OperatorType.Equal -> ASTEqual::class
            OperatorType.NotEqual -> ASTNotEqual::class
            OperatorType.GreaterThan -> ASTGreaterThan::class
            OperatorType.GreaterOrEqual -> ASTGreaterOrEqual::class
            OperatorType.LessThan -> ASTLessThan::class
            OperatorType.LessOrEqual -> ASTLessOrEqual::class
            OperatorType.Addition -> ASTAddition::class
            OperatorType.Subtraction -> ASTSubtraction::class
            OperatorType.Multiplication -> ASTMultiplication::class
            OperatorType.Division -> ASTDivision::class
            OperatorType.Modulo -> ASTModulo::class
            else -> null
        }
}
