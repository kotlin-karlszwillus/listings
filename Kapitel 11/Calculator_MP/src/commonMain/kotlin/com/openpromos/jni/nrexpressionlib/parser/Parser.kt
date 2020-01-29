package com.openpromos.jni.nrexpressionlib.parser

import com.openpromos.jni.nrexpressionlib.lexer.Lexer
import com.openpromos.jni.nrexpressionlib.lexer.Token
import com.openpromos.jni.nrexpressionlib.lexer.TokenType
import com.openpromos.jni.nrexpressionlib.syntaxtree.ASTExpression
import com.openpromos.jni.nrexpressionlib.syntaxtree.operators.binary.*
import com.openpromos.jni.nrexpressionlib.syntaxtree.literals.ASTLiteral
import com.openpromos.jni.nrexpressionlib.syntaxtree.literals.ASTBoolLiteral
import com.openpromos.jni.nrexpressionlib.syntaxtree.literals.ASTDictLiteral
import com.openpromos.jni.nrexpressionlib.syntaxtree.literals.ASTKeyValueTupel
import com.openpromos.jni.nrexpressionlib.syntaxtree.literals.ASTListLiteral
import com.openpromos.jni.nrexpressionlib.syntaxtree.literals.ASTNullLiteral
import com.openpromos.jni.nrexpressionlib.syntaxtree.literals.ASTNumberLiteral
import com.openpromos.jni.nrexpressionlib.syntaxtree.literals.ASTStringLiteral
import com.openpromos.jni.nrexpressionlib.syntaxtree.operators.other.ASTAccess
import com.openpromos.jni.nrexpressionlib.syntaxtree.operators.other.ASTCall
import com.openpromos.jni.nrexpressionlib.syntaxtree.operators.other.ASTConditionalOperator
import com.openpromos.jni.nrexpressionlib.syntaxtree.operators.other.ASTIdentifier
import com.openpromos.jni.nrexpressionlib.syntaxtree.operators.other.ASTLookup
import com.openpromos.jni.nrexpressionlib.syntaxtree.operators.other.ASTMapOperator
import com.openpromos.jni.nrexpressionlib.syntaxtree.operators.other.ASTSubscript
import com.openpromos.jni.nrexpressionlib.syntaxtree.operators.other.ASTWhereOperator
import com.openpromos.jni.nrexpressionlib.syntaxtree.statement.ASTBlock
import com.openpromos.jni.nrexpressionlib.syntaxtree.statement.ASTStatement
import com.openpromos.jni.nrexpressionlib.syntaxtree.operators.binary.*
import com.openpromos.jni.nrexpressionlib.syntaxtree.operators.unary.ASTArithmeticNegation
import com.openpromos.jni.nrexpressionlib.syntaxtree.operators.unary.ASTLogicalNegation
import com.openpromos.jni.nrexpressionlib.value.Element
import com.openpromos.jni.nrexpressionlib.value.OperatorType
import kotlin.reflect.KClass

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

    fun consumeCurrentToken() {
        when (this.currentToken!!.type) {
            TokenType.AtEnd, TokenType.LexerError -> throw IllegalStateException("implementation error: the parser should have caught this")
            else -> {
            }
        }

        this.currentToken = this.lexer.scanToken()
    }


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
    fun parseExpression(): ASTExpression {
        try {
            return this.parseExpression(Precedence.Lowest)
        } catch (e: Exception) {
            throw e
        }

    }

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

    fun parseWhereOperator(lhs: ASTExpression): ASTExpression {
        val identifier: String?

        try {
            identifier = this.consumeIdentifier()
            this.consume(Token(TokenType.Colon))
            val predicateExpression = this.parseExpression(Precedence.Functional)
            return ASTWhereOperator(lhs, identifier, predicateExpression)
        } catch (e: Exception) {
            throw e
        }
    }

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

        val newInstance = newOperatorInstance(nodeType!!)

        if (newInstance is ASTBinaryOperator) {
                newInstance.lhs = lhs
                newInstance.rhs = accumulatedRHS
                return newInstance
        }
        else {
            throw RuntimeException("nodeType must be a ASTBinaryOperator!")
        }
    }

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

                return if (node.value == token.value?.toDouble()) {
                    node
                } else {
                    throw NumberFormatException("Int ValueOutOfRange!")
                }
            }
            TokenType.Float -> {
                val node = ASTNumberLiteral(token.value)

                return if (node.value == token.value?.toDouble()) {
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

    fun parseStatement(): ASTStatement {
        return ASTStatement()
    }


    private fun newOperatorInstance(classType : KClass<*>) : ASTExpression {
        when (classType) {
            ASTExcept::class -> return ASTExcept()
            ASTContains::class -> return ASTContains()
            ASTLogicOr::class -> return ASTLogicOr()
            ASTLogicAnd::class -> return ASTLogicAnd()
            ASTEqual::class -> return ASTEqual()
            ASTNotEqual::class -> return ASTNotEqual()
            ASTGreaterThan::class -> return ASTGreaterThan()
            ASTGreaterOrEqual::class -> return ASTGreaterOrEqual()
            ASTLessThan::class -> return ASTLessThan()
            ASTLessOrEqual::class -> return ASTLessOrEqual()
            ASTAddition::class -> return ASTAddition()
            ASTSubtraction::class -> return ASTSubtraction()
            ASTMultiplication::class -> return ASTMultiplication()
            ASTDivision::class -> return ASTDivision()
            ASTModulo::class -> return ASTModulo()
            else -> throw ClassCastException("Class not found")
        }
    }
}
