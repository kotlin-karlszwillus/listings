package com.openpromos.jni.nrexpressionlib.lexer

/**
 * The Lexer to translate the interpreted characters into Tokens of the Language
 */
class Lexer {

    private var currentIndex: Int = 0
    private var endIndex: Int = 0
    private val buffer: ByteArray?

    constructor(expression: String) {
        var expression = expression
        // TODO: Make absolutely sure we get UTF-8 here!
        expression += "\u0000"
        buffer = expression.mapTo(mutableListOf(), { it.toByte()}).toByteArray()
        currentIndex = 0
        endIndex = expression.length
    }

    constructor(buffer: ByteArray) {
        if (buffer[buffer.size - 1].toInt() != 0) {
            throw Exception("Buffer not 0 terminated!")
        }

        this.buffer = buffer
        currentIndex = 0
        endIndex = currentIndex + (this.buffer.size - 1)
    }

    fun scanToken(): Token? {
        while (true) {
            val currentChar = buffer!![currentIndex]
            currentIndex += 1
            
            // continue and break not allowed in when-expression
            if (currentChar.toInt() in arrayListOf (9,10,13,32)) continue
            if (currentChar.toInt() == 47) return scanDivis() ?: continue

            // switch over the first character of a token
            return when (currentChar.toInt()) {

                0 /* nul */ -> scanNul()
                33 /* ! */ -> switchNextChar(61.toByte(), Token(TokenType.NotEqual), Token(TokenType.Not))
                34 /* " */ -> scanQuotedString(currentChar)
                36 /* $ */ -> scanLookup()
                37 /* % */ -> Token(TokenType.Modulo)
                38 /* & */ -> switchNextChar(38.toByte(), Token(TokenType.And), Token(TokenType.LexerError, "unexpected character"))
                39 /* ' */ -> scanQuotedString(currentChar)
                40 /* ( */ -> Token(TokenType.LeftParen)
                41 /* ) */ -> Token(TokenType.RightParen)
                42 /* * */ -> Token(TokenType.Star)
                43 /* + */ -> Token(TokenType.Plus)
                44 /* , */ -> Token(TokenType.Comma)
                45 /* - */ -> Token(TokenType.Minus)
                46 /* . */ -> if (buffer[currentIndex] >= 48 && buffer[currentIndex] <= 57) scanFloat(currentIndex - 1) else Token(TokenType.Dot)
                48, 49, 50, 51, 52, 53, 54, 55, 56, 57 /* 0-9 */ -> scanNumber()
                58 /* : */ -> switchNextChar(61.toByte(), Token(TokenType.Assign), Token(TokenType.Colon))
                59 /* ; */ -> Token(TokenType.Semicolon)
                60 /* < */ -> switchNextChar(61.toByte(), Token(TokenType.LessOrEqual), Token(TokenType.Less))
                61 /* = */ -> switchNextChar(61.toByte(), Token(TokenType.Equal), Token(TokenType.Equal))
                62 /* > */ -> switchNextChar(61.toByte(), Token(TokenType.GreaterOrEqual), Token(TokenType.Greater))
                63 /* ? */ -> Token(TokenType.Questionmark)
                65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90 /* A-Z */ -> scanIdentifier()
                91 /* [ */ -> Token(TokenType.LeftBracket)
                93 /* ] */ -> Token(TokenType.RightBracket)
                95 /* _ */ -> scanIdentifier()
                97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122 /* a-z */ -> scanIdentifier()
                123 /* { */ -> Token(TokenType.LeftBrace)
                124 /* | */ -> switchNextChar(124.toByte(), Token(TokenType.Or), Token(TokenType.LexerError, "unexpected character"))
                125 /* } */ -> Token(TokenType.RightBrace)
                else -> Token(TokenType.LexerError, "unexpected character")
            }
        }
    }

    private fun scanNul(): Token {
        if (currentIndex == buffer!!.size) {
            currentIndex -= 1
            return Token(TokenType.AtEnd)
        }
        return Token(TokenType.LexerError, "unexpected nul character")
    }

    private fun switchNextChar(nextChar: Byte, token: Token, defaultToken: Token): Token {
        if (buffer!![currentIndex] != nextChar) {
            return defaultToken
        }
        currentIndex += 1
        return token
    }

    private fun scanDivis(): Token? {
        when (buffer!![currentIndex].toInt()) {
            42 -> return skipMultiLineComment()
            47 -> {
                skipSingleLineComment()
                return null
            }
            else -> return Token(TokenType.Divis)
        }
    }

    private fun skipSingleLineComment() {
        currentIndex += 1 // skip over second '/'
        singleLineLoop@ while (true) {
            val currentChar = buffer!![currentIndex]
            currentIndex += 1
            if (currentChar.toInt() == 0 || currentChar.toInt() == 10) {
                break@singleLineLoop
            }
        }
    }

    private fun skipMultiLineComment(): Token? {
        currentIndex += 1 // skip over '*'
        multiLineLoop@ while (true) {
            val currentChar = buffer!![currentIndex]
            if (currentChar.toInt() == 0) {
                return Token(TokenType.LexerError, "unterminated mutliline comment")
            }
            currentIndex += 1
            if (currentChar.toInt() == 42 && buffer!![currentIndex].toInt() == 47) {
                break@multiLineLoop
            }
        }
        currentIndex += 1
        return null
    }

    private fun scanIdentifier(): Token {
        val start = currentIndex - 1
        while (true) {
            val currentChar = buffer!![currentIndex]
            if (!isIdentTrail(currentChar)) {
                break
            }
            currentIndex++
        }
        val end = currentIndex

        val string = getAsString(start, end - start, buffer)
        val keywordToken = getKeywordToken(string)

        return keywordToken ?: Token(TokenType.Identifier, string)
    }

    /**
     * Scan a integer and float numbers
     */
    private fun scanNumber(): Token {

        currentIndex--
        var start = currentIndex
        var currentChar: Byte

        intLoop@ while (true) {
            currentChar = buffer!![currentIndex]
            when (currentChar.toInt()) {
                48 -> if (start == currentIndex) {
                    start++
                }
                49, 50, 51, 52, 53, 54, 55, 56, 57 -> {
                }
                else -> break@intLoop
            }
            currentIndex++

            if (currentIndex >= buffer!!.size - 1) {
                break
            }
        }

        if (isIdentHead(currentChar)) {
            return Token(TokenType.LexerError, "bad float literal")
        }

        if (start == currentIndex) {
            start--
        }

        if (currentChar.toInt() == 46) {

            currentIndex++
            currentChar = buffer!![currentIndex]

            if (isDigit(currentChar.toInt())) {
                return scanFloat(start)
            }

            if (isIdentHead(currentChar)) {
                currentIndex--
                return Token(TokenType.Int, getAsString(start, currentIndex - start, buffer))
            }

            return Token(TokenType.Float, getAsString(start, currentIndex - start - 1, buffer))
        }

        return Token(TokenType.Int, getAsString(start, currentIndex - start, buffer))
    }

    /**
     * Scan a integer and float numbers
     */
    private fun scanFloat(start: Int): Token {

        val decimalPointPosition = currentIndex - 1

        var currentChar: Byte
        floatLoop@ while (true) {
            currentChar = buffer!![currentIndex]
            when (currentChar.toInt()) {
                48, 49, 50, 51, 52, 53, 54, 55, 56, 57 -> currentIndex++
                else -> break@floatLoop
            }
        }

        if (isIdentHead(currentChar)) {
            return Token(TokenType.LexerError, "bad float literal")
        }

        var end = currentIndex
        while (buffer!![end - 1].toInt() == 48) {
            end--
        }

        if (end - 1 != decimalPointPosition) {
            return Token(TokenType.Float, getAsString(start, end - start, buffer))
        }

        return if (start == decimalPointPosition) {
            Token(TokenType.Float, getAsString(end, 1, buffer))
        } else Token(TokenType.Float, getAsString(start, decimalPointPosition - start, buffer))

    }

    /**
     * Scan single or double quoted strings.
     */
    private fun scanQuotedString(quoteChar: Byte): Token {

        // The fast path only succeeds when there are no characters to unescape in the
        // string. Use the slow path otherwise.

        //KS: If the first item is null, return second one like: first != null ? first : second;

        val fastPath = scanQuotedStringFastPath(quoteChar)
        return fastPath ?: scanQuotedStringSlowPath(quoteChar)

    }


    private fun scanQuotedStringFastPath(quoteChar: Byte): Token? {

        val start = currentIndex
        var currentChar: Byte

        while (true) {
            currentChar = buffer!![currentIndex]
            currentIndex++
            if (currentChar.toInt() == 0) {
                if (currentIndex != endIndex) {
                    continue
                }
                return Token(TokenType.LexerError, "unterminated string literal")
            } else if (currentChar == quoteChar) {
                break
            } else if (currentChar.toInt() == 92 /* Backslash*/) {
                currentIndex = start
                return null
            } else {
                continue
            }
        }
        return Token(TokenType.String, getAsString(start, currentIndex - start - 1, buffer))
    }

    private fun scanQuotedStringSlowPath(quoteChar: Byte): Token {

        val builder = StringBuilder()

        var currentChar: Byte
        utf8FragmentLoop@ while (true) {
            currentChar = buffer!![currentIndex]
            currentIndex++
            if (currentChar.toInt() == 0) {
                if (currentIndex != endIndex) {
                    builder.append(currentChar.toInt().toChar())
                    continue
                }
                return Token(TokenType.LexerError, "unterminated string literal")
            } else if (currentChar == quoteChar) {
                break
            } else if (currentChar.toInt() == 92 /* Backslash*/) {

            } else {
                builder.append(currentChar.toInt().toChar())
                continue
            }

            val nextChar = buffer!![currentIndex]
            currentIndex++

            if (nextChar == quoteChar || nextChar.toInt() == 92 /* Backslash*/) {
                builder.append(nextChar.toInt().toChar())
                continue
            } else if (nextChar.toInt() == 110) {
                builder.append(10.toChar())
                continue
            } else {
                return Token(TokenType.LexerError, "unterminated string literal")
            }
        }

        return Token(TokenType.String, builder.toString())
    }

    private fun scanLookup(): Token {

        var currentChar = buffer!![currentIndex]
        val isMulti = currentChar.toInt() == 36

        if (isMulti) {
            currentIndex++
            currentChar = buffer!![currentIndex]
        }

        if (!isIdentHead(currentChar)) {
            return Token(TokenType.LexerError, "bad lookup name")
        }

        val start = currentIndex
        while (isIdentTrail(currentChar)) {
            currentIndex++
            currentChar = buffer!![currentIndex]
        }

        val string = getAsString(start, currentIndex - start, buffer)
        return if (isMulti) Token(TokenType.MultiLookup, string) else Token(TokenType.Lookup, string)
    }

    private fun isDigit(c: Int): Boolean {
        return c >= 48 && c <= 57
    }

    private fun isLetter(c: Int): Boolean {
        return c >= 65 && c <= 90 || c >= 97 && c <= 122
    }

    private fun isIdentTrail(c: Byte): Boolean {
        return isDigit(c.toInt()) || isLetter(c.toInt()) || c.toInt() == 95
    }

    private fun isIdentHead(c: Byte): Boolean {
        return isLetter(c.toInt()) || c.toInt() == 95
    }

    private fun getAsString(start: Int, length: Int, buffer: ByteArray): String {
        val bytes = CharArray(length)
        val end = start + length
        var pos = 0
        for (i in start until end) {
            bytes[pos] = buffer[i].toChar()
            pos++
        }
        return bytes.joinToString(separator = "")
    }

    private fun getKeywordToken(string: String?): Token? {

        if (string == null || string.length == 0) {
            return null
        }

        val firstChar = string[0]
        when (firstChar.toInt()) {
            78 -> return if (string == "NULL") {
                Token(TokenType.Null, null)
            } else {
                null
            }
            97 -> return if (string == "and") {
                Token(TokenType.And, null)
            } else if (string == "assert") {
                Token(TokenType.Assert, null)
            } else {
                null
            }
            98 -> return if (string == "break") {
                Token(TokenType.Break, null)
            } else {
                null
            }
            99 -> return if (string == "catch") {
                Token(TokenType.Catch, null)
            } else if (string == "contains") {
                Token(TokenType.Contains, null)
            } else if (string == "continue") {
                Token(TokenType.Continue, null)
            } else {
                null
            }
            101 -> return if (string == "else") {
                Token(TokenType.Else, null)
            } else if (string == "error") {
                Token(TokenType.Error, null)
            } else if (string == "except") {
                Token(TokenType.Except, null)
            } else {
                null
            }
            102 -> return if (string == "false") {
                Token(TokenType.False, null)
            } else if (string == "for") {
                Token(TokenType.For, null)
            } else {
                null
            }
            105 -> return if (string == "if") {
                Token(TokenType.If, null)
            } else if (string == "in") {
                Token(TokenType.In, null)
            } else {
                null
            }
            109 -> return if (string == "map") {
                Token(TokenType.Map, null)
            } else {
                null
            }
            111 -> return if (string == "or") {
                Token(TokenType.Or, null)
            } else {
                null
            }
            112 -> return if (string == "print") {
                Token(TokenType.Print, null)
            } else {
                null
            }
            114 -> return if (string == "return") {
                Token(TokenType.Return, null)
            } else {
                null
            }
            116 -> return if (string == "true") {
                Token(TokenType.True, null)
            } else if (string == "try") {
                Token(TokenType.Try, null)
            } else {
                null
            }
            119 -> return if (string == "where") {
                Token(TokenType.Where, null)
            } else if (string == "while") {
                Token(TokenType.While, null)
            } else {
                null
            }
            else -> return null
        }
    }
}
