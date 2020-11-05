package hr.fer.oprpp1.db;


import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class StudentDB {

  private static StudentDatabase db;

  public static void main(String[] args) {
    try {
      var data = new String(
        StudentDB.class
          .getResourceAsStream("/database.txt")
          .readAllBytes()
      ).split(System.lineSeparator());
      db = new StudentDatabase(data);
    } catch (IOException e) {
      System.err.println("Can't read file ./database.txt, exiting...");
      return;
    }
    var sc = new Scanner(System.in);
    System.out.print(" > ");
    System.out.flush();
    while (sc.hasNext()) {
      var command = sc.next();
      switch (command) {
        case "query" -> {
          QueryParser parser = new QueryParser(sc.nextLine());
          List<StudentRecord> results;
          if (parser.isDirectQuery()) {
            System.out.println("Using index for record retrieval.");
            results = List.of(db.forJMBAG(parser.getQueriedJMBAG()));
          } else {
            results = db.filter(new QueryFilter(parser.getQuery()));
          }
          prettyPrint(results);
          System.out.println("Records selected: " + results.size());
        }
        case "exit" -> {
          System.out.println("Bye!");
          sc.close();
          return;
        }
        default -> {
          System.out.println("Unknown verb.");
          sc.nextLine();
        }
      }
      System.out.print(" > ");
      System.out.flush();
    }
  }

  /**
   * Pretty prints database query result formated in table.
   *
   * @param result query result to print
   */
  private static void prettyPrint(List<StudentRecord> result) {
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

  private static void printDecorators(int... colWidths) {
    System.out.print("+");
    for (Integer i : colWidths) {
      System.out.print("=".repeat(i + 2));
      System.out.print("+");
    }
    System.out.println();
  }
}
