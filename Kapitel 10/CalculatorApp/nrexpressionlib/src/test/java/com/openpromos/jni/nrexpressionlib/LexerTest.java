package com.openpromos.jni.nrexpressionlib;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

/**
 *
 */
public class LexerTest {

    private void performTest(String input, String expectedOutput) {
        Lexer lexer = new Lexer(input);
        StringBuilder buffer = new StringBuilder();

        scanloop:
        while (true) {
            Token token = lexer.scanToken();
            System.out.println(token);
            switch (token.getType()) {
                case AtEnd:
                    break scanloop;
                case LexerError:
                    if (expectedOutput.equals(Token.Companion.getLEXER_ERROR())) return;
                    else {
                        fail();
                    }
                    break;
                default:
                    buffer.append(token.toString());
                    buffer.append(" ");
                    ;
            }
        }

        String result = buffer.length() > 0 ? buffer.subSequence(0, buffer.length() - 1).toString() : "";
        assertThat(result, is(equalTo(expectedOutput)));
    }

    @Test
    public void testFloatOne() {
        performTest("1.0", "Float(1)");
    }

    @Test
    public void testDot() {
        performTest(".", "Dot");
    }

    @Test
    public void testNot() {
        performTest("!", "Not");
    }

    @Test
    public void testEqual() {
        performTest("=", "Equal");
    }

    @Test
    public void testEqual2() {
        performTest("==", "Equal");
    }

    @Test
    public void testNotequal() {
        performTest("!=", "NotEqual");
    }

    @Test
    public void testGreaterOrEqual() {
        performTest(">=", "GreaterOrEqual");
    }

    @Test
    public void testGreater() {
        performTest(">", "Greater");
    }

    @Test
    public void testLessOrEqual() {
        performTest("<=", "LessOrEqual");
    }

    @Test
    public void testLess() {
        performTest("<", "Less");
    }

    @Test
    public void testAssign() {
        performTest(":=", "Assign");
    }

    @Test
    public void testMinus() {
        performTest("-", "Minus");
    }

    @Test
    public void testPlus() {
        performTest("+", "Plus");
    }

    @Test
    public void testComma() {
        performTest(",", "Comma");
    }

    @Test
    public void testStar() {
        performTest("*", "Star");
    }

    @Test
    public void testDivis() {
        performTest("/", "Divis");
    }

    @Test
    public void testModulo() {
        performTest("%", "Modulo");
    }

    @Test
    public void testLeftParenthesis() {
        performTest("(", "LeftParen");
    }

    @Test
    public void testRightParenthesis() {
        performTest(");", "RightParen Semicolon");
    }

    @Test
    public void testSemicolon() {
        performTest(";", "Semicolon");
    }

    @Test
    public void testLeftBrace() {
        performTest("{", "LeftBrace");
    }

    @Test
    public void testRightBrace() {
        performTest("}", "RightBrace");
    }

    @Test
    public void testLeftBracket() {
        performTest("[", "LeftBracket");
    }

    @Test
    public void testRightBracket() {
        performTest("]", "RightBracket");
    }

    @Test
    public void testQuestionmark() {
        performTest("?", "Questionmark");
    }

    @Test
    public void testColon() {
        performTest(":", "Colon");
    }

    @Test
    public void testAnd() {
        performTest("&&", "And");
    }

    @Test
    public void testOr() {
        performTest("||", "Or");
    }

    @Test
    public void testAndKeyword() {
        performTest("and", "And");
    }

    @Test
    public void testAssertKeyword() {
        performTest("assert", "Assert");
    }

    @Test
    public void testBreakKeyword() {
        performTest("break", "Break");
    }

    @Test
    public void testCatchKeyword() {
        performTest("catch", "Catch");
    }

    @Test
    public void testContainsKeyword() {
        performTest("contains", "Contains");
    }

    @Test
    public void testContinueKeyword() {
        performTest("continue", "Continue");
    }

    @Test
    public void testElseKeyword() {
        performTest("else", "Else");
    }

    @Test
    public void testErrorKeyword() {
        performTest("error", "Error");
    }

    @Test
    public void testExceptKeyword() {
        performTest("except", "Except");
    }

    @Test
    public void testFalseKeyword() {
        performTest("false", "False");
    }

    @Test
    public void testForKeyword() {
        performTest("for", "For");
    }

    @Test
    public void testIfKeyword() {
        performTest("if", "If");
    }

    @Test
    public void testInKeyword() {
        performTest("in", "In");
    }

    @Test
    public void testMapKeyword() {
        performTest("map", "Map");
    }

    @Test
    public void testNullKeyword() {
        performTest("NULL", "Null");
    }

    @Test
    public void testOrKeyword() {
        performTest("or", "Or");
    }

    @Test
    public void testPrintKeyword() {
        performTest("print", "Print");
    }

    @Test
    public void testReturnKeyword() {
        performTest("return", "Return");
    }

    @Test
    public void testTrueKeyword() {
        performTest("true", "True");
    }

    @Test
    public void testTryKeyword() {
        performTest("try", "Try");
    }

    @Test
    public void testWhereKeyword() {
        performTest("where", "Where");
    }

    @Test
    public void testWhileKeyword() {
        performTest("while", "While");
    }

    @Test
    public void testEmptySource() {
        performTest("", "");
    }

    @Test
    public void testSimpleSequence() {
        performTest("...", "Dot Dot Dot");
    }

    @Test
    public void testSkipWhitespace() {
        performTest("\t\n\r. \t\n\r.", "Dot Dot");
    }

    @Test
    public void testSingleLineComment() {
        performTest("// C++ comment\n.", "Dot");
    }

    @Test
    public void testMultiLineComment() {
        performTest("/* C comment */.", "Dot");
    }

    @Test
    public void testUnterminatedCComment() {
        performTest("/*", "LEXER_ERROR");
    }

    @Test
    public void testUnterminatedCComment2() {
        performTest("/*/", "LEXER_ERROR");
    }

    @Test
    public void testCCommentPattern() {
        performTest("/*/*/*", "Star");
    }

    @Test
    public void testShortestCComment() {
        performTest("/**/.", "Dot");
    }

    @Test
    public void testSingleStarInCComment() {
        performTest("/***/.", "Dot");
    }

    @Test
    public void testNullCharInCComment() {
        performTest("/*/0*/.", "Dot");
    }

    @Test
    public void testSingleLineCommentWithCEnding() {
        performTest("// C++*/\n.", "Dot");
    }

    @Test
    public void testSingleAndMultiLineComment() {
        performTest("// C++\n/* C */.", "Dot");
    }

    @Test
    public void testSingleLetterIdentifier() {
        performTest("i", "Ident(i)");
    }

    @Test
    public void testMultiLetterIdentifier() {
        performTest("self", "Ident(self)");
    }

    @Test
    public void testIdentifierSequence() {
        performTest("one, two and three", "Ident(one) Comma Ident(two) And Ident(three)");
    }

    @Test
    public void testIdentifierWithLeadingUnderscore() {
        performTest("_a", "Ident(_a)");
    }

    @Test
    public void testIdentifierWithTrailingUnderscore() {
        performTest("a_", "Ident(a_)");
    }

    @Test
    public void testIdentifierSequence2() {
        performTest("_ a", "Ident(_) Ident(a)");
    }

    @Test
    public void testNotAnIdentifier() {
        performTest("0_", "LEXER_ERROR");
    }

    @Test
    public void testSingleLookup() {
        performTest("$abc", "Lookup(abc)");
    }

    @Test
    public void testSingleLookupWithProp() {
        performTest("$ab.c", "Lookup(ab) Dot Ident(c)");
    }

    @Test
    public void testMultiLookup() {
        performTest("$$_0", "MultiLookup(_0)");
    }

    @Test
    public void testLookupChain() {
        performTest("$a$$b$c$$d", "Lookup(a) MultiLookup(b) Lookup(c) MultiLookup(d)");
    }

    @Test
    public void testEmptyLookup() {
        performTest("$", "LEXER_ERROR");
    }

    @Test
    public void testEmptyMultiLookup() {
        performTest("$$", "LEXER_ERROR");
    }

    @Test
    public void testBadLookupStartChar() {
        performTest("$0", "LEXER_ERROR");
    }

    @Test
    public void testBadMultiLookupStartChar() {
        performTest("$$0", "LEXER_ERROR");
    }

    @Test
    public void testInteger0() {
        performTest("0", "Int(0)");
    }

    @Test
    public void testInteger1() {
        performTest("1", "Int(1)");
    }

    @Test
    public void testInteger01() {
        performTest("01", "Int(1)");
    }

    @Test
    public void testInteger42() {
        performTest("42", "Int(42)");
    }

    @Test
    public void testIntegerSequence42() {
        performTest("4 2", "Int(4) Int(2)");
    }

    @Test
    public void testIntegerMinus123() {
        performTest("-123", "Minus Int(123)");
    }

    @Test
    public void testBadInteger() {
        performTest("1a", "LEXER_ERROR");
    }

    @Test
    public void testIntegerWithProperty() {
        performTest("1.a", "Int(1) Dot Ident(a)");
    }

    @Test
    public void testFloatZero() {
        performTest("0.0", "Float(0)");
    }

    @Test
    public void testFloatAltZeroA() {
        performTest("0.", "Float(0)");
    }

    @Test
    public void testFloatAltZeroB() {
        performTest(".0", "Float(0)");
    }

    @Test
    public void testFloatAltZeroC() {
        performTest("00.", "Float(0)");
    }

    @Test
    public void testFloatAltZeroD() {
        performTest(".00", "Float(0)");
    }

    @Test
    public void testFloatAltZeroE() {
        performTest("00.00", "Float(0)");
    }

    @Test
    public void testFloat42() {
        performTest("42.", "Float(42)");
    }

    @Test
    public void testFloatSkipRedundantZeros() {
        performTest("010.010", "Float(10.01)");
    }

    @Test
    public void testFloatSequence42() {
        performTest("4. .2", "Float(4) Float(.2)");
    }

    @Test
    public void testFloatMinus123() {
        performTest("-123.", "Minus Float(123)");
    }

    @Test
    public void testBadFloat2() {
        performTest(".1a", "LEXER_ERROR");
    }

    @Test
    public void testBadFloat3() {
        performTest("1.1a", "LEXER_ERROR");
    }

    @Test
    public void testFloatWithProperty() {
        performTest("1.1.a", "Float(1.1) Dot Ident(a)");
    }

    @Test
    public void testDoubleQuotedStringEmpty() {
        performTest("\"\"", "String()");
    }

    @Test
    public void testDoubleQuotedStringSimple() {
        performTest("\"...\"", "String(...)");
    }

    @Test
    public void testDoubleQuotedStringHello() {
        performTest("\"Hello, World!\"", "String(Hello, World!)");
    }

    @Test
    public void testDoubleQuotedStringEscapedQuote() {
        performTest("\"\\\"\"", "String(\")");
    }

    @Test
    public void testDoubleQuotedStringEscapedBackslash() {
        performTest("\"\\\\\"", "String(\\)");
    }

    @Test  public void testDoubleQuotedStringEscapedNewline() {
        performTest( "\"\n\"",  "String(\n)");
    }

    @Test
    public void testDoubleQuotedStringWithNulChar() {
        performTest("\"\0\"", "String(\0)");
    }

    @Test
    public void testDoubleQuotedStringUnterminated() {
        performTest("\"", "LEXER_ERROR");
    }

    @Test
    public void testDoubleQuotedStringUntermEsc() {
        performTest("\"\\", "LEXER_ERROR");
    }

    @Test
    public void testDoubleQuotedStringUntermPostEsc() {
        performTest("\"\\\"", "LEXER_ERROR");
    }

    @Test
    public void testDoubleQuotedStringUnknownEscape() {
        performTest("\"\\r\"", "LEXER_ERROR");
    }

    @Test
    public void testDoubleQuotedStringBadQuoteEscape() {
        performTest("\"\\'\"", "LEXER_ERROR");
    }

    @Test
    public void testDoubleQuotedStringNonBmpChar() {
        performTest("\"🍉\"", "String(🍉)");
    }

    @Test
    public void testSingleQuotedStringEmpty() {
        performTest("''", "String()");
    }

    @Test
    public void testSingleQuotedStringSimple() {
        performTest("'...'", "String(...)");
    }

    @Test
    public void testSingleQuotedStringHello() {
        performTest("'Hello, World!'", "String(Hello, World!)");
    }

    @Test
    public void testSingleQuotedStringEscapedQuote() {
        performTest("'\\''", "String(')");
    }

    @Test
    public void testSingleQuotedStringEscapedBackslash() {
        performTest("'\\\\'", "String(\\)");
    }

    @Test  public void testSingleQuotedStringEscapedNewline() {
        performTest( "'\n'",  "String(\n)");
    }

    @Test
    public void testSingleQuotedStringWithNulChar() {
        performTest("'\0'", "String(\0)");
    }

    @Test
    public void testSingleQuotedStringUnterminated() {
        performTest("'", "LEXER_ERROR");
    }

    @Test
    public void testSingleQuotedStringUntermEsc() {
        performTest("'\\", "LEXER_ERROR");
    }

    @Test
    public void testSingleQuotedStringUntermPostEsc() {
        performTest("'\\'", "LEXER_ERROR");
    }

    @Test
    public void testSingleQuotedStringUnknownEscape() {
        performTest("'\\r'", "LEXER_ERROR");
    }

    @Test
    public void testSingleQuotedStringBadQuoteEscape() {
        performTest("'\\\"'", "LEXER_ERROR");
    }

    @Test
    public void testHalfOr() {
        performTest("|", "LEXER_ERROR");
    }

    @Test
    public void testTripleOr() {
        performTest("|||", "LEXER_ERROR");
    }

    @Test
    public void testHalfAnd() {
        performTest("&", "LEXER_ERROR");
    }

    @Test
    public void testTripleAnd() {
        performTest("&&&", "LEXER_ERROR");
    }

    @Test
    public void testUnknownCharNull() {
        performTest("\0", "LEXER_ERROR");
    }

    @Test
    public void testUnknownCharBackslash() {
        performTest("\\", "LEXER_ERROR");
    }

    @Test
    public void testUnknownCharNumberSign() {
        performTest("#", "LEXER_ERROR");
    }

    @Test
    public void testUnknownCharAt() {
        performTest("@", "LEXER_ERROR");
    }

    @Test
    public void testUnknownCharCircumflex() {
        performTest("^", "LEXER_ERROR");
    }

    @Test
    public void testUnknownCharBacktick() {
        performTest("`", "LEXER_ERROR");
    }

    @Test
    public void testUnknownCharTilde() {
        performTest("~", "LEXER_ERROR");
    }

    @Test
    public void testPerformance() {
        performTest("[ \"1\", \"2\", \"3\", \"Hello, World!\", \"1️⃣\" ] ((map x : NUMBER(x)) where i: i % 2 == 1).count", "LeftBracket String(1) Comma String(2) Comma String(3) Comma String(Hello, World!) Comma String(1️⃣) RightBracket LeftParen LeftParen Map Ident(x) Colon Ident(NUMBER) LeftParen Ident(x) RightParen RightParen Where Ident(i) Colon Ident(i) Modulo Int(2) Equal Int(1) RightParen Dot Ident(count)");
    }
}
