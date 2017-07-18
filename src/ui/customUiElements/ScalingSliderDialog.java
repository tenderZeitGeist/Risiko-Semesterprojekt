package ui.customUiElements;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ScalingSliderDialog {

    public double createScalingSliderDialog() {
        JFrame parent = new JFrame();

        JOptionPane optionPane = new JOptionPane();
        JSlider slider = getSlider(optionPane);
        optionPane.setMessage(new Object[] { "Select Scaling Percent: ", slider });
        optionPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
        optionPane.setOptionType(JOptionPane.OK_CANCEL_OPTION);

        JDialog dialog = optionPane.createDialog(parent, "Select Scaling");
        dialog.setVisible(true);
        System.out.println("Input: " + optionPane.getInputValue());
        if(optionPane.getInputValue() == null){
            System.exit(0);
            return 666;

        }

        return slider.getValue();
    }

    static JSlider getSlider(final JOptionPane optionPane) {
        JSlider slider = new JSlider(0, 100, 70);
        slider.setMinorTickSpacing(1);
        slider.setMajorTickSpacing(50);
        slider.setPaintTicks(false);
        slider.setPaintLabels(true);
        ChangeListener changeListener = new ChangeListener() {
            public void stateChanged(ChangeEvent changeEvent) {
                JSlider theSlider = (JSlider) changeEvent.getSource();
                if (!theSlider.getValueIsAdjusting()) {
                    optionPane.setInputValue(new Integer(theSlider.getValue()));
                }
            }
        };
        slider.addChangeListener(changeListener);
        return slider;
    }

}