package com.openpromos.jni.nrexpressionlib;

import com.openpromos.jni.nrexpressionlib.ast.astbase.ASTExpression;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class ParserTests {

	private void performTest(String input, String expectedOutput) {
		Lexer lexer = new Lexer(input);
		Parser sut = new Parser(lexer);

		String result = "PARSER_ERROR";
		ASTExpression exp = null;

		try {
			exp = sut.parseExpression();
		} catch (ParserException pe) {
			pe.toString();
		} catch (Exception e) {
			throw new RuntimeException("Unexpected Exception: " + e.toString());
		}

		if (!sut.isAtEnd()) {
			result = "PARSER_ERROR";
		} else {
			if (exp != null) {
				result = exp.toString();
			}
		}

		if (result.equals(expectedOutput))  {
			System.out.println("Test passed with expected output: " + result);
		}

		assertThat(result, is(equalTo(expectedOutput)));
	}

	// --------- generated tests below this line: do not edit ---------
	@Test
	public void testEmpty() {
		performTest("", "PARSER_ERROR");
	}

	@Test
	public void testEmptyExpression() {
		performTest("()", "PARSER_ERROR");
	}

	@Test
	public void testInvalidToken() {
		performTest("$0", "PARSER_ERROR");
	}

	@Test
	public void testUnexpectedToken() {
		performTest(")", "PARSER_ERROR");
	}

	@Test
	public void testUnexpectedToken_1() {
		performTest("]", "PARSER_ERROR");
	}

	@Test
	public void testUnexpectedToken_2() {
		performTest("*", "PARSER_ERROR");
	}

	@Test
	public void testUnexpectedToken_3() {
		performTest("/", "PARSER_ERROR");
	}

	@Test
	public void testUnexpectedToken_4() {
		performTest("%", "PARSER_ERROR");
	}

	@Test
	public void testUnexpectedToken_5() {
		performTest("+", "PARSER_ERROR");
	}

	@Test
	public void testUnexpectedToken_6() {
		performTest("?", "PARSER_ERROR");
	}

	@Test
	public void testUnexpectedToken_7() {
		performTest("]", "PARSER_ERROR");
	}

	@Test
	public void testTwoExpressions() {
		performTest("1 2", "PARSER_ERROR");
	}

	@Test
	public void testIntLiteral() {
		performTest("1", "1");
	}

	@Test
	public void testFloatLiteral01() {
		performTest("0.1", "0.1");
	}

	@Test
	public void testFloatLiteral10() {
		performTest("1.0", "1");
	}

	@Test
	public void testStringLiteral() {
		performTest("\"default\"", "\"default\"");
	}

	@Test
	public void testBoolLiteral() {
		performTest("true", "true");
	}

	@Test
	public void testNullLiteral() {
		performTest("NULL", "NULL");
	}

	@Test
	public void testEmptyListLiteral() {
		performTest("[]", "[]");
	}

	@Test
	public void testListLiteral() {
		performTest("[1 ]", "[1]");
	}

	@Test
	public void testListLiteral_1() {
		performTest("[1, ]", "[1]");
	}

	@Test
	public void testListLiteral_2() {
		performTest("[1, 2, 3 ]", "[1, 2, 3]");
	}

	@Test
	public void testListLiteral_3() {
		performTest("[1, 2, 3, ]", "[1, 2, 3]");
	}

	@Test
	public void testListLiteral_4() {
		performTest("[1, 0.2, \"abc\", false, [0], [1:2], $abc]", "[1, 0.2, \"abc\", false, [0], [1:2], $abc]");
	}

	@Test
	public void testListLiteral_5() {
		performTest("[1, 0.2, \"abc\", false, [0], [1:2], $abc, ]", "[1, 0.2, \"abc\", false, [0], [1:2], $abc]");
	}

	@Test
	public void testMalformedListLiteral() {
		performTest("[", "PARSER_ERROR");
	}

	@Test
	public void testMalformedListLiteral_1() {
		performTest("[,]", "PARSER_ERROR");
	}

	@Test
	public void testMalformedListLiteral_2() {
		performTest("[ /* 1 */, 2, 3]", "PARSER_ERROR");
	}

	@Test
	public void testEmptyDictLiteral() {
		performTest("[:]", "[:]");
	}

	@Test
	public void testDictLiteral() {
		performTest("[1:2]", "[1:2]");
	}

	@Test
	public void testDictLiteral_1() {
		performTest("[1:2, ]", "[1:2]");
	}

	@Test
	public void testDictLiteral_2() {
		performTest("[1:2, 2:3, 3:4]", "[1:2, 2:3, 3:4]");
	}

	@Test
	public void testDictLiteral_3() {
		performTest("[1:2, 2:3, 3:4, ]", "[1:2, 2:3, 3:4]");
	}

	@Test
	public void testDictLiteral_4() {
		performTest("[\"1\":1, \"0.2\":0.2, \"abc\":\"abc\", \"false\":false, \"[0]\":[0], \"[1:2]\":[1:2], \"$abc\":$abc]", "[\"1\":1, \"0.2\":0.2, \"abc\":\"abc\", \"false\":false, \"[0]\":[0], \"[1:2]\":[1:2], \"$abc\":$abc]");
	}

	@Test
	public void testDictLiteral_5() {
		performTest("[\"1\":1, \"0.2\":0.2, \"abc\":\"abc\", \"false\":false, \"[0]\":[0], \"[1:2]\":[1:2], \"$abc\":$abc, ]", "[\"1\":1, \"0.2\":0.2, \"abc\":\"abc\", \"false\":false, \"[0]\":[0], \"[1:2]\":[1:2], \"$abc\":$abc]");
	}

	@Test
	public void testMalformedDictLiteral() {
		performTest("[ 1;", "PARSER_ERROR");
	}

	@Test
	public void testMalformedDictLiteral_1() {
		performTest("[ 1:2", "PARSER_ERROR");
	}

	@Test
	public void testMalformedDictLiteral_2() {
		performTest("[ 1:2, 1 2]", "PARSER_ERROR");
	}

	@Test
	public void testMalformedDictLiteral_3() {
		performTest("[ 1: ]", "PARSER_ERROR");
	}

	@Test
	public void testMalformedDictLiteral_4() {
		performTest("[ :1 ]", "PARSER_ERROR");
	}

	@Test
	public void testMalformedDictLiteral_5() {
		performTest("[ 1:1, 1: ]", "PARSER_ERROR");
	}

	@Test
	public void testMalformedDictLiteral_6() {
		performTest("[ 1:1, :1 ]", "PARSER_ERROR");
	}

	@Test
	public void testMalformedDictLiteral_7() {
		performTest("[ 1:1:1 ]", "PARSER_ERROR");
	}

	@Test
	public void testMalformedDictLiteral_8() {
		performTest("[ 1, 1:1 ]", "PARSER_ERROR");
	}

	@Test
	public void testMalformedDictLiteral_9() {
		performTest("[ 1:1, 1 ]", "PARSER_ERROR");
	}

	@Test
	public void testSingleLookup() {
		performTest("$a", "$a");
	}

	@Test
	public void testMultiLookup() {
		performTest("$$a", "$$a");
	}

	@Test
	public void testLookupChain() {
		performTest("$a$$b$c$$d", "$a$$b$c$$d");
	}

	@Test
	public void testIdentifier() {
		performTest("myName", "myName");
	}

	@Test
	public void testUnaryminus() {
		performTest("-a", "(-a)");
	}

	@Test
	public void testUnaryminus_1() {
		performTest("--a", "(-(-a))");
	}

	@Test
	public void testUnaryminus_2() {
		performTest("(-/* foo */a)", "(-a)");
	}

	@Test
	public void testUnarynot() {
		performTest("!a", "(!a)");
	}

	@Test
	public void testUnarynot_1() {
		performTest("!!a", "(!(!a))");
	}

	@Test
	public void testUnarymix() {
		performTest("-!-!a", "(-(!(-(!a))))");
	}

	@Test
	public void testParenthesis() {
		performTest("(a)", "a");
	}

	@Test
	public void testParenthesis_1() {
		performTest("((a))", "a");
	}

	@Test
	public void testMalformedParenthesis() {
		performTest("(", "PARSER_ERROR");
	}

	@Test
	public void testMalformedParenthesis_1() {
		performTest(")", "PARSER_ERROR");
	}

	@Test
	public void testMalformedParenthesis_2() {
		performTest("() 1", "PARSER_ERROR");
	}

	@Test
	public void testMalformedParenthesis_3() {
		performTest("-()", "PARSER_ERROR");
	}

	@Test
	public void testMalformedParenthesis_4() {
		performTest("(-1+)", "PARSER_ERROR");
	}

	@Test
	public void testExceptOperator() {
		performTest("1 except 2", "(1 except 2)");
	}

	@Test
	public void testConditionalOperator() {
		performTest("true ? \"foo\" : \"bar\"", "(true ? \"foo\" : \"bar\")");
	}

	@Test
	public void testMalformedConditionalOperator() {
		performTest("true ?", "PARSER_ERROR");
	}

	@Test
	public void testMalformedConditionalOperator_1() {
		performTest("true ? \"foo\"", "PARSER_ERROR");
	}

	@Test
	public void testMalformedConditionalOperator_2() {
		performTest("true ? \"foo\" :", "PARSER_ERROR");
	}

	@Test
	public void testWhereOperator() {
		performTest("1 where x : 2", "(1 where x : 2)");
	}

	@Test
	public void testMapOperator() {
		performTest("1 map x : 2", "(1 map x : 2)");
	}

	@Test
	public void testContainsOperator() {
		performTest("1 contains 2", "(1 contains 2)");
	}

	@Test
	public void testLogicorOperator() {
		performTest("1 || 2", "(1 || 2)");
	}

	@Test
	public void testLgicandOperator() {
		performTest("1 && 2", "(1 && 2)");
	}

	@Test
	public void testEqualOperator() {
		performTest("1 = 2", "(1 == 2)");
	}

	@Test
	public void testEqualOperator_1() {
		performTest("1 == 2", "(1 == 2)");
	}

	@Test
	public void testNotequalOperator() {
		performTest("1 != 2", "(1 != 2)");
	}

	@Test
	public void testGreaterthanOperator() {
		performTest("1 > 2", "(1 > 2)");
	}

	@Test
	public void testGreaterorequalOperator() {
		performTest("1 >= 2", "(1 >= 2)");
	}

	@Test
	public void testLessthanOperator() {
		performTest("1 < 2", "(1 < 2)");
	}

	@Test
	public void testLessorequalOperator() {
		performTest("1 <= 2", "(1 <= 2)");
	}

	@Test
	public void testAdditionOperator() {
		performTest("1 + 2", "(1 + 2)");
	}

	@Test
	public void testSubtractionOperator() {
		performTest("1 - 2", "(1 - 2)");
	}

	@Test
	public void testMultiplicationOperator() {
		performTest("1 * 2", "(1 * 2)");
	}

	@Test
	public void testDivisionOperator() {
		performTest("1 / 2", "(1 / 2)");
	}

	@Test
	public void testModuloOperator() {
		performTest("1 % 2", "(1 % 2)");
	}

	@Test
	public void testCallOperator() {
		performTest("a()", "(a())");
	}

	@Test
	public void testCallOperator_1() {
		performTest("a(1)", "(a(1))");
	}

	@Test
	public void testCallOperator_2() {
		performTest("a(1, true, $a, \"foo\")", "(a(1, true, $a, \"foo\"))");
	}

	@Test
	public void testMalformedCallOperator() {
		performTest("a(", "PARSER_ERROR");
	}

	@Test
	public void testMalformedCallOperator_1() {
		performTest("a(,)", "PARSER_ERROR");
	}

	@Test
	public void testMalformedCallOperator_2() {
		performTest("a(1,)", "PARSER_ERROR");
	}

	@Test
	public void testMalformedCallOperator_3() {
		performTest("a(1;)", "PARSER_ERROR");
	}

	@Test
	public void testAccessOperator() {
		performTest("a.b", "(a.b)");
	}

	@Test
	public void testAccessOperator_1() {
		performTest("a.b.c", "((a.b).c)");
	}

	@Test
	public void testAccessOperator_2() {
		performTest("1.b", "(1.b)");
	}

	@Test
	public void testAccessOperator_3() {
		performTest("1.0.b", "(1.b)");
	}

	@Test
	public void testMalformedAccessOperator() {
		performTest("a.0", "PARSER_ERROR");
	}

	@Test
	public void testMalformedAccessOperator_1() {
		performTest("(a.)", "PARSER_ERROR");
	}

	@Test
	public void testSubscriptOperator() {
		performTest("a[1]", "(a->[1])");
	}

	@Test
	public void testSubscriptOperator_1() {
		performTest("[0][1]", "([0]->[1])");
	}

	@Test
	public void testMalformedSubscriptOperator() {
		performTest("a[a b", "PARSER_ERROR");
	}

	@Test
	public void testPrecedence() {
		performTest("1 + 2 * 3", "(1 + (2 * 3))");
	}

	@Test
	public void testPrecedence_1() {
		performTest("(1 + 2) * 3", "((1 + 2) * 3)");
	}

	@Test
	public void testPrecedence_2() {
		performTest("1 + 2 - 3", "((1 + 2) - 3)");
	}

	@Test
	public void testPrecedence_3() {
		performTest("1 + (2 - 3)", "(1 + (2 - 3))");
	}

	@Test
	public void testPrecedence_4() {
		performTest("a * b + c * d % e - f", "(((a * b) + ((c * d) % e)) - f)");
	}

	@Test
	public void testPrecedence_5() {
		performTest("-a+b", "((-a) + b)");
	}

	@Test
	public void testPrecedence_6() {
		performTest("-a.b", "(-(a.b))");
	}

	@Test
	public void testPrecedence_7() {
		performTest("--a.b", "(-(-(a.b)))");
	}

	@Test
	public void testPrecedence_8() {
		performTest("--a % b", "((-(-a)) % b)");
	}

	@Test
	public void testPrecedence_9() {
		performTest("(-a).b", "((-a).b)");
	}

	@Test
	public void testPrecedence_10() {
		performTest("a.b.c", "((a.b).c)");
	}

	@Test
	public void testPrecedence_11() {
		performTest("a[1].m(2)", "(((a->[1]).m)(2))");
	}

	@Test
	public void testPrecedence_12() {
		performTest("a[1 + 2]", "(a->[(1 + 2)])");
	}

	@Test
	public void testPrecedence_13() {
		performTest("-[1][0]", "(-([1]->[0]))");
	}

	@Test
	public void testPrecedence_14() {
		performTest("[-1()]", "[(-(1()))]");
	}

	@Test
	public void testPrecedence_15() {
		performTest("[[a]()[b],[c]]", "[(([a]())->[b]), [c]]");
	}

	@Test
	public void testPrecedence_16() {
		performTest("a + b contains c + d && e", "(((a + b) contains (c + d)) && e)");
	}

	@Test
	public void testPrecedence_17() {
		performTest("a ? b ? 1 : 2 : c ? 3 : 4", "(a ? (b ? 1 : 2) : (c ? 3 : 4))");
	}

	@Test
	public void testManyOperators() {
		performTest("a || b && c == d != e > f >= g < h <= i + j - k * l / m % n ? 1 : 2", "((a || (b && ((c == d) != ((((e > f) >= g) < h) <= ((i + j) - (((k * l) / m) % n)))))) ? 1 : 2)");
	}

	@Test
	public void testComplexExpressionTest() {
		performTest("(([\"1\", \"2\", \"1234\", $foo, [], \"Hello, World!\", \"\\\"\", \"1️⃣\"] map element : NUMBER(element)) where each: each % 2 == 1).count", "((([\"1\", \"2\", \"1234\", $foo, [], \"Hello, World!\", \"\\\"\", \"1️⃣\"] map element : (NUMBER(element))) where each : ((each % 2) == 1)).count)");
	}
}

