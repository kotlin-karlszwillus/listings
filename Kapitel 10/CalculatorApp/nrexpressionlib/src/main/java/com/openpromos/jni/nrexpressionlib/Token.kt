package com.openpromos.jni.nrexpressionlib

/**
 * Created by szwillus on 25.01.16.
 */
class Token(val type: TokenType, val value: String? = null) {

    // equals the swift overridden == operator function
    // we don´ need the argument for 'lhs', as here the left hand side token is 'this'
    fun isEqualTo(rhs: Token): Boolean {
        when (type) {
            TokenType.Dot,
            TokenType.Not,
            TokenType.Equal,
            TokenType.NotEqual,
            TokenType.GreaterOrEqual,
            TokenType.Greater,
            TokenType.LessOrEqual,
            TokenType.Less,
            TokenType.Assign,
            TokenType.Minus,
            TokenType.Plus,
            TokenType.Comma,
            TokenType.Star,
            TokenType.Divis,
            TokenType.Modulo,
            TokenType.LeftParen,
            TokenType.RightParen,
            TokenType.Semicolon,
            TokenType.LeftBrace,
            TokenType.RightBrace,
            TokenType.LeftBracket,
            TokenType.RightBracket,
            TokenType.Questionmark,
            TokenType.Colon,
            TokenType.And,
            TokenType.Or,
            TokenType.Assert,
            TokenType.Break,
            TokenType.Catch,
            TokenType.Contains,
            TokenType.Continue,
            TokenType.Else,
            TokenType.Error,
            TokenType.Except,
            TokenType.False,
            TokenType.For,
            TokenType.If,
            TokenType.In,
            TokenType.Map,
            TokenType.Null,
            TokenType.Print,
            TokenType.Return,
            TokenType.True,
            TokenType.Try,
            TokenType.Where,
            TokenType.While,
            TokenType.AtEnd -> {
                return if (this.type == rhs.type) {
                    true
                } else false

            }
            else -> {
                return if (this.type == rhs.type && this.value == rhs.value) {
                    true
                } else false
            }
        }
    }

    override fun toString(): String {
        when (type) {
            TokenType.Identifier -> return "Ident($value)"
            TokenType.Int -> return "Int($value)"
            TokenType.Float -> return "Float($value)"
            TokenType.String -> return "String($value)"
            TokenType.Lookup -> return "Lookup($value)"
            TokenType.MultiLookup -> return "MultiLookup($value)"
            TokenType.LexerError -> return LEXER_ERROR
            else -> return type.toString()
        }
    }

    companion object {
        val LEXER_ERROR = "LEXER_ERROR"
    }
}
