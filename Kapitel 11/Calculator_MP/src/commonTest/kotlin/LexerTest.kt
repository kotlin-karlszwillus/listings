import com.openpromos.jni.nrexpressionlib.lexer.Lexer
import com.openpromos.jni.nrexpressionlib.lexer.Token
import com.openpromos.jni.nrexpressionlib.lexer.TokenType
import kotlin.test.*

/**
 *
 */
class LexerTest {

    private fun performTest(input: String, expectedOutput: String) {
        val lexer = Lexer(input)
        val buffer = StringBuilder()

        scanloop@ while (true) {
            val token = lexer.scanToken()
            println(token)
            when (token!!.type) {
                TokenType.AtEnd -> break@scanloop
                TokenType.LexerError -> if (expectedOutput == Token.LEXER_ERROR)
                    return
                else {
                    fail()
                }
                else -> {
                    buffer.append(token.toString())
                    buffer.append(" ")
                }
            }
        }

        val result = if (buffer.length > 0) buffer.subSequence(0, buffer.length - 1).toString() else ""
        assertEquals(expectedOutput, result)
    }

    @Test
    fun testFloatOne() {
        performTest("1.0", "Float(1)")
    }

    @Test
    fun testDot() {
        performTest(".", "Dot")
    }

    @Test
    fun testNot() {
        performTest("!", "Not")
    }

    @Test
    fun testEqual() {
        performTest("=", "Equal")
    }

    @Test
    fun testEqual2() {
        performTest("==", "Equal")
    }

    @Test
    fun testNotequal() {
        performTest("!=", "NotEqual")
    }

    @Test
    fun testGreaterOrEqual() {
        performTest(">=", "GreaterOrEqual")
    }

    @Test
    fun testGreater() {
        performTest(">", "Greater")
    }

    @Test
    fun testLessOrEqual() {
        performTest("<=", "LessOrEqual")
    }

    @Test
    fun testLess() {
        performTest("<", "Less")
    }

    @Test
    fun testAssign() {
        performTest(":=", "Assign")
    }

    @Test
    fun testMinus() {
        performTest("-", "Minus")
    }

    @Test
    fun testPlus() {
        performTest("+", "Plus")
    }

    @Test
    fun testComma() {
        performTest(",", "Comma")
    }

    @Test
    fun testStar() {
        performTest("*", "Star")
    }

    @Test
    fun testDivis() {
        performTest("/", "Divis")
    }

    @Test
    fun testModulo() {
        performTest("%", "Modulo")
    }

    @Test
    fun testLeftParenthesis() {
        performTest("(", "LeftParen")
    }

    @Test
    fun testRightParenthesis() {
        performTest(");", "RightParen Semicolon")
    }

    @Test
    fun testSemicolon() {
        performTest(";", "Semicolon")
    }

    @Test
    fun testLeftBrace() {
        performTest("{", "LeftBrace")
    }

    @Test
    fun testRightBrace() {
        performTest("}", "RightBrace")
    }

    @Test
    fun testLeftBracket() {
        performTest("[", "LeftBracket")
    }

    @Test
    fun testRightBracket() {
        performTest("]", "RightBracket")
    }

    @Test
    fun testQuestionmark() {
        performTest("?", "Questionmark")
    }

    @Test
    fun testColon() {
        performTest(":", "Colon")
    }

    @Test
    fun testAnd() {
        performTest("&&", "And")
    }

    @Test
    fun testOr() {
        performTest("||", "Or")
    }

    @Test
    fun testAndKeyword() {
        performTest("and", "And")
    }

    @Test
    fun testAssertKeyword() {
        performTest("assert", "Assert")
    }

    @Test
    fun testBreakKeyword() {
        performTest("break", "Break")
    }

    @Test
    fun testCatchKeyword() {
        performTest("catch", "Catch")
    }

    @Test
    fun testContainsKeyword() {
        performTest("contains", "Contains")
    }

    @Test
    fun testContinueKeyword() {
        performTest("continue", "Continue")
    }

    @Test
    fun testElseKeyword() {
        performTest("else", "Else")
    }

    @Test
    fun testErrorKeyword() {
        performTest("error", "Error")
    }

    @Test
    fun testExceptKeyword() {
        performTest("except", "Except")
    }

    @Test
    fun testFalseKeyword() {
        performTest("false", "False")
    }

    @Test
    fun testForKeyword() {
        performTest("for", "For")
    }

    @Test
    fun testIfKeyword() {
        performTest("if", "If")
    }

    @Test
    fun testInKeyword() {
        performTest("in", "In")
    }

    @Test
    fun testMapKeyword() {
        performTest("map", "Map")
    }

    @Test
    fun testNullKeyword() {
        performTest("NULL", "Null")
    }

    @Test
    fun testOrKeyword() {
        performTest("or", "Or")
    }

    @Test
    fun testPrintKeyword() {
        performTest("print", "Print")
    }

    @Test
    fun testReturnKeyword() {
        performTest("return", "Return")
    }

    @Test
    fun testTrueKeyword() {
        performTest("true", "True")
    }

    @Test
    fun testTryKeyword() {
        performTest("try", "Try")
    }

    @Test
    fun testWhereKeyword() {
        performTest("where", "Where")
    }

    @Test
    fun testWhileKeyword() {
        performTest("while", "While")
    }

    @Test
    fun testEmptySource() {
        performTest("", "")
    }

    @Test
    fun testSimpleSequence() {
        performTest("...", "Dot Dot Dot")
    }

    @Test
    fun testSkipWhitespace() {
        performTest("\t\n\r. \t\n\r.", "Dot Dot")
    }

    @Test
    fun testSingleLineComment() {
        performTest("// C++ comment\n.", "Dot")
    }

    @Test
    fun testMultiLineComment() {
        performTest("/* C comment */.", "Dot")
    }

    @Test
    fun testUnterminatedCComment() {
        performTest("/*", "LEXER_ERROR")
    }

    @Test
    fun testUnterminatedCComment2() {
        performTest("/*/", "LEXER_ERROR")
    }

    @Test
    fun testCCommentPattern() {
        performTest("/*/*/*", "Star")
    }

    @Test
    fun testShortestCComment() {
        performTest("/**/.", "Dot")
    }

    @Test
    fun testSingleStarInCComment() {
        performTest("/***/.", "Dot")
    }

    @Test
    fun testNullCharInCComment() {
        performTest("/*/0*/.", "Dot")
    }

    @Test
    fun testSingleLineCommentWithCEnding() {
        performTest("// C++*/\n.", "Dot")
    }

    @Test
    fun testSingleAndMultiLineComment() {
        performTest("// C++\n/* C */.", "Dot")
    }

    @Test
    fun testSingleLetterIdentifier() {
        performTest("i", "Ident(i)")
    }

    @Test
    fun testMultiLetterIdentifier() {
        performTest("self", "Ident(self)")
    }

    @Test
    fun testIdentifierSequence() {
        performTest("one, two and three", "Ident(one) Comma Ident(two) And Ident(three)")
    }

    @Test
    fun testIdentifierWithLeadingUnderscore() {
        performTest("_a", "Ident(_a)")
    }

    @Test
    fun testIdentifierWithTrailingUnderscore() {
        performTest("a_", "Ident(a_)")
    }

    @Test
    fun testIdentifierSequence2() {
        performTest("_ a", "Ident(_) Ident(a)")
    }

    @Test
    fun testNotAnIdentifier() {
        performTest("0_", "LEXER_ERROR")
    }

    @Test
    fun testSingleLookup() {
        performTest("\$abc", "Lookup(abc)")
    }

    @Test
    fun testSingleLookupWithProp() {
        performTest("\$ab.c", "Lookup(ab) Dot Ident(c)")
    }

    @Test
    fun testMultiLookup() {
        performTest("\$\$_0", "MultiLookup(_0)")
    }

    @Test
    fun testLookupChain() {
        performTest("\$a$\$b\$c$\$d", "Lookup(a) MultiLookup(b) Lookup(c) MultiLookup(d)")
    }

    @Test
    fun testEmptyLookup() {
        performTest("$", "LEXER_ERROR")
    }

    @Test
    fun testEmptyMultiLookup() {
        performTest("$$", "LEXER_ERROR")
    }

    @Test
    fun testBadLookupStartChar() {
        performTest("$0", "LEXER_ERROR")
    }

    @Test
    fun testBadMultiLookupStartChar() {
        performTest("$$0", "LEXER_ERROR")
    }

    @Test
    fun testInteger0() {
        performTest("0", "Int(0)")
    }

    @Test
    fun testInteger1() {
        performTest("1", "Int(1)")
    }

    @Test
    fun testInteger01() {
        performTest("01", "Int(1)")
    }

    @Test
    fun testInteger42() {
        performTest("42", "Int(42)")
    }

    @Test
    fun testIntegerSequence42() {
        performTest("4 2", "Int(4) Int(2)")
    }

    @Test
    fun testIntegerMinus123() {
        performTest("-123", "Minus Int(123)")
    }

    @Test
    fun testBadInteger() {
        performTest("1a", "LEXER_ERROR")
    }

    @Test
    fun testIntegerWithProperty() {
        performTest("1.a", "Int(1) Dot Ident(a)")
    }

    @Test
    fun testFloatZero() {
        performTest("0.0", "Float(0)")
    }

    @Test
    fun testFloatAltZeroA() {
        performTest("0.", "Float(0)")
    }

    @Test
    fun testFloatAltZeroB() {
        performTest(".0", "Float(0)")
    }

    @Test
    fun testFloatAltZeroC() {
        performTest("00.", "Float(0)")
    }

    @Test
    fun testFloatAltZeroD() {
        performTest(".00", "Float(0)")
    }

    @Test
    fun testFloatAltZeroE() {
        performTest("00.00", "Float(0)")
    }

    @Test
    fun testFloat42() {
        performTest("42.", "Float(42)")
    }

    @Test
    fun testFloatSkipRedundantZeros() {
        performTest("010.010", "Float(10.01)")
    }

    @Test
    fun testFloatSequence42() {
        performTest("4. .2", "Float(4) Float(.2)")
    }

    @Test
    fun testFloatMinus123() {
        performTest("-123.", "Minus Float(123)")
    }

    @Test
    fun testBadFloat2() {
        performTest(".1a", "LEXER_ERROR")
    }

    @Test
    fun testBadFloat3() {
        performTest("1.1a", "LEXER_ERROR")
    }

    @Test
    fun testFloatWithProperty() {
        performTest("1.1.a", "Float(1.1) Dot Ident(a)")
    }

    @Test
    fun testDoubleQuotedStringEmpty() {
        performTest("\"\"", "String()")
    }

    @Test
    fun testDoubleQuotedStringSimple() {
        performTest("\"...\"", "String(...)")
    }

    @Test
    fun testDoubleQuotedStringHello() {
        performTest("\"Hello, World!\"", "String(Hello, World!)")
    }

    @Test
    fun testDoubleQuotedStringEscapedQuote() {
        performTest("\"\\\"\"", "String(\")")
    }

    @Test
    fun testDoubleQuotedStringEscapedBackslash() {
        performTest("\"\\\\\"", "String(\\)")
    }

    @Test
    fun testDoubleQuotedStringEscapedNewline() {
        performTest("\"\n\"", "String(\n)")
    }

    @Test
    fun testDoubleQuotedStringWithNulChar() {
        performTest("\"\u0000\"", "String(\u0000)")
    }

    @Test
    fun testDoubleQuotedStringUnterminated() {
        performTest("\"", "LEXER_ERROR")
    }

    @Test
    fun testDoubleQuotedStringUntermEsc() {
        performTest("\"\\", "LEXER_ERROR")
    }

    @Test
    fun testDoubleQuotedStringUntermPostEsc() {
        performTest("\"\\\"", "LEXER_ERROR")
    }

    @Test
    fun testDoubleQuotedStringUnknownEscape() {
        performTest("\"\\r\"", "LEXER_ERROR")
    }

    @Test
    fun testDoubleQuotedStringBadQuoteEscape() {
        performTest("\"\\'\"", "LEXER_ERROR")
    }

    @Test
    fun testSingleQuotedStringEmpty() {
        performTest("''", "String()")
    }

    @Test
    fun testSingleQuotedStringSimple() {
        performTest("'...'", "String(...)")
    }

    @Test
    fun testSingleQuotedStringHello() {
        performTest("'Hello, World!'", "String(Hello, World!)")
    }

    @Test
    fun testSingleQuotedStringEscapedQuote() {
        performTest("'\\''", "String(')")
    }

    @Test
    fun testSingleQuotedStringEscapedBackslash() {
        performTest("'\\\\'", "String(\\)")
    }

    @Test
    fun testSingleQuotedStringEscapedNewline() {
        performTest("'\n'", "String(\n)")
    }

    @Test
    fun testSingleQuotedStringWithNulChar() {
        performTest("'\u0000'", "String(\u0000)")
    }

    @Test
    fun testSingleQuotedStringUnterminated() {
        performTest("'", "LEXER_ERROR")
    }

    @Test
    fun testSingleQuotedStringUntermEsc() {
        performTest("'\\", "LEXER_ERROR")
    }

    @Test
    fun testSingleQuotedStringUntermPostEsc() {
        performTest("'\\'", "LEXER_ERROR")
    }

    @Test
    fun testSingleQuotedStringUnknownEscape() {
        performTest("'\\r'", "LEXER_ERROR")
    }

    @Test
    fun testSingleQuotedStringBadQuoteEscape() {
        performTest("'\\\"'", "LEXER_ERROR")
    }

    @Test
    fun testHalfOr() {
        performTest("|", "LEXER_ERROR")
    }

    @Test
    fun testTripleOr() {
        performTest("|||", "LEXER_ERROR")
    }

    @Test
    fun testHalfAnd() {
        performTest("&", "LEXER_ERROR")
    }

    @Test
    fun testTripleAnd() {
        performTest("&&&", "LEXER_ERROR")
    }

    @Test
    fun testUnknownCharNull() {
        performTest("\u0000", "LEXER_ERROR")
    }

    @Test
    fun testUnknownCharBackslash() {
        performTest("\\", "LEXER_ERROR")
    }

    @Test
    fun testUnknownCharNumberSign() {
        performTest("#", "LEXER_ERROR")
    }

    @Test
    fun testUnknownCharAt() {
        performTest("@", "LEXER_ERROR")
    }

    @Test
    fun testUnknownCharCircumflex() {
        performTest("^", "LEXER_ERROR")
    }

    @Test
    fun testUnknownCharBacktick() {
        performTest("`", "LEXER_ERROR")
    }

    @Test
    fun testUnknownCharTilde() {
        performTest("~", "LEXER_ERROR")
    }

    @Test
    fun testPerformance() {
        //         performTest("[ \"1\", \"2\", \"3\", \"Hello, World!\", \"1️⃣\" ] ((map x : NUMBER(x)) where i: i % 2 == 1).count", "LeftBracket String(1) Comma String(2) Comma String(3) Comma String(Hello, World!) Comma String(1️⃣) RightBracket LeftParen LeftParen Map Ident(x) Colon Ident(NUMBER) LeftParen Ident(x) RightParen RightParen Where Ident(i) Colon Ident(i) Modulo Int(2) Equal Int(1) RightParen Dot Ident(count)");
    }
}