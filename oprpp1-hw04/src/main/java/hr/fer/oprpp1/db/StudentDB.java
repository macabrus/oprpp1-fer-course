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
            results = db.forJMBAG(parser.getQueriedJMBAG());
          } else {
            results = db.filter(new QueryFilter(parser.getQuery()));
          }
          TablePrinter.prettyPrint(results);
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

}
