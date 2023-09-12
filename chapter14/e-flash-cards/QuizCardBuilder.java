import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.*;

class QuizCardBuilder {
  private JTextArea questionTextArea;
  private JTextArea answerTextArea;
  private ArrayList<QuizCard> quizCards = new ArrayList<QuizCard>();
  private JFrame frame;

  public static void main(String[] args) {
    QuizCardBuilder quizCardBuilder = new QuizCardBuilder();
    quizCardBuilder.go();
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

    JButton nextCardButton = new JButton("Next card");

    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("File");
    JMenuItem newMenuItem = new JMenuItem("New");
    JMenuItem saveMenuItem = new JMenuItem("Save");

    nextCardButton.addActionListener(new NextCardListener());
    newMenuItem.addActionListener(new NewMenuListener());
    saveMenuItem.addActionListener(new SaveMenuListener());

    menuBar.add(fileMenu);
    fileMenu.add(newMenuItem);
    fileMenu.add(saveMenuItem);

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
      quizCards.add(new QuizCard(questionTextArea.getText(), answerTextArea.getText()));
      clearTextArea();
    }
  }

  private class NewMenuListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      quizCards.clear();
      clearTextArea();
    }
  }

  private class SaveMenuListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      JFileChooser fileDialog = new JFileChooser();
      fileDialog.showSaveDialog(frame);
      saveFile(fileDialog.getSelectedFile());
      quizCards.clear();
      clearTextArea();
    }
  }

  private void clearTextArea() {
    questionTextArea.setText("");
    answerTextArea.setText("");
  }

  private void saveFile(File file) {
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(file));
      for(QuizCard quizCard : quizCards) {
        writer.write(quizCard.getQuestion() + "/" + quizCard.getAnswer() + "\n");
      }
      writer.close();
    } catch(Exception ex) {
      ex.printStackTrace();
    }
  }
}
