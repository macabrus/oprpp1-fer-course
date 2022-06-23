package hr.fer.argparse;

import java.util.HashMap;
import java.util.Map;

public class ArgParser {

  private Map<String, String> mapsTo = new HashMap<>(){{
    put("W", "workers");
    put("T", "tracks");
    put("workers", "workers");
    put("tracks", "tracks");
  }};

  public Map<String, String> parse(String[] line) {
    Map<String, String> parsed = new HashMap<>();
    for (var arg : line) {
      String[] kv = arg.split("=", 2);
      if (kv.length != 2)
        continue;
      if (kv[0].startsWith("--") && kv[0].length() > 2){
        var clean = kv[0].replaceFirst("--", "");
        if (!mapsTo.containsKey(clean))
          continue;
        else
          kv[0] = mapsTo.get(clean);
        parsed.put(kv[0], kv[1]);
      }
      else if (kv[0].startsWith("-") && kv[0].length() == 2) {
        var clean = kv[0].replaceFirst("-","");
        if (!mapsTo.containsKey(clean))
          continue;
        else
          kv[0] = mapsTo.get(clean);
        parsed.put(kv[0], kv[1]);
      }
    }
    return parsed;
  }
}
