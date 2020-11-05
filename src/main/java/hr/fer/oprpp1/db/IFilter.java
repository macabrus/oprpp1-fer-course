package hr.fer.oprpp1.db;

public interface IFilter {
  boolean accepts(StudentRecord record);
}
