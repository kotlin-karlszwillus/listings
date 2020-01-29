package com.openpromos.jni.nrexpressionlib;

import com.openpromos.jni.nrexpressionlib.ast.astbase.ASTNode;
import com.openpromos.jni.nrexpressionlib.value.Value;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class ExpressionTests {
	private void performTest(String input, String expectedOutput) {
		Lexer lexer = new Lexer(input);
		Parser sut = new Parser(lexer);

		RuntimeDelegate testRuntimeDelegate = new TestRuntimeDelegate();
		Runtime runtime = new Runtime(testRuntimeDelegate);

		String result = "RUNTIME_ERROR";
		ASTNode node = null;

		try {
			node = sut.parseExpression();
		} catch (Exception e) {
			fail("could not parse input!");
		}

		if (!sut.isAtEnd()) {
			fail("could not parse input!");
		} else {
			if (node != null) {
				try {
					Value nodeValue = node.evaluate(runtime);

					if (nodeValue != null) {
						result = nodeValue.toString();
					}
				} catch (EvaluationError e) {
					if (expectedOutput.equals("RUNTIME_ERROR")) {
						result = "RUNTIME_ERROR";
					} else {
						result = "RUNTIME_ERROR: " + e.toString();
					}
				} catch (Exception e) {
					throw new RuntimeException("unexpexted error!");
				}
			}
		}

		if (result.equals(expectedOutput))  {
			System.out.println("Test passed with expected output: " + result);
		}

		assertThat(result, is(equalTo(expectedOutput)));
	}

	// --------- generated tests below this line: do not edit ---------
	@Test
	public void testNull() {
		performTest("NULL", "NULL");
	}

	@Test
	public void testBoolTrue() {
		performTest("true", "true");
	}

	@Test
	public void testBoolFalse() {
		performTest("false", "false");
	}

	@Test
	public void testNumberInt() {
		performTest("1", "1");
	}

	@Test
	public void testNumberFloat() {
		performTest("2.5", "2.5");
	}

	@Test
	public void testEmptyString() {
		performTest("\"\"", "\"\"");
	}

	@Test
	public void testHelloString() {
		performTest("\"Hello, World\"", "\"Hello, World\"");
	}

	@Test
	public void testEscapedString() {
		performTest("\"\\\"foo\\\"\"", "\"\\\"foo\\\"\"");
	}

	@Test
	public void testEmptyList() {
		performTest("[]", "[]");
	}

	@Test
	public void testSimpleList() {
		performTest("[1, 2, 3]", "[1, 2, 3]");
	}

	@Test
	public void testMixedList() {
		performTest("[true, \"foo\", 42]", "[true, \"foo\", 42]");
	}

	@Test
	public void testEmptyDictionary() {
		performTest("[:]", "[:]");
	}

	@Test
	public void testSimpleDictionary() {
		performTest("[\"a\":1, \"b\":2, \"c\":3]", "[\"a\":1, \"b\":2, \"c\":3]");
	}

	@Test
	public void testSimpleDictionary_1() {
		performTest("[\"b\":2, \"c\":3, \"a\":1]", "[\"a\":1, \"b\":2, \"c\":3]");
	}

	@Test
	public void testSimpleDictionary_2() {
		performTest("[\"b\":2, \"a\":1, \"c\":3]", "[\"a\":1, \"b\":2, \"c\":3]");
	}

	@Test
	public void testMixedDictionary() {
		performTest("[\"Bool\":true, \"String\":\"foo\", \"Number\":42]", "[\"Bool\":true, \"Number\":42, \"String\":\"foo\"]");
	}

	@Test
	public void testDuplicateKeyDictionary() {
		performTest("[\"foo\":1, \"foo\":1]", "RUNTIME_ERROR");
	}

	@Test
	public void testNegation() {
		performTest("-1", "-1");
	}

	@Test
	public void testNegationBadType() {
		performTest("-\"foo\"", "RUNTIME_ERROR");
	}

	@Test
	public void testLogicNegation() {
		performTest("!true", "false");
	}

	@Test
	public void testLogicNegationBadType() {
		performTest("!\"foo\"", "RUNTIME_ERROR");
	}

	@Test
	public void testExceptWithNoException() {
		performTest("\"foo\" except \"bar\"", "\"foo\"");
	}


	@Test
	public void testExceptWithLeftExceptio5() {
		performTest("true and $NEW_DAMAGE.checked except 'default'", "\"default\"");
	}


	@Test
	public void testExceptWithLeftExceptio4() {
		performTest("true and $NEW_DAMAGE.checked except \"bar\"", "\"bar\"");
	}

	@Test
	public void testExceptWithLeftException2() {
		performTest("true and 1/0 except \"bar\"", "\"bar\"");
	}

	@Test
	public void testExceptWithLeftException3() {
		performTest("false and 1/0 except \"bar\"", "false");
	}


	@Test
	public void testExceptWithLeftException() {
		performTest("1/0 except \"bar\"", "\"bar\"");
	}

	@Test
	public void testExceptWithRightException() {
		performTest("1/0 except 2/0", "RUNTIME_ERROR");
	}

	@Test
	public void testExceptChain() {
		performTest("1/0 except 2/0 except \"fallback\"", "\"fallback\"");
	}

	@Test
	public void testContains() {
		performTest("[] contains 1", "false");
	}

	@Test
	public void testContains_1() {
		performTest("[1] contains 1", "true");
	}

	@Test
	public void testContains_2() {
		performTest("[:] contains 1", "false");
	}

	@Test
	public void testContains_3() {
		performTest("[\"foo\":\"bar\"] contains \"foo\"", "true");
	}

	@Test
	public void testContains_4() {
		performTest("1 contains 1", "RUNTIME_ERROR");
	}

	@Test
	public void testLogicOr() {
		performTest("true or true", "true");
	}

	@Test
	public void testLogicOr_1() {
		performTest("false or true", "true");
	}

	@Test
	public void testLogicOr_2() {
		performTest("true or false", "true");
	}

	@Test
	public void testLogicOr_3() {
		performTest("false or false", "false");
	}

	@Test
	public void testLogicOrShortcut() {
		performTest("true or 1/0", "true");
	}

	@Test
	public void testLogicOrShortcut_1() {
		performTest("false or 1/0", "RUNTIME_ERROR");
	}

	@Test
	public void testLogicAnd() {
		performTest("true and true", "true");
	}

	@Test
	public void testLogicAnd_1() {
		performTest("false and true", "false");
	}

	@Test
	public void testLogicAnd_2() {
		performTest("true and false", "false");
	}

	@Test
	public void testLogicAnd_3() {
		performTest("false and false", "false");
	}

	@Test
	public void testLogicAndShortcut() {
		performTest("false and 1/0", "false");
	}

	@Test
	public void testLogicAndShortcut_1() {
		performTest("true and 1/0", "RUNTIME_ERROR");
	}

	@Test
	public void testEquality() {
		performTest("NULL == NULL", "true");
	}

	@Test
	public void testEquality_1() {
		performTest("NULL != NULL", "false");
	}

	@Test
	public void testEquality_2() {
		performTest("true == true", "true");
	}

	@Test
	public void testEquality_3() {
		performTest("true != true", "false");
	}

	@Test
	public void testEquality_4() {
		performTest("false == false", "true");
	}

	@Test
	public void testEquality_5() {
		performTest("false != false", "false");
	}

	@Test
	public void testEquality_6() {
		performTest("false == true", "false");
	}

	@Test
	public void testEquality_7() {
		performTest("false != true", "true");
	}

	@Test
	public void testEquality_8() {
		performTest("false == NULL", "false");
	}

	@Test
	public void testEquality_9() {
		performTest("false != NULL", "true");
	}

	@Test
	public void testEquality_10() {
		performTest("1 == 1", "true");
	}

	@Test
	public void testEquality_11() {
		performTest("1 != 1", "false");
	}

	@Test
	public void testEquality_12() {
		performTest("1 == 2", "false");
	}

	@Test
	public void testEquality_13() {
		performTest("1 != 2", "true");
	}

	@Test
	public void testEquality_14() {
		performTest("\"foo\" == \"foo\"", "true");
	}

	@Test
	public void testEquality_15() {
		performTest("\"foo\" != \"foo\"", "false");
	}

	@Test
	public void testEquality_16() {
		performTest("[1] == [1]", "true");
	}

	@Test
	public void testEquality_17() {
		performTest("[\"a\":1] == [\"a\":1]", "true");
	}

	@Test
	public void testComparison() {
		performTest("1 >  1", "false");
	}

	@Test
	public void testComparison_1() {
		performTest("1 >= 1", "true");
	}

	@Test
	public void testComparison_2() {
		performTest("1 <  1", "false");
	}

	@Test
	public void testComparison_3() {
		performTest("1 <= 1", "true");
	}

	@Test
	public void testComparison_4() {
		performTest("2 >  1", "true");
	}

	@Test
	public void testComparison_5() {
		performTest("2 >= 1", "true");
	}

	@Test
	public void testComparison_6() {
		performTest("2 <  1", "false");
	}

	@Test
	public void testComparison_7() {
		performTest("2 <= 1", "false");
	}

	@Test
	public void testComparison_8() {
		performTest("1 >  2", "false");
	}

	@Test
	public void testComparison_9() {
		performTest("1 >= 2", "false");
	}

	@Test
	public void testComparison_10() {
		performTest("1 <  2", "true");
	}

	@Test
	public void testComparison_11() {
		performTest("1 <= 2", "true");
	}

	@Test
	public void testComparison_12() {
		performTest("\"b\" > \"a\"", "true");
	}

	@Test
	public void testComparison_13() {
		performTest("\"b\" >= \"a\"", "true");
	}

	@Test
	public void testComparison_14() {
		performTest("\"a\" < \"b\"", "true");
	}

	@Test
	public void testComparison_15() {
		performTest("\"a\" <= \"b\"", "true");
	}

	@Test
	public void testComparisonMismatchingTypes() {
		performTest("1 >  \"a\"", "RUNTIME_ERROR");
	}

	@Test
	public void testComparisonMismatchingTypes_1() {
		performTest("1 >= \"a\"", "RUNTIME_ERROR");
	}

	@Test
	public void testComparisonMismatchingTypes_2() {
		performTest("1 <  \"a\"", "RUNTIME_ERROR");
	}

	@Test
	public void testComparisonMismatchingTypes_3() {
		performTest("1 <= \"a\"", "RUNTIME_ERROR");
	}

	@Test
	public void testAddition() {
		performTest("1 + 2", "3");
	}

	@Test
	public void testAdditionStrings() {
		performTest("\"foo\" + \"bar\"", "\"foobar\"");
	}

	@Test
	public void testAdditionUnsupportedTypes() {
		performTest("true + true", "RUNTIME_ERROR");
	}

	@Test
	public void testSubtraction() {
		performTest("2 - 1", "1");
	}

	@Test
	public void testSubtractionUnsupportedTypes() {
		performTest("\"foo\" - \"bar\"", "RUNTIME_ERROR");
	}

	@Test
	public void testMultiplication() {
		performTest("1 * 2", "2");
	}

	@Test
	public void testMultiplicationUnsupportedTypes() {
		performTest("\"foo\" * \"bar\"", "RUNTIME_ERROR");
	}

	@Test
	public void testDivision() {
		performTest("65536 / 256", "256");
	}

	@Test
	public void testDivisionByZero() {
		performTest("42 / 0", "RUNTIME_ERROR");
	}

	@Test
	public void testDivisionUnsupportedTypes() {
		performTest("\"foo\" / \"bar\"", "RUNTIME_ERROR");
	}

	@Test
	public void testModulo() {
		performTest("5 % 4", "1");
	}

	@Test
	public void testModuloByZero() {
		performTest("42 % 0", "RUNTIME_ERROR");
	}

	@Test
	public void testModuloUnsupportedTypes() {
		performTest("\"foo\" % \"bar\"", "RUNTIME_ERROR");
	}

	@Test
	public void testConditionalOperator() {
		performTest("true ? \"foo\" : \"bar\"", "\"foo\"");
	}

	@Test
	public void testConditionalOperator_1() {
		performTest("false ? \"foo\" : \"bar\"", "\"bar\"");
	}

	@Test
	public void testConditionalOperatorBadType() {
		performTest("\"baz\" ? \"foo\" : \"bar\"", "RUNTIME_ERROR");
	}

	@Test
	public void testConditionalOperatorShortcut() {
		performTest("true ? \"foo\" : 1/0", "\"foo\"");
	}

	@Test
	public void testConditionalOperatorShortcut_1() {
		performTest("false ? 1/0 : \"bar\"", "\"bar\"");
	}

	@Test
	public void testLookup() {
		performTest("$testLookup", "\"$testLookup\"");
	}

	@Test
	public void testLookup_1() {
		performTest("$$testLookup", "\"$$testLookup\"");
	}

	@Test
	public void testIdentifier() {
		performTest("testVariable", "\"testVariable's value\"");
	}

	@Test
	public void testIdentifier_1() {
		performTest("foo", "RUNTIME_ERROR");
	}

	@Test
	public void testAccess() {
		performTest("true.typeString", "\"Boolean\"");
	}

	@Test
	public void testAccess_1() {
		performTest("[].count", "0");
	}

	@Test
	public void testAccess_2() {
		performTest("[\"a\":1].count", "1");
	}

	@Test
	public void testAccess_3() {
		performTest("\"abc\".len", "3");
	}

	@Test
	public void testWhereOperator() {
		performTest("[1] where x: true", "[1]");
	}

	@Test
	public void testMapOperator() {
		performTest("[1] map x: \"foo\"", "[\"foo\"]");
	}

	@Test
	public void testCall() {
		performTest("testFunction()", "\"testFunction's result\"");
	}

	@Test
	public void testSubscript() {
		performTest("[1][0]", "1");
	}

	@Test
	public void testSubscript_1() {
		performTest("[\"foo\":1][\"foo\"]", "1");
	}

	@Test
	public void testSimpleExpression() {
		performTest("1 + 2 * 3", "7");
	}
}
