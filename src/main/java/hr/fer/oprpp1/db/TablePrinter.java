package hr.fer.oprpp1.db;

import java.util.List;

public class TablePrinter {

  /**
   * Pretty prints database query result formated in table.
   *
   * @param result query result to print
   */
  public static void prettyPrint(List<StudentRecord> result) {
    if (result.size() == 0)
      return;

    int maxJmbagLen = result.stream().map(s -> s.getJmbag().length()).max(Integer::compare).get();
    int maxLastNameLen = result.stream().map(s -> s.getLastName().length()).max(Integer::compare).get();
    int maxFirstNameLen = result.stream().map(s -> s.getFirstName().length()).max(Integer::compare).get();
    printDecorators(maxJmbagLen, maxLastNameLen, maxFirstNameLen, 1);
    for (var student : result) {
      var jmbag = student.getJmbag();
      var lName = student.getLastName();
      var fName = student.getFirstName();
      var grade = student.getFinalGrade();
      System.out.print("|");
      System.out.print(" " + jmbag + " ".repeat(maxJmbagLen - jmbag.length()) + " ");
      System.out.print("|");
      System.out.print(" " + lName + " ".repeat(maxLastNameLen - lName.length()) + " ");
      System.out.print("|");
      System.out.print(" " + fName + " ".repeat(maxFirstNameLen - fName.length()) + " ");
      System.out.print("|");
      System.out.print(" " + grade + " ");
      System.out.println("|");
    }
    printDecorators(maxJmbagLen, maxLastNameLen, maxFirstNameLen, 1);
  }

  public static void printDecorators(int... colWidths) {
    System.out.print("+");
    for (Integer i : colWidths) {
      System.out.print("=".repeat(i + 2));
      System.out.print("+");
    }
    System.out.println();
  }
}
