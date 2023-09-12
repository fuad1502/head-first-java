import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

class QuizCardPlayer {
  enum GameState {
    SHOW_QUESTION,
    SHOW_ANSWER
  }

  private JTextArea questionTextArea;
  private JTextArea answerTextArea;
  private JButton nextCardButton;
  private ArrayList<QuizCard> quizCards = new ArrayList<QuizCard>();
  private GameState gameState = GameState.SHOW_QUESTION;
  private QuizCard currentCard;
  private JFrame frame;

  public static void main(String[] args) {
    QuizCardPlayer quizCardPlayer = new QuizCardPlayer();
    quizCardPlayer.go();
  }

  public void go() {
    frame = new JFrame();
    JPanel panel = new JPanel();

    JLabel questionLabel = new JLabel("Question:");
    JLabel answerLabel = new JLabel("Answer:");

    questionTextArea = new JTextArea(6, 18);
    answerTextArea = new JTextArea(6, 18);
    Font bigFont = new Font("sanserif", Font.BOLD, 24);

    questionTextArea.setLineWrap(true);
    questionTextArea.setWrapStyleWord(true);
    answerTextArea.setLineWrap(true);
    answerTextArea.setWrapStyleWord(true);
    questionTextArea.setFont(bigFont);
    answerTextArea.setFont(bigFont);

    JScrollPane questionScrollPane = new JScrollPane(questionTextArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    JScrollPane answerScrollPane = new JScrollPane(answerTextArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

    nextCardButton = new JButton("Next card");

    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("File");
    JMenuItem openMenuItem = new JMenuItem("Open");

    nextCardButton.addActionListener(new NextCardListener());
    openMenuItem.addActionListener(new OpenMenuListener());

    menuBar.add(fileMenu);
    fileMenu.add(openMenuItem);

    panel.add(questionLabel);
    panel.add(questionScrollPane);
    panel.add(answerLabel);
    panel.add(answerScrollPane);
    panel.add(nextCardButton);

    frame.getContentPane().add(BorderLayout.CENTER, panel);
    frame.setJMenuBar(menuBar);

    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setSize(500, 600);
    frame.setVisible(true);
  }

  private class NextCardListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      switch(gameState) {
        case SHOW_QUESTION :
          showQuestion();
          break;
        case SHOW_ANSWER :
          showAnswer();
          break;
      }
    }
  }

  private class OpenMenuListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      JFileChooser fileDialog = new JFileChooser();
      fileDialog.showOpenDialog(frame);
      createCards(fileDialog.getSelectedFile());
      showQuestion();
    }
  }

  private void showQuestion() {
    quizCards.remove(currentCard);
    if(quizCards.isEmpty()) {
      currentCard = null;
      questionTextArea.setText("No more cards in this set.");
      answerTextArea.setText("");
    } else {
      int randomIdx = (int) (Math.random() * quizCards.size());
      currentCard = quizCards.get(randomIdx);
      questionTextArea.setText(currentCard.getQuestion());
      answerTextArea.setText("");
      gameState = GameState.SHOW_ANSWER;
      nextCardButton.setText("Show answer");
    }
  }

  private void showAnswer() {
    if(currentCard != null) {
      answerTextArea.setText(currentCard.getAnswer());
    }
    gameState = GameState.SHOW_QUESTION;
    nextCardButton.setText("Next card");
  }

  private void createCards(File file) {
    quizCards.clear();
    try {
      BufferedReader reader = new BufferedReader(new FileReader(file));
      String line;
      while((line = reader.readLine()) != null) {
        String[] tokens = line.split("/");
        quizCards.add(new QuizCard(tokens[0], tokens[1]));
      }
      reader.close();
    } catch(Exception ex) {
      ex.printStackTrace();
    }
  }
}
