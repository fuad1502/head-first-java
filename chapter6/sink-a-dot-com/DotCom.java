import java.util.ArrayList;

public class DotCom {
  private ArrayList<String> locationCells;
  private String name;

  public void setLocation(ArrayList<String> locs) {
    locationCells = locs;
  }

  public void setName(String n) {
    name = n;
  }

  public String checkYourself(String userInput) {
    int index = locationCells.indexOf(userInput);
    if(index != -1) {
      locationCells.remove(index);
      if(locationCells.size() == 0) {
        return "kill";
      } else {
        return "hit";
      }
    } else {
      return "miss";
    }
  }
}
