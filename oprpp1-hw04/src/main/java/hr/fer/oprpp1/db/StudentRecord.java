package hr.fer.oprpp1.db;

public class StudentRecord {

  private String jmbag;
  private String lastName;
  private String firstName;
  private int finalGrade;

  public StudentRecord(String jmbag, String lastName, String firstName, int finalGrade) {
    this.jmbag = jmbag;
    this.lastName = lastName;
    this.firstName = firstName;
    this.finalGrade = finalGrade;
  }

  public String getJmbag() {
    return jmbag;
  }

  public void setJmbag(String jmbag) {
    this.jmbag = jmbag;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public int getFinalGrade() {
    return finalGrade;
  }

  public void setFinalGrade(int finalGrade) {
    this.finalGrade = finalGrade;
  }
}
