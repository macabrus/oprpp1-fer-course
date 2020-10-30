package hr.fer.oprpp1.custom.scripting.parser;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.custom.collections.ObjectStack;
import hr.fer.oprpp1.custom.scripting.elems.*;
import hr.fer.oprpp1.custom.scripting.lexer.LexerState;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptLexer;
import hr.fer.oprpp1.custom.scripting.lexer.Token;
import hr.fer.oprpp1.custom.scripting.lexer.TokenType;
import hr.fer.oprpp1.custom.scripting.nodes.*;

import static hr.fer.oprpp1.custom.scripting.lexer.TokenType.*;

/**
 * Parser implementation for SmartScript 'language'. Manages its own instance of lexer and its state depending on
 *  consumed tokens.
 */
public class SmartScriptParser {

  /**
   * Bound instance of parsed document.
   */
  private DocumentNode document;

  /**
   * Instance of stack collection used when building tree of Nodes
   */
  private final ObjectStack stack = new ObjectStack();

  public SmartScriptParser(String documentBody) {
    if (documentBody == null)
      throw new SmartScriptParserException("Document body must not be null");

    parseDocument(new SmartScriptLexer(documentBody));
  }

  /**
   * @return root node of document
   */
  public DocumentNode getDocumentNode() {
    return document;
  }

  /**
   * Consumes tokens from lexer object and builds {@link Node}(s) consisting of parsed {@link Element}(s).
   * @param lexer lexer object that produces tokens
   */
  private void parseDocument(SmartScriptLexer lexer) {
    document = new DocumentNode();
    // Root node on stack
    stack.push(document);
    while (lexer.getToken() == null || lexer.getToken().getType() != EOF) {
      var token = lexer.nextToken();
      switch (token.getType()) {
        case TEXT -> ((Node) (stack.peek())).addChildNode(new TextNode((String) token.getValue()));
        case TAG_START -> {
          // Setting parsing mode to TAG
          lexer.setState(LexerState.TAG);
          if (lexer.nextToken().getType() != VAR)
            throw new SmartScriptParserException("Unknown tag type " + lexer.getToken());
          // Examining type of tag
          switch ((String) lexer.getToken().getValue()) {
            case "FOR" -> {
              var forNode = parseForLoopTag(lexer);
              ((Node) (stack.peek())).addChildNode(forNode);
              stack.push(forNode);
            }
            case "=" -> {
              var echoNode = parseEchoTag(lexer);
              ((Node) (stack.peek())).addChildNode(echoNode);
            }
            case "END" -> {
              if (stack.size() == 1)
                throw new SmartScriptParserException("Extra END tag found.");
              stack.pop();
              lexer.nextToken();
              lexer.setState(LexerState.TEXT);
            }
            default -> throw new SmartScriptParserException("Unknown tag type " + lexer.getToken());
          }
          // Setting parsing mode back to TEXT
          lexer.setState(LexerState.TEXT);
        }
        case EOF -> {
          // Expecting only root document node to be on stack after finished
          if (stack.size() != 1)
            throw new SmartScriptParserException("Missing " + (stack.size() - 1) + " closing tag(s).");
          // Popping root from stack
          stack.pop();
          return;
        }
      }
    }
  }

  /**
   * This method makes sure all tokens from the moment it is called are of expected type for {@link ForLoopNode} creation.
   * @param lexer token source
   * @return constructed {@link ForLoopNode} from token elements
   */
  private ForLoopNode parseForLoopTag(SmartScriptLexer lexer) {
    var varNameToken = lexer.nextToken();
    var startToken = lexer.nextToken();
    var endToken = lexer.nextToken();
    Token stepToken = null;
    if (isTypeIn(lexer.nextToken().getType(), VAR, STRING, INT)) {
      stepToken = lexer.getToken();
      // Getting next tag which is closing tag...
      lexer.nextToken();
    }
    if (lexer.getToken().getType() != TAG_CLOSE)
      throw new SmartScriptParserException("Expected closing sequence ('$}') for loop tag");

    // Checking if types are ok
    if (!isTypeIn(varNameToken.getType(), VAR))
      throw new SmartScriptParserException(varNameToken.getValue() + " is not a valid variable name.");
    if (!isTypeIn(startToken.getType(), VAR, STRING, INT))
      throw new SmartScriptParserException(startToken.getValue() + " is not valid start of range.");
    if (!isTypeIn(endToken.getType(), VAR, STRING, INT))
      throw new SmartScriptParserException(endToken.getValue() + " is not a valid end of range.");
    if (stepToken != null)
      if (!isTypeIn(stepToken.getType(), VAR, STRING, INT))
        throw new SmartScriptParserException(endToken.getValue() + " is not a valid step value.");

    return new ForLoopNode(
      (ElementVariable) wrapInElement(varNameToken),
      wrapInElement(startToken),
      wrapInElement(endToken),
      wrapInElement(stepToken)
    );
  }

  /**
   * This method makes sure all tokens from the moment it is called are of expected type for {@link EchoNode} creation.
   * @param lexer token source
   * @return constructed {@link EchoNode} from token elements
   */
  private EchoNode parseEchoTag(SmartScriptLexer lexer) {
    var params = new ArrayIndexedCollection();

    while (lexer.nextToken().getType() != TAG_CLOSE) {
      var token = lexer.getToken();
      if (isTypeIn(token.getType(), VAR, FUNCTION, DOUBLE, INT, OPERATOR, STRING)) {
        params.add(wrapInElement(token));
      } else
        throw new SmartScriptParserException("Illegal token type in ECHO tag: " + token);
    }
    var elems = new Element[params.size()];
    for (int i = 0; i < elems.length; i++)
      elems[i] = (Element) params.get(i);

    return new EchoNode(elems);
  }

  /*---------- UTILITY METHODS ----------*/

  private boolean isTypeIn(TokenType type, TokenType... types) {
    for (TokenType t : types)
      if (type == t)
        return true;
    return false;
  }

  private Element wrapInElement(Token token) {
    if (token == null)
      return null;
    return switch (token.getType()) {
      case VAR -> new ElementVariable((String) token.getValue());
      case FUNCTION -> new ElementFunction((String) token.getValue());
      case DOUBLE -> new ElementConstantDouble((Double) token.getValue());
      case STRING -> new ElementString((String) token.getValue());
      case INT -> new ElementConstantInteger((Integer) token.getValue());
      case OPERATOR -> new ElementOperator((String) token.getValue());
      default -> throw new SmartScriptParserException("Invalid token type.");
    };
  }

}
