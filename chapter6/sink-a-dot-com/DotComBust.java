import java.util.*;

public class DotComBust {
  ArrayList<DotCom> dotComList = new ArrayList<DotCom>();
  private GameHelper helper = new GameHelper();
  private int numberOfGuesses = 0;

  private void setupGame() {
    DotCom one = new DotCom();
    one.setName("Pets.com");
    DotCom two = new DotCom();
    two.setName("eToys.com");
    DotCom three = new DotCom();
    three.setName("Go2.com");
    dotComList.add(one);
    dotComList.add(two);
    dotComList.add(three);

    System.out.println("Your goal is to sink three dot coms.");
    System.out.println("Pets.com, eToys.com, Go2.com");
    System.out.println("Try to sink them all in the fewest number of guesses.");

    for(DotCom dotCom : dotComList) {
      dotCom.setLocation(helper.placeDotCom(3));
    }
  }

  private void startPlaying() {
    while(!dotComList.isEmpty()) {
      String guess = helper.getUserInput("Enter a guess: ");
      checkUserGuess(guess);
    }
    finishGame();
  }

  private void finishGame() {
    System.out.println("All Dot Coms are dead! Your stock is now worthless!");
    if(numberOfGuesses <= 18) {
      System.out.println("It only took you " + numberOfGuesses + " guesses.");
      System.out.println("You got out before your options sank.");
    } else {
      System.out.println("Took you long enough. " + numberOfGuesses + " guesses.");
      System.out.println("Fish are dancing with your options.");
    }
  }

  private void checkUserGuess(String guess) {
    numberOfGuesses++;
    String res = "miss";
    for(DotCom dotCom : dotComList) {
      res = dotCom.checkYourself(guess);
      if(res == "kill") {
        dotComList.remove(dotCom);
      }
      if(res == "kill" || res == "hit") {
        break;
      }
    }
    System.out.println(res);
  }

  public static void main(String[] args) {
    DotComBust game = new DotComBust();
    game.setupGame();
    game.startPlaying();
  }
}
