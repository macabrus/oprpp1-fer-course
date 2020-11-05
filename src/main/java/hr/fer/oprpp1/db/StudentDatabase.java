package hr.fer.oprpp1.db;

import java.util.*;
import java.util.stream.Collectors;

public class StudentDatabase {

  private Map<String, StudentRecord> jmbagIndex = new HashMap<>();
  private List<StudentRecord> records = new ArrayList<>();
  private Set<String> jmbags = new HashSet<>();

  public StudentDatabase(String... data) {
    for (String line : data) {
      var sc = new Scanner(line.strip());
      sc.useDelimiter("\\t");
      var student = new StudentRecord(sc.next(), sc.next(), sc.next(), sc.nextInt());
      if (jmbags.contains(student.getJmbag()))
        throw new RuntimeException("Duplicate JMBAG found: " + student.getJmbag());
      else
        jmbags.add(student.getJmbag());
      if (student.getFinalGrade() < 1 || student.getFinalGrade() > 5)
        throw new RuntimeException("Final grades must be 1-5 but was " + student.getFinalGrade());
      records.add(student);
      jmbagIndex.put(student.getJmbag(), student);
    }
  }

  public StudentRecord forJMBAG(String jmbag) {
    return jmbagIndex.get(jmbag);
  }

  public List<StudentRecord> filter(IFilter filter) {
    return records.stream().filter(filter::accepts).collect(Collectors.toList());
  }

}
