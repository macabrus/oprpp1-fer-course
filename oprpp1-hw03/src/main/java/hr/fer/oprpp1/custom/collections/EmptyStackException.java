package hr.fer.oprpp1.custom.collections;

public class EmptyStackException extends RuntimeException {

  EmptyStackException() {}

  EmptyStackException(String message) {
    super(message);
  }

}
