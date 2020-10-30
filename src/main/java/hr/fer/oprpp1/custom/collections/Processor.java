package hr.fer.oprpp1.custom.collections;

/**
 * This class represents an conceptual contract between clients which will have objects to be processed, and each
 * concrete Processor which knows how to perform the selected operation. Each concrete Processor will be defined as a
 * new class which inherits from the class Processor.
 */
public interface Processor<T> {
    void process(T value);
}
