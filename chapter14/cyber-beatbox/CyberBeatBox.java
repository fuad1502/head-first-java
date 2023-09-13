import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.*;
import javax.sound.midi.*;
import java.util.ArrayList;

class CyberBeatBox {

  private static final String[] INSTRUMENT_NAMES = {
    "Bass Drum", 
    "Closed Hi-Hat",
    "Open Hi-Hat",
    "Acoustic Snare",
    "Crash Cymbal",
    "Hand Clap",
    "High Tom",
    "Hi Bongo",
    "Maracas",
    "Whistle",
    "Low Conga",
    "Cow Bell",
    "Vibraslap",
    "Low-mid Tom",
    "High Agogo",
    "Open High Conga"
  };
  private static final int[] INSTRUMENT_TONES = {35, 42, 46, 38, 49, 39, 50, 60, 70, 72, 64, 56, 58, 47, 67, 63};

  private ArrayList<InstrumentBox> instrumentBoxs = new ArrayList<InstrumentBox>();

  private Sequencer player;

  public static void main(String[] args) {
    CyberBeatBox cyberBeatBox = new CyberBeatBox();
    cyberBeatBox.go();
  }

  public void go() {
    JPanel instrumentsPanel = createInstrumentsPanel();

    JButton startButton = new JButton("Start");
    JButton stopButton = new JButton("Stop");
    JButton tempoUpButton = new JButton("Tempo Up");
    JButton tempoDownButton = new JButton("Tempo Down");
    JButton serializeButton = new JButton("Serialize");
    JButton restoreButton = new JButton("Restore");
    startButton.addActionListener(new StartButtonListener());
    stopButton.addActionListener(new StopButtonListener());
    tempoUpButton.addActionListener(new TempoUpListener());
    tempoDownButton.addActionListener(new TempoDownListener());
    serializeButton.addActionListener(new SerializeButtonListener());
    restoreButton.addActionListener(new RestoreButtonListener());

    Box buttonPanel = new Box(BoxLayout.Y_AXIS);
    buttonPanel.add(startButton);
    buttonPanel.add(stopButton);
    buttonPanel.add(tempoUpButton);
    buttonPanel.add(tempoDownButton);
    buttonPanel.add(serializeButton);
    buttonPanel.add(restoreButton);
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

    JFrame frame = new JFrame();
    frame.getContentPane().add(buttonPanel, BorderLayout.EAST);
    frame.getContentPane().add(instrumentsPanel, BorderLayout.CENTER);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setSize(650, 450);
    frame.setVisible(true);

    setupMidi();
  }

  private class StartButtonListener implements ActionListener {
    public void actionPerformed(ActionEvent ev) {
      try {
        player.stop();
        Sequence seq = new Sequence(Sequence.PPQ, 4);
        Track track = seq.createTrack();
        fillTrack(track);
        player.setSequence(seq);
        player.start();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  private class StopButtonListener implements ActionListener {
    public void actionPerformed(ActionEvent ev) {
      player.stop();
    }
  }

  private class TempoUpListener implements ActionListener {
    public void actionPerformed(ActionEvent ev) {
      float tempo = player.getTempoFactor();
      player.setTempoFactor(tempo * 1.03f);
    }
  }

  private class TempoDownListener implements ActionListener {
    public void actionPerformed(ActionEvent ev) {
      float tempo = player.getTempoFactor();
      player.setTempoFactor(tempo / 1.03f);
    }
  }

  private class SerializeButtonListener implements ActionListener {
    public void actionPerformed(ActionEvent ev) {
      try {
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Checkbox.ser"));
        for(InstrumentBox instrument : instrumentBoxs) {
          outputStream.writeObject(instrument.getBeats());
        }
        outputStream.close();
      } catch(Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  private class RestoreButtonListener implements ActionListener {
    public void actionPerformed(ActionEvent ev) {
      try {
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Checkbox.ser"));
        for(InstrumentBox instrument : instrumentBoxs) {
          @SuppressWarnings("unchecked")
          ArrayList<Boolean> beats = (ArrayList<Boolean>) inputStream.readObject();
          instrument.setCheckBoxs(beats);
        }
        inputStream.close();
      } catch(Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  // Start of internal function definitions

  private JPanel createInstrumentsPanel() {
    createInstrumentBoxs();
    ArrayList<JLabel> labels = createInstrumentLabels();

    JPanel instrumentsPanel = new JPanel();
    GroupLayout layout = new GroupLayout(instrumentsPanel);
    instrumentsPanel.setLayout(layout);

    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);

    GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
    GroupLayout.ParallelGroup labelGroup = layout.createParallelGroup();
    for(JLabel label : labels) {
      labelGroup.addComponent(label);
    }
    hGroup.addGroup(labelGroup);
    GroupLayout.ParallelGroup instrumentBoxGroup = layout.createParallelGroup();
    for(InstrumentBox box : instrumentBoxs) {
      instrumentBoxGroup.addComponent(box);
    }
    hGroup.addGroup(instrumentBoxGroup);
    layout.setHorizontalGroup(hGroup);

    GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
    for(int i = 0; i < labels.size(); i++) {
      vGroup.addGroup(layout.createParallelGroup().addComponent(labels.get(i)).addComponent(instrumentBoxs.get(i)));
    }
    layout.setVerticalGroup(vGroup);

    return instrumentsPanel;
  }

  private void createInstrumentBoxs() {
    for(int tone : INSTRUMENT_TONES) {
      instrumentBoxs.add(new InstrumentBox(tone));
    }
  }

  private ArrayList<JLabel> createInstrumentLabels() {
    ArrayList<JLabel> labels = new ArrayList<JLabel>();
    for(String name : INSTRUMENT_NAMES) {
      labels.add(new JLabel(name));
    }
    return labels;
  }

  private void setupMidi() {
    try {
      player = MidiSystem.getSequencer();
      player.open();
      player.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
      player.setTempoInBPM(120);
    } catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  private void fillTrack(Track track) {
    for(InstrumentBox instrument : instrumentBoxs) {
      ArrayList<Boolean> beats = instrument.getBeats();
      int tone = instrument.getTone();
      int tick = 0;
      for(boolean beat : beats) {
        if(beat) {
          track.add(makeEvent(144, 9, tone, 100, tick));
          track.add(makeEvent(128, 9, tone, 100, tick + 1));
        }
        tick += 2;
      }
      track.add(makeEvent(176,1,127,0,32));
    }
    track.add(makeEvent(192, 9, 1, 0, 31));
  }

  private MidiEvent makeEvent(int comd, int chan, int one, int two, int tick) {
    MidiEvent event = null;
    try {
      ShortMessage message = new ShortMessage();
      message.setMessage(comd, chan, one, two);
      event = new MidiEvent(message, tick);
    } catch(Exception ex) {
      ex.printStackTrace();
    }
    return event;
  }
}

