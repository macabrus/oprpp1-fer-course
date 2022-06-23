package hr.fer.oprpp1.db;

public enum TokenType {

  // column name
  VAR,

  // string literals in query
  STRING,

  // numbers in query
  NUM,

  // AND operator
  CONJUNCTION,

  // Operators
  LT,
  GT,
  LTE,
  GTE,
  NEQ,
  EQ,
  LIKE
}
