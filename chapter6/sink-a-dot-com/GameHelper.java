import java.io.*;
import java.util.*;

public class GameHelper {
  private final int BOARD_LENGTH = 7;
  private enum Direction {
    DOWN,
    RIGHT
  }
  private boolean[][] grid = new boolean[BOARD_LENGTH][BOARD_LENGTH];

  public String getUserInput(String prompt) {
    String inputLine = null;
    System.out.println(prompt);
    try {
      BufferedReader is = new BufferedReader(
        new InputStreamReader(System.in)
      );
      inputLine = is.readLine();
    } catch (IOException e) {
      System.out.println("IOException: " + e);
    }
    return inputLine.toLowerCase();
  }

  public ArrayList<String> placeDotCom(int comSize) {
    // Find place to put dot com
    boolean foundPlace = false;
    int headPosX;
    int headPosY;
    Direction dir;
    do {
      // Generate random starting position and direction
      headPosX = (int) (Math.random() * (BOARD_LENGTH - comSize));
      headPosY = (int) (Math.random() * (BOARD_LENGTH - comSize)) + comSize - 1;
      dir = (Math.random() > 0.5)? Direction.DOWN : Direction.RIGHT;  
      // Check if it collides with previous dot coms
      int posX = headPosX;
      int posY = headPosY;
      foundPlace = true;
      for(int i = 0; i < comSize; i++) {
        if(grid[posX][posY] == true) {
          // collides
          foundPlace = false;
          break;
        }
        // Update next position
        if(dir == Direction.DOWN) {
          posY--;
        } else {
          posX++;
        }
      }
    } while(!foundPlace);
    // Create return value and update grid
    ArrayList<String> cellLocations = new ArrayList<String>();
    String rowCharacters = "abcdefg";
    int posX = headPosX;
    int posY = headPosY;
    for(int i = 0; i < comSize; i++) {
      grid[posX][posY] = true;
      String location = String.valueOf(rowCharacters.charAt(posY)) + Integer.toString(posX);
      System.out.print(location + " ");
      cellLocations.add(location);
      if(dir == Direction.DOWN) {
        posY--;
      } else {
        posX++;
      }
    }
    System.out.println("");
    // Return cellLocations
    return cellLocations;
  }
}
