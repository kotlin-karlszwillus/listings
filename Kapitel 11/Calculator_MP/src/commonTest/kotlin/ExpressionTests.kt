import com.openpromos.jni.nrexpressionlib.EvaluationError
import com.openpromos.jni.nrexpressionlib.lexer.Lexer
import com.openpromos.jni.nrexpressionlib.parser.Parser
import com.openpromos.jni.nrexpressionlib.runtime.Runtime
import com.openpromos.jni.nrexpressionlib.runtime.RuntimeDelegate
import com.openpromos.jni.nrexpressionlib.runtime.TestRuntimeDelegate
import com.openpromos.jni.nrexpressionlib.syntaxtree.ASTNode
import com.openpromos.jni.nrexpressionlib.value.Value
import kotlin.test.*

class ExpressionTests {
    private fun performTest(input: String, expectedOutput: String) {
        val lexer = Lexer(input)
        val sut = Parser(lexer)

        val testRuntimeDelegate = TestRuntimeDelegate()
        val runtime = Runtime(testRuntimeDelegate)

        var result = "RUNTIME_ERROR"
        var node: ASTNode? = null

        try {
            node = sut.parseExpression()
        } catch (e: Exception) {
            fail("could not parse input!")
        }

        if (!sut.isAtEnd) {
            fail("could not parse input!")
        } else {
            if (node != null) {
                try {
                    val nodeValue = node.evaluate(runtime)

                    if (nodeValue != null) {
                        result = nodeValue.toString()
                    }
                } catch (e: Exception) {
                    // throw RuntimeException("unexpexted error!")
                }

            }
        }

        if (result == expectedOutput) {
            println("Test passed with expected output: $result")
        }
        assertEquals(expectedOutput, result)
    }

    // --------- generated tests below this line: do not edit ---------
    @Test
    fun testNull() {
        performTest("NULL", "NULL")
    }

    @Test
    fun testBoolTrue() {
        performTest("true", "true")
    }

    @Test
    fun testBoolFalse() {
        performTest("false", "false")
    }

    @Test
    fun testNumberInt() {
        performTest("1", "1")
    }

    @Test
    fun testNumberFloat() {
        performTest("2.5", "2.5")
    }

    @Test
    fun testEmptyString() {
        performTest("\"\"", "\"\"")
    }

    @Test
    fun testHelloString() {
        performTest("\"Hello, World\"", "\"Hello, World\"")
    }

    @Test
    fun testEscapedString() {
        performTest("\"\\\"foo\\\"\"", "\"\\\"foo\\\"\"")
    }

    @Test
    fun testEmptyList() {
        performTest("[]", "[]")
    }

    @Test
    fun testSimpleList() {
        performTest("[1, 2, 3]", "[1, 2, 3]")
    }

    @Test
    fun testMixedList() {
        performTest("[true, \"foo\", 42]", "[true, \"foo\", 42]")
    }

    @Test
    fun testEmptyDictionary() {
        performTest("[:]", "[:]")
    }

    @Test
    fun testSimpleDictionary() {
        performTest("[\"a\":1, \"b\":2, \"c\":3]", "[\"a\":1, \"b\":2, \"c\":3]")
    }

    @Test
    fun testSimpleDictionary_1() {
        performTest("[\"b\":2, \"c\":3, \"a\":1]", "[\"a\":1, \"b\":2, \"c\":3]")
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    @Test
    fun testSimpleDictionary_2() {
        performTest("[\"b\":2, \"a\":1, \"c\":3]", "[\"a\":1, \"b\":2, \"c\":3]")
    }

    @Test
    fun testMixedDictionary() {
        performTest(
            "[\"Bool\":true, \"String\":\"foo\", \"Number\":42]",
            "[\"Bool\":true, \"Number\":42, \"String\":\"foo\"]"
        )
    }

    @Test
    fun testDuplicateKeyDictionary() {
        performTest("[\"foo\":1, \"foo\":1]", "RUNTIME_ERROR")
    }

    @Test
    fun testNegation() {
        performTest("-1", "-1")
    }

    @Test
    fun testNegationBadType() {
        performTest("-\"foo\"", "RUNTIME_ERROR")
    }

    @Test
    fun testLogicNegation() {
        performTest("!true", "false")
    }

    @Test
    fun testLogicNegationBadType() {
        performTest("!\"foo\"", "RUNTIME_ERROR")
    }

    @Test
    fun testExceptWithNoException() {
        performTest("\"foo\" except \"bar\"", "\"foo\"")
    }


    @Test
    fun testExceptWithLeftExceptio5() {
        performTest("true and \$NEW_DAMAGE.checked except 'default'", "\"default\"")
    }


    @Test
    fun testExceptWithLeftExceptio4() {
        performTest("true and \$NEW_DAMAGE.checked except \"bar\"", "\"bar\"")
    }

    @Test
    fun testExceptWithLeftException2() {
        performTest("true and 1/0 except \"bar\"", "\"bar\"")
    }

    @Test
    fun testExceptWithLeftException3() {
        performTest("false and 1/0 except \"bar\"", "false")
    }


    @Test
    fun testExceptWithLeftException() {
        performTest("1/0 except \"bar\"", "\"bar\"")
    }

    @Test
    fun testExceptWithRightException() {
        performTest("1/0 except 2/0", "RUNTIME_ERROR")
    }

    @Test
    fun testExceptChain() {
        performTest("1/0 except 2/0 except \"fallback\"", "\"fallback\"")
    }

    @Test
    fun testContains() {
        performTest("[] contains 1", "false")
    }

    @Test
    fun testContains_1() {
        performTest("[1] contains 1", "true")
    }

    @Test
    fun testContains_2() {
        performTest("[:] contains 1", "false")
    }

    @Test
    fun testContains_3() {
        performTest("[\"foo\":\"bar\"] contains \"foo\"", "true")
    }

    @Test
    fun testContains_4() {
        performTest("1 contains 1", "RUNTIME_ERROR")
    }

    @Test
    fun testLogicOr() {
        performTest("true or true", "true")
    }

    @Test
    fun testLogicOr_1() {
        performTest("false or true", "true")
    }

    @Test
    fun testLogicOr_2() {
        performTest("true or false", "true")
    }

    @Test
    fun testLogicOr_3() {
        performTest("false or false", "false")
    }

    @Test
    fun testLogicOrShortcut() {
        performTest("true or 1/0", "true")
    }

    @Test
    fun testLogicOrShortcut_1() {
        performTest("false or 1/0", "RUNTIME_ERROR")
    }

    @Test
    fun testLogicAnd() {
        performTest("true and true", "true")
    }

    @Test
    fun testLogicAnd_1() {
        performTest("false and true", "false")
    }

    @Test
    fun testLogicAnd_2() {
        performTest("true and false", "false")
    }

    @Test
    fun testLogicAnd_3() {
        performTest("false and false", "false")
    }

    @Test
    fun testLogicAndShortcut() {
        performTest("false and 1/0", "false")
    }

    @Test
    fun testLogicAndShortcut_1() {
        performTest("true and 1/0", "RUNTIME_ERROR")
    }

    @Test
    fun testEquality() {
        performTest("NULL == NULL", "true")
    }

    @Test
    fun testEquality_1() {
        performTest("NULL != NULL", "false")
    }

    @Test
    fun testEquality_2() {
        performTest("true == true", "true")
    }

    @Test
    fun testEquality_3() {
        performTest("true != true", "false")
    }

    @Test
    fun testEquality_4() {
        performTest("false == false", "true")
    }

    @Test
    fun testEquality_5() {
        performTest("false != false", "false")
    }

    @Test
    fun testEquality_6() {
        performTest("false == true", "false")
    }

    @Test
    fun testEquality_7() {
        performTest("false != true", "true")
    }

    @Test
    fun testEquality_8() {
        performTest("false == NULL", "false")
    }

    @Test
    fun testEquality_9() {
        performTest("false != NULL", "true")
    }

    @Test
    fun testEquality_10() {
        performTest("1 == 1", "true")
    }

    @Test
    fun testEquality_11() {
        performTest("1 != 1", "false")
    }

    @Test
    fun testEquality_12() {
        performTest("1 == 2", "false")
    }

    @Test
    fun testEquality_13() {
        performTest("1 != 2", "true")
    }

    @Test
    fun testEquality_14() {
        performTest("\"foo\" == \"foo\"", "true")
    }

    @Test
    fun testEquality_15() {
        performTest("\"foo\" != \"foo\"", "false")
    }

    @Test
    fun testEquality_16() {
        performTest("[1] == [1]", "true")
    }

    @Test
    fun testEquality_17() {
        performTest("[\"a\":1] == [\"a\":1]", "true")
    }

    @Test
    fun testComparison() {
        performTest("1 >  1", "false")
    }

    @Test
    fun testComparison_1() {
        performTest("1 >= 1", "true")
    }

    @Test
    fun testComparison_2() {
        performTest("1 <  1", "false")
    }

    @Test
    fun testComparison_3() {
        performTest("1 <= 1", "true")
    }

    @Test
    fun testComparison_4() {
        performTest("2 >  1", "true")
    }

    @Test
    fun testComparison_5() {
        performTest("2 >= 1", "true")
    }

    @Test
    fun testComparison_6() {
        performTest("2 <  1", "false")
    }

    @Test
    fun testComparison_7() {
        performTest("2 <= 1", "false")
    }

    @Test
    fun testComparison_8() {
        performTest("1 >  2", "false")
    }

    @Test
    fun testComparison_9() {
        performTest("1 >= 2", "false")
    }

    @Test
    fun testComparison_10() {
        performTest("1 <  2", "true")
    }

    @Test
    fun testComparison_11() {
        performTest("1 <= 2", "true")
    }

    @Test
    fun testComparison_12() {
        performTest("\"b\" > \"a\"", "true")
    }

    @Test
    fun testComparison_13() {
        performTest("\"b\" >= \"a\"", "true")
    }

    @Test
    fun testComparison_14() {
        performTest("\"a\" < \"b\"", "true")
    }

    @Test
    fun testComparison_15() {
        performTest("\"a\" <= \"b\"", "true")
    }

    @Test
    fun testComparisonMismatchingTypes() {
        performTest("1 >  \"a\"", "RUNTIME_ERROR")
    }

    @Test
    fun testComparisonMismatchingTypes_1() {
        performTest("1 >= \"a\"", "RUNTIME_ERROR")
    }

    @Test
    fun testComparisonMismatchingTypes_2() {
        performTest("1 <  \"a\"", "RUNTIME_ERROR")
    }

    @Test
    fun testComparisonMismatchingTypes_3() {
        performTest("1 <= \"a\"", "RUNTIME_ERROR")
    }

    @Test
    fun testAddition() {
        performTest("1 + 2", "3")
    }

    @Test
    fun testAdditionStrings() {
        performTest("\"foo\" + \"bar\"", "\"foobar\"")
    }

    @Test
    fun testAdditionUnsupportedTypes() {
        performTest("true + true", "RUNTIME_ERROR")
    }

    @Test
    fun testSubtraction() {
        performTest("2 - 1", "1")
    }

    @Test
    fun testSubtractionUnsupportedTypes() {
        performTest("\"foo\" - \"bar\"", "RUNTIME_ERROR")
    }

    @Test
    fun testMultiplication() {
        performTest("1 * 2", "2")
    }

    @Test
    fun testMultiplicationUnsupportedTypes() {
        performTest("\"foo\" * \"bar\"", "RUNTIME_ERROR")
    }

    @Test
    fun testDivision() {
        performTest("65536 / 256", "256")
    }

    @Test
    fun testDivisionByZero() {
        performTest("42 / 0", "RUNTIME_ERROR")
    }

    @Test
    fun testDivisionUnsupportedTypes() {
        performTest("\"foo\" / \"bar\"", "RUNTIME_ERROR")
    }

    @Test
    fun testModulo() {
        performTest("5 % 4", "1")
    }

    @Test
    fun testModuloByZero() {
        performTest("42 % 0", "RUNTIME_ERROR")
    }

    @Test
    fun testModuloUnsupportedTypes() {
        performTest("\"foo\" % \"bar\"", "RUNTIME_ERROR")
    }

    @Test
    fun testConditionalOperator() {
        performTest("true ? \"foo\" : \"bar\"", "\"foo\"")
    }

    @Test
    fun testConditionalOperator_1() {
        performTest("false ? \"foo\" : \"bar\"", "\"bar\"")
    }

    @Test
    fun testConditionalOperatorBadType() {
        performTest("\"baz\" ? \"foo\" : \"bar\"", "RUNTIME_ERROR")
    }

    @Test
    fun testConditionalOperatorShortcut() {
        performTest("true ? \"foo\" : 1/0", "\"foo\"")
    }

    @Test
    fun testConditionalOperatorShortcut_1() {
        performTest("false ? 1/0 : \"bar\"", "\"bar\"")
    }

    @Test
    fun testLookup() {
        performTest("\$testLookup", "\"\$testLookup\"")
    }

    @Test
    fun testLookup_1() {
        performTest("$\$testLookup", "\"$\$testLookup\"")
    }

    @Test
    fun testIdentifier() {
        performTest("testVariable", "\"testVariable's value\"")
    }

    @Test
    fun testIdentifier_1() {
        performTest("foo", "RUNTIME_ERROR")
    }

    @Test
    fun testAccess() {
        performTest("true.typeString", "\"Boolean\"")
    }

    @Test
    fun testAccess_1() {
        performTest("[].count", "0")
    }

    @Test
    fun testAccess_2() {
        performTest("[\"a\":1].count", "1")
    }

    @Test
    fun testAccess_3() {
        performTest("\"abc\".len", "3")
    }

    @Test
    fun testWhereOperator() {
        performTest("[1] where x: true", "[1]")
    }

    @Test
    fun testMapOperator() {
        performTest("[1] map x: \"foo\"", "[\"foo\"]")
    }

    @Test
    fun testCall() {
        performTest("testFunction()", "\"testFunction's result\"")
    }

    @Test
    fun testSubscript() {
        performTest("[1][0]", "1")
    }

    @Test
    fun testSubscript_1() {
        performTest("[\"foo\":1][\"foo\"]", "1")
    }

    @Test
    fun testSimpleExpression() {
        performTest("1 + 2 * 3", "7")
    }
}
