package hr.fer.oprpp1.custom;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Prompt implements Action {

  private Map<String, Action> actions = new HashMap<>();
  private String prompt;
  private String exitAction;
  private InputStream in;

  public Prompt() {

  }

  public Prompt(String prompt) {
    this.prompt = prompt;
  }

  public final void registerAction(String key, Action action) {
    actions.put(key, action);
  }

  public Map<String, Action> getActions() {
    return actions;
  }

  public final void setExitAction(String exitAction) {
    this.exitAction = exitAction;
  }

  public final void setPrompt(String prompt) {
    this.prompt = prompt;
  }

  public void setInputStream(InputStream in) {
    this.in = in;
  }

  @Override
  public void perform(String[] args) {
    var sc = new Scanner(in);
    if (prompt != null)
      System.out.print(prompt);
    while (sc.hasNextLine()) {
      var actionLiteral = sc.nextLine().strip().split(" ");
      if (actionLiteral.length == 0)
        continue;
      if (actionLiteral[0].equals(exitAction))
        return;
      if (!actions.containsKey(actionLiteral[0]))
        throw new UnsupportedOperationException("Action doesn't exist " + actionLiteral[0]);
      else
        actions.get(actionLiteral[0]).perform(Arrays.copyOfRange(actionLiteral, 1, actionLiteral.length));
      if (prompt != null)
        System.out.print(prompt);
    }
  }
}
