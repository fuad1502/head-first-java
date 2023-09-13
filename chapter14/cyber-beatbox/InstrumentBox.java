import javax.swing.*;
import java.util.ArrayList;

public class InstrumentBox extends Box {
  private static final int NUMBER_OF_BEATS = 16;

  private int tone;
  private ArrayList<JCheckBox> checkBoxs = new ArrayList<JCheckBox>();

  public InstrumentBox(int tone) {
    super(BoxLayout.X_AXIS);
    for(int i = 0; i < NUMBER_OF_BEATS; i++) {
      checkBoxs.add(new JCheckBox());
      this.add(checkBoxs.get(i));
    }
    this.tone = tone;
  }

  public ArrayList<Boolean> getBeats() {
    ArrayList<Boolean> beats = new ArrayList<Boolean>();
    for(JCheckBox checkBox : checkBoxs) {
      beats.add(checkBox.isSelected());
    }
    return beats;
  }

  public int getTone() {
    return tone;
  }
}
