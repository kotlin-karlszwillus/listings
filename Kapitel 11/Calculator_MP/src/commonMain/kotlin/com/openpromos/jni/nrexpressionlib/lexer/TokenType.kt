package com.openpromos.jni.nrexpressionlib.lexer

/**
 * The existing TokenTypes as Defined by the NRExpressionLib
 */
enum class TokenType {

    // Punctuation Tokens
    Dot,
    Not,
    Equal,
    NotEqual,
    GreaterOrEqual,
    Greater,
    LessOrEqual,
    Less,
    Assign,
    Minus,
    Plus,
    Comma,
    Star,
    Divis,
    Modulo,
    LeftParen,
    RightParen,
    Semicolon,
    LeftBrace,
    RightBrace,
    LeftBracket,
    RightBracket,
    Questionmark,
    Colon,
    And,
    Or,

    // Keyword Tokens
    Assert,
    Break,
    Catch,
    Contains,
    Continue,
    Else,
    Error,
    Except,
    False,
    For,
    If,
    In,
    Map,
    Null,
    Print,
    Return,
    True,
    Try,
    Where,
    While,

    // Tokens with associated data
    Identifier,
    Int,
    Float,
    String,
    Lookup,
    MultiLookup,

    // Special Tokens

    LexerError,
    AtEnd
}
