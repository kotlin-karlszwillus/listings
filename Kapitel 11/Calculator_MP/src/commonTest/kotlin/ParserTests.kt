import com.openpromos.jni.nrexpressionlib.lexer.Lexer
import com.openpromos.jni.nrexpressionlib.parser.Parser
import com.openpromos.jni.nrexpressionlib.parser.ParserException
import com.openpromos.jni.nrexpressionlib.syntaxtree.ASTExpression

import kotlin.test.*

class ParserTests {

    private fun performTest(input: String, expectedOutput: String) {
        val lexer = Lexer(input)
        val sut = Parser(lexer)

        var result = "PARSER_ERROR"
        var exp: ASTExpression? = null

        try {
            println(input)
            exp = sut.parseExpression()
        } catch (e: Exception) {
            // We silently eat the expression
            // throw RuntimeException("Unexpected Exception: $e")
        }

        if (!sut.isAtEnd) {
            result = "PARSER_ERROR"
        } else {
            if (exp != null) {
                result = exp.toString()
            }
        }

        if (result == expectedOutput) {
            println("Test passed with expected output: $result")
        }
        assertEquals(expectedOutput, result)
    }

    // --------- generated tests below this line: do not edit ---------
    @Test
    fun testEmpty() {
        performTest("", "PARSER_ERROR")
    }

    @Test
    fun testEmptyExpression() {
        performTest("()", "PARSER_ERROR")
    }

    @Test
    fun testInvalidToken() {
        performTest("$0", "PARSER_ERROR")
    }

    @Test
    fun testUnexpectedToken() {
        performTest(")", "PARSER_ERROR")
    }

    @Test
    fun testUnexpectedToken_1() {
        performTest("]", "PARSER_ERROR")
    }

    @Test
    fun testUnexpectedToken_2() {
        performTest("*", "PARSER_ERROR")
    }

    @Test
    fun testUnexpectedToken_3() {
        performTest("/", "PARSER_ERROR")
    }

    @Test
    fun testUnexpectedToken_4() {
        performTest("%", "PARSER_ERROR")
    }

    @Test
    fun testUnexpectedToken_5() {
        performTest("+", "PARSER_ERROR")
    }

    @Test
    fun testUnexpectedToken_6() {
        performTest("?", "PARSER_ERROR")
    }

    @Test
    fun testUnexpectedToken_7() {
        performTest("]", "PARSER_ERROR")
    }

    @Test
    fun testTwoExpressions() {
        performTest("1 2", "PARSER_ERROR")
    }

    @Test
    fun testIntLiteral() {
        performTest("1", "1")
    }

    @Test
    fun testFloatLiteral01() {
        performTest("0.1", "0.1")
    }

    @Test
    fun testFloatLiteral10() {
        performTest("1.0", "1")
    }

    @Test
    fun testStringLiteral() {
        performTest("\"default\"", "\"default\"")
    }

    @Test
    fun testBoolLiteral() {
        performTest("true", "true")
    }

    @Test
    fun testNullLiteral() {
        performTest("NULL", "NULL")
    }

    @Test
    fun testEmptyListLiteral() {
        performTest("[]", "[]")
    }

    @Test
    fun testListLiteral() {
        performTest("[1 ]", "[1]")
    }

    @Test
    fun testListLiteral_1() {
        performTest("[1, ]", "[1]")
    }

    @Test
    fun testListLiteral_2() {
        performTest("[1, 2, 3 ]", "[1, 2, 3]")
    }

    @Test
    fun testListLiteral_3() {
        performTest("[1, 2, 3, ]", "[1, 2, 3]")
    }

    @Test
    fun testListLiteral_4() {
        performTest("[1, 0.2, \"abc\", false, [0], [1:2], \$abc]", "[1, 0.2, \"abc\", false, [0], [1:2], \$abc]")
    }

    @Test
    fun testListLiteral_5() {
        performTest("[1, 0.2, \"abc\", false, [0], [1:2], \$abc, ]", "[1, 0.2, \"abc\", false, [0], [1:2], \$abc]")
    }

    @Test
    fun testMalformedListLiteral() {
        performTest("[", "PARSER_ERROR")
    }

    @Test
    fun testMalformedListLiteral_1() {
        performTest("[,]", "PARSER_ERROR")
    }

    @Test
    fun testMalformedListLiteral_2() {
        performTest("[ /* 1 */, 2, 3]", "PARSER_ERROR")
    }

    @Test
    fun testEmptyDictLiteral() {
        performTest("[:]", "[:]")
    }

    @Test
    fun testDictLiteral() {
        performTest("[1:2]", "[1:2]")
    }

    @Test
    fun testDictLiteral_1() {
        performTest("[1:2, ]", "[1:2]")
    }

    @Test
    fun testDictLiteral_2() {
        performTest("[1:2, 2:3, 3:4]", "[1:2, 2:3, 3:4]")
    }

    @Test
    fun testDictLiteral_3() {
        performTest("[1:2, 2:3, 3:4, ]", "[1:2, 2:3, 3:4]")
    }

    @Test
    fun testDictLiteral_4() {
        performTest(
            "[\"1\":1, \"0.2\":0.2, \"abc\":\"abc\", \"false\":false, \"[0]\":[0], \"[1:2]\":[1:2], \"\$abc\":\$abc]",
            "[\"1\":1, \"0.2\":0.2, \"abc\":\"abc\", \"false\":false, \"[0]\":[0], \"[1:2]\":[1:2], \"\$abc\":\$abc]"
        )
    }

    @Test
    fun testDictLiteral_5() {
        performTest(
            "[\"1\":1, \"0.2\":0.2, \"abc\":\"abc\", \"false\":false, \"[0]\":[0], \"[1:2]\":[1:2], \"\$abc\":\$abc, ]",
            "[\"1\":1, \"0.2\":0.2, \"abc\":\"abc\", \"false\":false, \"[0]\":[0], \"[1:2]\":[1:2], \"\$abc\":\$abc]"
        )
    }

    @Test
    fun testMalformedDictLiteral() {
        performTest("[ 1;", "PARSER_ERROR")
    }

    @Test
    fun testMalformedDictLiteral_1() {
        performTest("[ 1:2", "PARSER_ERROR")
    }

    @Test
    fun testMalformedDictLiteral_2() {
        performTest("[ 1:2, 1 2]", "PARSER_ERROR")
    }

    @Test
    fun testMalformedDictLiteral_3() {
        performTest("[ 1: ]", "PARSER_ERROR")
    }

    @Test
    fun testMalformedDictLiteral_4() {
        performTest("[ :1 ]", "PARSER_ERROR")
    }

    @Test
    fun testMalformedDictLiteral_5() {
        performTest("[ 1:1, 1: ]", "PARSER_ERROR")
    }

    @Test
    fun testMalformedDictLiteral_6() {
        performTest("[ 1:1, :1 ]", "PARSER_ERROR")
    }

    @Test
    fun testMalformedDictLiteral_7() {
        performTest("[ 1:1:1 ]", "PARSER_ERROR")
    }

    @Test
    fun testMalformedDictLiteral_8() {
        performTest("[ 1, 1:1 ]", "PARSER_ERROR")
    }

    @Test
    fun testMalformedDictLiteral_9() {
        performTest("[ 1:1, 1 ]", "PARSER_ERROR")
    }

    @Test
    fun testSingleLookup() {
        performTest("\$a", "\$a")
    }

    @Test
    fun testMultiLookup() {
        performTest("$\$a", "$\$a")
    }

    @Test
    fun testLookupChain() {
        performTest("\$a$\$b\$c$\$d", "\$a$\$b\$c$\$d")
    }

    @Test
    fun testIdentifier() {
        performTest("myName", "myName")
    }

    @Test
    fun testUnaryminus() {
        performTest("-a", "(-a)")
    }

    @Test
    fun testUnaryminus_1() {
        performTest("--a", "(-(-a))")
    }

    @Test
    fun testUnaryminus_2() {
        performTest("(-/* foo */a)", "(-a)")
    }

    @Test
    fun testUnarynot() {
        performTest("!a", "(!a)")
    }

    @Test
    fun testUnarynot_1() {
        performTest("!!a", "(!(!a))")
    }

    @Test
    fun testUnarymix() {
        performTest("-!-!a", "(-(!(-(!a))))")
    }

    @Test
    fun testParenthesis() {
        performTest("(a)", "a")
    }

    @Test
    fun testParenthesis_1() {
        performTest("((a))", "a")
    }

    @Test
    fun testMalformedParenthesis() {
        performTest("(", "PARSER_ERROR")
    }

    @Test
    fun testMalformedParenthesis_1() {
        performTest(")", "PARSER_ERROR")
    }

    @Test
    fun testMalformedParenthesis_2() {
        performTest("() 1", "PARSER_ERROR")
    }

    @Test
    fun testMalformedParenthesis_3() {
        performTest("-()", "PARSER_ERROR")
    }

    @Test
    fun testMalformedParenthesis_4() {
        performTest("(-1+)", "PARSER_ERROR")
    }

    @Test
    fun testExceptOperator() {
        performTest("1 except 2", "(1 except 2)")
    }

    @Test
    fun testConditionalOperator() {
        performTest("true ? \"foo\" : \"bar\"", "(true ? \"foo\" : \"bar\")")
    }

    @Test
    fun testMalformedConditionalOperator() {
        performTest("true ?", "PARSER_ERROR")
    }

    @Test
    fun testMalformedConditionalOperator_1() {
        performTest("true ? \"foo\"", "PARSER_ERROR")
    }

    @Test
    fun testMalformedConditionalOperator_2() {
        performTest("true ? \"foo\" :", "PARSER_ERROR")
    }

    @Test
    fun testWhereOperator() {
        performTest("1 where x : 2", "(1 where x : 2)")
    }

    @Test
    fun testMapOperator() {
        performTest("1 map x : 2", "(1 map x : 2)")
    }

    @Test
    fun testContainsOperator() {
        performTest("1 contains 2", "(1 contains 2)")
    }

    @Test
    fun testLogicorOperator() {
        performTest("1 || 2", "(1 || 2)")
    }

    @Test
    fun testLgicandOperator() {
        performTest("1 && 2", "(1 && 2)")
    }

    @Test
    fun testEqualOperator() {
        performTest("1 = 2", "(1 == 2)")
    }

    @Test
    fun testEqualOperator_1() {
        performTest("1 == 2", "(1 == 2)")
    }

    @Test
    fun testNotequalOperator() {
        performTest("1 != 2", "(1 != 2)")
    }

    @Test
    fun testGreaterthanOperator() {
        performTest("1 > 2", "(1 > 2)")
    }

    @Test
    fun testGreaterorequalOperator() {
        performTest("1 >= 2", "(1 >= 2)")
    }

    @Test
    fun testLessthanOperator() {
        performTest("1 < 2", "(1 < 2)")
    }

    @Test
    fun testLessorequalOperator() {
        performTest("1 <= 2", "(1 <= 2)")
    }

    @Test
    fun testAdditionOperator() {
        performTest("1 + 2", "(1 + 2)")
    }

    @Test
    fun testSubtractionOperator() {
        performTest("1 - 2", "(1 - 2)")
    }

    @Test
    fun testMultiplicationOperator() {
        performTest("1 * 2", "(1 * 2)")
    }

    @Test
    fun testDivisionOperator() {
        performTest("1 / 2", "(1 / 2)")
    }

    @Test
    fun testModuloOperator() {
        performTest("1 % 2", "(1 % 2)")
    }

    @Test
    fun testCallOperator() {
        performTest("a()", "(a())")
    }

    @Test
    fun testCallOperator_1() {
        performTest("a(1)", "(a(1))")
    }

    @Test
    fun testCallOperator_2() {
        performTest("a(1, true, \$a, \"foo\")", "(a(1, true, \$a, \"foo\"))")
    }

    @Test
    fun testMalformedCallOperator() {
        performTest("a(", "PARSER_ERROR")
    }

    @Test
    fun testMalformedCallOperator_1() {
        performTest("a(,)", "PARSER_ERROR")
    }

    @Test
    fun testMalformedCallOperator_2() {
        performTest("a(1,)", "PARSER_ERROR")
    }

    @Test
    fun testMalformedCallOperator_3() {
        performTest("a(1;)", "PARSER_ERROR")
    }

    @Test
    fun testAccessOperator() {
        performTest("a.b", "(a.b)")
    }

    @Test
    fun testAccessOperator_1() {
        performTest("a.b.c", "((a.b).c)")
    }

    @Test
    fun testAccessOperator_2() {
        performTest("1.b", "(1.b)")
    }

    @Test
    fun testAccessOperator_3() {
        performTest("1.0.b", "(1.b)")
    }

    @Test
    fun testMalformedAccessOperator() {
        performTest("a.0", "PARSER_ERROR")
    }

    @Test
    fun testMalformedAccessOperator_1() {
        performTest("(a.)", "PARSER_ERROR")
    }

    @Test
    fun testSubscriptOperator() {
        performTest("a[1]", "(a->[1])")
    }

    @Test
    fun testSubscriptOperator_1() {
        performTest("[0][1]", "([0]->[1])")
    }

    @Test
    fun testMalformedSubscriptOperator() {
        performTest("a[a b", "PARSER_ERROR")
    }

    @Test
    fun testPrecedence() {
        performTest("1 + 2 * 3", "(1 + (2 * 3))")
    }

    @Test
    fun testPrecedence_1() {
        performTest("(1 + 2) * 3", "((1 + 2) * 3)")
    }

    @Test
    fun testPrecedence_2() {
        performTest("1 + 2 - 3", "((1 + 2) - 3)")
    }

    @Test
    fun testPrecedence_3() {
        performTest("1 + (2 - 3)", "(1 + (2 - 3))")
    }

    @Test
    fun testPrecedence_4() {
        performTest("a * b + c * d % e - f", "(((a * b) + ((c * d) % e)) - f)")
    }

    @Test
    fun testPrecedence_5() {
        performTest("-a+b", "((-a) + b)")
    }

    @Test
    fun testPrecedence_6() {
        performTest("-a.b", "(-(a.b))")
    }

    @Test
    fun testPrecedence_7() {
        performTest("--a.b", "(-(-(a.b)))")
    }

    @Test
    fun testPrecedence_8() {
        performTest("--a % b", "((-(-a)) % b)")
    }

    @Test
    fun testPrecedence_9() {
        performTest("(-a).b", "((-a).b)")
    }

    @Test
    fun testPrecedence_10() {
        performTest("a.b.c", "((a.b).c)")
    }

    @Test
    fun testPrecedence_11() {
        performTest("a[1].m(2)", "(((a->[1]).m)(2))")
    }

    @Test
    fun testPrecedence_12() {
        performTest("a[1 + 2]", "(a->[(1 + 2)])")
    }

    @Test
    fun testPrecedence_13() {
        performTest("-[1][0]", "(-([1]->[0]))")
    }

    @Test
    fun testPrecedence_14() {
        performTest("[-1()]", "[(-(1()))]")
    }

    @Test
    fun testPrecedence_15() {
        performTest("[[a]()[b],[c]]", "[(([a]())->[b]), [c]]")
    }

    @Test
    fun testPrecedence_16() {
        performTest("a + b contains c + d && e", "(((a + b) contains (c + d)) && e)")
    }

    @Test
    fun testPrecedence_17() {
        performTest("a ? b ? 1 : 2 : c ? 3 : 4", "(a ? (b ? 1 : 2) : (c ? 3 : 4))")
    }

    @Test
    fun testManyOperators() {
        performTest(
            "a || b && c == d != e > f >= g < h <= i + j - k * l / m % n ? 1 : 2",
            "((a || (b && ((c == d) != ((((e > f) >= g) < h) <= ((i + j) - (((k * l) / m) % n)))))) ? 1 : 2)"
        )
    }

    @Test
    fun testComplexExpressionTest() {
        performTest(
            "(([\"1\", \"2\", \"1234\", \$foo, [], \"Hello, World!\", \"\\\"\"] map element : NUMBER(element)) where each: each % 2 == 1).count",
            "((([\"1\", \"2\", \"1234\", \$foo, [], \"Hello, World!\", \"\\\"\"] map element : (NUMBER(element))) where each : ((each % 2) == 1)).count)"
        )
    }
}

