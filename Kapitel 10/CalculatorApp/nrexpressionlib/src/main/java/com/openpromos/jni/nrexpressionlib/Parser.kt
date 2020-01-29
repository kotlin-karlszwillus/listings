package com.openpromos.jni.nrexpressionlib

import android.util.Log

import com.openpromos.jni.nrexpressionlib.ast.astbase.ASTBinaryOperator
import com.openpromos.jni.nrexpressionlib.ast.astbase.ASTExpression
import com.openpromos.jni.nrexpressionlib.ast.astbase.ASTLiteral
import com.openpromos.jni.nrexpressionlib.ast.astbase.ASTNode
import com.openpromos.jni.nrexpressionlib.ast.astbinaryoperators.*
import com.openpromos.jni.nrexpressionlib.ast.astliterals.ASTBoolLiteral
import com.openpromos.jni.nrexpressionlib.ast.astliterals.ASTDictLiteral
import com.openpromos.jni.nrexpressionlib.ast.astliterals.ASTKeyValueTupel
import com.openpromos.jni.nrexpressionlib.ast.astliterals.ASTListLiteral
import com.openpromos.jni.nrexpressionlib.ast.astliterals.ASTNullLiteral
import com.openpromos.jni.nrexpressionlib.ast.astliterals.ASTNumberLiteral
import com.openpromos.jni.nrexpressionlib.ast.astliterals.ASTStringLiteral
import com.openpromos.jni.nrexpressionlib.ast.astotheroperators.ASTAccess
import com.openpromos.jni.nrexpressionlib.ast.astotheroperators.ASTCall
import com.openpromos.jni.nrexpressionlib.ast.astotheroperators.ASTConditionalOperator
import com.openpromos.jni.nrexpressionlib.ast.astotheroperators.ASTIdentifier
import com.openpromos.jni.nrexpressionlib.ast.astotheroperators.ASTLookup
import com.openpromos.jni.nrexpressionlib.ast.astotheroperators.ASTMapOperator
import com.openpromos.jni.nrexpressionlib.ast.astotheroperators.ASTSubscript
import com.openpromos.jni.nrexpressionlib.ast.astotheroperators.ASTWhereOperator
import com.openpromos.jni.nrexpressionlib.ast.aststatement.ASTBlock
import com.openpromos.jni.nrexpressionlib.ast.aststatement.ASTStatement
import com.openpromos.jni.nrexpressionlib.ast.astunaryoperators.ASTArithmeticNegation
import com.openpromos.jni.nrexpressionlib.ast.astunaryoperators.ASTLogicalNegation
import com.openpromos.jni.nrexpressionlib.value.Element
import com.openpromos.jni.nrexpressionlib.value.LookupDescription
import com.openpromos.jni.nrexpressionlib.value.OperatorType

import junit.framework.Assert

import java.io.IOException
import java.util.ArrayList
import java.util.Hashtable
import java.util.Objects

/**
 * Created by voigt on 22.06.16.
 *
 * The `Parser` transforms a stream of `Token`s to the abstract syntax tree (AST)
 * representation.
 * It checks syntax of the source code and returns an error if the input does not conform.
 * It also determines the order in which expressions evaluate their arguments.
 */
class Parser(private val lexer: Lexer) {
    var currentToken: Token? = null

    val isAtEnd: Boolean
        get() {
            when (this.currentToken!!.type) {
                TokenType.AtEnd -> return true
                else -> return false
            }
        }

    init {
        this.currentToken = this.lexer.scanToken()
    }

    @Throws(IllegalStateException::class)
    fun consumeCurrentToken() {
        when (this.currentToken!!.type) {
            TokenType.AtEnd, TokenType.LexerError -> throw IllegalStateException("implementation error: the parser should have caught this")
            else -> {
            }
        }

        this.currentToken = this.lexer.scanToken()
    }


    @Throws(Exception::class)
    fun consumeIdentifier(): String {
        val identifier = Token(TokenType.Identifier, this.currentToken!!.value)
        if (!this.currentToken!!.isEqualTo(identifier)) {
            throw ParserException(this.unexpectedToken())
        }

        try {
            this.consumeCurrentToken()
            return identifier.value ?: throw IllegalStateException("Identifier Token found, but data null")
        } catch (e: IllegalStateException) {
            throw e
        }

    }

    @Throws(Exception::class)
    fun consume(token: Token) {
        if (!token.isEqualTo(this.currentToken!!)) {
            throw ParserException(this.unexpectedToken())
        }
        try {
            this.consumeCurrentToken()
        } catch (e: IllegalStateException) {
            throw e
        }

    }

    fun unexpectedToken(): ParserException.ParserErrorType {
        when (currentToken!!.type) {
            TokenType.AtEnd -> return ParserException.ParserErrorType.UnexpectedEnd
            TokenType.LexerError -> return ParserException.ParserErrorType.InvalidToken
            else -> return ParserException.ParserErrorType.UnexpectedToken
        }
    }

    /** Parses an expression and returns it as an AST node.
     * This method parses a whole expression, respecting precedence and
     * associativity. It combines operators on the go using precedence
     * climbing.
     * - seealso: [wikipedia article](https://en.wikipedia.org/wiki/Operator-precedence_parser) on precedence climbing
     */
    @Throws(Exception::class)
    fun parseExpression(): ASTExpression {
        try {
            return this.parseExpression(Precedence.Lowest)
        } catch (e: Exception) {
            throw e
        }

    }

    @Throws(Exception::class)
    private fun parseExpression(minPrecedence: Precedence): ASTExpression {
        val primary: ASTExpression?

        try {
            primary = this.parsePrimaryExpression()
        } catch (e: Exception) {
            throw e
        }

        try {
            return this.parseCombinedExpression(primary, minPrecedence)
        } catch (e: Exception) {
            throw e
        }

    }

    @Throws(Exception::class)
    private fun parsePrimaryExpression(): ASTExpression {

        when (this.currentToken!!.type) {

            TokenType.Lookup, TokenType.MultiLookup -> {
                try {
                    return this.parseLookup()
                } catch (e: Exception) {
                    throw e
                }
            }

            TokenType.Identifier -> {
                try {
                    val name = this.consumeIdentifier()
                    return ASTIdentifier(name)
                } catch (e: Exception) {
                    throw e
                }


            }

            TokenType.Int, TokenType.Float, TokenType.String, TokenType.True, TokenType.False, TokenType.Null, TokenType.LeftBracket -> {
                run {
                    try {
                        return this.parseLiteral()
                    } catch (e: Exception) {
                        throw e
                    }
                }

            }

            TokenType.Minus -> {
                run {
                    try {
                        this.consume(Token(TokenType.Minus))
                    } catch (e: Exception) {
                        throw e
                    }

                    try {
                        val expression = this.parseExpression(Precedence.Prefix)
                        return ASTArithmeticNegation(expression)
                    } catch (e: Exception) {
                        throw e
                    }
                }

            }

            TokenType.Not -> {
                run {
                    try {
                        this.consume(Token(TokenType.Not))
                    } catch (e: Exception) {
                        throw e
                    }

                    try {
                        val expression = this.parseExpression(Precedence.Prefix)
                        return ASTLogicalNegation(expression)
                    } catch (e: Exception) {
                        throw e
                    }
                }
            }

            TokenType.LeftParen -> {
                run {
                    try {
                        this.consume(Token(TokenType.LeftParen))
                    } catch (e: Exception) {
                        throw e
                    }

                    var expression: ASTExpression? = null
                    try {
                        expression = this.parseExpression()
                    } catch (e: Exception) {
                        throw e
                    }

                    try {
                        this.consume(Token(TokenType.RightParen))
                        return expression
                    } catch (e: Exception) {
                        throw e
                    }
                }
            }
            else -> throw ParserException(this.unexpectedToken())
        }
    }

    // This is the core precedence climbing expression parser.
    @Throws(Exception::class)
    private fun parseCombinedExpression(originalExpression: ASTExpression, minPrecedence: Precedence?): ASTExpression {

        var accumulatedLHS = originalExpression
        var leftOperator = Operator(this.currentToken!!)

        while (leftOperator.type != null && leftOperator.precedence!!.value >= minPrecedence!!.value) {
            try {
                this.consumeCurrentToken()
            } catch (e: Exception) {
                throw e
            }

            when (leftOperator.type) {
                OperatorType.Conditional -> try {
                    accumulatedLHS = this.parseConditionalOperator(accumulatedLHS)
                } catch (e: Exception) {
                    throw e
                }

                OperatorType.Where -> try {
                    accumulatedLHS = this.parseWhereOperator(accumulatedLHS)
                } catch (e: Exception) {
                    throw e
                }

                OperatorType.Map -> try {
                    accumulatedLHS = this.parseMapOperator(accumulatedLHS)
                } catch (e: Exception) {
                    throw e
                }

                OperatorType.Except, OperatorType.Contains, OperatorType.LogicOr, OperatorType.LogicAnd, OperatorType.Equal, OperatorType.NotEqual, OperatorType.GreaterThan, OperatorType.GreaterOrEqual, OperatorType.LessThan, OperatorType.LessOrEqual, OperatorType.Addition, OperatorType.Subtraction, OperatorType.Multiplication, OperatorType.Division, OperatorType.Modulo -> try {
                    accumulatedLHS = this.parseBinaryOperator(leftOperator, accumulatedLHS)
                } catch (e: Exception) {
                    throw e
                }

                OperatorType.Call -> try {
                    accumulatedLHS = this.parseCallOperator(accumulatedLHS)
                } catch (e: Exception) {
                    throw e
                }

                OperatorType.Access -> try {
                    accumulatedLHS = this.parseAccessOperator(accumulatedLHS)
                } catch (e: Exception) {
                    throw e
                }

                OperatorType.Subscript -> try {
                    accumulatedLHS = this.parseSubscriptOperator(accumulatedLHS)
                } catch (e: Exception) {
                    throw e
                }

            }


            leftOperator = Operator(this.currentToken!!)
        }

        return accumulatedLHS
    }

    @Throws(Exception::class)
    fun parseConditionalOperator(condition: ASTExpression): ASTExpression {
        val positiveExpression: ASTExpression?

        try {
            positiveExpression = parseExpression()
        } catch (e: Exception) {
            throw e
        }

        try {
            this.consume(Token(TokenType.Colon))
        } catch (e: Exception) {
            throw e
        }

        try {
            val negativeExpression = this.parseExpression(Precedence.Conditional)
            return ASTConditionalOperator(condition, positiveExpression, negativeExpression)
        } catch (e: Exception) {
            throw e
        }

    }

    @Throws(Exception::class)
    fun parseWhereOperator(lhs: ASTExpression): ASTExpression {
        val identifier: String?

        try {
            identifier = this.consumeIdentifier()
        } catch (e: Exception) {
            throw e
        }

        try {
            this.consume(Token(TokenType.Colon))
        } catch (e: Exception) {
            throw e
        }

        try {
            val predicateExpression = this.parseExpression(Precedence.Functional)
            return ASTWhereOperator(lhs, identifier, predicateExpression)
        } catch (e: Exception) {
            throw e
        }

    }

    @Throws(Exception::class)
    private fun parseMapOperator(lhs: ASTExpression): ASTExpression {
        val identifier: String?

        try {
            identifier = consumeIdentifier()
        } catch (e: Exception) {
            throw e
        }

        try {
            this.consume(Token(TokenType.Colon))
        } catch (e: Exception) {
            throw e
        }

        try {
            val transformExpression = this.parseExpression(Precedence.Functional)
            return ASTMapOperator(lhs, identifier, transformExpression)
        } catch (e: Exception) {
            throw e
        }

    }

    @Throws(Exception::class)
    private fun parseBinaryOperator(leftOperator: Operator, lhs: ASTExpression?): ASTExpression {
        var accumulatedRHS: ASTExpression

        try {
            accumulatedRHS = this.parsePrimaryExpression()
        } catch (e: Exception) {
            throw e
        }

        var rightOperator = Operator(this.currentToken!!)

        while (rightOperator.type != null && rightOperator.bindsStrongerThan(leftOperator)) {
            try {
                accumulatedRHS = this.parseCombinedExpression(accumulatedRHS, rightOperator.precedence)
                rightOperator = Operator(this.currentToken!!)
            } catch (e: Exception) {
                throw e
            }

        }

        val nodeType = leftOperator.binaryOperatorNodeType()

        val newInstance = nodeType!!.newInstance()

        if (newInstance is ASTBinaryOperator) {
                newInstance.lhs = lhs
                newInstance.rhs = accumulatedRHS
                return newInstance
        }
        else {
            throw RuntimeException("nodeType must be a ASTBinaryOperator!")
        }
    }

    @Throws(Exception::class)
    private fun parseCallOperator(callable: ASTExpression): ASTExpression {
        val arguments = ArrayList<ASTExpression>()

        val rightParen = Token(TokenType.RightParen)

        if (this.currentToken!!.isEqualTo(rightParen)) {
            try {
                this.consume(rightParen)
            } catch (e: Exception) {
                throw e
            }

            return ASTCall(callable, arguments)
        }

        argumentsLoop@ while (true) {
            var expression: ASTExpression? = null

            try {
                expression = parseExpression()
            } catch (e: Exception) {
                throw e
            }

            if (expression != null) arguments.add(expression)

            when (this.currentToken!!.type) {
                TokenType.Comma -> try {
                    this.consume(Token(TokenType.Comma))
                } catch (e: Exception) {
                    throw e
                }

                TokenType.RightParen -> {
                    try {
                        this.consume(rightParen)
                    } catch (e: Exception) {
                        throw e
                    }

                    break@argumentsLoop
                }
                else -> throw ParserException(this.unexpectedToken())
            }
        }

        return ASTCall(callable, arguments)
    }

    @Throws(Exception::class)
    private fun parseAccessOperator(`object`: ASTExpression): ASTExpression {
        var identifier: String? = null

        try {
            identifier = this.consumeIdentifier()
        } catch (e: Exception) {
            throw e
        }

        //Log.d("Parser", "Object: " + object.toString() + " identifier: " + identifier);
        return ASTAccess(`object`, identifier)

    }

    @Throws(Exception::class)
    private fun parseSubscriptOperator(container: ASTExpression): ASTExpression {
        var indexExpression: ASTExpression
        try {
            indexExpression = this.parseExpression()
        } catch (e: Exception) {
            throw e
        }

        try {
            this.consume(Token(TokenType.RightBracket))

        } catch (e: Exception) {
            throw e
        }

        return ASTSubscript(container, indexExpression)
    }

    /**
     * literals
     * @return ASTLiteral
     * @throws Exception
     */
    @Throws(Exception::class)
    private fun parseLiteral(): ASTLiteral {

        val token = this.currentToken

        try {
            consumeCurrentToken()
        } catch (e: Exception) {
            throw e
        }

        when (token!!.type) {

            TokenType.Int -> {
                val node = ASTNumberLiteral(token.value)

                return if (node.value == java.lang.Double.parseDouble(token.value)) {
                    node
                } else {
                    throw NumberFormatException("Int ValueOutOfRange!")
                }
            }
            TokenType.Float -> {
                val node = ASTNumberLiteral(token.value)

                return if (node.value == java.lang.Double.parseDouble(token.value)) {
                    node
                } else {
                    throw NumberFormatException("Int ValueOutOfRange!")
                }
            }
            TokenType.String -> return ASTStringLiteral(token.value)

            TokenType.True -> return ASTBoolLiteral(true)

            TokenType.False -> return ASTBoolLiteral(false)

            TokenType.Null -> return ASTNullLiteral

            TokenType.LeftBracket -> {
                run {
                    try {
                        return this.parseContainerLiteral()
                    } catch (e: Exception) {
                        throw e
                    }
                }
                throw RuntimeException("parseLiteral on bad token")
            }
            else -> throw RuntimeException("parseLiteral on bad token")
        }
    }

    @Throws(Exception::class)
    private fun parseContainerLiteral(): ASTLiteral {
        val literal: ASTLiteral?

        try {
            literal = this.parseEmptyContainerLiteral()
        } catch (e: Exception) {
            throw e
        }

        if (literal != null) {
            return literal
        }

        var firstElement: ASTExpression? = null
        try {
            firstElement = this.parseExpression()
        } catch (e: Exception) {
            throw e
        }

        when (this.currentToken!!.type) {

            TokenType.RightBracket -> {
                try {
                    this.consume(Token(TokenType.RightBracket))
                } catch (e: Exception) {
                    throw e
                }

                val elements = ArrayList<ASTExpression>()
                if (firstElement != null) elements.add(firstElement)
                return ASTListLiteral(elements)
            }
            TokenType.Comma -> {
                try {
                    return this.parseListLiteral(firstElement)
                } catch (e: Exception) {
                    throw e
                }
            }
            TokenType.Colon -> {
                try {
                    return this.parseDictLiteral(firstElement)
                } catch (e: Exception) {
                    throw e
                }
            }
            else -> throw ParserException(this.unexpectedToken())
        }
    }

    @Throws(Exception::class)
    private fun parseEmptyContainerLiteral(): ASTLiteral? {
        when (this.currentToken!!.type) {

            TokenType.RightBracket -> {
                try {
                    this.consume(Token(TokenType.RightBracket))
                } catch (e: Exception) {
                    throw e
                }

                return ASTListLiteral(ArrayList())
            }
            TokenType.Colon -> {
                try {
                    this.consume(Token(TokenType.Colon))
                } catch (e: Exception) {
                    throw e
                }

                try {
                    this.consume(Token(TokenType.RightBracket))
                } catch (e: Exception) {
                    throw e
                }

                return ASTDictLiteral(ArrayList())
            }

            else -> return null
        }
    }

    @Throws(Exception::class)
    private fun parseListLiteral(firstElement: ASTExpression?): ASTListLiteral {

        val elements = ArrayList<ASTExpression>()
        if (firstElement != null) elements.add(firstElement)

        elementsLoop@ while (true) {
            when (this.currentToken!!.type) {
                TokenType.Comma -> {
                    try {
                        this.consume(Token(TokenType.Comma))
                    } catch (e: Exception) {
                        throw e
                    }

                    val rightBracketToken = Token(TokenType.RightBracket)

                    if (rightBracketToken.isEqualTo(this.currentToken!!)) {
                        try {
                            this.consume(Token(TokenType.RightBracket))
                        } catch (e: Exception) {
                            throw e
                        }

                        break@elementsLoop
                    }
                }
                TokenType.RightBracket -> {
                    try {
                        this.consume(Token(TokenType.RightBracket))
                    } catch (e: Exception) {
                        throw e
                    }

                    break@elementsLoop
                }
                else -> throw ParserException(this.unexpectedToken())
            }

            var expression: ASTExpression? = null
            try {
                expression = this.parseExpression()
            } catch (e: Exception) {
                throw e
            }

            if (expression != null) elements.add(expression)
        }

        return ASTListLiteral(elements)
    }

    @Throws(Exception::class)
    private fun parseDictLiteral(firstKey: ASTExpression?): ASTDictLiteral {

        val pairs = ArrayList<ASTExpression>()

        var key = firstKey

        pairsLoop@ while (true) {
            try {
                this.consume(Token(TokenType.Colon))
            } catch (e: Exception) {
                throw e
            }

            var expression: ASTExpression? = null
            try {
                expression = this.parseExpression()
            } catch (e: Exception) {
                throw e
            }

            val tupel = ASTKeyValueTupel(key, expression)
            pairs.add(tupel)

            when (this.currentToken!!.type) {

                TokenType.Comma -> {
                    try {
                        this.consume(Token(TokenType.Comma))
                    } catch (e: Exception) {
                        throw e
                    }

                    if (this.currentToken!!.isEqualTo(Token(TokenType.RightBracket))) {
                        try {
                            this.consume(Token(TokenType.RightBracket))
                        } catch (e: Exception) {
                            throw e
                        }

                        break@pairsLoop
                    }
                }
                TokenType.RightBracket -> {
                    try {
                        this.consume(Token(TokenType.RightBracket))
                    } catch (e: Exception) {
                        throw e
                    }

                    break@pairsLoop
                }

                else -> throw ParserException(this.unexpectedToken())
            }

            try {
                key = this.parseExpression()
            } catch (e: Exception) {
                throw e
            }

        }

        return ASTDictLiteral(pairs)
    }

    /**
     * subexpressions
     * @return ASTLookup
     * @throws Exception
     */
    @Throws(Exception::class)
    private fun parseLookup(): ASTLookup {

        val elements = ArrayList<Element>()

        elementsLoop@ while (true) {
            val element: Element

            when (this.currentToken!!.type) {
                // FIXME: using empty string instead of null -correct?
                TokenType.Lookup -> element = Element(Element.ElementType.Single, this.currentToken!!.value ?: "")
                TokenType.MultiLookup -> element = Element(Element.ElementType.Multi, this.currentToken!!.value ?: "")
                else -> break@elementsLoop
            }

            try {
                this.consumeCurrentToken()
            } catch (e: Exception) {
                throw e
            }

            elements.add(element)
        }

        return if (!elements.isEmpty()) {
            ASTLookup(elements)
        } else {
            throw Exception("Lookup: elements array is empty!")
        }
    }

    @Throws(Exception::class)
    fun parseProgram(): ASTBlock {
        val statements = ArrayList<ASTStatement>()
        var statement: ASTStatement

        while (!this.isAtEnd) {
            try {
                statement = this.parseStatement()
            } catch (e: Exception) {
                throw e
            }

            statements.add(statement)
        }

        return ASTBlock(statements)
    }

    @Throws(Exception::class)
    fun parseStatement(): ASTStatement {
        /*        switch (this.currentToken.type) {

        }*/

        return ASTStatement()
    }
}
